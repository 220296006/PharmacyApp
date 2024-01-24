import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import * as alertify from 'alertifyjs';
import { catchError, finalize, of, tap } from 'rxjs';
import { AuthService } from 'src/app/services/auth-service/auth-service.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit{
  loginForm: FormGroup;

  hidePassword = true;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private snackBar: MatSnackBar,  // Inject MatSnackBar
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }
  ngOnInit(): void {
  }

  togglePasswordVisibility() {
    this.hidePassword = !this.hidePassword;
  }

  onLogin() {
    if (this.loginForm.valid) {
      const { email, password } = this.loginForm.value; // Destructure email and password
      this.authService.login(email,password )
        .pipe(
          tap(() => {
            alertify.success('Login successful');  // Show success message with alertify
          }),
          catchError(error => {
            console.error('Login failed:', error);
            this.snackBar.open('Login failed. Please check your credentials and try again.', 'Close', {
              duration: 5000,
              horizontalPosition: 'center',
              verticalPosition: 'top'
            });
            return of(null);  // Continue the observable chain
          }),
          finalize(() => {
            // This block will be executed on completion, whether successful or with an error
          })
        )
        .subscribe(response => {
          if (response) {
            // Only navigate if the login was successful
            this.router.navigate(['/home']);
          }
        });
    }
  }
}

