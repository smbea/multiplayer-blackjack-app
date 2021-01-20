const Game = require("./Game")

class GameManager{
    
    constructor(){
        this.game_list = {}
    }

    getRoom(id){
        return this.game_list[id];
    }

    createRoom(){
        const id = Math.floor((1 + Math.random()) * 0x10000).toString(16).substring(5);
        let new_game = new Game(id, 1)
        return id
    }
}

module.exports = GameManager