import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { User } from 'src/app/model/user';
import { UserService } from 'src/app/services/user-service/userservice.service';

@Component({
  selector: 'app-update-user-dialog',
  templateUrl: './update-user-dialog.component.html',
  styleUrls: ['./update-user-dialog.component.scss'],
})
export class UpdateUserDialogComponent implements OnInit {
  user: User = {
    id: null, firstName: '', lastName: '', email: '', address: '', phone: '',
    middleName: '',
    imageUrl: '',
    enabled: false,
    isUsingMfa: false,
    createdAt: undefined,
    isNotLocked: false
  };

  constructor(
    public dialogRef: MatDialogRef<UpdateUserDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    if (this.data?.user) {
      this.user = { ...this.data.user }; 
    }
  }

  onCancelClick(): void {
    this.dialogRef.close();
  }

  onUpdateClick(): void {
    this.userService.updateUserById(this.user.id, this.user).subscribe({
      next: (response) => {
        console.log('Response from server:', response);
        if (response.status === 'CREATED') {
          this.dialogRef.close(true); // Indicate success
        } else {
          console.error('Error: ' + response.message);
          this.dialogRef.close(false); // Indicate failure
        }
      },
      error: (error) => {
        console.error('Error updating user:', error);
        this.dialogRef.close(false); // Indicate failure
      },
    });
  }
}
