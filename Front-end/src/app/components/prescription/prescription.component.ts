import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Prescription } from 'src/app/model/prescription';
import { PrescriptionService } from 'src/app/services/prescription-service/prescription-service.service';
import { UpdatePrescriptionDialogComponent } from '../update-prescription-dialog/update-prescription-dialog.component';
import * as alertify from 'alertifyjs';
import { CreatePrescriptionDialogComponent } from '../create-prescription-dialog/create-prescription-dialog.component';

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
    private createPresctiptionDialog: MatDialog
  ) {}

  onCreatePrescriptionDialog(id: any) {
    const dialogRef = this.createPresctiptionDialog.open(CreatePrescriptionDialogComponent,{
      width: '400px',
      exitAnimationDuration: '1000ms',
      enterAnimationDuration: '1000ms',
      data:
      {
        id: id
      }
    })
    dialogRef.afterClosed().subscribe(response=> {
      response = this.getAllPrescriptionData();
    });    }

  ngOnInit() {
    this.getAllPrescriptionData();
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
      },
    });
  }

  onUpdatePrescription(prescriptionId: number) {
    this.prescriptionService.getPrescriptionById(prescriptionId).subscribe({
      next: (response) => {
        console.log('Response from server:', response);
        if (
          response.status === 'OK' &&
          response.data &&
          response.data.prescription
        ) {
          console.log('Data passed to dialog:', response.data);
          const inventory = response.data.prescription;
          if (inventory) {
            this.openUpdateDialog(inventory);
          } else {
            console.error('Error: Prescription not found in response.data');
          }
        } else {
          console.error('Error: Prescription data not present in the response');
        }
      },
      error: (error) => {
        console.error('Error fetching prescription:', error);
      },
    });
  }

  deletePrescription(id: number) {
    alertify.confirm('Are you sure you want to permanently delete this prescription?', () => {
      this.prescriptionService.deletePrescriptionById(id).subscribe({
      next: (response) => {
        console.log('Response from server:', response);
        if (response.status === 'OK') {
          alertify.success('Prescription deleted successfully!');
          this.getAllPrescriptionData();
        } else {
          console.error('Error: ' + response.message);
        }
      },
      error: (error) => {
        console.error('Error deleting prescription:', error);
        alertify.error('An error occurred while deleting the prescription.');
      },
    });
  }, () => {
  });
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
