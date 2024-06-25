import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Prescription } from 'src/app/interface/prescription';
import { PrescriptionService } from 'src/app/services/prescription-service/prescription-service.service';
import { UpdatePrescriptionDialogComponent } from '../update-prescription-dialog/update-prescription-dialog.component';
import { CreatePrescriptionDialogComponent } from '../create-prescription-dialog/create-prescription-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from 'src/app/services/auth-service/auth-service.service';

@Component({
  selector: 'app-prescription',
  templateUrl: './prescription.component.html',
  styleUrls: ['./prescription.component.scss'],
})
export class PrescriptionComponent implements OnInit {

  tableDataSource: MatTableDataSource<Prescription[]> = new MatTableDataSource<
    Prescription[]
  >([]);
  displayedColumns: string[] = [
    'id',
    'customerId',
    'doctorName',
    'doctorAddress',
    'issueDate',
    'options',
  ];

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private prescriptionService: PrescriptionService,
    private updatePrescriptionDialog: MatDialog,
    private createPresctiptionDialog: MatDialog,
    private snackBar: MatSnackBar,
    private authService: AuthService
  ) { }

  onCreatePrescriptionDialog(id: any) {
    const permissions = this.authService.getPermissionsFromToken();
    if (permissions.includes('CREATE:PRESCRIPTION')) {
      const dialogRef = this.createPresctiptionDialog.open(CreatePrescriptionDialogComponent, {
        width: '400px',
        exitAnimationDuration: '1000ms',
        enterAnimationDuration: '1000ms',
        data:
        {
          id: id
        }
      })
      dialogRef.afterClosed().subscribe(response => {
        response = this.getAllPrescriptionData();
        this.snackBar.open('Prescription created successfully!', 'Close', {
          duration: 3000,
        });
      });
    } else {
      this.snackBar.open('You do not have permission to create an Prescription.', 'Close', {
        duration: 3000,
      });
    }
  }

  ngOnInit() {
    this.getAllPrescriptionData();
    const permissions = this.authService.getPermissionsFromToken();
    console.log('User permissions:', permissions);
  }

  getAllPrescriptionData() {
    this.prescriptionService.getAllPrescriptionData().subscribe({
      next: (response) => {
        console.log('Response from server:', response);
        if (
          response.status === 'OK' &&
          response.data &&
          Array.isArray(response.data.page)
        ) {
          console.log('Data received:', response.data.page);
          this.tableDataSource.data = response.data.page;
          this.tableDataSource.paginator = this.paginator;
          this.tableDataSource.sort = this.sort;
        } else {
          console.error('Error: ' + response.message);
        }
      },
      error: (error) => {
        console.error('Error fetching data:', error);
        this.snackBar.open('An error occurred while fetching inventory data.', 'Close', {
          duration: 3000,
        });
      },
    });
  }

  onUpdatePrescription(prescriptionId: number) {
    const permissions = this.authService.getPermissionsFromToken();
    if (permissions.includes('UPDATE:INVENTORY')) {
    this.prescriptionService.getPrescriptionById(prescriptionId).subscribe({
      next: (response) => {
        console.log('Response from server:', response);
        if (
          response.status === 'OK' &&
          response.data &&
          response.data.prescription
        ) {
          console.log('Data passed to dialog:', response.data);
          const prescription = response.data.prescription;
          if (prescription) {
            this.openUpdateDialog(prescription);
          } else {
            console.error('Error: Prescription not found in response.data');
          }
        } else {
          console.error('Error: Prescription data not present in the response');
        }
      },
      error: (error) => {
        console.error('Error fetching prescription:', error);
        this.snackBar.open('An error occurred while fetching prescription data.', 'Close', {
          duration: 3000,
        });
      },
    });
  } else {
    this.snackBar.open('You do not have permission to update an prescription.', 'Close', {
      duration: 3000,
    });
  }
}

  deletePrescription(id: number) {
    const permissions = this.authService.getPermissionsFromToken();
    if (permissions.includes('DELETE:PRESCRIPTION')) {
      const dialogRef = this.snackBar.open('Are you sure you want to delete this Prescription?', 'Delete', {
        duration: 5000, // Adjust duration as needed
        verticalPosition: 'bottom',
        horizontalPosition: 'center',
        panelClass: ['snackbar-confirm'],
      });
      dialogRef.onAction().subscribe(() => {
      this.prescriptionService.deletePrescriptionById(id).subscribe({
        next: (response) => {
          console.log('Response from server:', response);
          if (response.status === 'OK') {
            this.snackBar.open('Inventory deleted successfully!', 'Close', {
              duration: 3000,
            });
            this.getAllPrescriptionData();
          } else {
            console.error('Error: ' + response.message);
          }
        },
        error: (error) => {
          console.error('Error deleting prescription:', error);
          this.snackBar.open('An error occurred while deleting the inventory.', 'Close', {
            duration: 3000,
          });
        },
      });
    });
  } else {
    this.snackBar.open('You do not have permission to delete an inventory.', 'Close', {
      duration: 3000,
    });
  }
}



  openUpdateDialog(prescription: Prescription): void {
    console.log('Inventory data passed to dialog:', prescription);
    const dialogRef = this.updatePrescriptionDialog.open(
      UpdatePrescriptionDialogComponent,
      {
        width: '400px',
        exitAnimationDuration: '1000ms',
        enterAnimationDuration: '1000ms',
        data: { prescription: prescription },
      }
    );

    dialogRef.afterClosed().subscribe((result) => {
      console.log('The update dialog was closed');
      if (result) {
        this.getAllPrescriptionData();
      }
    });
  }
}
