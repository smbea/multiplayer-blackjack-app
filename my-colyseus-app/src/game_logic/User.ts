import { Handler } from "express"
import {Hand} from "./Hand"


export class User{
    money:number
    hand:Hand
    key:string
    username:string
    current_bet:number

    constructor(key:string, username:string){
        
        this.key = key
        this.username = username

        this.money = 500

        this.current_bet = 0

        

    }
}