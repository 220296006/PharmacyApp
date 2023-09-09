export interface User {
  id: number;
  firstName: string;
  middleName: string;
  lastName: string;
  email: string;
  password: string;
  address: string;
  phone: string;
  imageUrl: string;
  enabled: boolean;
  isUsingMfa: boolean;
  createdAt: Date;
  isNotLocked: boolean;
}

