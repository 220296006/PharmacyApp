import { PaymentStatus } from "./payment-status";

export interface Invoice {
    id: number;
    amount: number;
    dueDate: Date;
    paymentStatus: PaymentStatus;
    customer: {
      id: number;
    };
  }
  

  