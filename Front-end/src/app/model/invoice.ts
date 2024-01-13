export interface Invoice{
    id: number;
    amount: string;
    due_date: Date;
    paid: boolean;
    customer:{
        id: number;
    }

}