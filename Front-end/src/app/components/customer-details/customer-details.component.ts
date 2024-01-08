import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Customer } from 'src/app/model/customer';
import { CustomerService } from 'src/app/services/customer-service/customer.service';

@Component({
  selector: 'app-customer-details',
  templateUrl: './customer-details.component.html',
  styleUrls: ['./customer-details.component.scss'],
})
export class CustomerDetailsComponent implements OnInit {
  customer: Customer | undefined;

  constructor(
    private route: ActivatedRoute,
    private customerService: CustomerService
  ) {}

  ngOnInit(): void {
    // Extract customer ID from route parameters
    this.route.params.subscribe((params) => {
      const customerId = +params['id'];
      this.getCustomerById(customerId);
    });
  }

  getCustomerById(customerId: number): void {
    console.log('Loading customer details for ID:', customerId);
    this.customerService.getCustomerById(customerId).subscribe({
      next: (response: any) => {
        console.log('Response from server:', response);
        this.customer = response.data.customer;
      },
      error: (error) => {
        console.error('Error fetching customer details:', error);
        this.handleError(error);
      },
      complete: () => {
        console.log('Customer fetch operation completed.');
      },
    });
  }

  private handleError(error: any): void {
    // Log the error or perform additional error handling here
    console.error('Detailed error:', error);

    // You can also show a user-friendly message or redirect to an error page
    // For now, let's log a simple message to the console
    console.error('An error occurred while fetching customer details. Please try again later.');
  }
}
