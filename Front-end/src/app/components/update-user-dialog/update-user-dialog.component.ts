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
    id: null, firstName: '', lastName: '', email: '', address: '', phone: '', password: '',
    middleName: '',
    imageUrl: '',
    enabled: false,
    isUsingMfa: false,
    createdAt: undefined,
    isNotLocked: false,
    role: ''
  };

  constructor(
    public dialogRef: MatDialogRef<UpdateUserDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    console.log('Data received in dialog:', this.data);
    if (this.data?.user) {
      console.log('User data in dialog:', this.data.user);
      this.user = this.data.user;
    }
  }
   
  onCancelClick(): void {
    this.dialogRef.close();
  }

  onUpdateClick(): void {
    this.userService.updateUserData(this.user.id, this.user).subscribe({
      next: (response) => {
        console.log('Response from server:', response);
        if (response.status === 'CREATED') {
          this.dialogRef.close(true); 
        } else {
          console.error('Error: ' + response.message);
          this.dialogRef.close(false); 
        }
      },
      error: (error) => {
        console.error('Error updating user:', error);
        this.dialogRef.close(false); 
      },
    });
  }
  
}
