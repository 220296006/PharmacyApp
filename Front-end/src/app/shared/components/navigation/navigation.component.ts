import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { User } from 'src/app/model/user';
import { AuthService } from 'src/app/services/auth-service/auth-service.service';
import { Router } from '@angular/router';
import * as alertify from 'alertifyjs';
import { UserService } from 'src/app/services/user-service/userservice.service';

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
  
  constructor(
    private authService: AuthService, 
    private router: Router,
    private userService: UserService) {}

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
      if (user) {
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
    alertify.success('You have been logged out');
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

  ngOnDestroy(): void {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
    }
  }
}
