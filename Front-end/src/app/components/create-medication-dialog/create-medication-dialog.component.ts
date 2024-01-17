import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import * as alertify from 'alertifyjs';
import { InventoryService } from 'src/app/services/inventory-service/inventory.service';
import { MedicationService } from 'src/app/services/medication-service/medication-service.service';

@Component({
  selector: 'app-create-medication-dialog',
  templateUrl: './create-medication-dialog.component.html',
  styleUrls: ['./create-medication-dialog.component.scss'],
})
export class CreateMedicationDialogComponent implements OnInit {
  medicationForm: FormGroup;
  availableMedications: any[] = [];

  constructor(
    private fb: FormBuilder,
    private medicationService: MedicationService,
    private inventoryService: InventoryService, 
    private dialogRef: MatDialogRef<CreateMedicationDialogComponent>
  ) { }

  ngOnInit(): void {
    this.initForm();
    this.loadAvailableMedications();
  }

  initForm(): void {
    this.medicationForm = this.fb.group({
      prescriptionId: ['', [Validators.required]],
      name: ['', [Validators.required]],
      dosage: ['', [Validators.required]],
      frequency: ['', [Validators.required]],
    });
  }

  loadAvailableMedications(): void {
    this.inventoryService.getAvailableMedications().subscribe(
      (medications) => {
       this.availableMedications = medications;
      },
      (error) => {
        console.error('Error fetching available medications:', error);
      }
    );
  }

  onSubmit(medication: any): void {
    if (Object.values(medication).length > 0) {
      this.medicationForm.patchValue(medication);
      return;
    }
    const payload = {
      ...this.medicationForm.getRawValue(),
      prescription: {
        id: this.medicationForm.get('prescriptionId').value,
      }
    };

    this.medicationService.createMedication(payload).subscribe({
      next: (response) => {
        console.log('Medication added successfully:', response.data.customer);
        alertify.success('Medication added successfully');
        this.dialogRef.close(response.data.customer);
      },
      error: (error) => {
        console.error('Error adding medication:', error);
        alertify.error('Error adding medication. Please try again.');
      },
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}


