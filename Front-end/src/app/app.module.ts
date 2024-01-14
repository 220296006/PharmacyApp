import { MatFormFieldModule } from '@angular/material/form-field';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { CustomerService } from './services/customer-service/customer.service';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavigationComponent } from './shared/components/navigation/navigation.component';
import { AngularMaterialModule } from './modules/material.module';
import { UserComponent } from './components/user/user.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatTableModule } from '@angular/material/table';
import { CustomerComponent } from './components/customer/customer.component';
import { MatSortModule } from '@angular/material/sort';
import { MatPaginatorModule } from '@angular/material/paginator';
import { InvoiceComponent } from './components/invoice/invoice.component';
import { MedicationComponent } from './components/medication/medication.component';
import { PrescriptionComponent } from './components/prescription/prescription.component';
import { InventoryComponent } from './components/inventory/inventory.component';
import { HomeComponent } from './components/home/home.component';
import { UserService } from './services/user-service/userservice.service';
import { CustomerDetailsComponent } from './components/customer-details/customer-details.component';
import { PrescriptionService } from './services/prescription-service/prescription-service.service';
import { UpdateUserDialogComponent } from './components/update-user-dialog/update-user-dialog.component';
import { UpdateCustomerDialogComponent } from './components/update-customer-dialog/update-customer-dialog.component';
import { MedicationService } from './services/medication-service/medication-service.service';
import { UpdateMedicationDialogComponent } from './components/update-medication-dialog/update-medication-dialog.component';
import { InventoryService } from './services/inventory-service/inventory.service';
import { UpdateInventoryDialogComponent } from './components/update-inventory-dialog/update-inventory-dialog.component';
import { UpdatePrescriptionDialogComponent } from './components/update-prescription-dialog/update-prescription-dialog.component';
import { InvoiceService } from './services/invoice-service/invoice.service';
import { UpdateInvoiceDialogComponent } from './components/update-invoice-dialog/update-invoice-dialog.component';
import { FooterComponent } from './shared/components/footer/footer.component';

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
    FooterComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    AngularMaterialModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    MatDialogModule,
    MatFormFieldModule
  ],
  providers: [
    UserService,
    CustomerService,
    InvoiceService,
    InventoryService,
    MedicationService,
    PrescriptionService,
    { provide: MAT_DIALOG_DATA, useValue: {} },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
