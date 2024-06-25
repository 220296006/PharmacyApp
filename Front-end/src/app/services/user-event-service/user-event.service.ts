import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, of } from 'rxjs';
import { UserEvent } from 'src/app/interface/UserEvent';

@Injectable({
  providedIn: 'root'
})
export class UserEventService {

  private readonly serverUrl: string = 'http://localhost:8080';

  
  constructor(private http: HttpClient) {}

  getUserEventsByUserId(userId: number): Observable<UserEvent[]> {
    return this.http.get<UserEvent[]>(`${this.serverUrl}/user-events/${userId}`);
  }

  getUserEvents(): Observable<UserEvent[]> {
    return this.http.get<UserEvent[]>(`${this.serverUrl}/user-events/all`).pipe(
      catchError(this.handleError<UserEvent[]>('getUserEvents', []))
    );
  }

  createUserEvent(userEvent: UserEvent): Observable<UserEvent> {
    return this.http.post<UserEvent>(`${this.serverUrl}/user-events/create`, userEvent).pipe(
      catchError(this.handleError<UserEvent>('createUserEvent'))
    );
  }

  updateUserEvent(userEvent: UserEvent): Observable<UserEvent> {
    const url = `${this.serverUrl}/user-events/update/${userEvent.id}`;
    return this.http.put<UserEvent>(url, userEvent).pipe(
      catchError(this.handleError<UserEvent>('updateUserEvent'))
    );
  }

  deleteUserEvent(id: number): Observable<{}> {
    const url = `${this.serverUrl}/user-events/delete/${id}`;
    return this.http.delete(url).pipe(
      catchError(this.handleError('deleteUserEvent'))
    );
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }

}