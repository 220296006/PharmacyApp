import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { InvoiceService } from '../../../services/invoice-service/invoice.service';
import { PaymentStatus } from 'src/app/interface/payment-status';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-create-invoice-dialog',
  templateUrl: './create-invoice-dialog.component.html',
  styleUrls: ['./create-invoice-dialog.component.scss'],
})
export class CreateInvoiceDialogComponent implements OnInit {
  invoiceForm: FormGroup;
  paymentStatusOptions: PaymentStatus[] = Object.values(PaymentStatus);


  constructor(
    private fb: FormBuilder,
    private invoiceService: InvoiceService,
    private dialogRef: MatDialogRef<CreateInvoiceDialogComponent>,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.initForm();
  }

  initForm(): void {
    this.invoiceForm = this.fb.group({
      customerId: ['', [Validators.required]],
      amount: ['', [Validators.required, Validators.min(0)]],
      dueDate: ['', [Validators.required]],
      paymentStatus:  ['', [Validators.required]],
    });
  }

  onSubmit(invoice: any): void {
    if (Object.values(invoice).length > 0) {
      this.invoiceForm.patchValue(invoice);
      return;
    } else {
      const payload = {
        ...this.invoiceForm.getRawValue(),
        customer: {
          id: this.invoiceForm.get('customerId').value
        },
      };

    this.invoiceService.createInvoice(payload).subscribe({
      next: (response) => {
        console.log('Invoice created successfully:', response.data.invoice);
        this.snackBar.open('Invoice created successfully', 'Close', {
          duration: 3000,
          panelClass: ['success-snackbar'],
        })
        this.dialogRef.close(response.data.invoice);
      },
      error: (error) => {
        console.error('Error creating invoice:', error);
        this.snackBar.open('Error creating invoice', 'Close', {
          duration: 3000,
          panelClass: ['error-snackbar'],
        })
      },
    });
  }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
