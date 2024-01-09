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

  constructor(private http: HttpClient) { }

  getAllUserData(
    name: string = '',
    page: number = 0,
    size: number = 10
  ): Observable<ApiResponse<User>> {
    return this.http.get<ApiResponse<User>>(
      `${this.serverUrl}/user/all?name=${name}&page=${page}&size=${size}`
    );
  }

  getUserById(userId: number): Observable<ApiResponse<User>> {
    return this.http.get<ApiResponse<User>>(`${this.serverUrl}/user/update/${userId}`)
    .pipe(catchError(this.handleError));
  }


  onUpdateUser(id: number, user: User): Observable<ApiResponse<User>> {
    return this.http
      .get<ApiResponse<User>>(`${this.serverUrl}/user/update/${id}`)
      .pipe(catchError(this.handleError));
  }

  updateUser(id: number, user: User): Observable<ApiResponse<User>> {
    return this.http
      .put<ApiResponse<User>>(`${this.serverUrl}/user/update/${id}`, user)
      .pipe(catchError(this.handleError));
  }

  deleteUser(id: number): Observable<ApiResponse<User>> {
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
