import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserProfileComponent } from '../components/user/user-profile/user-profile.component';
import { ProfileComponent } from '../components/user/profile/profile.component';
import { PasswordComponent } from '../components/user/password/password.component';
import { AuthorizationComponent } from '../components/user/authorization/authorization.component';
import { AccountComponent } from '../components/user/account/account.component';
import { AuthenticationComponent } from '../components/user/authentication/authentication.component';

const routes: Routes = [
  {
    path: 'user-profile',
    component: UserProfileComponent,
    children: [
      { path: 'profile', component: ProfileComponent },
      { path: 'password', component: PasswordComponent },
      { path: 'authorization', component: AuthorizationComponent },
      { path: 'account', component: AccountComponent },
      { path: 'authentication', component: AuthenticationComponent },
      { path: '', redirectTo: 'profile', pathMatch: 'full' }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserProfileRoutingModule { }
