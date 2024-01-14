import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Medication } from 'src/app/model/medication';
import { MedicationService } from 'src/app/services/medication-service/medication-service.service';
import { UpdateMedicationDialogComponent } from '../update-medication-dialog/update-medication-dialog.component';
@Component({
  selector: 'app-medication',
  templateUrl: './medication.component.html',
  styleUrls: ['./medication.component.scss']
})
export class MedicationComponent implements OnInit{
onCreateMedication() {
throw new Error('Method not implemented.');
}
  tableDataSource: MatTableDataSource<Medication> = new MatTableDataSource<Medication>([]);
  displayedColumns: string[] = [
    'id',
    'name',
    'dosage',
    'frequency',
    'options'
  ];
  
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private medicationService: MedicationService,
    private updateDialog: MatDialog) {}

  ngOnInit(){
    this.getAllMedicationData();
  }

  getAllMedicationData(){
    this.medicationService.getAllMedicationData().subscribe({
      next: (response) => {
        console.log('Response from server:', response);
        if (
          response.status === 'OK' &&
          response.data &&
          Array.isArray(response.data.medication)
        ) {
          console.log('Data received:', response.data.medication);
          this.tableDataSource.data = response.data.medication;
          this.tableDataSource.paginator = this.paginator;
          this.tableDataSource.sort = this.sort;
          
        } else {
          console.error('Error: ' + response.message);
        }
      },
      error: (error) => {
        console.error('Error fetching data:', error);
      },
    });

  }


  onUpdateMedication(medicationId: number) {
    this.medicationService.getMedicationById(medicationId).subscribe({
      next: (response) => {
        console.log('Response from server:', response);

        if (response.status === 'OK' && response.data && response.data.medication) {
          console.log('Data passed to dialog:', response.data);
          const medication = response.data.medication;
          if (medication) {
            this.openUpdateDialog(medication);
          } else {
            console.error('Error: User not found in response.data.user');
          }
        } else {
          console.error('Error: User data not present in the response');
        }
      },
      error: (error) => {
        console.error('Error fetching user:', error);
      },
    });
  }

  onDeleteMedication(id: number) {
    this.medicationService.deleteMedicationById(id).subscribe({
      next: (response) => {
        console.log('Response from server:', response);
        if (response.status === 'OK') {
          this.getAllMedicationData();
        } else {
          console.error('Error: ' + response.message);
        }
      },
      error: (error) => {
        console.error('Error deleting user:', error);
      },
    });
  }

  openUpdateDialog(medication: Medication): void {
    console.log('User data passed to dialog:', medication); // Check if user is defined
    const dialogRef = this.updateDialog.open(UpdateMedicationDialogComponent, {
      width: '400px',
      exitAnimationDuration: '1000ms',
      enterAnimationDuration: '1000ms',
      data: { medication: medication },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log('The update dialog was closed');
      if (result) {
        this.getAllMedicationData();
      }
    });
  }
}
