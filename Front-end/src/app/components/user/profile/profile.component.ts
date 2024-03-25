import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ApiResponse } from 'src/app/model/api-response';
import { User } from 'src/app/model/user';
import { AuthService } from 'src/app/services/auth-service/auth-service.service';
import { UserService } from 'src/app/services/user-service/userservice.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
})
export class ProfileComponent implements OnInit {
  updateUserForm: FormGroup;
  user: User;

  constructor(private fb: FormBuilder, private authService: AuthService, private userService: UserService) {
    this.updateUserForm = this.fb.group({
      firstName: ['', Validators.required],
      middleName: [''],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required]],
      address: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    this.authService.getUserInfo().subscribe({
      next: (response: ApiResponse<User>) => {
        if (response && response.data) {
          this.user = response.data.user;
          console.log('Response from server:', this.user);
          // Populate the form with user data
          this.updateUserForm.patchValue({
            firstName: this.user.firstName,
            lastName: this.user.lastName,
            middleName: this.user.middleName,
            email: this.user.email,
            phone: this.user.phone,
            address: this.user.address
          });
        } else {
          console.error('No user data found in the response');
        }
      },
      error: (error) => {
        console.error('Error fetching user info:', error);
        // Handle the error here, e.g., display an error message to the user
      },
    });
  }
  

  onUpdateUser() {
    const updatedUser = this.updateUserForm.value;
    const userId = this.user.id; // Assuming id is stored in the user object

    this.userService.updateUserData(userId, updatedUser).subscribe(
      (response) => {
        console.log('User updated successfully:', response);
        // Handle success (e.g., show a success message)
        // Update currentUser in AuthService (if applicable)
        this.authService.updateCurrentUser(updatedUser);
      },
      (error) => {
        console.error('Failed to update user:', error);
        // Handle error (e.g., show an error message based on error details)
      },
    );
  }
}
