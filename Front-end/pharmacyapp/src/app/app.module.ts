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
import { UserService } from './services/userservice.service';
import { InvoiceComponent } from './components/invoice/invoice.component';
import { MedicationComponent } from './components/medication/medication.component';
import { PrescriptionComponent } from './components/prescription/prescription.component';
import { InventoryComponent } from './components/inventory/inventory.component';
import { HomeComponent } from './components/home/home.component';

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
    HomeComponent
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
    MatPaginatorModule
  ],
  providers: [UserService],
  bootstrap: [AppComponent],
})
export class AppModule {}
