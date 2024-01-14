export interface Invoice{
    id: number;
    amount: BigInt;
    due_date: Date;
    paid: boolean;
    customer:{
        id: number;
    }

}