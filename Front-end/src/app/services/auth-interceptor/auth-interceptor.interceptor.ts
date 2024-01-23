import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor() {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('token');

    if (token) {
      // Clone the request and add the token to the headers
      const clonedRequest = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });

      return next.handle(clonedRequest);
    }

    return next.handle(request);
  }
}
