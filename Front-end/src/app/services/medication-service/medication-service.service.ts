import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { ApiResponse } from 'src/app/model/api-response';
import { Medication } from 'src/app/model/medication';

@Injectable({
  providedIn: 'root',
})
export class MedicationService {
  private readonly serverUrl: string = 'http://localhost:8080';
  
  
  createMedication(medication: Medication): Observable<ApiResponse<Medication>>{
    console.log(medication)
    return this.http.post<ApiResponse<Medication>>
    (`${this.serverUrl}/medication/create`, medication)
    .pipe(catchError(this.handleError));
  } 

  constructor(private http: HttpClient) {}

  getAllMedicationData(
    name: string = '',
    page: number = 0,
    size: number = 50
  ): Observable<ApiResponse<Medication[]>> {
    return this.http
      .get<ApiResponse<Medication[]>>(
        `${this.serverUrl}/medication/all?name=${name}&page=${page}&size=${size}`
      )
      .pipe(catchError(this.handleError));
  }

  getMedicationById(medicationId: number): Observable<ApiResponse<Medication>> {
    const url = `${this.serverUrl}/medication/read/${medicationId}`;
    return this.http
      .get<ApiResponse<Medication>>(url)
      .pipe(catchError(this.handleError));
  }

  updateMedicationData(
    id: number,
    medication: Medication
  ): Observable<ApiResponse<Medication>> {
    return this.http
      .put<ApiResponse<Medication>>(
        `${this.serverUrl}/medication/update/${id}`,
        medication
      )
      .pipe(catchError(this.handleError));
  }

  deleteMedicationById(id: number): Observable<ApiResponse<Medication>> {
    return this.http
      .delete<ApiResponse<Medication>>(
        `${this.serverUrl}/medication/delete/${id}`
      )
      .pipe(catchError(this.handleError));
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
}
