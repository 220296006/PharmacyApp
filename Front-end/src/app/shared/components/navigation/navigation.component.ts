import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { User } from 'src/app/interface/user';
import { AuthService } from 'src/app/services/auth-service/auth-service.service';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user-service/userservice.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss'],
})
export class NavigationComponent implements OnInit, OnDestroy {
  user: User | null = null;
  private userSubscription: Subscription;
  profileImageUrl: string | null ='assets/images/default.jpeg';
  errorMessage: string | null = null;
  userId: number | null = null;
  loggedInUser: User | null = null;
  searchKeyword: string = ''; // New property to hold search keyword
  searchResults: User[] = []; // Array to hold search results
  
  constructor(
    private authService: AuthService, 
    private router: Router,
    private userService: UserService,
    private snackBar: MatSnackBar
  ) {}

    ngOnInit(): void {
      this.userSubscription = this.authService.currentUser.subscribe((user) => {
        this.loggedInUser = this.authService.getLoggedInUser();
        if (this.loggedInUser) {
          this.userId = this.loggedInUser.id;
          if (this.loggedInUser.imageUrl) {
            this.loadProfileImage(this.userId);
          }
        } else {
          this.errorMessage = 'Failed to load profile image. Please try again later.';
        }
        this.user = user;
        if (user && user.id) {
          this.loadProfileImage(user.id); // Fetch profile image URL
        } else {
          this.profileImageUrl = null; // Reset profile image URL when user is not logged in
        }
      });
    }
  

  isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  onLogout() {
    this.authService.logout();
    this.router.navigate(['/login']);
    this.snackBar.open('You have been logged out', 'Close', {
      duration: 3000,
      panelClass: ['success-snackbar'],
    })
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
      () => {
        this.errorMessage = 'Failed to load profile image. Please try again later.';
      }
    );
  }

  // Function to search users
  searchUsers() {
    if (this.searchKeyword.trim() !== '') {
      this.userService.getUsersByFirstName(this.searchKeyword).subscribe(
        (users: User[]) => {
          this.searchResults = users;
        },
        (error) => {
          console.error('Error searching users:', error);
          // Handle error
        }
      );
    } else {
      this.searchResults = []; // Clear search results if search keyword is empty
    }
  }

  ngOnDestroy(): void {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
    }
  }
}
