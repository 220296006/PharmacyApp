import { Component, Input } from '@angular/core';
import { Invoice } from 'src/app/model/invoice';

@Component({
  selector: 'app-customer-invoice',
  templateUrl: './customer-invoice.component.html',
  styleUrls: ['./customer-invoice.component.scss']
})
export class CustomerInvoiceComponent {
  @Input() invoices: Invoice[] | undefined;
  
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
  
  

}
