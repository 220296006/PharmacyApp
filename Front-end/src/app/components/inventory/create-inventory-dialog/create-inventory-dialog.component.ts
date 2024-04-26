// create-inventory-dialog.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { InventoryService } from '../../../services/inventory-service/inventory.service';
import * as alertify from 'alertifyjs';

@Component({
  selector: 'app-create-inventory-dialog',
  templateUrl: './create-inventory-dialog.component.html',
  styleUrls: ['./create-inventory-dialog.component.scss'],
})
export class CreateInventoryDialogComponent implements OnInit {
  inventoryForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private inventoryService: InventoryService,
    private dialogRef: MatDialogRef<CreateInventoryDialogComponent>
  ) {}

  ngOnInit(): void {
    this.initForm();
  }

  initForm(): void {
    this.inventoryForm = this.fb.group({
      medicationId: ['', [Validators.required]],
      name: ['', [Validators.required]],
      description: ['', [Validators.required]],
      quantity: ['', [Validators.required]],
      price: ['', [Validators.required]],
    });
  }

  onSubmit(inventory: any): void {
    if (Object.values(inventory).length > 0) {
      this.inventoryForm.patchValue(inventory);
      return;
    } else {
      const payload = {
        ...this.inventoryForm.getRawValue(),
        medication: {
          id: this.inventoryForm.get('medicationId').value,
        },
      };
      this.inventoryService.createInventory(payload).subscribe({
        next: (response) => {
          console.log(
            'Inventory item added successfully:',
            response.data.inventory
          );
          alertify.success('Inventory item added successfully');
          this.dialogRef.close(response.data.inventory);
        },
        error: (error) => {
          console.error('Error adding inventory item:', error);
          alertify.error('Error adding inventory item. Please try again.');
        },
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
