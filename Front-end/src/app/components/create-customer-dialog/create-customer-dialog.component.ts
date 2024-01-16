import { CustomerService } from './../../services/customer-service/customer.service';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import * as alertify from 'alertifyjs';

@Component({
  selector: 'app-create-customer-dialog',
  templateUrl: './create-customer-dialog.component.html',
  styleUrls: ['./create-customer-dialog.component.scss'],
})
export class CreateCustomerDialogComponent implements OnInit {
  customerForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private customerService: CustomerService,
    private dialogRef: MatDialogRef<CreateCustomerDialogComponent>
  ) {}
  ngOnInit(): void {
    this.initForm();
  }

  initForm(): void {
    this.customerForm = this.fb.group({
      userId: ['', [Validators.required]],
      city: ['', [Validators.required]],
      state: [''],
      zipCode: ['', [Validators.required]],
    });
  }

  onSubmit(customer: any): void {
    if (Object.values(customer).length > 0) {
      // If customer object is provided, set it directly
      this.customerForm.patchValue(customer);
    } else {
      // Otherwise, create the complete payload including the user object
      const payload = {
        ...this.customerForm.getRawValue(),
        user: {
          id: this.customerForm.get('userId').value, // Assuming userId is the user ID
          firstName: '', // Add other user properties as needed
          imageUrl: '',
          address: ''
        }
      };
  
      this.customerService.createCustomer(payload).subscribe({
        next: (response) => {
          console.log('Customer added successfully:', response.data.customer);
          alertify.success('Customer added successfully');
          this.dialogRef.close(response.data.customer);
        },
        error: (error) => {
          console.error('Error adding customer:', error);
          alertify.error('Error adding customer. Please try again.');
        },
      });
    }
  }
  
  

  onCancel(): void {
    this.dialogRef.close();
  }
}
