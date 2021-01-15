


class Hand{

    cards:Array<Card>
    can_hit:boolean

    constructor(cards:Array<Card>){
        this.cards = cards
        this.can_hit = true
    }


    getCount(){
        
        let count = 0
        let aces = 0
        
        this.cards.forEach(card => {
            if(card.value == 'A'){
                count += 11
                aces += 1
            }
            else if(card.value == 'K' || card.value == 'Q' || card.value == 'J')
                count +=10
            else
                count += parseInt(card.value)
        });

        while (count > 21 && aces > 0){
            count -=10
            aces -= 1
        }

        return count
    }

    addCard(new_card:Card):void{
        this.cards.push(new_card)
        if(this.getCount() > 21)
            this.can_hit = false
    }

    hold(){
        this.can_hit = false
    }


}