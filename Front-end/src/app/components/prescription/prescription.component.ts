import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Prescription } from 'src/app/model/prescription';
import { PrescriptionService } from 'src/app/services/prescription-service/prescription-service.service';

@Component({
  selector: 'app-prescription',
  templateUrl: './prescription.component.html',
  styleUrls: ['./prescription.component.scss']
})
export class PrescriptionComponent implements OnInit {

  tableDataSource: MatTableDataSource<Prescription[]> = new MatTableDataSource<Prescription[]>(
    []
  );
  displayedColumns: string[] = [
    'id',
    'customerId',
    'doctorName',
    'doctorAddress',
    'issue_date',
    'options'
  ];

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private prescriptionService: PrescriptionService) {}

  ngOnInit() {
    this.getAllPrescritionData();
  }
  getAllPrescritionData() {
    this.prescriptionService.getAllPrescritionData().subscribe({
      next: (response) => {
        console.log('Response from server:', response);
        if (response.status === 'OK' && Array.isArray(response.data.page)) {
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
  editPrescription(_t74: any) {
    throw new Error('Method not implemented.');
    }
    deletePrescription(_t74: any) {
    throw new Error('Method not implemented.');
    }
}

