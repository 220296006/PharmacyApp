export interface Invoice{
    id: number;
    amount: BigInt;
    dueDate: Date;
    paid: boolean;
    customer:{
        id: number;
    }

}