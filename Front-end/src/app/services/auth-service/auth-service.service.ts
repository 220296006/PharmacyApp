import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { ApiResponse } from 'src/app/model/api-response';
import { User } from 'src/app/model/user';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  loginAsAdmin(email: string, password: string): Observable<any> {
    const loginUrl = `${this.apiUrl}/user/login/admin`;
    const body = { email, password };

    return this.http.post<any>(loginUrl, body).pipe(
      tap((response) => console.log('Login response:', response)),
      tap((response) => {
        // Check if the response contains a valid token
        if (response.token) {
          this.saveToken(response.token);
        } else {
          console.error("Login response doesn't contain a token");
        }
      }),
      catchError(this.handleError<any>('Login'))
    );
  }

  forgotPassword(email: string): Observable<ApiResponse<any>> {
    const forgotPasswordUrl = `${this.apiUrl}/user/forgot-password`;
    const body = { email };

    return this.http.post<ApiResponse<any>>(forgotPasswordUrl, body);
  }

  registerUser(user: User): Observable<ApiResponse<User>> {
    console.log('Registering user:', user);
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.post<ApiResponse<User>>(
      `${this.apiUrl}/user/register`,
      user,
      { headers }
    );
  }

  login(email: string, password: string): Observable<any> {
    const loginUrl = `${this.apiUrl}/user/login`;
    const body = { email, password };

    return this.http.post<any>(loginUrl, body).pipe(
      tap((response) => console.log('Login response:', response)),
      tap((response) => {
        // Check if the response contains a valid token
        if (response.token) {
          this.saveToken(response.token);
        } else {
          console.error("Login response doesn't contain a token");
        }
      }),
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
    const token = localStorage.getItem('token');
    if (!token) {
      return of(null);
    }
    const decodedToken: any = jwtDecode(token);
    console.log('Decoded Token:', decodedToken);

    // Extract user information from the token
    const email = decodedToken.sub;
    const [firstName, lastName] = email.split('@')[0].split('.').filter((part: string) => part.trim());
    const initials = firstName.charAt(0) + lastName.charAt(0);

    // Extract user role from the token (assuming it's included in the token payload)
    const userRole = decodedToken.role; // Adjust this according to your token structure

    // Create user object including role
    const user: User = {
      email: initials,
      id: 0,
      firstName: 'firstName',
      middleName: 'astName',
      lastName: '',
      address: '',
      phone: '',
      password: '',
      imageUrl: '',
      enabled: false,
      isUsingMfa: false,
      createdAt: undefined,
      isNotLocked: false,
      role: userRole // Add the role to the user object
    };

    return of(user);
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }
}
