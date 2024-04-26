import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { UserService } from 'src/app/services/user-service/userservice.service';
import * as alertify from 'alertifyjs';

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
    private dialogRef: MatDialogRef<CreateUserDialogComponent>
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
        alertify.success('User added successfully');
        this.dialogRef.close(response.data.user);
      },
      error: (error) => {
        console.error('Error adding user:', error);
        alertify.error('Error adding user. Please try again.');
      },
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
