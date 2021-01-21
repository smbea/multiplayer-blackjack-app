var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');

var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');
var http = require('http')
var WebSocket = require('ws')

const Game = require('./public/javascripts/Game');
const GameManager = require('./public/javascripts/GameManager');
const { ppid } = require('process');


var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/users', usersRouter);

function sleep(time) {
  return new Promise((resolve) => setTimeout(resolve, time));
}

// catch 404 and forward to error handler
app.use(function (req, res, next) {
  next(createError(404));
});

// error handler
app.use(function (err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});


console.log("Server started")

const httpS = http.createServer(app)


const wsServer = new WebSocket.Server({ port: 8080, clientTracking: true })

wsServer.getUniqueID = function () {
  function s4() {
    return Math.floor((1 + Math.random()) * 0x10000).toString(16).substring(1);
  }
  return s4() + s4() + '-' + s4();
};

function getById(id, clientsSet) {
  arrayClients = Array.from(clientsSet)
  let ret
  arrayClients.forEach(client => {

    if (client.id == id)
      ret = client
  });
  return ret
}

app.locals.game_manager = new GameManager()
//app.locals.game_manager.createRoom()

function handleMessages(message, ws_id) {

  const { action, room_id } = message
  let room
  if (action != 'create_room')
    room = app.locals.game_manager.getRoom(room_id)
  if (true) {
    let { username } = message
    let res, game_id, card
    let ret, key, arr, balance, value
    switch (action) {

      case "join_room":

        [ret, key] = room.addPlayer(username, ws_id)

        if (ret == 0)
          res = { type: "res_join_room", status: "success", key: key }
        else if (ret == 1)
          res = { type: "res_join_room", status: "fail", error_message: "Username already taken" }
        return res;

      case 'create_room':
        game_id = app.locals.game_manager.createRoom();
        arr = (app.locals.game_manager.getRoom(game_id)).addNewPlayer(username, ws_id);
        [ret, key] = arr
        if (ret == 0)
          res = { "type": "res_create_room", "status": "success", "room_id": game_id + '', "key": key }
        else
          res = { "type": "res_create_room", "status": "fail", "error_message": "Unknown error :(" }
        return res

      case "ready_up":
        key = message.key
        ret = room.readyPlayer(username, key)

        if (ret == 0) {
          res = { type: "res_ready", status: "success" }
        }
        else
          res = { type: "res_ready", status: "fail", error_message: "Key check fail" }
        return res

      case "hit":
        key = message.key
        arr = room.hitPlayer(username, key)
        ret = arr[0]
        card = arr[1]
        value = arr[2]

        if (ret == 0)
          res = { type: "res_hit", status: "success", new_card: card, hand_value: value }
        else
          res = { type: "res_hit", status: "fail", error_message: card }
        return res

      case "hold":
        key = message.key
        ret = room.standPlayer(username, key)

        if (ret == 0)
          res = { type: "res_hold", status: "success" }
        else
          res = { type: "res_hold", status: "fail", error_message: "Key check fail" }
        return res

      case "bet":
        key = message.key
        value = message.value
        arr = room.makeBetPlayer(username, key, value)
        console.log(arr)
        ret = arr[0]
        balance = arr[1]
        if (ret == 0) {
          res = { type: "res_bet", status: "success", new_balance: balance }
        }
        else if (ret == 2) {
          res = { type: "res_bet", status: "fail", error_message: "Invalid bet amount" }
        }
        else {
          res = { type: "res_bet", status: "fail", error_message: "Key check fail" }
        }

        return res

      case "exit":
        key = message
        ret = room.removePlayer(username, key)

        if (ret == 0)
          res = { type: "res_exit", status: "success" }
        else if (ret == 2)
          res = { type: "res_exit", status: "fail", error_message: "Player doesnt exist" }
        else
          res = { type: "res_exit", status: "fail", error_message: "Key check fail" }
        return res;


    }
  }
}

wsServer.on('connection', (ws) => {

  console.log("Connection started!")
  ws.id = wsServer.getUniqueID()


  ws.on('message', (message) => {


    let msg_data = JSON.parse(message)
    console.log(`Message received. Content: ${JSON.stringify(msg_data)}`)

    let room = app.locals.game_manager.getRoom(msg_data.room_id)
    let response = handleMessages(msg_data, ws.id)

    // if (response.type == "res_bet") {
    //   sleep(1000).then(() => {
    //     ws.send(JSON.stringify({ type: "game_start", players: game_start_info[ws.id] }))
    //     sleep(4000).then(() => {
    //       ws.send(JSON.stringify({ type: "your_turn" }))

    //       sleep(1000).then(() => {
    //         let cards = room.dealCards()
    //         ws.send(JSON.stringify({ type: "deal_card", cards: cards }))
    //       });

    //     });
    //   });
    // }

    ws.send(JSON.stringify(response))
    console.log(`Response: ${JSON.stringify(response)}`)
    let game_start_info

    if (msg_data.action != 'create_room') {
      switch (room.state) {
        case 'waitRoom':
          if (room.checkAllReady()) {

            room.state = 'startGame'
            console.log("All players ready. Waiting for bets...")
            room.resetReady()
          }
          break;
        case 'startGame':
          if (room.checkAllReady()) {
            room.state = 'waitPlayers'
            console.log("All bets were made. Current turn:")
            game_start_info = room.getStartInfo()

            Array.from(wsServer.clients).forEach((socket) => {
              if (game_start_info[socket.id] != null) {
                msg = { type: "game_start", players: Object.values(game_start_info) }
                console.log("Message:", msg)
                socket.send(JSON.stringify(msg))
              }
            })
            let cards = room.dealCards()
            let player, socket
            for (player_id in room.players) {
              player = room.players[player_id]

              socket = getById(player.ws_id, wsServer.clients)
              socket.send(JSON.stringify({ type: "deal_card", cards: cards }))
            }

            let current_player_username = room.getUsernames()[room.current_turn]
            let current_player = room.players[current_player_username]
            room.resetReady()
            getById(current_player.ws_id, wsServer.clients).send(JSON.stringify({ type: "your_turn" }))
          }
          break;
        case 'waitPlayers':
          if (response.status == 'success') {
            if (response.type == 'res_hit') {
              let all_sockets = room.getSockets()
              for (socket in all_sockets) {
                getById(socket, wsServer.clients).send(JSON.stringify({
                  type: "update_op", username: msg_data.username, new_card: response.new_card, hand_value: response.hand_value
                }))
              }
            }
            if (!room.checkAllReady()) {
              room.current_turn += 1
              if (room.current_turn == length(room.players))
                current_turn == 0
              while (!room.getCurrentPlayer().hand.can_hit)
                room.current_turn += 1
              getById(room.getCurrentPlayer().ws_id, wsServer.clients).send(JSON.stringify({ type: "your_turn" }))
            }
            else {
              room.state = 'endRound'
              const winner = room.checkWinner()
              game_start_info = room.getStartInfo()
              Array.from(wsServer.clients).forEach((socket) => {

                if (game_start_info[socket.id] != null) {
                  let message
                  if (game_start_info[socket.id].username == winner)
                    message = { type: "round_end",result:"win", new_balance: game_start_info[socket.id].balance }
                  else
                    message = { type: "round_end",result:"loss", new_balance: game_start_info[socket.id].balance }
                  console.log('Message:' + message)
                  socket.send(message)
                }
              });
              room.state = 'startGame'
              room.deck.resetDeck()
            }
            break;
          }
      }
    }

  })

  //ws.send("You are now connected to the game server")

})



module.exports = app;
