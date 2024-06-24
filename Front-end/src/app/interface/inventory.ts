export interface Inventory{
    id: number;
    name: string;
    description: string;
    quantity: BigInt;
    price: BigInt;
    created_at: string;
    updated_at: string;
    medication:{
        id:  number
    }
}