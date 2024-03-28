import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'src/app/services/auth-service/auth-service.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss'],
})
export class RegistrationComponent implements OnInit {
  registrationForm: FormGroup;
  hidePassword = true;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar, // Inject MatSnackBar
  ) {
    this.registrationForm = this.fb.group({
      firstName: ['', Validators.required],
      middleName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required]],
      address: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  ngOnInit(): void {
    console.log('Registration component initialized'); // Add logging
  }

  onRegister() {
    if (this.registrationForm.valid) {
      console.log('Form is valid. Proceeding with registration.'); // Add logging

      this.authService
        .registerUser(this.registrationForm.getRawValue())
        .subscribe({
          next: (response) => {
            console.log('Registration successful:', response); // Add logging
            if (response.status === 'CREATED' && response.data && response.data.user) {
              console.log('Registration successful:', response.message);
              this.snackBar.open('Registration Successful!', 'Close', {
                duration: 5000,
                horizontalPosition: 'center',
                verticalPosition: 'top',
              });
              this.router.navigate(['/login']);
            } else {
              console.error('Error during registration:', response.message);
            }
          },
          error: (error) => {
            console.error('Registration failed:', error); // Add logging
            this.snackBar.open('Registration failed: Please check your details and try again.', undefined, {
              duration: 5000,
              horizontalPosition: 'center',
              verticalPosition: 'top',
            });
          },
        });
    } else {
      console.error('Form is not valid'); // Add logging
    }
  }

  togglePasswordVisibility() {
    this.hidePassword = !this.hidePassword;
  }
}
