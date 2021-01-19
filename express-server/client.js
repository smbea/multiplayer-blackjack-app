const WebSocket = require('ws')
const http = require('http')

const ws = new WebSocket('ws://localhost:8080');

ws.on('open', function open() {
    console.log('sending...') 
    ws.send(JSON.stringify({type:'new_player', username:'delta'}));
});

ws.on('message', function incoming(data) {
  console.log(data);
});

