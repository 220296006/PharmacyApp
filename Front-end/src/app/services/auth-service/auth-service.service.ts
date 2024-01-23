import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { ApiResponse } from 'src/app/model/api-response';
import { User } from 'src/app/model/user';

@Injectable({
  providedIn: 'root'
})
export class AuthService {


  private apiUrl = 'http://localhost:8080'; // Replace with your server URL

  constructor(private http: HttpClient) { }

  registerUser(user: User): Observable<ApiResponse<User>>{
    console.log(user)
    return this.http.post<ApiResponse<User>>
    (`${this.apiUrl}/user/register`, user)
  }  

  login(email: string, password: string): Observable<any> {
    const loginUrl = `${this.apiUrl}/login`;
    const body = { email, password };

    return this.http.post<any>(loginUrl, body)
      .pipe(
        tap(response => this.saveToken(response.token)),
        catchError(this.handleError<any>('Login'))
      );
  }

  public saveToken(token: string): void {
    localStorage.setItem('token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  logout(): void {
    localStorage.removeItem('token');
  }

  isLoggedIn(): boolean {
    return this.getToken() !== null;
  }

  getUserInfo(token: string): Observable<User> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<User>('apiUrl', { headers })
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }


  
}
