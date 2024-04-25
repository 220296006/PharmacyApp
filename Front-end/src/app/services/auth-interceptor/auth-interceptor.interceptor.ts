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

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private snackBar: MatSnackBar, private router: Router) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('token');
    
    //console.log('Interceptor: Token from localStorage:', token);

    if (token) {
      //console.log('Interceptor: Adding Authorization header');
      const authReq = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      });

      //console.log('Interceptor: Request after adding Authorization header:', authReq);
      return next.handle(authReq);
    }

    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('Interceptor: HTTP error occurred:', error);

        if (error.status === 403) {
          console.error('Interceptor: Forbidden request. Redirecting to access denied page.');
          this.snackBar.open('You don\'t have permission to access this resource.', 'Close', {
            duration: 5000,
            horizontalPosition: 'center',
            verticalPosition: 'top',
          });
          // Navigate to the access denied page
          //this.router.navigate(['/access-denied']);
        } else {
          console.error('Interceptor: An unexpected error occurred. Please try again later.');
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
