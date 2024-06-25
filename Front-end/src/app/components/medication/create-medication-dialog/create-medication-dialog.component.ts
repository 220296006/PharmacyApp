import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
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
    private dialogRef: MatDialogRef<CreateMedicationDialogComponent>,
    private snackBar: MatSnackBar
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
        this.snackBar.open('Medication added successfully', 'Close', {
          duration: 3000,
          panelClass: ['success-snackbar'],
        })
        this.dialogRef.close(response.data.customer);
        this.snackBar.open('Medication added successfully', 'Close', {
          duration: 3000,
          panelClass: ['success-snackbar'],
        })
      },
      error: (error) => {
        console.error('Error adding medication:', error);
        this.snackBar.open('An error occurred while adding the medication.', 'Close', {
          duration: 3000,
          panelClass: ['error-snackbar'],
        })
      },
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}


