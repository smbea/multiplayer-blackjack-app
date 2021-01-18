var { Deck } = require('./Deck')
const { User } = require('./User')

export class Game {
    constructor(id, num_decks) {
        this.id = id
        this.deck = new Deck(num_decks)
        this.players = {}
        this.state = 'wait_for_players'
        this.gameOn = true
    }

    gameLoop(){

        while(this.gameOn){
            
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
        if(this.checkKey(username,key)){
            this.players[username].hand.hold()
            return true
        }
        return false
    }

    makeBetPlayer(username,key, bet_value){
        if(this.checkKey(username,key) && current_bet <= this.players[username].money){
            this.players[username].current_bet = bet_value
            this.players[username].money -= bet_value
            return this.players[username].money
        }
        return -1
    }
}