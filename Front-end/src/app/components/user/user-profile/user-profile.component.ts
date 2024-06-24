import { Component, Input, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Subscription } from 'rxjs';
import { UserEvent } from 'src/app/interface/UserEvent';
import { User } from 'src/app/interface/user';
import { AuthService } from 'src/app/services/auth-service/auth-service.service';
import { UserEventService } from 'src/app/services/user-event-service/user-event.service';
import { UserService } from 'src/app/services/user-service/userservice.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss'],
})
export class UserProfileComponent implements OnInit {
  selectedSection = 'profile';
  profileImageUrl: string | null = 'assets/images/default.jpeg';
  selectedFile: File | null = null; 
  userId: number | null = null;
  loggedInUser: User | null = null;
  errorMessage: string | null = null;
  successMessage: string | null = null;
  uploadInProgress = false;
  activityLogs: UserEvent[] = [];
  private subscriptions: Subscription[] = [];
  userEvents: UserEvent[] = [];

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private snackBar: MatSnackBar,
    private userEventService: UserEventService,
  ) {}

  ngOnInit(): void {
    this.loadUserEvents();
    console.log('LoadUserEvents: ', this.loadUserEvents);
    this.loggedInUser = this.authService.getLoggedInUser();
    if (this.loggedInUser) {
      this.userId = this.loggedInUser.id;
      if (this.loggedInUser.imageUrl) {
        this.loadProfileImage(this.userId);
      }
    } else {
      console.error(
        'Failed to retrieve user information. User ID unavailable for upload.'
      );
    }
  }

  report(userEvent: any): void {
    // Perform the reporting logic here
    console.log('Reporting user event:', userEvent);
    // You can add more logic here, such as sending a report to the server or displaying a modal for reporting
  }

  loadUserEvents(): void {
    this.userEventService.getUserEvents().subscribe(
      (events: UserEvent[]) => {
        this.userEvents = events;
      },
      (error) => {
        console.error('Error fetching user events:', error);
      }
    );
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
        this.errorMessage =
          'Failed to load profile image. Please try again later.';
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
      this.errorMessage =
        'User ID not available. Please refresh the page and try again.';
      return;
    }
    if (!this.selectedFile) {
      this.errorMessage = 'No file selected';
      return;
    }
  
    this.uploadInProgress = true;
  
    this.userService.uploadProfileImage(this.userId, this.selectedFile).subscribe({
      next: (response: any) => {
        console.log('Image uploaded successfully:', response);
        if (response && response.success && response.image) {
          this.profileImageUrl = response.image;
          this.selectedFile = null;
          this.successMessage = 'Image uploaded successfully';
          this.snackBar.open('Image uploaded successfully', 'Close', {
            duration: 3000,
            panelClass: ['snackbar-success']
          });
        } else {
          console.error('Failed to upload image:', response);
          this.snackBar.open('Failed to upload image', 'Close', {
            duration: 3000,
            panelClass: ['snackbar-error']   
          });
        }
        this.uploadInProgress = false;
      },
      error: (error: any) => {
        console.error('Error uploading image:', error);
        this.snackBar.open('Error uploading image', 'Close', {
          duration: 3000,
          panelClass: ['snackbar-error']
        });
        this.uploadInProgress = false;
      }
    });
  }
  

  onDelete(): void {
    if (!this.userId) {
      this.errorMessage = 'User ID not available. Please refresh the page and try again.';
      return;
    }
  
    this.uploadInProgress = true;
  
    const sub = this.userService.deleteProfileImage(this.userId)
    .subscribe({
      next:(response: any) => {
        console.log('Image deleted successfully:', response);
        // Assuming the server returns a success message
        if (response && response.success) {
          this.profileImageUrl = 'assets/images/default.jpeg'; // Set a default image URL
          this.snackBar.open('Image deleted successfully', 'Close', {
            duration: 3000,
            panelClass: ['snackbar-success']
          });
        } else {
          console.error('Failed to delete image:', response);
          this.snackBar.open(this.errorMessage, 'Close', {
            duration: 3000,
            panelClass: ['snackbar-error']
          });
        }
        this.uploadInProgress = false;
      },
      error: (error: any) => {
        console.error('Error deleting image:', error);
        this.snackBar.open(this.errorMessage, 'Close', {
          duration: 3000,
          panelClass: ['snackbar-error']
        });
        this.uploadInProgress = false;
      },
    });
    this.subscriptions.push(sub);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}


