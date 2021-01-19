"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.Hand = void 0;
class Hand {
    constructor(cards) {
        this.cards = cards;
        this.can_hit = true;
    }
    getCount() {
        let count = 0;
        let aces = 0;
        this.cards.forEach(card => {
            if (card.value == 'A') {
                count += 11;
                aces += 1;
            }
            else if (card.value == 'K' || card.value == 'Q' || card.value == 'J')
                count += 10;
            else
                count += parseInt(card.value);
        });
        while (count > 21 && aces > 0) {
            count -= 10;
            aces -= 1;
        }
        return count;
    }
    addCard(new_card) {
        this.cards.push(new_card);
        if (this.getCount() > 21)
            this.can_hit = false;
    }
    hold() {
        this.can_hit = false;
    }
}
exports.Hand = Hand;
//# sourceMappingURL=Hand.js.map