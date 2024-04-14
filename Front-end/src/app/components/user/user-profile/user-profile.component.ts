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
  errorMessage: string| null = null;
  successMessage: string| null = null;

  constructor(
    private authService: AuthService,
    private userService: UserService
  ) { }
  
  ngOnInit(): void {
    // Retrieve logged-in user's information from session storage
    this.loggedInUser = this.authService.getLoggedInUser();
    console.log('User object:', this.loggedInUser);
  
    // Use the user's ID directly from the loggedInUser object
    if (this.loggedInUser) {
      this.userId = this.loggedInUser.id;
      if (this.loggedInUser.imageUrl) {
        this.profileImageUrl = this.loggedInUser.imageUrl;
      }
      console.log('User ID:', this.userId);
    } else {
      console.error('Failed to retrieve user information. User ID unavailable for upload.');
    }
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
    // Check if user ID and file are available
    if (!this.userId) {
      this.errorMessage = 'User ID not available. Please refresh the page and try again.';
      return;
    }
    if (!this.selectedFile) {
      this.errorMessage = 'No file selected';
      return;
    }

    this.uploadInProgress = true; // Set flag to indicate upload is in progress

    this.userService.uploadProfileImage(this.userId, this.selectedFile).subscribe(
      (response: any) => {
        console.log('Image uploaded successfully:', response);
        if (response && response.success && response.imageData) {
          // Fetch the updated user information
          this.userService.getUserById(this.userId).subscribe((user) => {
            this.profileImageUrl = user.data.user.imageUrl; // Update profile image URL
            this.selectedFile = null; // Clear the selected file
            this.successMessage = 'Image uploaded successfully';
          });
        } else {
          console.error('Failed to upload image:', response);
          this.errorMessage = response.message || 'Failed to upload image. Please try again later.';
        }
        this.uploadInProgress = false;
      },
      (error) => {
        console.error('Error uploading image:', error);
        this.errorMessage = 'Failed to upload image. Please try again later.';
        this.uploadInProgress = false;
      }
    );
  }

}
