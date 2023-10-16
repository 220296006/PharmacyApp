import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from 'src/app/model/api-response';
import { User } from 'src/app/model/user';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private readonly serverUrl: string = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  getAllUserData(
    name: string = '',
    page: number = 0,
    size: number = 10
  ): Observable<ApiResponse<User>> {
    return this.http.get<ApiResponse<User>>(
      `${this.serverUrl}/user/all?name=${name}&page=${page}&size=${size}`
    );
  }
}
