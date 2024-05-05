import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Medication } from 'src/app/model/medication';
import { MedicationService } from 'src/app/services/medication-service/medication-service.service';
import { UpdateMedicationDialogComponent } from '../update-medication-dialog/update-medication-dialog.component';
import { CreateMedicationDialogComponent } from '../create-medication-dialog/create-medication-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from 'src/app/services/auth-service/auth-service.service';

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
    'prescription_id',
    'name',
    'dosage',
    'frequency',
    'options'
  ];
  
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private medicationService: MedicationService,
    private updateMedicationDialog: MatDialog,
    private createMedicationDialog: MatDialog,
    private snackBar: MatSnackBar,
    private authService: AuthService
  ) {}

  ngOnInit(){
    this.getAllMedicationData();
    const permissions = this.authService.getPermissionsFromToken();
    console.log('User permissions:', permissions);
  }


  onCreateMedicationDialog(id: any){
    const permissions = this.authService.getPermissionsFromToken();
    if (permissions.includes('CREATE:MEDICATION')) {
    const dialogRef = this.createMedicationDialog.open(CreateMedicationDialogComponent,{
    width: '400px',
    exitAnimationDuration: '1000ms',
    enterAnimationDuration: '1000ms',
    data:
    {
      id: id
    }
  })
  dialogRef.afterClosed().subscribe(response=> {
    response = this.getAllMedicationData();
    this.snackBar.open('Medication created successfully!', 'Close', {
      duration: 3000,
    });
  });
} else {
  this.snackBar.open('You do not have permission to create an Medication.', 'Close', {
    duration: 3000,
  });
}
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
        this.snackBar.open('An error occurred while fetching medication data.', 'Close', {
          duration: 3000,
        });
      },
    });
  }

  onUpdateMedication(medicationId: number) {
    const permissions = this.authService.getPermissionsFromToken();
    if (permissions.includes('UPDATE:MEDICATION')) {
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
        this.snackBar.open('An error occurred while fetching medicaton data.', 'Close', {
          duration: 3000,
        });
      },
    });
  } else {
    this.snackBar.open('You do not have permission to update an medicaton.', 'Close', {
      duration: 3000,
    });
  }
}

  onDeleteMedication(id: number) {
    const permissions = this.authService.getPermissionsFromToken();
    if (permissions.includes('DELETE:MEDICATION')) {
      const dialogRef = this.snackBar.open('Are you sure you want to delete this Medication?', 'Delete', {
        duration: 5000, // Adjust duration as needed
        verticalPosition: 'bottom',
        horizontalPosition: 'center',
        panelClass: ['snackbar-confirm'],
      });
      dialogRef.onAction().subscribe(() => {
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
        console.error('Error deleting medication:', error);
        this.snackBar.open('An error occurred while deleting the medicaton.', 'Close', {
          duration: 3000,
        });
      },
    });
  });
} else {
  this.snackBar.open('You do not have permission to delete an medicaton.', 'Close', {
    duration: 3000,
  });
}
}

  openUpdateDialog(medication: Medication): void {
    console.log('User data passed to dialog:', medication); // Check if user is defined
    const dialogRef = this.updateMedicationDialog.open(UpdateMedicationDialogComponent, {
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
