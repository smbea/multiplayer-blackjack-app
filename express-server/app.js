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


function handleMessages(message, ws_id) {

  const { action, room_id } = message
  let room = app.locals.game_manager.getRoom(room_id)

  switch (action) {

    case "join_room":
      const { username } = message
      res = room.addPlayer(username, ws_id)
      return res; //dont just return string, create response msg

    case "create_room":
      const { username } = message
      app.locals.game_manager.createRoom();
      res = room.addPlayer(username, ws_id)
      return res

    case "ready_up":
      const { username, key } = message
      res = room.readyPlayer(username, key)
      return res

    case "hit":
      const { username, key } = message
      res = room.hitPlayer(username, key)
      return

    case "hold":
      const { username, key } = message
      res = room.standPlayer(username, key)
      return

    case "bet":
      const { username, key, value } = message
      res = room.makeBetPlayer(username, key, value)
      return

    case "exit":
      const { username, key } = message
      res = room.removePlayer(username, key)
      return 

  }
}

wsServer.on('connection', (ws) => {

  console.log("Connection started!")
  ws.id = wsServer.getUniqueID()


  ws.on('message', (message) => {

    let msg_data = JSON.parse(message)
    handleMessages(msg_data)

    console.log(`Message received. TYPE:${type} username:${username}`)
    setTimeout(() => ws.send("Message received. Content:" + message))
  })

  ws.send("You are now connected to the game server")

})

app.locals.game_manager = new GameManager()

module.exports = app;
