export interface ApiResponse<T> {
  timeStamp: string;
  statusCode: number;
  status: string;
  message: string;
  data: {
    invoice?: T;
    user?: T; 
    page?: T; 
    customer?: T;
    medication?: T;
    inventory?: T;
    prescription?: T;
    invoices?: T;
  };
}