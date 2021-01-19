"use strict";

import { Hand } from "./Hand";


export class House{
    
    

    constructor(){
        this.hand = undefined
    }

    setHand(hand){
        this.hand = hand
    }

    play(){
        let count = this.hand.getCount()

        if (count < 17)
            return 'HIT'
        else{
            this.hand.can_hit = false
            return 'HOLD'
        }
    }
}
//# sourceMappingURL=House.js.map