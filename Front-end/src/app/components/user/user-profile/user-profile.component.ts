import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/model/user';
import { AuthService } from 'src/app/services/auth-service/auth-service.service';
import { UserService } from 'src/app/services/user-service/userservice.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {
  selectedSection: string = 'profile';
  profileImageUrl: string | ArrayBuffer | null = 'assets/images/default.jpeg';
  selectedFile: File | null = null;
  userId: number | null = null;
  activityLogs: any;
  uploadInProgress: boolean = false;
  loggedInUser: User | null = null;

  constructor(
    private authService: AuthService,
    private userService: UserService
  ) { }
  
  ngOnInit(): void {
    this.authService.currentUser.subscribe(user => {
      if (user) {
        this.userId = user.id;
        if (user.imageUrl) {
          this.profileImageUrl = user.imageUrl;
        }
        console.log('User ID:', this.userId);
      } else {
        // If user is not available from observable, try to fetch from local storage
        const userInfo = this.authService.getLoggedInUser();
        if (userInfo && userInfo.id) {
          this.userId = userInfo.id;
          if (userInfo.imageUrl) {
            this.profileImageUrl = userInfo.imageUrl;
          }
          console.log('User ID:', this.userId);
        } else {
          console.error('Failed to retrieve user information. User ID unavailable for upload.');
        }
      }
    });
  }
  
  

  selectSection(section: string) {
    this.selectedSection = section;
  }

  triggerFileInput() {
    document.getElementById('fileInput')?.click();
  }

  onFileSelected(event: any) {
    const files = event.target.files;
    if (files && files.length > 0) {
      this.selectedFile = files[0];
      const reader = new FileReader();
      reader.onload = () => {
        this.profileImageUrl = reader.result;
      };
      reader.readAsDataURL(this.selectedFile);
    }
  }

  onUpload() {
    if (!this.userId) {
      console.error('User ID not available. Please refresh the page and try again.');
      return;
    }
  
    if (!this.selectedFile) {
      console.error('No file selected');
      return;
    }

    this.uploadInProgress = true; // Set flag to indicate upload is in progress
  
    this.userService.uploadProfileImage(this.userId, this.selectedFile).subscribe(
      (response) => {
        console.log('Image uploaded successfully:', response);
        if (response.success) {
          // Assuming the server returns the updated image URL
          this.profileImageUrl = response.imageUrl;
          this.selectedFile = null; // Clear the selected file
          // Optionally, display success message to user
        } else {
          console.error('Failed to upload image:', response.message);
          // Optionally, display error message to user
        }
        this.uploadInProgress = false; // Reset the flag after upload completes
      },
      (error) => {
        console.error('Error uploading image:', error);
        // Optionally, display error message to user
        this.uploadInProgress = false; // Reset the flag after upload completes
      }
    );
  }
}
