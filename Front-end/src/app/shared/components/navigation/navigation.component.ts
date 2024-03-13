import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { User } from 'src/app/model/user';
import { AuthService } from 'src/app/services/auth-service/auth-service.service';
import { Router } from '@angular/router';
import * as alertify from 'alertifyjs';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss'],
})
export class NavigationComponent implements OnInit, OnDestroy {
  user: User | null = null;
  private userSubscription: Subscription;

  constructor(private authService: AuthService, private router: Router, private alertifyService: AlertifyService) {}

  ngOnInit(): void {
    this.userSubscription = this.authService.currentUser.subscribe((user) => {
      this.user = user;
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

  ngOnDestroy(): void {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
    }
  }
}
