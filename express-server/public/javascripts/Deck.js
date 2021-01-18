"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const Card_1 = require("./Card");
const Hand_1 = require("./Hand");
export class Deck {
    constructor(num_decks) {
        this.cards_deck = [];
        this.one_deck = [];
        this.values = ['a', 'k', 'q', 'j', '10', '9', '8', '7', '6', '5', '4', '3', '2'];
        this.suits = ['spades', 'clubs', 'diamonds', 'hearts'];
        this.num_decks = num_decks;
        this.values.forEach(value => {
            this.suits.forEach(suit => {
                this.one_deck.push(new Card_1.Card(value, suit));
            });
        });
        for (let i = 0; i < num_decks; i++)
            this.cards_deck = this.cards_deck.concat(this.one_deck);
        this.shuffleDeck();
    }
    shuffleDeck() {
        for (let i = this.cards_deck.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [this.cards_deck[i], this.cards_deck[j]] = [this.cards_deck[j], this.cards_deck[i]];
        }
    }
    getTopDeck() {
        let top = Object.assign({}, this.cards_deck.pop());
        return top;
    }
    resetDeck() {
        this.cards_deck = [];
        for (let i = 0; i < this.num_decks; i++)
            this.cards_deck = this.cards_deck.concat(this.one_deck);
        this.shuffleDeck();
    }
    drawHands(num_players) {
        let hands = [];
        let dealerHand;
        let c1, c2;
        for (let i = 0; i < (num_players); i++) {
            c1 = this.getTopDeck();
            c2 = this.getTopDeck();
            hands.push(new Hand_1.Hand([c1, c2]));
        }
        let cd1 = this.getTopDeck();
        let cd2;
        this.cards_deck.forEach(card => {
            if (card.value == c2.value && card.suit == c2.suit)
                cd2 = card;
        });
        if (cd2 == undefined)
            cd2 = this.getTopDeck();
        cd2.show = false;
        dealerHand = new Hand_1.Hand([cd1, cd2]);
        return { hands, dealerHand };
    }
}

//# sourceMappingURL=Deck.js.map