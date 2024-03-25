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

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    // Initialize form and load current user details
    this.updateUserForm = this.formBuilder.group({
      firstName: [''],
      middleName: [''], // Ensure all form controls are initialized
      lastName: [''],
      email: [''],
      phone: [''],
      address: [''],
      
    });

    // Retrieve current user details
    this.authService.currentUser.subscribe((user) => {
      if (user) {
        this.currentUser = user;
        console.log(this.currentUser);

        this.updateUserForm.patchValue({
          firstName: user.firstName  || '',
          middleName: user.middleName  || '',
          lastName: user.lastName || '',
          email: user.email || '',
          phone: user.middleName || '',
          address: user.address || '',
        });
      }
    });
  }

  onUpdateUser(): void {
    const updatedUser = this.updateUserForm.value as User;
    // Handle null values for middleName, phone, and address
    updatedUser.middleName = updatedUser.middleName || '';
    updatedUser.phone = updatedUser.phone || '';
    updatedUser.address = updatedUser.address || '';
    this.userService
      .updateUserData(this.currentUser.id, updatedUser)
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
            this.authService.updateCurrentUser(updatedUser);
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
