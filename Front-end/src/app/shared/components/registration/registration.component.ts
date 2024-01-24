import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'src/app/services/auth-service/auth-service.service';
import * as alertify from 'alertifyjs';
import { Router } from '@angular/router';


@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent {
  registrationForm: FormGroup;
  hidePassword = true;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.registrationForm = this.fb.group({
      firstName: ['', Validators.required],
      middleName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required]],
      address: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onRegister() {
    if (this.registrationForm.valid) {
      this.authService.registerUser(this.registrationForm.getRawValue()).subscribe({
        next: (response) => {
          console.log("onRegister", response);
          if (response.status === 'CREATED' && response.data && response.data.user) {
            console.log('Registration successful:', response.message);
            const token = response.token;
            this.authService.saveToken(token);
            alertify.success("Registration Successful");
            this.router.navigate(['/login']);
          } else {
            console.error('Error during registration:', response.message);
          }
        },
        error: (error) => {
          console.error('Registration failed:', error);
        },
      });
    } else {
      console.error('Form is not valid');
    }
  }

  togglePasswordVisibility() {
    this.hidePassword = !this.hidePassword;
  }
}
