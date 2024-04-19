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
  selectedSection = 'profile';
  profileImageUrl: string | null ='assets/images/default.jpeg';
  selectedFile: File | null = null;
  userId: number | null = null;
  loggedInUser: User | null = null;
  errorMessage: string | null = null;
  successMessage: string | null = null;
  uploadInProgress = false;
  activityLogs: any;

  constructor(
    private authService: AuthService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.loggedInUser = this.authService.getLoggedInUser();
    if (this.loggedInUser) {
      this.userId = this.loggedInUser.id;
      if (this.loggedInUser.imageUrl) {
        this.loadProfileImage(this.userId);
      }
    } else {
      console.error('Failed to retrieve user information. User ID unavailable for upload.');
    }
  }

  loadProfileImage(userId: number): void {
    this.userService.getImageData(userId).subscribe(
      (data: Blob) => {
        // Convert Blob to base64 data URL
        const reader = new FileReader();
        reader.onloadend = () => {
          this.profileImageUrl = reader.result as string;
        };
        reader.readAsDataURL(data);
      },
      (error) => {
        console.error('Error loading profile image:', error);
        this.errorMessage = 'Failed to load profile image. Please try again later.';
      }
    );
  }
  

  selectSection(section: string): void {
    this.selectedSection = section;
  }

  triggerFileInput(): void {
    document.getElementById('fileInput')?.click();
  }

  onFileSelected(event: any): void {
    const files = event.target.files;
    if (files && files.length > 0) {
      this.selectedFile = files[0];
      // You can optionally display the selected image preview here
    } else {
      console.error('No file selected');
      this.errorMessage = 'No file selected. Please select an image file.';
    }
  }

  onUpload(): void {
    if (!this.userId) {
      this.errorMessage = 'User ID not available. Please refresh the page and try again.';
      return;
    }
    if (!this.selectedFile) {
      this.errorMessage = 'No file selected';
      return;
    }

    this.uploadInProgress = true;

    this.userService.uploadProfileImage(this.userId, this.selectedFile).subscribe(
      (response: any) => {
        console.log('Image uploaded successfully:', response);
        // Assuming the server returns the updated image URL in the response
        if (response && response.success && response.imageUrl) {
          this.profileImageUrl = response.imageUrl;
          this.selectedFile = null;
          this.successMessage = 'Image uploaded successfully';
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
