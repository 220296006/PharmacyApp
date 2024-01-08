import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Customer } from 'src/app/model/customer';
import { ApiResponse } from 'src/app/model/api-response';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private readonly serverUrl: string = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  getAllCustomerData(
    name: string = '',
    page: number = 0,
    size: number = 10
  ): Observable<ApiResponse<Customer>> {
    return this.http.get<ApiResponse<Customer>>(
      `${this.serverUrl}/customer/all?name=${name}&page=${page}&size=${size}`
    );
  }
}
