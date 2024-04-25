import { CustomerDetailsComponent } from './components/customer-details/customer-details.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { CustomerComponent } from './components/customer-table/customer.component';
import { InvoiceComponent } from './components/invoice/invoice.component';
import { MedicationComponent } from './components/medication/medication.component';
import { PrescriptionComponent } from './components/prescription/prescription.component';
import { InventoryComponent } from './components/inventory/inventory.component';
import { UserComponent } from './components/user-table/user.component';
import { NavigationComponent } from './shared/components/navigation/navigation.component';
import { CreateUserDialogComponent } from './components/create-user-dialog/create-user-dialog.component';
import { RegistrationComponent } from './shared/components/registration/registration.component';
import { LoginComponent } from './shared/components/login/login.component';
import { ForgotPasswordComponent } from './shared/components/forgot-password/forgot-password.component';
import { UserProfileComponent } from './components/user/user-profile/user-profile.component';
import { AccountComponent } from './components/user/account/account.component';
import { AuthenticationComponent } from './components/user/authentication/authentication.component';
import { AuthorizationComponent } from './components/user/authorization/authorization.component';
import { PasswordComponent } from './components/user/password/password.component';
import { ProfileComponent } from './components/user/profile/profile.component';

const routes: Routes = [
  { path: 'login', pathMatch: 'full', component: LoginComponent },
  {path:  'user-profile', pathMatch: 'full', component: UserProfileComponent,},
  { path: 'register', pathMatch: 'full', component: RegistrationComponent },
  { path: 'forgot-password', pathMatch: 'full', component: ForgotPasswordComponent },
  { path: '', pathMatch: 'full',  component: LoginComponent},
  { path: 'home', pathMatch: 'full', component: HomeComponent },
  { path: 'nav', pathMatch: 'full', component: NavigationComponent },
  { path: 'customer', pathMatch: 'full', component: CustomerComponent },
  { path: 'invoice', pathMatch: 'full', component: InvoiceComponent },
  { path: 'medication', pathMatch: 'full', component: MedicationComponent },
  { path: 'prescription', pathMatch: 'full', component: PrescriptionComponent },
  { path: 'inventory', pathMatch: 'full', component: InventoryComponent },
  { path: 'user', pathMatch: 'full', component: UserComponent },
  { path: 'customer-details/:id', component: CustomerDetailsComponent },
  { path: 'create-user-dialog', pathMatch: 'full', component: CreateUserDialogComponent },
  { path: '**', redirectTo: 'login' }, 

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
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
