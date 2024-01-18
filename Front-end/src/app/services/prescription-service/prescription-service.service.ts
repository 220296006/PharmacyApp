import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { ApiResponse } from 'src/app/model/api-response';
import { Prescription } from 'src/app/model/prescription';

@Injectable({
  providedIn: 'root'
})
export class PrescriptionService{

  private readonly serverUrl: string = 'http://localhost:8080';


  constructor(private http: HttpClient) { }

  createPrescription(prescription: Prescription): Observable<ApiResponse<Prescription>>{
    console.log(prescription)
    return this.http.post<ApiResponse<Prescription>>
    (`${this.serverUrl}/prescription/create`, prescription)
    .pipe(catchError(this.handleError));
  }  
  getAllPrescriptionData(
    name: string = '',
    page: number = 0,
    size: number = 50
  ): Observable<ApiResponse<Prescription>> {
    return this.http.get<ApiResponse<Prescription>>(
      `${this.serverUrl}/prescription/all?name=${name}&page=${page}&size=${size}`
    );
  }

  getPrescriptionById(prescriptionId: number): Observable<ApiResponse<Prescription>> {
    const url = `${this.serverUrl}/prescription/read/${prescriptionId}`;
    return this.http
      .get<ApiResponse<Prescription>>(url)
      .pipe(catchError(this.handleError));
  }

  updatePrescriptionData(
    id: number,
    prescription: Prescription
  ): Observable<ApiResponse<Prescription>> {
    return this.http
      .put<ApiResponse<Prescription>>(
        `${this.serverUrl}/prescription/update/${id}`,
        prescription
      )
      .pipe(catchError(this.handleError));
  }

  deletePrescriptionById(id: number): Observable<ApiResponse<Prescription>> {
    return this.http
      .delete<ApiResponse<Prescription>>(
        `${this.serverUrl}/prescription/delete/${id}`
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

