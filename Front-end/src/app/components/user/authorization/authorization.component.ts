import { Component, OnInit } from '@angular/core';
import { RoleService } from 'src/app/services/role-service/role-service.service';
import { User } from 'src/app/model/user';
import { AuthService } from 'src/app/services/auth-service/auth-service.service';
import { Role } from 'src/app/model/role';
import { MatSnackBar } from '@angular/material/snack-bar';
import { jwtDecode } from 'jwt-decode';

@Component({
  selector: 'app-authorization',
  templateUrl: './authorization.component.html',
  styleUrls: ['./authorization.component.scss']
})
export class AuthorizationComponent implements OnInit {
  selectedRole: Role | undefined; // Ensure selectedRole is of type Role
  rolePermissions: any[] = [];
  currentUser: User | null = null;
  userPermissions: string[] = [];

  constructor(
    private roleService: RoleService, 
    private authService: AuthService, 
    private snackBar: MatSnackBar) {}
    ngOnInit(): void {
      // Step 1: Retrieve the current user
      this.currentUser = this.authService.getLoggedInUser();
      if (this.currentUser) {
        console.log('Current user:', this.currentUser);
    
        // Step 2: Retrieve user permissions from token
        const token = this.authService.getToken();
        if (token) {
          const decodedToken: any = jwtDecode(token);
          const permissions = decodedToken.permissions?.flatMap((role: any) => role.permissions) || [];
          console.log('User permissions:', permissions);
          this.userPermissions = permissions;
        } else {
          console.error('No JWT token found.');
        }
    
        // Step 3: Retrieve user role from the list of roles
        this.roleService.listRoles().subscribe((roles) => {
          const userRole = roles.find((role) => role.name === this.currentUser!.role);
          if (userRole) {
            this.selectedRole = { ...userRole }; // Copy the role object
            console.log('User role:', this.selectedRole);
          } else {
            console.error('User role not found in roles list.');
          }
        });
      } else {
        console.error('No user is currently logged in.');
      }
    }
    
  fetchRoles(): void {
    this.roleService.listRoles().subscribe({
      next: (roles) => {
        this.rolePermissions = roles.map(role => ({
          role: role.name,
          permissions: role.permissions
        }));
      },
      error: (error) => {
        console.error('Error fetching roles:', error);
      }
    });
  }

  updateRole(): void {
    if (this.currentUser && this.selectedRole) {
      const userId = this.currentUser.id;
      // Check if the current user has the role 'ADMIN' or 'SYSADMIN'
      if (['ROLE_ADMIN', 'ROLE_SYSADMIN'].includes(this.currentUser.role)) {
        this.roleService.updateUserRole(userId, this.selectedRole.name).subscribe({
          next: (updatedRole) => {
            console.log('Role updated successfully:', updatedRole);
            this.snackBar.open('Role updated successfully', 'Close', {
              duration: 3000,
            });
          },
          error: (error) => {
            console.error('Error updating role:', error);
            this.snackBar.open('Error updating role', 'Close', {
              duration: 3000,
            });
          }
        });
      } else {
        // User does not have the required role
        this.snackBar.open('You do not have permission to change roles', 'Close', {
          duration: 3000,
        });
      }
    } else {
      console.error('No user is currently logged in or selected role is undefined.');
    }
  }
}
