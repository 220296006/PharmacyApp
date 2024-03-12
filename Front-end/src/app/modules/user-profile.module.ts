import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserProfileRoutingModule } from './user-profile-routing.module';
import { AccountComponent } from '../components/user/account/account.component';
import { AuthenticationComponent } from '../components/user/authentication/authentication.component';
import { AuthorizationComponent } from '../components/user/authorization/authorization.component';
import { PasswordComponent } from '../components/user/password/password.component';
import { ProfileComponent } from '../components/user/profile/profile.component';
import { UserProfileComponent } from '../components/user/user-profile/user-profile.component';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    UserProfileComponent,
    ProfileComponent,
    PasswordComponent,
    AuthorizationComponent,
    AccountComponent,
    AuthenticationComponent
  ],
  imports: [
    CommonModule,
    UserProfileRoutingModule,
    FormsModule
  ]
})
export class UserProfileModule { }
