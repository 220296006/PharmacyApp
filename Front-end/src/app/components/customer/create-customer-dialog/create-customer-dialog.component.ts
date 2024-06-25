import { CustomerService } from '../../../services/customer-service/customer.service';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';

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
    private dialogRef: MatDialogRef<CreateCustomerDialogComponent>,
    private snackBar: MatSnackBar,
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
      this.customerForm.patchValue(customer);
    } else {
      const payload = {
        ...this.customerForm.getRawValue(),
        user: {
          id: this.customerForm.get('userId').value,
          firstName: '', 
          imageUrl: '',
          address: '',
        },
      };

      this.customerService.createCustomer(payload).subscribe({
        next: (response) => {
          console.log('Customer added successfully:', response.data.customer);
          this.dialogRef.close(response.data.customer);
          this.snackBar.open('Customer added successfully!', 'Close', {
            duration: 3000,
            panelClass: ['success-snackbar'],
          })
        },
        error: (error) => {
          console.error('Error adding customer:', error);
          this.snackBar.open('An error occurred while adding the customer.', 'Close', {
            
          })
        },
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
