import { Inventory } from '../../../model/inventory';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { InventoryService } from 'src/app/services/inventory-service/inventory.service';
import { MatDialog } from '@angular/material/dialog';
import { UpdateInventoryDialogComponent } from '../update-inventory-dialog/update-inventory-dialog.component';
import { CreateInventoryDialogComponent } from '../create-inventory-dialog/create-inventory-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from 'src/app/services/auth-service/auth-service.service';

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
    private createInventoryDialog: MatDialog,
    private snackBar: MatSnackBar,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.getAllInventoryData();
    const permissions = this.authService.getPermissionsFromToken();
    console.log('User permissions:', permissions);
  }

  onCreateInventoryDialog(id: any) {
    const permissions = this.authService.getPermissionsFromToken();
    if (permissions.includes('CREATE:INVENTORY')) {
      const dialogRef = this.createInventoryDialog.open(CreateInventoryDialogComponent, {
        width: '400px',
        exitAnimationDuration: '1000ms',
        enterAnimationDuration: '1000ms',
        data:
        {
          id: id
        }
      });
      dialogRef.afterClosed().subscribe(response => {
        response = this.getAllInventoryData();
        this.snackBar.open('Inventory created successfully!', 'Close', {
          duration: 3000,
        });
      });
    } else {
      this.snackBar.open('You do not have permission to create an Inventory.', 'Close', {
        duration: 3000,
      });
    }
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
        this.snackBar.open('An error occurred while fetching inventory data.', 'Close', {
          duration: 3000,
        });
      },
    });
  }

  onUpdateInventory(inventoryId: number) {
    const permissions = this.authService.getPermissionsFromToken();
    if (permissions.includes('UPDATE:INVENTORY')) {
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
          this.snackBar.open('An error occurred while fetching inventory data.', 'Close', {
            duration: 3000,
          });
        },
      });
    } else {
      this.snackBar.open('You do not have permission to update an inventory.', 'Close', {
        duration: 3000,
      });
    }
  }

  onDeleteInventory(id: number) {
    const permissions = this.authService.getPermissionsFromToken();
    if (permissions.includes('DELETE:INVENTORY')) {
      const dialogRef = this.snackBar.open('Are you sure you want to delete this Inventory?', 'Delete', {
        duration: 5000, // Adjust duration as needed
        verticalPosition: 'bottom',
        horizontalPosition: 'center',
        panelClass: ['snackbar-confirm'],
      });
      dialogRef.onAction().subscribe(() => {
        this.inventoryService.deleteInventoryById(id).subscribe({
          next: (response) => {
            console.log('Response from server:', response);
            if (response.status === 'OK') {
              this.snackBar.open('Inventory deleted successfully!', 'Close', {
                duration: 3000,
              });
              this.getAllInventoryData();
            } else {
              console.error('Error: ' + response.message);
            }
          },
          error: (error) => {
            console.error('Error deleting inventory:', error);
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
