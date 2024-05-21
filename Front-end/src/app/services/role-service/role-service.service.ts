import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Role } from 'src/app/model/role';

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  private readonly serverUrl: string = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  getRoleByEmail(email: string): Observable<Role> {
    return this.http.get<Role>(`${this.serverUrl}/roles/getRoleByUserEmail?email=${email}`);
  }

  updateUserRole(userId: number, roleName: string): Observable<Role> {
    return this.http.put<Role>(`${this.serverUrl}/roles/updateUserRole/${userId}`, null, {
      params: {
        userId: userId.toString(),
        roleName: roleName
      }
    });
  }

  listRoles(): Observable<Role[]> {
    return this.http.get<Role[]>(`${this.serverUrl}/roles/list`);
  }
}
