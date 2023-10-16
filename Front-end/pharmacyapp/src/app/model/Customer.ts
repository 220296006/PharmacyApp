export interface Customer {
  id: number;
  address: string;
  city: string;
  zipCode: string;
  state: string;
  user: {
    id: number;
    firstName: string; 
  }
}
