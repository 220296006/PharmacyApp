export interface Invoice{
    id: number;
    amount: BigInt;
    due_date: string;
    paid: boolean;
    customer:{
        id: number;
    }

}