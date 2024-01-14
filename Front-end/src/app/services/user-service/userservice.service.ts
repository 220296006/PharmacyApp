import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
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
    size: number = 50
  ): Observable<ApiResponse<User[]>> {
    return this.http.get<ApiResponse<User[]>>(
      `${this.serverUrl}/user/all?name=${name}&page=${page}&size=${size}`
    );
  }

  getUserById(id: number): Observable<ApiResponse<User>> {
    return this.http
      .get<ApiResponse<User>>(`${this.serverUrl}/user/read/${id}`)
      .pipe(catchError(this.handleError));
  }

  updateUserData(id: number, user: User): Observable<ApiResponse<User>> {
    return this.http
      .put<ApiResponse<User>>(`${this.serverUrl}/user/update/${id}`, user)
      .pipe(catchError(this.handleError));
  }

  deleteUserById(id: number): Observable<ApiResponse<User>> {
    return this.http
      .delete<ApiResponse<User>>(`${this.serverUrl}/user/delete/${id}`)
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
