import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, of, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { ApiResponse } from 'src/app/model/api-response';
import { jwtDecode } from 'jwt-decode';
import { User } from 'src/app/model/user';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly  apiUrl: string = 'http://localhost:8080'; 
  private userSubject = new BehaviorSubject<User | null>(null);
  public currentUser: Observable<User | null>;


  constructor(private http: HttpClient) {
    this.currentUser = this.userSubject.asObservable();
    const token = this.getToken();
    if (token) {
      this.decodeTokenAndSetUser(token); // Set initial user from token if available
    }
  }

  getPermissionsFromToken(): string[] {
    const token = this.getToken();
    if (token) {
      const decodedToken: any = jwtDecode(token);
      return decodedToken.permissions?.flatMap((role: any) => role.permissions) || [];
    } else {
      return [];
    }
  }

  decodeTokenAndSetUser(token: string) {
    try {
      const decodedToken: any = jwtDecode(token);
      //console.log('Decoded token:', decodedToken);
      const user = this.buildUserFromToken(decodedToken);
      this.userSubject.next(user);
      // Extract and log permissions
      if (decodedToken.permissions) {
        console.log('User permissions:', decodedToken.permissions);
      } else {
        console.log('No permissions found in the token.');
      }
    } catch (error) {
      console.error('Error decoding token:', error);
      this.userSubject.next(null); // Set user to null on decode error
    }
  }


  updateCurrentUser(user: User) {
    sessionStorage.setItem('loggedInUser', JSON.stringify(user));
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
    const forgotPasswordUrl = `${this.apiUrl}/user/password-reset/forgot`;
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
        // Check if the response contains a valid token and user object
        if (response.token && response.user) {
          this.saveToken(response.token);
          console.log('Token saved successfully:', response.token);
          // Store user info in session storage
          this.storeUserInfo(response.user);
          // Update user upon successful login using token information
          this.decodeTokenAndSetUser(response.token);
        } else {
          console.error("Login response doesn't contain a token or user object");
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

  storeUserInfo(userInfo: User) {
    sessionStorage.setItem('loggedInUser', JSON.stringify(userInfo));
    //console.log('User information stored in session storage:', userInfo);
  }

  getLoggedInUser(): User | null {
    const userInfo = sessionStorage.getItem('loggedInUser');
    const loggedInUser = userInfo ? JSON.parse(userInfo) : null;
    //console.log('Retrieved logged-in user information from session storage:', loggedInUser);
    return loggedInUser;
  }



  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }
}
