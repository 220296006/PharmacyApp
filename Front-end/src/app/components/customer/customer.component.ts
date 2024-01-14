import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Customer } from 'src/app/model/customer';
import { CustomerService } from 'src/app/services/customer-service/customer.service';
import { UpdateCustomerDialogComponent } from '../update-customer-dialog/update-customer-dialog.component';

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
    private updateDialog: MatDialog
  ) {}

  ngOnInit() {
    this.getAllCustomerData();
  }

  onCreateCustomer() {
    throw new Error('Method not implemented.');
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
    this.customerService.deleteCustomerById(id).subscribe({
      next: (response) => {
        console.log('Response from server:', response);
        if (response.status === 'OK') {
          this.getAllCustomerData();
        } else {
          console.error('Error: ' + response.message);
        }
      },
      error: (error) => {
        console.error('Error deleting user:', error);
      },
    });
  }

  openUpdateDialog(customer: Customer): void {
    console.log('Customer data passed to dialog:', customer); // Check if user is defined
    const dialogRef = this.updateDialog.open(UpdateCustomerDialogComponent, {
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
