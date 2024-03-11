import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { tap, catchError, of, finalize } from 'rxjs';
import { AuthService } from 'src/app/services/auth-service/auth-service.service';

@Component({
  selector: 'app-admin-login',
  templateUrl: './admin-login.component.html',
  styleUrls: ['./admin-login.component.scss']
})
export class AdminLoginComponent implements OnInit {
  loginForm: FormGroup;

  hidePassword = true;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
    });
  }

  ngOnInit(): void {}

  togglePasswordVisibility() {
    this.hidePassword = !this.hidePassword;
  }

  loginAsAdmin() {
    if (this.loginForm.valid) {
      const { email, password } = this.loginForm.value;
      this.authService.loginAsAdmin(email, password)
        .pipe(
          tap(() => {
            this.snackBar.open('Login Successful', 'Close', {
              duration: 5000,
              horizontalPosition: 'center',
              verticalPosition: 'top',
            });
          }),
          catchError(error => {
            console.error('Login failed:', error);
            this.snackBar.open('Login failed. Please check your credentials and try again.', 'Close', {
              duration: 5000,
              horizontalPosition: 'center',
              verticalPosition: 'top',
            });
            return of(null);
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
