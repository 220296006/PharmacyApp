import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { ApiResponse } from 'src/app/interface/api-response';
import { Customer } from 'src/app/interface/customer';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {
  private readonly serverUrl: string = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  // Method to fetch customer image data by customer ID
  getCustomerImageData(customerId: number): Observable<Blob> {
    return this.http.get(`${this.serverUrl}/user/image/${customerId}`, { responseType: 'blob' });
  }

  createCustomer(customer: Customer): Observable<ApiResponse<Customer>>{
    console.log(customer)
    return this.http.post<ApiResponse<Customer>>
    (`${this.serverUrl}/customer/create`, customer)
    .pipe(catchError(this.handleError));
  } 

  getAllCustomerData(
    name: string = '',
    page: number = 0,
    size: number = 50
  ): Observable<ApiResponse<Customer>> {
    return this.http
      .get<ApiResponse<Customer>>(
        `${this.serverUrl}/customer/all?name=${name}&page=${page}&size=${size}`
      )
      .pipe(catchError(this.handleError));
  }

  getCustomerById(customerId: number): Observable<ApiResponse<Customer>> {
    const url = `${this.serverUrl}/customer/read/${customerId}`;
    return this.http
      .get<ApiResponse<Customer>>(url)
      .pipe(catchError(this.handleError));
  }

  updateCustomerData(
    id: number,
    customer: Customer
  ): Observable<ApiResponse<Customer>> {
    return this.http
      .put<ApiResponse<Customer>>(
        `${this.serverUrl}/customer/update/${id}`,
        customer
      )
      .pipe(catchError(this.handleError));
  }

  deleteCustomerById(id: number): Observable<ApiResponse<Customer>> {
    return this.http
      .delete<ApiResponse<Customer>>(`${this.serverUrl}/customer/delete/${id}`)
      .pipe(catchError(this.handleError));
  }

  handleError(error: HttpErrorResponse) {
    if (error.status === 0) {
      console.error('An error occurred:', error.error);
    } else {
      console.error(
        `Backend returned code ${error.status}, body was: `,
        error.error
      );
    }
    return throwError(
      () => new Error('Something bad happened; please try again later.')
    );
  }
}
