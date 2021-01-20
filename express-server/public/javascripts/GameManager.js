const Game = require("./Game")

class GameManager{
    
    constructor(){
        this.game_list = {}
    }

    getRoom(id){
        return this.game_list[id];
    }

    createRoom(){
        let id = Math.floor((1 + Math.random()*1000));
        let new_game = new Game(id, 1)
        this.game_list[id] = new_game
        return id
    }
}

module.exports = GameManager