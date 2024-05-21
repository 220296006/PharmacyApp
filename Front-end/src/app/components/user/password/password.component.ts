import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user-service/userservice.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from 'src/app/services/auth-service/auth-service.service';

@Component({
  selector: 'app-password',
  templateUrl: './password.component.html',
  styleUrls: ['./password.component.scss']
})
export class PasswordComponent implements OnInit {
  currentPassword: string = '';
  newPassword: string = '';
  showCurrentPassword: boolean = false;
  showNewPassword: boolean = false;
  userId: number | null = null;

  constructor(
    private userService: UserService,
    private snackBar: MatSnackBar,
    private authService: AuthService // Inject AuthService
  ) {}

  ngOnInit(): void {
    const loggedInUser = this.authService.getLoggedInUser();
    if (loggedInUser) {
      this.userId = loggedInUser.id;
    } else {
      this.snackBar.open('User ID is not set or user is not logged in', 'Close', { duration: 3000 });
    }
  }

  // fetchCurrentPassword(): void {
  //   if (this.userId) {
  //     console.log(`Attempting to fetch current password for user ID: ${this.userId}`);
  //     this.userService.getCurrentPassword(this.userId).subscribe({
  //       next: (password) => {
  //         console.log('Current password fetched successfully:', password);
  //         this.currentPassword = password;
  //       },
  //       error: (error) => {
  //         console.error('Error fetching current password:', error);
  //         this.snackBar.open('Error fetching current password', 'Close', { duration: 3000 });
  //       }
  //     });
  //   } else {
  //     console.error('Cannot fetch current password: User ID is null');
  //   }
  // }

  toggleCurrentPasswordVisibility(): void {
    this.showCurrentPassword = !this.showCurrentPassword;
    console.log(`Current password visibility toggled: ${this.showCurrentPassword}`);
  }

  toggleNewPasswordVisibility(): void {
    this.showNewPassword = !this.showNewPassword;
    console.log(`New password visibility toggled: ${this.showNewPassword}`);
  }

  changePassword(): void {
    if (this.currentPassword && this.newPassword) {
      if (this.userId) {
        this.userService.changePassword(this.userId, this.currentPassword, this.newPassword).subscribe({
          next: (response: string) => {
            this.snackBar.open(response, 'Close', { duration: 3000, panelClass: ['snackbar-success'] });
            this.currentPassword = '';
            this.newPassword = '';
          },
          error: (error) => {
            this.snackBar.open('Failed to change password', 'Close', { duration: 3000, panelClass: ['snackbar-error'] });
            console.error('Error changing password:', error);
          }
        });
      } else {
        this.snackBar.open('User ID is null, cannot change password', 'Close', { duration: 3000, panelClass: ['snackbar-error'] });
      }
    } else {
      this.snackBar.open('Please fill in both fields', 'Close', { duration: 3000, panelClass: ['snackbar-warning'] });
    }
  }
}