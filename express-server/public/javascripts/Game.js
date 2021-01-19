var Deck = require('./Deck')
const { User } = require('./User')




function generateKey() {
    let r = Math.random().toString(36).substring(12);
    return r
}

//JUST use string and make check before action
gamestates = ['startGame','waitPlayers','waitRoom', 'updateBoard','endRound']


class Game {
    constructor(id, num_decks) {

        this.id = id
        this.deck = new Deck(num_decks)
        this.players = {}
        this.state = 'waitRoom'
        this.ready = {}

    }

    setState(state) {
        this.state = state
    }

    


    addNewPlayer(username, ws_id) {
        key = generateKey()
        let new_player = new User(key, username, ws_id)
        if (this.players[username] == null) {
            this.players[username] = new_player
            this.ready[username] = false
            return "Successfully joined game"
        }
        else {
            err = "Connection refused: Attempted connection had equal username to already existing player"
            console.log(err)
            return err
        }
    }

    readyPlayer(username, key) {
        if (this.checkKey(username, key)) {
            this.ready[username] = true
            return "success"
        }
        return "Error: Key check failed"
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

    removePlayer(username, key) {
        if (this.checkKey(username, key)) {
            if (this.players[username] != null) {
                delete this.players[username]
                return 0
            }
            return 2
        }
        return 1
    }
}

module.exports = Game