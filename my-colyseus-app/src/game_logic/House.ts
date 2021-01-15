import { Hand } from "./Hand";


export class House{
    hand:Hand

    constructor(){

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