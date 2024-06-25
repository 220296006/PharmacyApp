export interface Customer {
  id: number;
  city: string;
  zipCode: string;
  state: string;
  user: {
    id: number;
    firstName: string;
    imageUrl: string;
    address: string;
  };
}
