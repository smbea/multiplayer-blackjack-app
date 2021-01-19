"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.User = void 0;
class User {
    constructor(key, username) {
        this.key = key;
        this.username = username;
        this.money = 500;
        this.current_bet = 0;
    }

    giveHand(hand) {
        this.hand = hand
    }

    addCardToHand(card) {
        this.hand.addCard(card)
    }
}
exports.User = User;
//# sourceMappingURL=User.js.map