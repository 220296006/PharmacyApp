import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, catchError, map, throwError } from 'rxjs';
import { ApiResponse } from 'src/app/model/api-response';
import { User } from 'src/app/model/user';

@Injectable({
  providedIn: 'root',
})
export class UserService {

  
  private readonly serverUrl: string = 'http://localhost:8080';

  constructor(private http: HttpClient) {}


  createUser(user: User): Observable<ApiResponse<User>>{
    console.log(user)
    return this.http.post<ApiResponse<User>>
    (`${this.serverUrl}/user/register`, user)
    .pipe(catchError(this.handleError));
  }  
  
  getAllUserData(
    name: string = '',
    page: number = 0,
    size: number = 50
  ): Observable<ApiResponse<User[]>> {
    return this.http.get<ApiResponse<User[]>>(
      `${this.serverUrl}/user/all?name=${name}&page=${page}&size=${size}`
    );
  }

  getUserById(id: number): Observable<ApiResponse<User>> {
    return this.http
      .get<ApiResponse<User>>(`${this.serverUrl}/user/read/${id}`)
      .pipe(catchError(this.handleError));
  }

  updateUserData(id: number, user: User): Observable<ApiResponse<User>> {
    return this.http.put<ApiResponse<User>>(`${this.serverUrl}/user/update/${id}`, user)
      .pipe(catchError(this.handleError));
  }

  deleteUserById(id: number): Observable<ApiResponse<User>> {
    return this.http.delete<ApiResponse<User>>(`${this.serverUrl}/user/delete/${id}`)
      .pipe(catchError(this.handleError));
  }

  uploadProfileImage(userId: number, imageFile: File): Observable<string> {
    const formData: FormData = new FormData();
    formData.append('image', imageFile);
    
    return this.http.post<string>(`${this.serverUrl}/user/image/${userId}`, formData).pipe(
      map(profileImageUrl => {
        // Return the image URL received from the server
        return profileImageUrl;
      }),
      catchError((error: HttpErrorResponse) => {
        if (error.error instanceof ErrorEvent) {
          // Client-side or network error occurred. Handle it accordingly.
          console.error('An error occurred:', error.error.message);
          return throwError('Unable to connect to server. Please check your internet connection.');
        } else {
          // Backend returned an unsuccessful response code
          console.error(
            `Backend returned code ${error.status}, body was: `,
            error.error
          );
          let errorMessage = 'Something bad happened; please try again later.';
          switch (error.status) {
            case 400: // Bad Request (e.g., empty file)
              errorMessage = 'The uploaded file is empty. Please select a valid image.';
              break;
            case 413: // Payload Too Large
              errorMessage = 'The uploaded image is too large. Please select a smaller image.';
              break;
            case 403: // Forbidden (access denied)
              errorMessage = 'You are not authorized to upload profile images.'
              break;
            case 500: // Internal Server Error
              errorMessage = 'An internal server error occurred during upload. Please try again later.';
              break;
            default:
              // Handle other potential error codes
              break;
          }
          return throwError(errorMessage);
        }
      })
    );
  }
  
  getImageData(userId: number): Observable<any> {
    const url = `${this.serverUrl}/user/image/${userId}`;
    return this.http.get(url, { responseType: 'blob' }).pipe(
      catchError(() => {
        return throwError('Failed to fetch image data');
      })
    );
  }
  

  deleteProfileImage(userId: number): Observable<any> {
    return this.http.delete(`${this.serverUrl}/user/image/${userId}`);
  }
  
  
  handleError(error: HttpErrorResponse) {
    if (error.status === 0) {
      console.error('An error occurred:', error.error);
    } else {
      console.error(
        `Backend returned code ${error.status}, body was: `,
        error.error
      );
    }
    return throwError(
      () => new Error('Something bad happened; please try again later.')
    );
  }

  getCurrentPassword(userId: number): Observable<string> {
    console.log(`Fetching current password for user ID: ${userId}`);
    return this.http.get(`${this.serverUrl}/user/current-password`, {
      params: { userId: userId.toString() },
      responseType: 'text'
    }).pipe(
      map(response => {
        console.log('Raw password response:', response);
        return response;
      }),
      catchError(error => {
        console.error('Error in getCurrentPassword:', error);
        throw error;
      })
    );
  }
  
  changePassword(userId: number, currentPassword: string, newPassword: string): Observable<string> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const params = new HttpParams()
      .set('userId', userId.toString())
      .set('currentPassword', currentPassword)
      .set('newPassword', newPassword);
  
    return this.http.post(`${this.serverUrl}/user/change-password`, null, { headers, params, responseType: 'text' }).pipe(
      catchError(error => { throw error; })
    );
  }
  
}
