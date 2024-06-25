import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Invoice } from 'src/app/interface/invoice';
import { InvoiceService } from 'src/app/services/invoice-service/invoice.service';

@Component({
  selector: 'app-update-invoice-dialog',
  templateUrl: './update-invoice-dialog.component.html',
  styleUrls: ['./update-invoice-dialog.component.scss'],
})
export class UpdateInvoiceDialogComponent implements OnInit {
  invoice: Invoice = {
    id: null,
    amount: undefined,
    dueDate: undefined,
    paymentStatus: undefined,
    customer: {
      id: null
    },
  };

  constructor(
    public dialogRef: MatDialogRef<UpdateInvoiceDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private invoiceService: InvoiceService
  ) {}

  ngOnInit(): void {
    console.log('Data received in dialog:', this.data);
    if (this.data?.invoice) {
      console.log('Invoice data in dialog:', this.data.invoice);
      this.invoice = this.data.invoice;
    }
  }

  onCancelClick(): void {
    this.dialogRef.close();
  }

  onUpdateClick(): void {
    this.invoiceService.updateInvoiceData(this.invoice.id, this.invoice).subscribe({
      next: (response) => {
        console.log('Response from server:', response);
        if (response.status === 'CREATED') {
          this.dialogRef.close(true); 
        } else {
          console.error('Error: ' + response.message);
          this.dialogRef.close(false); 
        }
      },
      error: (error) => {
        console.error('Error updating invoice:', error);
        this.dialogRef.close(false); 
      },
    });
  }
}
