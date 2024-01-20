// create-prescription-dialog.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import * as alertify from 'alertifyjs';
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
    private dialogRef: MatDialogRef<CreatePrescriptionDialogComponent>
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
        alertify.success('Prescription created successfully');
        this.dialogRef.close(response.data.prescription);
      },
      error: (error) => {
        console.error('Error creating prescription:', error);
        alertify.error('Error creating prescription. Please try again.');
      },
    });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
