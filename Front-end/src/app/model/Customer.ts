export interface Customer {
  id: number;
  imageUrl: string;
  address: string;
  city: string;
  zipCode: string;
  state: string;
  user: {
    id: number;
    firstName: string;
  };
}