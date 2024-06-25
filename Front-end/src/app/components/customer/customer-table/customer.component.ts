import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CustomerService } from 'src/app/services/customer-service/customer.service';
import { UpdateCustomerDialogComponent } from '../update-customer-dialog/update-customer-dialog.component';
import { CreateCustomerDialogComponent } from '../create-customer-dialog/create-customer-dialog.component';
import { AuthService } from 'src/app/services/auth-service/auth-service.service';
import { Customer } from 'src/app/interface/customer';

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.scss'],
})
export class CustomerComponent implements OnInit {

  tableDataSource: MatTableDataSource<Customer[]> = new MatTableDataSource<Customer[]>([]);
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
    private createCustomerDialog: MatDialog,
    private snackBar: MatSnackBar,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.getAllCustomerData();
    const permissions = this.authService.getPermissionsFromToken();
    console.log('User permissions:', permissions);
  }

  onCreateCustomerDialog(id: any) {
    const permissions = this.authService.getPermissionsFromToken();
    if (permissions.includes('CREATE:CUSTOMER')) {
      const dialogRef = this.createCustomerDialog.open(CreateCustomerDialogComponent, {
        width: '400px',
        exitAnimationDuration: '1000ms',
        enterAnimationDuration: '1000ms',
        data: { id: id }
      });
      dialogRef.afterClosed().subscribe(response => {
        this.getAllCustomerData();
        this.snackBar.open('Customer created successfully!', 'Close', {
          duration: 3000,
        });
      });
    } else {
      this.snackBar.open('You do not have permission to create a customer.', 'Close', {
        duration: 3000,
      });
    }
  }

  getAllCustomerData() {
    this.customerService.getAllCustomerData().subscribe({
      next: (response) => {
        console.log('Response from server:', response);
        if (response.status === 'OK' && Array.isArray(response.data.page)) {
          this.tableDataSource.data = response.data.page;
          this.tableDataSource.paginator = this.paginator;
          this.tableDataSource.sort = this.sort;

          // Load profile images for customers
          this.loadCustomerProfileImages();
        } else {
          console.error('Error: ' + response.message);
        }
      },
      error: (error) => {
        console.error('Error fetching data:', error);
        this.snackBar.open('An error occurred while fetching customer data.', 'Close', {
          duration: 3000,
        });
      },
    });
  }

  loadCustomerProfileImages(): void {
    if (Array.isArray(this.tableDataSource.data)) {
      this.tableDataSource.data.forEach((customer: any) => {
        if (customer.user && customer.user.id) {
          this.customerService.getCustomerImageData(customer.user.id).subscribe(
            (data: Blob) => {
              const reader = new FileReader();
              reader.onloadend = () => {
                customer.user.imageUrl = reader.result as string;
              };
              reader.readAsDataURL(data);
            },
            (error) => {
              console.error('Error loading profile image:', error);
              // Handle error (e.g., display default image)
            }
          );
        }
      });
    }
  }

  

  onUpdateCustomer(customerId: number) {
    const permissions = this.authService.getPermissionsFromToken();
    if (permissions.includes('UPDATE:CUSTOMER')) {
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
          this.snackBar.open('An error occurred while fetching customer data.', 'Close', {
            duration: 3000,
          });
        },
      });
    } else {
      this.snackBar.open('You do not have permission to update a customer.', 'Close', {
        duration: 3000,
      });
    }
  }

  onDeleteCustomer(id: number) {
    const permissions = this.authService.getPermissionsFromToken();
    if (permissions.includes('DELETE:CUSTOMER')) {
      const dialogRef = this.snackBar.open('Are you sure you want to delete this user?', 'Delete', {
        duration: 5000, // Adjust duration as needed
        verticalPosition: 'top',
        horizontalPosition: 'center',
        panelClass: ['snackbar-confirm'],
      });
      dialogRef.onAction().subscribe(() => {
      this.customerService.deleteCustomerById(id).subscribe({
        next: (response) => {
          console.log('Response from server:', response);
          if (response.status === 'OK') {
            this.snackBar.open('Customer deleted successfully!', 'Close', {
              duration: 3000,
            });
            this.getAllCustomerData();
          } else {
            console.error('Error: ' + response.message);
          }
        },
        error: (error) => {
          console.error('Error deleting customer:', error);
          this.snackBar.open('An error occurred while deleting the customer.', 'Close', {
            duration: 3000,
          });
        },
      });
    });
    } else {
      this.snackBar.open('You do not have permission to delete a customer.', 'Close', {
        duration: 3000,
      });
    }
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
