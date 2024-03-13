import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse,
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private snackBar: MatSnackBar, private router: Router) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('token');

    if (token) {
      try {
        const decodedToken: any = jwtDecode(token);
        const roles: string[] = decodedToken.roles || [];

        // Check if the user has any of the allowed roles
        if (roles.includes('ROLE_ADMIN') || roles.includes('ROLE_MANAGER') || roles.includes('ROLE_SYSADMIN')) {
          request = request.clone({
            setHeaders: {
              Authorization: `Bearer ${token}`,
            },
          });
        }
      } catch (error) {
        console.error('Error decoding JWT token:', error);
      }
    }

    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 403) {
          console.error('Forbidden request. Redirecting to access denied page.');
          this.snackBar.open('You don\'t have permission to access this resource.', 'Close', {
            duration: 5000,
            horizontalPosition: 'center',
            verticalPosition: 'top',
          });
          // Navigate to the access denied page
          this.router.navigate(['/access-denied']);
        } else {
          console.error('HTTP error occurred:', error);
          this.snackBar.open('An unexpected error occurred. Please try again later.', 'Close', {
            duration: 5000,
            horizontalPosition: 'center',
            verticalPosition: 'top',
          });
        }
        // Throw the error to propagate it further
        return throwError(error);
      })
    );
  }
}