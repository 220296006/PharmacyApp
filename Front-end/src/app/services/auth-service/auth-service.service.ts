import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { ApiResponse } from 'src/app/model/api-response';
import { User } from 'src/app/model/user';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'http://localhost:8080';

  forgotPassword(email: string): Observable<ApiResponse<any>> {
    const forgotPasswordUrl = `${this.apiUrl}/user/forgot-password`;
    const body = { email };

    return this.http.post<ApiResponse<any>>(forgotPasswordUrl, body);
  }


  constructor(private http: HttpClient) { }

  registerUser(user: User): Observable<ApiResponse<User>> {
    console.log('Registering user:', user);
    return this.http.post<ApiResponse<User>>
      (`${this.apiUrl}/user/register`, user)
  }

  login(email: string, password: string): Observable<any> {
    const loginUrl = `${this.apiUrl}/user/login`;
    const body = { email, password };

    // Set the headers with the Bearer token
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${this.getToken()}`  // Replace yourTokenVariableHere with the actual token
    });

    return this.http.post<any>(loginUrl, body, { headers })
      .pipe(
        tap(response => console.log('Login response:', response)), // Add this line for logging
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

  getUserInfo(): Observable<User> {
    const token = this.getToken();
    if (!token) {
      return of(null);
    }

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<User>(`${this.apiUrl}/user/info`, { headers });
  }


  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }



}
