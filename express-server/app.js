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


const wsServer = new WebSocket.Server({ port: 8080 })

wsServer.on('connection', (ws) => {
  console.log("Connection started!")

  ws.on('message', (message) => {

    let msg_data = JSON.parse(message)
    const { type, username } = msg_data
    if (type == 'new_player')
      app.locals.game.gameEmitter.emit('new_player', username)

    console.log(`Message received. TYPE:${type} username:${username}`)
    setTimeout(() => ws.send("Message received. Content:" + message))
  })

  ws.send("You are now connected to the game server")

})



app.locals.game = new Game(1, 1)
//app.locals.game.startGameLoop()



module.exports = app;
