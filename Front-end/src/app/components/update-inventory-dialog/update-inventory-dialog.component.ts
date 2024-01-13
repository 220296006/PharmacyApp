import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Inventory } from 'src/app/model/inventory';
import { InventoryService } from 'src/app/services/inventory-service/inventory.service';

@Component({
  selector: 'app-update-inventory-dialog',
  templateUrl: './update-inventory-dialog.component.html',
  styleUrls: ['./update-inventory-dialog.component.scss']
})
export class UpdateInventoryDialogComponent implements OnInit {
 
  inventory: Inventory = {
   id: null,
   name: '',
   description: '',
   quantity: null,
   price: null,
   created_at: null,
   updated_at: null,
   medication: {
     id: null
   }
 }
 
 constructor(
  public dialogRef: MatDialogRef<UpdateInventoryDialogComponent>,
  @Inject(MAT_DIALOG_DATA) public data: any,
  private inventoryService: InventoryService){}

 ngOnInit(): void {
  console.log('Data received in dialog:', this.data);
  if (this.data?.inventory) {
    console.log('Inventory data in dialog:', this.data.inventory);
    this.inventory = this.data.inventory;
  }
  }

  onCancelClick(): void {
    this.dialogRef.close();
  }

  
  onUpdateClick(): void {
    this.inventoryService.updateMedicationData(this.inventory.id, this.inventory).subscribe({
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
