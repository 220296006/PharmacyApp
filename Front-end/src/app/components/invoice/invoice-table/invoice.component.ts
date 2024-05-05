import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Invoice } from 'src/app/model/invoice';
import { InvoiceService } from 'src/app/services/invoice-service/invoice.service';
import { UpdateInvoiceDialogComponent } from '../update-invoice-dialog/update-invoice-dialog.component';
import { CreateInvoiceDialogComponent } from '../create-invoice-dialog/create-invoice-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from 'src/app/services/auth-service/auth-service.service';

@Component({
  selector: 'app-invoice',
  templateUrl: './invoice.component.html',
  styleUrls: ['./invoice.component.scss'],
})
export class InvoiceComponent implements OnInit {

  tableDataSource: MatTableDataSource<Invoice> = new MatTableDataSource<Invoice>([]);
  displayedColumns: string[] = [
    'id',
    'customer_id',
    'amount',
    'dueDate',
    'paymentStatus',
    'options',
  ];

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  invoices: any;

  constructor(
    private invoiceService: InvoiceService,
    private updateDialog: MatDialog,
    private createInvoiceDialog: MatDialog,
    private snackBar: MatSnackBar,
    private authService: AuthService 
  ) {}

  ngOnInit(): void {
    this.getAllInvoiceData();
    const permissions = this.authService.getPermissionsFromToken();
    console.log('User permissions:', permissions);
  }

  onCreateInvoiceDialog(id: any) {
    const permissions = this.authService.getPermissionsFromToken();
    if (permissions.includes('CREATE:INVOICE')) {
    const dialogRef = this.createInvoiceDialog.open(CreateInvoiceDialogComponent,{
      width: '400px',
      exitAnimationDuration: '1000ms',
      enterAnimationDuration: '1000ms',
      data:
      {
        id: id
      }
    });
    dialogRef.afterClosed().subscribe(response=> {
      response = this.getAllInvoiceData();
      this.snackBar.open('Invoice created successfully!', 'Close', {
        duration: 3000,
      });
    });
  } else {
    this.snackBar.open('You do not have permission to create an Invoice.', 'Close', {
      duration: 3000,
    });
  }
}

    getPaymentStatusClass(paymentStatus: string): string {
      switch (paymentStatus) {
        case 'PAID':
          return 'paid-status';
        case 'CANCELLED':
          return 'cancelled-status';
        case 'PENDING':
          return 'pending-status';
        case 'OVERDUE':
          return 'overdue-status';
        default:
          return '';
      }
    }


  getAllInvoiceData() {
    this.invoiceService.getAllInvoiceData().subscribe({
      next: (response) => {
        console.log('Response from server:', response);
        if (
          response.status === 'OK' &&
          response.data &&
          Array.isArray(response.data.invoices)
        ) {
          console.log('Data received:', response.data.invoices);
          this.tableDataSource.data = response.data.invoices;
          this.tableDataSource.paginator = this.paginator;
          this.tableDataSource.sort = this.sort;
        } else {
          console.error('Error: ' + response.message);
        }
      },
      error: (error) => {
      console.error('Error fetching data:', error);
      this.snackBar.open('An error occurred while fetching invoice data.', 'Close', {
          duration: 3000,
        });
      },
    });
  }

  onUpdateInvoice(invoiceId: number) {
    const permissions = this.authService.getPermissionsFromToken();
    if (permissions.includes('UPDATE:INVOICE')) {
    this.invoiceService.getInvoiceById(invoiceId).subscribe({
      next: (response) => {
        console.log('Response from server:', response);
        if (response.status === 'OK' && 
        response.data && 
        response.data.invoice
        ) {
          console.log('Data passed to dialog:', response.data);
          const invoice = response.data.invoice;
          if (invoice) {
            this.openUpdateDialog(invoice);
          } else {
            console.error('Error:  Invoice not found in response.data');
          }
        } else {
          console.error('Error:  Invoice data not present in the response');
        }
      },
      error: (error) => {
        console.error('Error fetching  Invoice:', error);
        this.snackBar.open('An error occurred while fetching invoice data.', 'Close', {
          duration: 3000,
        });
      },
    });
  } else {
    this.snackBar.open('You do not have permission to update an invoice.', 'Close', {
      duration: 3000,
    });
  }
}


  onDeleteInvoice(id: number) {
    const permissions = this.authService.getPermissionsFromToken();
    if (permissions.includes('DELETE:INVENTORY')) {
      const dialogRef = this.snackBar.open('Are you sure you want to delete this Inventory?', 'Delete', {
        duration: 5000, // Adjust duration as needed
        verticalPosition: 'bottom',
        horizontalPosition: 'center',
        panelClass: ['snackbar-confirm'],
      });
      dialogRef.onAction().subscribe(() => {   
      this.invoiceService.deleteInvoiceById(id).subscribe({
      next: (response) => {
        console.log('Response from server:', response);
        if (response.status === 'OK') {
          this.snackBar.open('Inventory deleted successfully!', 'Close', {
            duration: 3000,
          });
          this.getAllInvoiceData();
        } else {
          console.error('Error: ' + response.message);
        }
          this.snackBar.open('An error occurred while deleting the invoice.', 'Close', {
            duration: 3000,
          });
        },
      });
    });
  } else {
    this.snackBar.open('You do not have permission to delete an invoice.', 'Close', {
      duration: 3000,
    });
  }
}

  openUpdateDialog(invoice: Invoice): void {
    console.log('Invoice data passed to dialog:', invoice);
    const dialogRef = this.updateDialog.open(UpdateInvoiceDialogComponent, {
      width: '400px',
      exitAnimationDuration: '1000ms',
      enterAnimationDuration: '1000ms',
      data: { invoice: invoice },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log('The update dialog was closed');
      if (result) {
        this.getAllInvoiceData();
      }
    });
  }
}
