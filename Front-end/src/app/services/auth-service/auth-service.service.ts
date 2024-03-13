import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, of, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { ApiResponse } from 'src/app/model/api-response';
import { User } from 'src/app/model/user';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080';
  private userSubject = new BehaviorSubject<User | null>(null);
  public currentUser: Observable<User | null>;


  constructor(private http: HttpClient){
    this.currentUser = this.userSubject.asObservable();
    const token = this.getToken();
    if (token) {
      this.decodeTokenAndSetUser(token); // Set initial user from token if available
    }
  }


  decodeTokenAndSetUser(token: string) {
    try {
      const decodedToken: any = jwtDecode(token);
      const user = this.buildUserFromToken(decodedToken);
      this.userSubject.next(user);
    } catch (error) {
      console.error('Error decoding token:', error);
      this.userSubject.next(null); // Set user to null on decode error
    }
  }

  updateCurrentUser(user: User) {
    this.userSubject.next(user);
  }

  buildUserFromToken(decodedToken: any): User {
    const email = decodedToken.sub || '';
    const [firstName, lastName] = email.split('@')[0].split('.').filter((part: string) => part.trim());
    const userRole = decodedToken.roles ? decodedToken.roles[0] : '';

    return {
      email,
      id: decodedToken.id || 0,
      firstName,
      middleName: decodedToken.middleName || '',
      lastName,
      address: decodedToken.address || '',
      phone: decodedToken.phone || '',
      password: '',
      imageUrl: decodedToken.imageUrl || '',
      enabled: decodedToken.enabled || false,
      isUsingMfa: decodedToken.isUsingMfa || false,
      createdAt: decodedToken.createdAt ? new Date(decodedToken.createdAt) : new Date(),
      isNotLocked: decodedToken.isNotLocked || false,
      role: userRole || '',
    };
  }


  updateUserRole(userId: number, newRole: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/roles/updateUserRole/${userId}`, { roleName: newRole });
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
          console.log('Token saved successfully:', response.token);
        } else {
          console.error("Login response doesn't contain a token");
        }
      }),
      catchError(this.handleError<any>('Login'))
    );
  }
  

  public saveToken(token: string){
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

   decodeToken(): User {
    const token = this.getToken();
    if (!token) {
      throw new Error('No token available');
    }
  
    const decodedToken: any = jwtDecode(token);
    const email = decodedToken.sub || '';
    const [firstName, lastName] = email.split('@')[0].split('.').filter((part: string) => part.trim());
    const userRole = decodedToken.roles ? decodedToken.roles[0] : '';
  
    return {
      email: email,
      id: decodedToken.id || 0,
      firstName: firstName || '',
      middleName: decodedToken.middleName || '',
      lastName: lastName || '',
      address: decodedToken.address || '',
      phone: decodedToken.phone || '',
      password: '',
      imageUrl: decodedToken.imageUrl || '',
      enabled: decodedToken.enabled || false,
      isUsingMfa: decodedToken.isUsingMfa || false,
      createdAt: decodedToken.createdAt ? new Date(decodedToken.createdAt) : new Date(),
      isNotLocked: decodedToken.isNotLocked || false,
      role: userRole || ''
    };
  }

  getUserInfo():Observable<ApiResponse<User>> {
    const token = localStorage.getItem('token');
    if (!token) {
      throw new Error('No token available');
    }
    return this.http.get<ApiResponse<User>>(`${this.apiUrl}/user/info`, {
      headers: { Authorization: token }
  }).pipe(
      catchError(error => {
          console.error('Error fetching user info:', error);
          return throwError('Error fetching user info');
      })
  );
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }
}
