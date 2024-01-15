import { PaymentStatus } from "./payment-status";

export interface Invoice {
    id: number;
    amount: BigInt;
    dueDate: Date;
    paymentStatus: PaymentStatus;
    customer: {
      id: number;
    };
  }
  

  