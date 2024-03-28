import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { User } from 'src/app/model/user';
import { AuthService } from 'src/app/services/auth-service/auth-service.service';
import { UserService } from 'src/app/services/user-service/userservice.service';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

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
    private userService: UserService
  ) {}

  ngOnInit(): void {
    // Retrieve logged-in user's information from session storage
    this.loggedInUser = this.authService.getLoggedInUser();
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
    const updatedUser = this.updateUserForm.value as User;
    // Handle null values for middleName, phone, and address
    updatedUser.middleName = updatedUser.middleName || '';
    updatedUser.phone = updatedUser.phone || '';
    updatedUser.address = updatedUser.address || '';
    this.userService.updateUserData(this.currentUser.id, updatedUser)
      .pipe(
        catchError((error) => {
          console.error('Network error:', error);
          return throwError('Network error occurred');
        })
      )
      .subscribe(
        (response) => {
          if (response) {
            // Update current user details in AuthService
            this.authService.updateCurrentUser(updatedUser); // Corrected from currentUser to updateCurrentUser
            console.log('User updated successfully:', response.message);
          } else {
            console.error('Failed to update user:', response.message);
          }
        },
        (error) => {
          console.error('Server error:', error);
        }
      );
  }
}
