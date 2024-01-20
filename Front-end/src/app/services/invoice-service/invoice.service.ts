import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { ApiResponse } from 'src/app/model/api-response';
import { Invoice } from 'src/app/model/invoice';

@Injectable({
  providedIn: 'root'
})
export class InvoiceService {

  private readonly serverUrl: string = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  createInvoice(invoice: Invoice): Observable<ApiResponse<Invoice>>{
    console.log(invoice)
    return this.http.post<ApiResponse<Invoice>>
    (`${this.serverUrl}/invoice/create`, invoice)
    .pipe(catchError(this.handleError));
  }  


  getAllInvoiceData(
    name: string = '',
    page: number = 0,
    size: number = 50
  ): Observable<ApiResponse<Invoice[]>> {
    return this.http
      .get<ApiResponse<Invoice[]>>(
        `${this.serverUrl}/invoice/all?name=${name}&page=${page}&size=${size}`
      )
      .pipe(catchError(this.handleError));
  }

  getInvoiceById(invoiceId: number): Observable<ApiResponse<Invoice>> {
    const url = `${this.serverUrl}/invoice/read/${invoiceId}`;
    return this.http
      .get<ApiResponse<Invoice>>(url)
      .pipe(catchError(this.handleError));
  }

  getInvoicesByCustomerId(customerId: number): Observable<ApiResponse<Invoice>> {
    const url = `${this.serverUrl}/invoice/read/customer/${customerId}`;
    return this.http
      .get<ApiResponse<Invoice>>(url)
      .pipe(catchError(this.handleError));
  }

  updateInvoiceData(
    id: number,
    invoice: Invoice
  ): Observable<ApiResponse<Invoice>> {
    return this.http
      .put<ApiResponse<Invoice>>(
        `${this.serverUrl}/inventory/update/${id}`,
        invoice
      )
      .pipe(catchError(this.handleError));
  }



deleteInvoiceById(id: number): Observable<ApiResponse<Invoice>> {
  return this.http
    .delete<ApiResponse<Invoice>>(
      `${this.serverUrl}/invoice/delete/${id}`
    )
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
