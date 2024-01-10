import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiResponse } from 'src/app/model/api-response';
import { Prescription } from 'src/app/model/prescription';

@Injectable({
  providedIn: 'root'
})
export class PrescriptionService{
  private readonly serverUrl: string = 'http://localhost:8080';


  constructor(private http: HttpClient) { }

  getAllPrescritionData(
    name: string = '',
    page: number = 0,
    size: number = 50
  ): Observable<ApiResponse<Prescription>> {
    return this.http.get<ApiResponse<Prescription>>(
      `${this.serverUrl}/prescription/all?name=${name}&page=${page}&size=${size}`
    );
  }
  }

