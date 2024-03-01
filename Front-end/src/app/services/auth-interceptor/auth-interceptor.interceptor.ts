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
import { MatSnackBar } from '@angular/material/snack-bar'; // Import MatSnackBar

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private snackBar: MatSnackBar) {} // Inject MatSnackBar

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('token');

    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      });
    }

    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          console.error('Unauthorized request. Redirecting to login page.');
          this.snackBar.open('Unauthorized request. Please log in.', 'Close', {
            duration: 5000,
            horizontalPosition: 'center',
            verticalPosition: 'top',
          });
          // Handle unauthorized access, e.g., redirect to login page
        } else if (error.status === 403) {
          console.error('Forbidden request. Redirecting to access denied page.');
          this.snackBar.open('Forbidden request. You don\'t have permission to access this resource.', 'Close', {
            duration: 5000,
            horizontalPosition: 'center',
            verticalPosition: 'top',
          });
          // Handle forbidden access, e.g., redirect to access denied page
        } else {
          console.error('HTTP error occurred:', error);
          this.snackBar.open('An unexpected error occurred. Please try again later.', 'Close', {
            duration: 5000,
            horizontalPosition: 'center',
            verticalPosition: 'top',
          });
          // Handle other HTTP errors as needed
        }
        throw error; // Throw the error directly
      })
    );
  }
}
