var Deck = require('./Deck')
const { User } = require('./User')




function generateKey() {
    let r = Math.random().toString(36).substring(12);
    return r
}

//JUST use string and make check before action
gamestates = ['startGame', 'waitPlayers', 'waitRoom', 'updateBoard', 'endRound']


class Game {
    constructor(id, num_decks) {

        this.id = id
        this.deck = new Deck(num_decks)
        this.players = {}
        this.current_turn = 0
        this.current_pot = 0
        this.state = 'waitRoom'
        this.ready = {}

    }

    setState(state) {
        this.state = state
    }

    checkAllReady() {
        for (let key in this.ready) {
            if (!this.ready[key])
                return false
        }
        return true
    }

    resetReady(){
        for (let key in this.ready) {
            this.ready[key] = false
        }
    }

    getStartInfo() {
        let game_info = {}
        for (player in this.players) {
            const pl_info = { username: player.username, balance: player.money }
            game_info[player.ws_id] = pl_info
        }
        return game_info
    }

    getUsernames(){
        return this.players.keys()
    }

    addNewPlayer(username, ws_id) {
        key = generateKey()
        let new_player = new User(key, username, ws_id)
        if (this.players[username] == null) {
            this.players[username] = new_player
            this.ready[username] = false
            return [0, key]
        }
        else {
            return [1, -1]
        }
    }

    readyPlayer(username, key) {
        if (this.checkKey(username, key)) {
            this.ready[username] = true
            return 0
        }
        return 1
    }

    checkKey(username, key) {
        return key == this.players[username].key
    }

    hitPlayer(username, key) {
        if (this.checkKey(username, key) && this.players[username].hand.can_hit) {
            let new_card = this.deck.getTopDeck()
            this.players[username].hand.addCardToHand(new_card)
            const hand_value = this.players[username].hand.getCount()
            return [0, new_card, hand_value]
        }
        return [1, -1, -1]
    }

    standPlayer(username, key) {
        if (this.checkKey(username, key)) {
            this.players[username].hand.hold()
            return 0
        }
        return 1
    }

    makeBetPlayer(username, key, bet_value) {
        if (this.checkKey(username, key)) {
            if (current_bet <= this.players[username].money) {
                this.players[username].current_bet = bet_value
                this.players[username].money -= bet_value
                this.ready[username] = true
                this.current_pot += bet_value
                return [0, this.players[username].money]
            }
            return [2, -1]
        }
        return [1, -1]
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