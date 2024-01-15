import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Invoice } from 'src/app/model/invoice';
import { InvoiceService } from 'src/app/services/invoice-service/invoice.service';
import { UpdateInvoiceDialogComponent } from '../update-invoice-dialog/update-invoice-dialog.component';

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
    private updateDialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.getAllInvoiceData();
  }

  onCreateInvoice() {
    throw new Error('Method not implemented.');
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
      },
    });
  }

  onUpdateInvoice(invoiceId: number) {
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
      },
    });
  }

  onDeleteInvoice(id: number) {
    this.invoiceService.deleteInvoiceById(id).subscribe({
      next: (response) => {
        console.log('Response from server:', response);
        if (response.status === 'OK') {
          this.getAllInvoiceData();
        } else {
          console.error('Error: ' + response.message);
        }
      },
      error: (error) => {
        console.error('Error deleting invoice:', error);
      },
    });
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
