import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserService } from 'src/app/services/user-service/userservice.service';

@Component({
  selector: 'app-create-user-dialog',
  templateUrl: './create-user-dialog.component.html',
  styleUrls: ['./create-user-dialog.component.scss'],
})
export class CreateUserDialogComponent implements OnInit {
  userForm: FormGroup;
  hidePassword = true;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private dialogRef: MatDialogRef<CreateUserDialogComponent>,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.initForm();
  }

  initForm(): void {
    this.userForm = this.fb.group({
      firstName: ['', [Validators.required]],
      middleName: [''],
      lastName: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      address: [''],
      phone: [''],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  togglePasswordVisibility() {
    this.hidePassword = !this.hidePassword;
  }

  onSubmit(user: any): void {
    if (Object.values(user).length > 0) {
      let rowData = { ...user };
      this.userForm.patchValue(rowData);
      return;
    }
    this.userService.createUser(this.userForm.getRawValue()).subscribe({
      next: (response) => {
        console.log('User added successfully:', response.data.user);
        this.snackBar.open('User added successfully', 'Close', {
          duration: 3000,
          panelClass: ['success-snackbar'],
        })
        this.dialogRef.close(response.data.user);
      },
      error: (error) => {
        console.error('Error adding user:', error);
        this.snackBar.open('Error adding user. Please try again.', 'Close', {
          duration: 3000,
          panelClass: ['error-snackbar'],
        })
      },
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
