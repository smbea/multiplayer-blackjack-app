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

wsServer.getById = function (id) {
  for (client in this.clients) {
    if (client.id == id)
      return client
  }
}


function handleMessages(message, ws_id) {

  const { action, room_id } = message
  let room = app.locals.game_manager.getRoom(room_id)


  switch (action) {

    case "join_room":
      const { username } = message
      const [ret, key] = room.addPlayer(username, ws_id)
      let res;
      if (ret == 0)
        res = { type: "res_join_room", status: "success", key: key }
      else if (ret == 1)
        res = { type: "res_join_room", status: "fail", error_message: "Username already taken" }
      return res;

    case "create_room":
      const { username } = message
      const game_id = app.locals.game_manager.createRoom();
      const [ret, key] = room.addPlayer(username, ws_id)
      let res;
      if (ret == 0)
        res = { type: "res_create_room", status: "success", room_id: game_id, key: key }
      else
        res = { type: "res_create_room", status: "fail", error_message: "Unknown error :(" }
      return res

    case "ready_up":
      const { username, key } = message
      const ret = room.readyPlayer(username, key)
      let res;
      if (ret == 0) {
        res = { type: "res_ready", status: "success" }
      }
      else
        res = { type: "res_ready", status: "fail", error_message: "Key check fail" }
      return res

    case "hit":
      const { username, key } = message
      const [ret, card, value] = room.hitPlayer(username, key)
      let res;
      if (ret == 0)
        res = { type: "res_hit", status: "success", new_card: card, hand_value: value }
      else
        res = { type: "res_hit", status: "fail", error_message: card }
      return

    case "hold":
      const { username, key } = message
      const ret = room.standPlayer(username, key)
      let res;
      if (ret == 0)
        res = { type: "res_hold", status: "success" }
      else
        res = { type: "res_hold", status: "fail", error_message: "Key check fail" }
      return

    case "bet":
      const { username, key, value } = message
      const [ret, balance] = room.makeBetPlayer(username, key, value)
      let res;
      if (ret == 0) {
        res = { type: "res_bet", status: "success", new_balance: balance }
      }
      else if (ret == 2) {
        res = { type: "res_exit", status: "fail", error_message: "Invalid bet amount" }
      }
      else {
        res = { type: "res_exit", status: "fail", error_message: "Key check fail" }
      }

      return res

    case "exit":
      const { username, key } = message
      const res = room.removePlayer(username, key)
      let res;
      if (ret == 0)
        res = { type: "res_exit", status: "success" }
      else if (ret == 2)
        res = { type: "res_exit", status: "fail", error_message: "Player doesnt exist" }
      else
        res = { type: "res_exit", status: "fail", error_message: "Key check fail" }
      return res;


  }
}

wsServer.on('connection', (ws) => {

  console.log("Connection started!")
  ws.id = wsServer.getUniqueID()


  ws.on('message', (message) => {

    let msg_data = JSON.parse(message)
    let room = app.locals.game_manager.getRoom(msg_data.room_id)
    let response = handleMessages(msg_data)

    console.log(`Message received. Content: ${msg_data}`)
    ws.send(JSON.stringify(response))

    switch (room.state) {
      case 'waitRoom':
        if (room.checkAllReady()) {
          game_start_info = room.getStartInfo()
          for (ws_client in wsServer.clients) {
            if (game_start_info[ws_client.id] != null)
              ws_client.send(JSON.stringify({ type: "game_start", players: game_start_info[ws_client.id] }))
          }
          room.state = 'startGame'
          console.log("All players ready. Waiting for bets...")
          room.resetReady()
        }
        break;
      case 'startGame':
        if (room.checkAllReady()) {
          room.state = 'waitPlayers'
          console.log("All bets were made. Current turn:")
          let current_player_username = room.getUsernames()[room.current_player]
          let current_player = room.players[current_player_username]
          room.resetReady()
          wsServer.getById(current_player.ws_id).send(JSON.stringify({ type: "your_turn" }))
        }
        break;
      case 'waitPlayers':
        if (response.status == 'success') {
          if (response.type == 'res_hit') {
            let all_sockets = room.getSockets()
            for (socket in all_sockets) {
              wsServer.getById(socket).send(JSON.stringify({
                type: "update_op", username: msg_data.username, new_card: response.new_card, hand_value: response.hand_value
              }))
            }
          }
          if (!room.checkAllReady()) {
            room.current_turn += 1
            while (!room.getCurrentPlayer().hand.can_hit)
              room.current_turn += 1
            wsServer.getById(room.getCurrentPlayer().ws_id).send(JSON.stringify({ type: "your_turn" }))
          }
          else
            room.state = 'endRound'
        }
    }

  })

  ws.send("You are now connected to the game server")

})

app.locals.game_manager = new GameManager()
//app.locals.game_manager.createRoom()

module.exports = app;
