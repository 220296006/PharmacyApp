export interface User {
  id: number;
  firstName: string;
  middleName: string;
  lastName: string;
  email: string;
  address: string;
  phone: string;
  password: string;
  imageUrl: string;
  enabled: boolean;
  isUsingMfa: boolean;
  createdAt: Date;
  isNotLocked: boolean;
}

