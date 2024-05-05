import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Medication } from 'src/app/model/medication';
import { MedicationService } from 'src/app/services/medication-service/medication-service.service';
import { UpdateCustomerDialogComponent } from '../../customer/update-customer-dialog/update-customer-dialog.component';

@Component({
  selector: 'app-update-medication-dialog',
  templateUrl: './update-medication-dialog.component.html',
  styleUrls: ['./update-medication-dialog.component.scss'],
})
export class UpdateMedicationDialogComponent implements OnInit {
  medication: Medication = {
    id: null,
    name: '',
    dosage: '',
    frequency: '',
    prescription: {
      id: null,
    },
  };

  constructor(
    public dialogRef: MatDialogRef<UpdateCustomerDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private medicationService: MedicationService
  ) {}

  ngOnInit(): void {
    console.log('Data received in dialog:', this.data);
    if (this.data?.medication) {
      console.log('User data in dialog:', this.data.medication);
      this.medication = this.data.medication;
    }
  }

  onCancelClick(): void {
    this.dialogRef.close();
  }

  onUpdateClick(): void {
    this.medicationService.updateMedicationData(this.medication.id, this.medication).subscribe({
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
