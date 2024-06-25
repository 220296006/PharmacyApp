export interface ApiResponse<T> {
  imageData: string | ArrayBuffer;
  token: any;
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