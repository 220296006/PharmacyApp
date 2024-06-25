// create-prescription-dialog.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { PrescriptionService } from 'src/app/services/prescription-service/prescription-service.service';

@Component({
  selector: 'app-create-prescription-dialog',
  templateUrl: './create-prescription-dialog.component.html',
  styleUrls: ['./create-prescription-dialog.component.scss'],
})
export class CreatePrescriptionDialogComponent implements OnInit {
  prescriptionForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private prescriptionService: PrescriptionService,
    private dialogRef: MatDialogRef<CreatePrescriptionDialogComponent>,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.initForm();
  }

  initForm(): void {
    this.prescriptionForm = this.fb.group({
      customerId: ['', [Validators.required]],
      doctorName: ['', [Validators.required]],
      doctorAddress: ['', [Validators.required]],
      issueDate: ['', [Validators.required]],
    });
  }

  onSubmit(prescription: any): void {
    if (Object.values(prescription).length > 0) {
      this.prescriptionForm.patchValue(prescription);
      return;
    }else{
      const payload = {
        ...this.prescriptionForm.getRawValue(),
        customer:{
          id: this.prescriptionForm.get("customerId").value
        }
      }
    this.prescriptionService.createPrescription(payload).subscribe({
      next: (response) => {
        console.log('Prescription created successfully:', response.data.prescription);
        this.snackBar.open('Prescription created successfully', 'Close', {
          duration: 3000,
          panelClass: ['success-snackbar'],
        })
        this.dialogRef.close(response.data.prescription);
      },
      error: (error) => {
        console.error('Error creating prescription:', error);
        this.snackBar.open('Error creating prescription', 'Close', {
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
