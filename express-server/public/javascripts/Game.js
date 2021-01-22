var Deck = require('./Deck');
const { Hand } = require('./Hand');
const { User } = require('./User')




function generateKey() {
    let r = (Math.random()).toString().substring(12);
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

    dealCards(){
        let ret = {}
        let c1, c2;
        for(let player_id in this.players){
            c1 = this.deck.getTopDeck()
            c2 = this.deck.getTopDeck()
            let player_hand = new Hand([c1,c2])

            this.players[player_id].giveHand(player_hand)

            ret[player_id] = {hand: player_hand, hand_value = player_hand.getCount()}
        }
        return ret
    }

    resetReady(){
        for (let key in this.ready) {
            this.ready[key] = false
        }
    }

    getSockets(){
        let sockets = []
        for(let player_id in this.players)
            sockets.push(this.players[player_id].ws_id)
        return sockets;
    }

    getStartInfo() {
        let game_info = {}
        let player
        for (let player_id in this.players) {
            player = this.players[player_id]
            const pl_info = { username: player.username, balance: player.money }
            game_info[player.ws_id] = pl_info
        }
        return game_info
    }

    getUsernames(){
        return Object.keys(this.players)
    }

    getCurrentPlayer(){
        let usernames = this.getUsernames()
        let current_username = usernames[this.current_turn]

        return this.players[current_username]
    }

    checkTurn(username){
        if(Object.keys(this.players)[this.current_turn] == username)
            return true
        return false
    }

    addNewPlayer(username, ws_id) {
        let key = generateKey()
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
        return (key === this.players[username].key)
    }

    hitPlayer(username, key) {
        if (this.checkKey(username, key) && this.players[username].hand.can_hit && this.checkTurn(username)) {
            let new_card = this.deck.getTopDeck()
            this.players[username].hand.addCard(new_card)
            const hand_value = this.players[username].hand.getCount()
            if (hand_value > 21)
                this.ready[username] = true
            return [0, new_card, hand_value]
        }
        return [1, -1, -1]
    }

    standPlayer(username, key) {
        if (this.checkKey(username, key) && this.checkTurn(username)) {
            this.players[username].hand.hold()
            this.ready[username] = true
            return 0
        }
        return 1
    }

    makeBetPlayer(username, key, bet_value) {
        if (this.checkKey(username, key)) {
            if (bet_value <= this.players[username].money) {
                this.players[username].current_bet = bet_value
                this.players[username].money -= bet_value
                this.ready[username] = true
                this.current_pot += bet_value
                return [0, (this.players[username].money)]
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

    checkWinner(){
        let username_winner = null
        let hand_value
        let best_value = 0

        for(let player_id in this.players){
            let player = this.players[player_id]
            hand_value = player.hand.getCount()
            if(hand_value <= 21 && hand_value > best_value){
                best_value = hand_value
                username_winner = player.username
            }
        }
        if (username_winner != null)
            this.players[username_winner].money += this.current_pot
        return username_winner
    }
}

module.exports = Game