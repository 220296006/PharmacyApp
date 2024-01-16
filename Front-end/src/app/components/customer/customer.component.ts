import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Customer } from 'src/app/model/customer';
import { CustomerService } from 'src/app/services/customer-service/customer.service';
import { UpdateCustomerDialogComponent } from '../update-customer-dialog/update-customer-dialog.component';
import { CreateCustomerDialogComponent } from '../create-customer-dialog/create-customer-dialog.component';
import * as alertify from 'alertifyjs';

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.scss'],
})
export class CustomerComponent implements OnInit {


  tableDataSource: MatTableDataSource<Customer[]> = new MatTableDataSource<
    Customer[]
  >([]);
  displayedColumns: string[] = [
    'id',
    'imageUrl',
    'firstName',
    'address',
    'city',
    'state',
    'zipCode',
    'options',
  ];

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private customerService: CustomerService,
    private updateCustomerDialog: MatDialog,
    private createCustomerDialog: MatDialog

  ) {}

  ngOnInit() {
    this.getAllCustomerData();
  }

  onCreateCustomerDialog(id: any) {
    const dialogRef = this.createCustomerDialog.open(CreateCustomerDialogComponent,{
      width: '400px',
      exitAnimationDuration: '1000ms',
      enterAnimationDuration: '1000ms',
      data:
      {
        id: id
      }
    })
    dialogRef.afterClosed().subscribe(response=> {
      response = this.getAllCustomerData();
    });
    }

  getAllCustomerData() {
    this.customerService.getAllCustomerData().subscribe({
      next: (response) => {
        console.log('Response from server:', response);
        if (response.status === 'OK' && Array.isArray(response.data.page)) {
          this.tableDataSource.data = response.data.page;
          this.tableDataSource.paginator = this.paginator;
          this.tableDataSource.sort = this.sort;
        } else {
          console.error('Error: ' + response.message);
        }
      },
      error: (error) => {
        console.error('Error fetching data:', error);
      },
    });
  }

  onUpdateCustomer(customerId: number) {
    this.customerService.getCustomerById(customerId).subscribe({
      next: (response) => {
        console.log('Response from server:', response);
        if (
          response.status === 'OK' &&
          response.data &&
          response.data.customer
        ) {
          console.log('Data passed to dialog:', response.data);
          const customer = response.data.customer;
          if (customer) {
            this.openUpdateDialog(customer);
          } else {
            console.error('Error: Customer not found in response.data.user');
          }
        } else {
          console.error('Error: Customer data not present in the response');
        }
      },
      error: (error) => {
        console.error('Error fetching customer:', error);
      },
    });
  }

  onDeleteCustomer(id: number) {
    alertify.confirm('Are you sure you want to permanently delete this customer?', () => {
    this.customerService.deleteCustomerById(id).subscribe({
      next: (response) => {
        console.log('Response from server:', response);
        if (response.status === 'OK') {
          alertify.success('Customer deleted successfully!');
          this.getAllCustomerData();.0
          
        } else {
          console.error('Error: ' + response.message);
        }
      },
      error: (error) => {
        console.error('Error deleting customer:', error);
        alertify.error('An error occurred while deleting the customer.');
      },
    });
  }, () => {
  });
}

  openUpdateDialog(customer: Customer): void {
    console.log('Customer data passed to dialog:', customer); // Check if user is defined
    const dialogRef = this.updateCustomerDialog.open(UpdateCustomerDialogComponent, {
      width: '400px',
      exitAnimationDuration: '1000ms',
      enterAnimationDuration: '1000ms',
      data: { customer: customer },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log('The update dialog was closed');
      if (result) {
        this.getAllCustomerData();
      }
    });
  }
}
