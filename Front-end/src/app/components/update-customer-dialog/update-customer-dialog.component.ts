import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Customer } from 'src/app/model/customer';
import { CustomerService } from 'src/app/services/customer-service/customer.service';

@Component({
  selector: 'app-update-customer-dialog',
  templateUrl: './update-customer-dialog.component.html',
  styleUrls: ['./update-customer-dialog.component.scss'],
})
export class UpdateCustomerDialogComponent implements OnInit {

  customer: Customer = {
    id: null,
    imageUrl: '',
    address: '',
    city: '',
    zipCode: '',
    state: '',
    user: {
      firstName: '',
      id: null
    }
  };

  constructor(
    public dialogRef: MatDialogRef<UpdateCustomerDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private customerService: CustomerService
  ) {}

  ngOnInit(): void {
    console.log('Data received in dialog:', this.data);
    if (this.data?.customer) {
      console.log('User data in dialog:', this.data.customer);
      this.customer = this.data.customer;
    }  }


    onCancelClick(): void {
      this.dialogRef.close();
    }
  
    onUpdateClick(): void {
      this.customerService.updateCustomerData(this.customer.id, this.customer).subscribe({
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
          console.error('Error updating customer:', error);
          this.dialogRef.close(false); 
        },
      });
    }
}
