import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth-service/auth-service.service';
import * as alertify from 'alertifyjs';
import { User } from 'src/app/model/user';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss']
})
export class NavigationComponent implements OnInit {
  loggedInUser: User;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.loadLoggedInUser();
  }

  loadLoggedInUser() {
    this.authService.getUserInfo().subscribe({
      next: (userInfo) => {
        this.loggedInUser = userInfo;
        console.log(userInfo)
      },
      error: (error) => {
        console.error('Error fetching user info:', error);
      }
    });
  }
  


  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  onLogout() {
    this.authService.logout();
    this.router.navigate(['/login']);
    alertify.success('You have been logged out');
  }
}
