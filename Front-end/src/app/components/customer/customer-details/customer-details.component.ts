import { MedicationService } from 'src/app/services/medication-service/medication-service.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CustomerService } from 'src/app/services/customer-service/customer.service';
import { Prescription } from 'src/app/interface/prescription'; // Import Prescription model
import { PrescriptionService } from 'src/app/services/prescription-service/prescription-service.service';
import { forkJoin } from 'rxjs';
import { Medication } from 'src/app/interface/medication';
import { Invoice } from 'src/app/interface/invoice';
import { InvoiceService } from 'src/app/services/invoice-service/invoice.service';
import { Customer } from 'src/app/interface/customer';

@Component({
  selector: 'app-customer-details',
  templateUrl: './customer-details.component.html',
  styleUrls: ['./customer-details.component.scss'],
})
export class CustomerDetailsComponent implements OnInit {

  selectedTab: string;

  selectTab(tab: string): void {
    this.selectedTab = tab;
  }

  customers: Customer | undefined;
  prescriptions: Prescription []| undefined; 
  medications: Medication []| undefined; 
  invoices: Invoice []| undefined; 

  constructor(
    private route: ActivatedRoute,
    private customerService: CustomerService,
    private prescriptionService: PrescriptionService,
    private medicationService: MedicationService,
    private invoiceService: InvoiceService
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      const customerId = +params['id'];
      const prescription_id = +params['id'];
  
      forkJoin({
        customer: this.customerService.getCustomerById(customerId),
        prescriptions: this.prescriptionService.getPrescriptionsByCustomerId(customerId),
        medications: this.medicationService.getMedicationsByPrescriptionId(prescription_id),
        invoices: this.invoiceService.getInvoicesByCustomerId(customerId),
      }).subscribe({
        next: (responses: any) => {
          console.log('Response from server:', responses);
          this.customers = responses.customer.data.customer;
          this.loadProfileImage(this.customers);
          this.prescriptions = responses.prescriptions.data.prescription;
          this.medications = responses.medications.data.medications;
          this.invoices = responses.invoices.data.invoices;
        },
        error: (error) => {
          console.error('Error fetching data:', error);
          this.handleError(error);
        },
        complete: () => {
          //console.log('Data fetch operation completed.');
        },
      });
    });
  }

  loadProfileImage(customer: Customer): void {
    if (customer.user && customer.user.id) {
      this.customerService.getCustomerImageData(customer.user.id).subscribe(
        (data: Blob) => {
          const reader = new FileReader();
          reader.onloadend = () => {
            customer.user.imageUrl = reader.result as string;
          };
          reader.readAsDataURL(data);
        },
        (error) => {
          console.error('Error loading profile image:', error);
          // Handle error (e.g., display default image)
        }
      );
    }
  }
  

  getCustomerById(customerId: number) {
    console.log('Loading customer details for ID:', customerId);
    this.customerService.getCustomerById(customerId).subscribe({
      next: (response: any) => {
        console.log('Response from server:', response);
        this.customers = response.data.customer;
        console.log('Customer Url:', this.customers.user.imageUrl);
        if (this.customers.user) {
          this.customers.user.imageUrl = response.data.customer.user.imageUrl;
        }
        this.prescriptions = response.data.prescriptions;
        this.medications = response.data.medications;
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
  

  getInvoicesByCustomerId(customerId: number) {
    console.log('Loading invoices for customer ID:', customerId);
    this.invoiceService.getInvoicesByCustomerId(customerId).subscribe({
      next: (response: any) => {
        console.log(' Invoices from server:', response);
        this.prescriptions = response.data.invoice; 
      },
      error: (error) => {
        console.error('Error fetching  invoice:', error);
        this.handleError(error);
      },
      complete: () => {
        console.log(' Invoices fetch operation completed.');
      },
    });
  }

  getPrescriptionById(customerId: number) {
    console.log('Loading prescriptions for customer ID:', customerId);
    this.prescriptionService.getPrescriptionsByCustomerId(customerId).subscribe({
      next: (response: any) => {
        console.log('Prescriptions from server:', response);
        this.prescriptions = response.data.prescription; 
      },
      error: (error) => {
        console.error('Error fetching prescriptions:', error);
        this.handleError(error);
      },
      complete: () => {
        console.log('Prescriptions fetch operation completed.');
      },
    });
  }

  getMedicationsByPrescriptionId(prescription_id: number) {
    console.log('Loading prescriptions for customer ID:', prescription_id);
    this.medicationService.getMedicationsByPrescriptionId(prescription_id).subscribe({
      next: (response: any) => {
        console.log('Medications from server:', response);
        this.prescriptions = response.data.medications; 
      },
      error: (error) => {
        console.error('Error fetching medications:', error);
        this.handleError(error);
      },
      complete: () => {
        console.log('Medications fetch operation completed.');
      },
    });
  }
  
  

  private handleError(error: any): void {
    console.error('Detailed error:', error);
    console.error('An error occurred. Please try again later.');
  }
}
