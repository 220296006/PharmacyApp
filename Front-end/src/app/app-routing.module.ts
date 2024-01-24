import { CustomerDetailsComponent } from './components/customer-details/customer-details.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { CustomerComponent } from './components/customer/customer.component';
import { InvoiceComponent } from './components/invoice/invoice.component';
import { MedicationComponent } from './components/medication/medication.component';
import { PrescriptionComponent } from './components/prescription/prescription.component';
import { InventoryComponent } from './components/inventory/inventory.component';
import { UserComponent } from './components/user/user.component';
import { NavigationComponent } from './shared/components/navigation/navigation.component';
import { CreateUserDialogComponent } from './components/create-user-dialog/create-user-dialog.component';
import { RegistrationComponent } from './shared/components/registration/registration.component';
import { LoginComponent } from './shared/components/login/login.component';

const routes: Routes = [
  { path: 'register', pathMatch: 'full', component: RegistrationComponent },
  { path: 'login', pathMatch: 'full', component: LoginComponent },
  { path: '', pathMatch: 'full', component: HomeComponent },
  { path: 'nav', pathMatch: 'full', component: NavigationComponent },
  { path: 'home', pathMatch: 'full', component: HomeComponent },
  { path: 'customer', pathMatch: 'full', component: CustomerComponent },
  { path: 'invoice', pathMatch: 'full', component: InvoiceComponent },
  { path: 'medication', pathMatch: 'full', component: MedicationComponent },
  { path: 'prescription', pathMatch: 'full', component: PrescriptionComponent },
  { path: 'inventory', pathMatch: 'full', component: InventoryComponent },
  { path: 'user', pathMatch: 'full', component: UserComponent },
  { path: 'customer-details/:id', component: CustomerDetailsComponent },
  { path: 'create-user-dialog', pathMatch: 'full', component: CreateUserDialogComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
