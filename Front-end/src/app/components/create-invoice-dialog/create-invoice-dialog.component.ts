import { Customer } from 'src/app/model/customer';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { InvoiceService } from './../../services/invoice-service/invoice.service';
import * as alertify from 'alertifyjs';
import { PaymentStatus } from 'src/app/model/payment-status';

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
    private dialogRef: MatDialogRef<CreateInvoiceDialogComponent>
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
        alertify.success('Invoice created successfully');
        this.dialogRef.close(response.data.invoice);
      },
      error: (error) => {
        console.error('Error creating invoice:', error);
        alertify.error('Error creating invoice. Please try again.');
      },
    });
  }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
