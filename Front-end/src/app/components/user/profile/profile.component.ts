import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { User } from 'src/app/model/user';
import { AuthService } from 'src/app/services/auth-service/auth-service.service';
import { UserService } from 'src/app/services/user-service/userservice.service';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
})
export class ProfileComponent implements OnInit {
  updateUserForm: FormGroup;
  currentUser: User;


  loggedInUser: User | null = null;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private userService: UserService,
    private snackBar: MatSnackBar 
  ) {}

  ngOnInit(): void {
    // Retrieve logged-in user's information from session storage
    this.loggedInUser = this.authService.getLoggedInUser();

    // Initialize currentUser with loggedInUser
    this.currentUser = this.loggedInUser ? { ...this.loggedInUser } : null;
    // Initialize form and populate with user information
    this.updateUserForm = this.formBuilder.group({
      firstName: [this.loggedInUser ? this.loggedInUser.firstName : ''],
      middleName: [this.loggedInUser ? this.loggedInUser.middleName : ''],
      lastName: [this.loggedInUser ? this.loggedInUser.lastName : ''],
      email: [this.loggedInUser ? this.loggedInUser.email : ''],
      phone: [this.loggedInUser ? this.loggedInUser.phone : ''],
      address: [this.loggedInUser ? this.loggedInUser.address : '']
    });
  }
  

   updateFormWithUserData(): void {
    if (this.currentUser) {
      this.updateUserForm.patchValue({
        firstName: this.currentUser.firstName || '',
        middleName: this.currentUser.middleName || '',
        lastName: this.currentUser.lastName || '',
        email: this.currentUser.email || '',
        phone: this.currentUser.phone || '',
        address: this.currentUser.address || '',
      });
    }
  }

  onUpdateUser(): void {
    console.log('Current user:', this.currentUser);
    console.log('Current user ID:', this.currentUser?.id);
    if (!this.currentUser || !this.currentUser.id) {
      console.error('Current user or user ID is undefined');
      return;
    }
  
    const updatedUser = this.updateUserForm.value as User;
    this.userService.updateUserData(this.currentUser.id, updatedUser)
      .pipe(
        catchError((error) => {
          console.error('Network error:', error);
          this.snackBar.open('Network error occurred', 'Dismiss', {
            duration: 3000,
            panelClass: ['error-snackbar'] // Apply custom CSS class for error
          });
          return throwError('Network error occurred');
        })
      )
      .subscribe({
        next: (response) => {
          if (response && response.status === 'OK') {
            // Update current user details in AuthService
            this.authService.updateCurrentUser(updatedUser);
            console.log('User updated successfully:', response.message);
            this.snackBar.open('User updated successfully', 'Dismiss', {
              duration: 3000,
              panelClass: ['success-snackbar'] // Apply custom CSS class for success
            });
          } else {
            console.error('Failed to update user:', response.message);
            this.snackBar.open('Failed to update user', 'Dismiss', {
              duration: 3000,
              panelClass: ['error-snackbar'] // Apply custom CSS class for error
            });
          }
        },
        error: (error) => {
          console.error('Server error:', error);
          this.snackBar.open('Server error occurred', 'Dismiss', {
            duration: 3000,
            panelClass: ['error-snackbar'] // Apply custom CSS class for error
          });
        }
      });
  }
  
  
}
