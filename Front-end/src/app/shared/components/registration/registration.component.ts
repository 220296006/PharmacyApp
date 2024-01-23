import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'src/app/services/auth-service/auth-service.service';
import * as alertify from 'alertifyjs';
import { Router } from '@angular/router';
import { User } from 'src/app/model/user';


@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent {
  registrationForm: FormGroup;

  hidePassword = true;

  togglePasswordVisibility() {
    this.hidePassword = !this.hidePassword;
  }

  constructor(private fb: FormBuilder, 
    private authService: AuthService,
    private router: Router,

    ) {
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

  onRegister(user: any) {
    if (Object.values(user).length > 0) {
      let rowData = { ...user };
      this.registrationForm.patchValue(rowData);
      return;
    }
      this.authService.registerUser(this.registrationForm.getRawValue()).subscribe({
        next: (response) => {
        console.log("onRegister", response);
        if (
          response.status === 'OK' &&
          response.data &&
          Array.isArray(response.data.page)
        ) {
        const token = response.token;
          this.authService.saveToken(token); 
          alertify.success("Register Succsessful")
          this.router.navigate(['/login'])
        }else {
          console.error('Error: ' + response.message);
        }
      },
        error: (error) => {
          console.error('Registration failed:', error);
        },
  });
}
}
