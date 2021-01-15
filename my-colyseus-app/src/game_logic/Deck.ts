import {Card} from "./Card";
 

class Deck{    
    
    cards_deck:Array<Card> = []
    one_deck:Array<Card> = []
    num_decks:number

    values = ['A','K','Q','J','10','9','8','7','6','5','4','3','2']
    suits = ['spades','clubs','diamonds','hearts']

    shuffleDeck() {
        for (let i = this.cards_deck.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [this.cards_deck[i], this.cards_deck[j]] = [this.cards_deck[j], this.cards_deck[i]];
        }
    }

    constructor(num_decks:number){
        this.num_decks = num_decks
        
        this.values.forEach(value =>{
            this.suits.forEach(suit=>{
                this.one_deck.push(new Card(value,suit))
            })
        })

        for(let i = 0; i < num_decks; i++)
            this.cards_deck = this.cards_deck.concat(this.one_deck)
     
        this.shuffleDeck()
    }

    getTopDeck(){
        let top = this.cards_deck.pop()
        return top
    }

    resetDeck(){
        this.cards_deck = []
        for(let i = 0; i < this.num_decks; i++)
            this.cards_deck = this.cards_deck.concat(this.one_deck)
     
        this.shuffleDeck()
    }
}


