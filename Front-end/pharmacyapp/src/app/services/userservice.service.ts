import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from '../model/api-response';
import { Page } from '../model/page';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly serverUrl: string = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  getAllUserData(name: string = '', page: number = 0, size: number = 10): Observable<ApiResponse<Page>> {
    return this.http.get<ApiResponse<Page>>(`${this.serverUrl}/user/all?name=${name}&page=${page}&size=${size}`);
  }
}
