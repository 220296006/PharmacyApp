import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { UserService } from 'src/app/services/user-service/userservice.service';

@Component({
  selector: 'app-update-user-dialog',
  templateUrl: './update-user-dialog.component.html',
  styleUrls: ['./update-user-dialog.component.scss'],
})
export class UpdateUserDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<UpdateUserDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private userService: UserService
  ) {}

  onCancelClick(): void {
    this.dialogRef.close();
  }

  onUpdateClick(): void {
    this.userService.updateUser(this.data.user.id, this.data.user).subscribe({
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
