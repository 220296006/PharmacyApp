import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor() {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('token');

    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }

    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          console.error('Unauthorized request. Redirecting to login page.');
          // Handle unauthorized access, e.g., redirect to login page
        } else if (error.status === 403) {
          console.error('Forbidden request. Redirecting to access denied page.');
          // Handle forbidden access, e.g., redirect to access denied page
        } else {
          console.error('HTTP error occurred:');
          console.error(error);
          // Handle other HTTP errors as needed
        }
        return throwError(error);
      })
    );
  }
}
