import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiResponse } from '../model/api-response';
import { Page } from '../model/page';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly serverUrl: string = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

    // Make call to the back end API to retrieve page of users
    users$ = (name: string = '', page: number = 0, size: number = 10): Observable<ApiResponse<Page>> => 
    this.http.get<ApiResponse<Page>>(`${this.serverUrl}/users/all?name=${name}&page=${page}&size=${size}`);

}
