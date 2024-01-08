import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { Customer } from 'src/app/model/customer';
import { ApiResponse } from 'src/app/model/api-response';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {
  private readonly serverUrl: string = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  getAllCustomerData(
    name: string = '',
    page: number = 0,
    size: number = 10
  ): Observable<ApiResponse<Customer>> {
    return this.http
      .get<ApiResponse<Customer>>(
        `${this.serverUrl}/customer/all?name=${name}&page=${page}&size=${size}`
      )
      .pipe(catchError(this.handleError));
  }

  getCustomerById(customerId: number): Observable<ApiResponse<Customer>> {
    const url = `${this.serverUrl}/read/${customerId}`;
    return this.http.get<ApiResponse<Customer>>(url).pipe(catchError(this.handleError));
  }
  

  handleError(error: HttpErrorResponse) {
    if (error.status === 0) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong.
      console.error(
        `Backend returned code ${error.status}, body was: `,
        error.error
      );
    }
    // Return an observable with a user-facing error message.
    return throwError(
      () => new Error('Something bad happened; please try again later.')
    );
  }
}
