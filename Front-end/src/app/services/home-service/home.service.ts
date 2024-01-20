import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError, catchError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HomeService {

  private readonly serverUrl: string = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  getCustomersCount(): Observable<number> {
    return this.http.get<number>(`${this.serverUrl}/customer/count`)
    .pipe(catchError(this.handleError));
  }

  getInvoicesCount(): Observable<number> {
    return this.http.get<number>(`${this.serverUrl}/invoice/count`)
    .pipe(catchError(this.handleError));
  }

  getTotalBilledAmount(): Observable<number> {
    return this.http.get<number>(`${this.serverUrl}/invoice/total-billed-amount`)
    .pipe(catchError(this.handleError));
  };


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
