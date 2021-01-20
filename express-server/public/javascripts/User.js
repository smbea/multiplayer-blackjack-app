"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.User = void 0;
class User {
    constructor(key, username, ws_id) {
        this.key = key;
        this.username = username;
        this.ws_id = ws_id;
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