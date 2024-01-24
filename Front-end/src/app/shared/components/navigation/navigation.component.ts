import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth-service/auth-service.service';
import * as alertify from 'alertifyjs'
import { jwtDecode } from 'jwt-decode';


@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss']
})
export class NavigationComponent  implements OnInit{
 
  loggedInUser: any;

  constructor(private service: AuthService) { }


  ngOnInit(): void {
    this.loadLoggedInUser();
    this.isLoggedIn();
  }

  loadLoggedInUser() {
    const token = localStorage.getItem('token'); // Assuming the token is stored in localStorage
    if (token) {
      try {
        const decodedToken = jwtDecode(token);
        console.log('Decoded Token:', decodedToken);
        // Further processing of the decoded token, such as extracting user information
      } catch (error) {
        console.error('Error decoding token:', error);
      }
    }
  }
  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  onLogout() {
    localStorage.removeItem('token');
    alertify.success("You have been logged out")
  }
}


