import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { ApiResponse } from 'src/app/model/api-response';
import { Inventory } from 'src/app/model/inventory';

@Injectable({
  providedIn: 'root',
})
export class InventoryService {
  private readonly serverUrl: string = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  getAllInventoryData(
    name: string = '',
    page: number = 0,
    size: number = 50
  ): Observable<ApiResponse<Inventory[]>> {
    return this.http
      .get<ApiResponse<Inventory[]>>(
        `${this.serverUrl}/inventory/all?name=${name}&page=${page}&size=${size}`
      )
      .pipe(catchError(this.handleError));
  }

  getInventoryById(inventoryId: number): Observable<ApiResponse<Inventory>> {
    const url = `${this.serverUrl}/inventory/read/${inventoryId}`;
    return this.http
      .get<ApiResponse<Inventory>>(url)
      .pipe(catchError(this.handleError));
  }

  updateMedicationData(
    id: number,
    inventory: Inventory
  ): Observable<ApiResponse<Inventory>> {
    return this.http
      .put<ApiResponse<Inventory>>(
        `${this.serverUrl}/inventory/update/${id}`,
        inventory
      )
      .pipe(catchError(this.handleError));
  }

  deleteInventoryById(id: number): Observable<ApiResponse<Inventory>> {
    return this.http
      .delete<ApiResponse<Inventory>>(
        `${this.serverUrl}/inventory/delete/${id}`
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
