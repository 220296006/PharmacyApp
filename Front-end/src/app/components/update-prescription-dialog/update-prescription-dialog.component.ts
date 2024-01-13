import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Prescription } from 'src/app/model/prescription';
import { PrescriptionService } from 'src/app/services/prescription-service/prescription-service.service';

@Component({
  selector: 'app-update-prescription-dialog',
  templateUrl: './update-prescription-dialog.component.html',
  styleUrls: ['./update-prescription-dialog.component.scss'],
})
export class UpdatePrescriptionDialogComponent implements OnInit {
  prescription: Prescription = {
    id: null,
    doctorName: '',
    doctorAddress: '',
    issue_date: undefined,
    customer: {
      id: null,
    },
  };

  constructor(
    public dialogRef: MatDialogRef<UpdatePrescriptionDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private prescriptionService: PrescriptionService
  ) {}

  ngOnInit(): void {
    console.log('Data received in dialog:', this.data);
    if (this.data?.prescription) {
      console.log('Prescription data in dialog:', this.data.prescription);
      this.prescription = this.data.prescription;
    }
  }
  onCancelClick(): void {
    this.dialogRef.close();
  }

  onUpdateClick(): void {
    this.prescriptionService.updatePrescriptionData(this.prescription.id, this.prescription).subscribe({
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
        console.error('Error updating medication:', error);
        this.dialogRef.close(false); 
      },
    });
  }
}
