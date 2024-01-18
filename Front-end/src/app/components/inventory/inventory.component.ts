import { Inventory } from './../../model/inventory';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { InventoryService } from 'src/app/services/inventory-service/inventory.service';
import { MatDialog } from '@angular/material/dialog';
import { UpdateInventoryDialogComponent } from '../update-inventory-dialog/update-inventory-dialog.component';
import * as alertify from 'alertifyjs';
import { CreateInventoryDialogComponent } from '../create-inventory-dialog/create-inventory-dialog.component';

@Component({
  selector: 'app-inventory',
  templateUrl: './inventory.component.html',
  styleUrls: ['./inventory.component.scss'],
})
export class InventoryComponent implements OnInit {

  tableDataSource: MatTableDataSource<Inventory> =
    new MatTableDataSource<Inventory>([]);
  displayedColumns: string[] = [
    'id',
    'medicationId',
    'name',
    'description',
    'quantity',
    'price',
    'created_at',
    'updated_at',
    'options',
  ];

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private inventoryService: InventoryService,
    private updateInventoryDialog: MatDialog,
    private createInventoryDialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.getAllInventoryData();
  }

  onCreateInventoryDialog(id: any) {
    const dialogRef = this.createInventoryDialog.open(CreateInventoryDialogComponent,{
      width: '400px',
      exitAnimationDuration: '1000ms',
      enterAnimationDuration: '1000ms',
      data:
      {
        id: id
      }
    })
    dialogRef.afterClosed().subscribe(response=> {
      response = this.getAllInventoryData();
    });
    }

  getAllInventoryData() {
    this.inventoryService.getAllInventoryData().subscribe({
      next: (response) => {
        console.log('Response from server:', response);
        if (
          response.status === 'OK' &&
          response.data &&
          Array.isArray(response.data.inventory)
        ) {
          console.log('Data received:', response.data.inventory);
          this.tableDataSource.data = response.data.inventory;
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

  onUpdateInventory(inventoryId: number) {
    this.inventoryService.getInventoryById(inventoryId).subscribe({
      next: (response) => {
        console.log('Response from server:', response);
        if (
          response.status === 'OK' &&
          response.data &&
          response.data.inventory
        ) {
          console.log('Data passed to dialog:', response.data);
          const inventory = response.data.inventory;
          if (inventory) {
            this.openUpdateDialog(inventory);
          } else {
            console.error('Error: Inventory not found in response.data');
          }
        } else {
          console.error('Error: Inventory data not present in the response');
        }
      },
      error: (error) => {
        console.error('Error fetching user:', error);
      },
    });
  }

  onDeleteInventory(id: number) {
    alertify.confirm('Are you sure you want to permanently delete this inventory?', () => {

    this.inventoryService.deleteInventoryById(id).subscribe({
      next: (response) => {
        console.log('Response from server:', response);
        if (response.status === 'OK') {
          alertify.success('inventory deleted successfully!');
          this.getAllInventoryData();
        } else {
          console.error('Error: ' + response.message);
        }
      },
      error: (error) => {
        console.error('Error deleting inventory:', error);
        alertify.error('An error occurred while deleting the inventory.');
      },
    });
  }, () => {
  });
}
  openUpdateDialog(inventory: Inventory): void {
    console.log('Inventory data passed to dialog:', inventory);
    const dialogRef = this.updateInventoryDialog.open(UpdateInventoryDialogComponent, {
      width: '400px',
      exitAnimationDuration: '1000ms',
      enterAnimationDuration: '1000ms',
      data: { inventory: inventory },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log('The update dialog was closed');
      if (result) {
        this.getAllInventoryData();
      }
    });
  }
}
