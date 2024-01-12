export interface ApiResponse<T> {
  timeStamp: string;
  statusCode: number;
  status: string;
  message: string;
  data: {
    user?: T; 
    page?: T; 
    customer?: T;
    medication?: T;
  };
}
