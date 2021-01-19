var Deck  = require('./Deck')
const { User } = require('./User')

const EventEmitter = require('events');

class MyEmitter extends EventEmitter { }

gamestates = {
    startGame: 2,
    waitPlayer: 1,
    waitRoom: 0,
    updateBoard: 3,
    endRound: 4
}






class Game {
    constructor(id, num_decks) {
        
        this.id = id
        this.deck = new Deck(num_decks)
        this.players = {}
        // this.state = waitRoom
        this.gameOn = true

        this.gameEmitter = new MyEmitter();
        this.setupEmitter();
        
    }


    setupEmitter(){

        this.gameEmitter.on('new_player', (username) => {
            //this.addNewPlayer(username, key)
            console.log(`A new player has joined. Username:${username}`);
        });

        this.gameEmitter.on('hit', (username, key) =>{
            //this.hitPlayer(username, key)
            console.log(`Player ${username} has hit!`)
        })

        this.gameEmitter.on('stand', (username, key) =>{
            //this.standPlayer(username, key)
            console.log(`Player ${username} has stand!`)
        })

        this.gameEmitter.on('make_bet', (username, key, bet_value) =>{
            //this.makeBetPlayer(username, key, bet_value)
            console.log(`Player ${username} has changed its bet to ${bet_value}!`)
        })
    }

    async startGameLoop() {

        while (this.gameOn) {

        }
        
    }

    addNewPlayer(username, key) {
        let new_player = new User(key, username)
        if (this.players[username] == null)
            this.players[username] = new_player
        else
            console.log("Connection refused: Attempted connection had equal username to already existing player")
    }

    checkKey(username, key) {
        return key == this.players[username].key
    }

    hitPlayer(username, key) {
        if (this.checkKey(username, key) && this.players[username].hand.can_hit) {
            let new_card = this.deck.getTopDeck()
            this.players[username].hand.addCardToHand(new_card)
            return this.players[username].hand
        }
    }

    standPlayer(username, key) {
        if (this.checkKey(username, key)) {
            this.players[username].hand.hold()
            return true
        }
        return false
    }

    makeBetPlayer(username, key, bet_value) {
        if (this.checkKey(username, key) && current_bet <= this.players[username].money) {
            this.players[username].current_bet = bet_value
            this.players[username].money -= bet_value
            return this.players[username].money
        }
        return -1
    }
}

module.exports = Game