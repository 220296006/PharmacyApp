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
    const token = localStorage.getItem('token');
    if (token) {
      const decodedToken: any = jwtDecode(token);
      this.service.getUserInfo(decodedToken).subscribe(
        (user: any) => {
          this.loggedInUser = user.logged_in_as;
        }
      );
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


