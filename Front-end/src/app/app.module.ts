import { MatFormFieldModule } from '@angular/material/form-field';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { CustomerService } from './services/customer-service/customer.service';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AngularMaterialModule } from './modules/material.module';
import { UserComponent } from './components/user/user-table/user.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatTableModule } from '@angular/material/table';
import { CustomerComponent } from './components/customer/customer-table/customer.component';
import { MatSortModule } from '@angular/material/sort';
import { MatPaginatorModule } from '@angular/material/paginator';
import { InvoiceComponent } from './components/invoice/invoice-table/invoice.component';
import { MedicationComponent } from './components/medication/medication-table/medication.component';
import { PrescriptionComponent } from './components/prescription/prescription-table/prescription.component';
import { InventoryComponent } from './components/inventory/inventory-table/inventory.component';
import { HomeComponent } from './components/home/home.component';
import { UserService } from './services/user-service/userservice.service';
import { CustomerDetailsComponent } from './components/customer/customer-details/customer-details.component';
import { PrescriptionService } from './services/prescription-service/prescription-service.service';
import { UpdateUserDialogComponent } from './components/user/update-user-dialog/update-user-dialog.component';
import { UpdateCustomerDialogComponent } from './components/customer/update-customer-dialog/update-customer-dialog.component';
import { MedicationService } from './services/medication-service/medication-service.service';
import { UpdateMedicationDialogComponent } from './components/medication/update-medication-dialog/update-medication-dialog.component';
import { InventoryService } from './services/inventory-service/inventory.service';
import { UpdateInventoryDialogComponent } from './components/inventory/update-inventory-dialog/update-inventory-dialog.component';
import { UpdatePrescriptionDialogComponent } from './components/prescription/update-prescription-dialog/update-prescription-dialog.component';
import { InvoiceService } from './services/invoice-service/invoice.service';
import { UpdateInvoiceDialogComponent } from './components/invoice/update-invoice-dialog/update-invoice-dialog.component';
import { FooterComponent } from './shared/components/footer/footer.component';
import { CreateUserDialogComponent } from './components/user/create-user-dialog/create-user-dialog.component';
import { CreateCustomerDialogComponent } from './components/customer/create-customer-dialog/create-customer-dialog.component';
import { CreateMedicationDialogComponent } from './components/medication/create-medication-dialog/create-medication-dialog.component';
import { CreateInventoryDialogComponent } from './components/inventory/create-inventory-dialog/create-inventory-dialog.component';
import { CreatePrescriptionDialogComponent } from './components/prescription/create-prescription-dialog/create-prescription-dialog.component';
import { CreateInvoiceDialogComponent } from './components/invoice/create-invoice-dialog/create-invoice-dialog.component';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatCardModule } from '@angular/material/card';
import { CustomerPrescriptionsComponent } from './components/customer/customer-prescriptions/customer-prescriptions.component';
import { CustomerMedicationsComponent } from './components/customer/customer-medications/customer-medications.component';
import { CustomerInvoiceComponent } from './components/customer/customer-invoice/customer-invoice.component';
import { HomeService } from './services/home-service/home.service';
import { RegistrationComponent } from './shared/components/registration/registration.component';
import { AuthService } from './services/auth-service/auth-service.service';
import { LoginComponent } from './shared/components/login/login.component';
import { AuthInterceptor } from './services/auth-interceptor/auth-interceptor.interceptor';
import { NavigationComponent } from './shared/components/navigation/navigation.component';
import { ForgotPasswordComponent } from './shared/components/forgot-password/forgot-password.component';
import { ToastrModule, ToastrService  } from 'ngx-toastr';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { UserProfileRoutingModule } from './modules/user-profile-routing.module';
import { UserProfileModule } from './modules/user-profile.module';
import { MatTooltipModule } from '@angular/material/tooltip';

@NgModule({
  declarations: [
    AppComponent,
    NavigationComponent,
    UserComponent,
    CustomerComponent,
    InvoiceComponent,
    MedicationComponent,
    PrescriptionComponent,
    InventoryComponent,
    HomeComponent,
    CustomerDetailsComponent,
    UpdateUserDialogComponent,
    UpdateCustomerDialogComponent,
    UpdateMedicationDialogComponent,
    UpdateInventoryDialogComponent,
    UpdatePrescriptionDialogComponent,
    UpdateInvoiceDialogComponent,
    FooterComponent,
    CreateUserDialogComponent,
    CreateCustomerDialogComponent,
    CreateMedicationDialogComponent,
    CreateInventoryDialogComponent,
    CreatePrescriptionDialogComponent,
    CreateInvoiceDialogComponent,
    CustomerPrescriptionsComponent,
    CustomerMedicationsComponent,
    CustomerInvoiceComponent,
    RegistrationComponent,
    LoginComponent,
    ForgotPasswordComponent,
  ],
  imports: [
  BrowserModule,
    AppRoutingModule,
    AngularMaterialModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    MatDialogModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatCardModule,
    ToastrModule.forRoot({
      timeOut: 3000, // Set default toast duration to 3 seconds
      progressBar: true, // Enable progress bar
      positionClass: 'toast-top-right', // Set default position to top-right
    }),
    MatSnackBarModule,
    UserProfileRoutingModule,
    UserProfileModule,
    MatTooltipModule
    
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true,
    },
    AuthService,
    UserService,
    HomeService,
    CustomerService,
    InvoiceService,
    InventoryService,
    MedicationService,
    PrescriptionService,
    ToastrService,
    { provide: MAT_DIALOG_DATA, useValue: {} },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
