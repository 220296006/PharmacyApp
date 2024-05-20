import { Component } from '@angular/core';
import { UserService } from 'src/app/services/user-service/userservice.service';

@Component({
  selector: 'app-password',
  templateUrl: './password.component.html',
  styleUrls: ['./password.component.scss']
})
export class PasswordComponent {
currentPassword: any;
newPassword: any;
id: number;
errorMessage: string;

constructor(private userService: UserService) {}

changePassword(): void {
  this.userService.changePassword(this.id, this.currentPassword, this.newPassword)
  .subscribe({
    next: () => {
      console.log('Password changed successfully');
      // Optionally, you can reset the form or show a success message here
    },
    error: error => {
      console.error('Error changing password:', error);
      this.errorMessage = error.message || 'An error occurred while changing the password';
    }
  });
}

}