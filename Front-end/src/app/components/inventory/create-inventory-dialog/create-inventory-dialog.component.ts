// create-inventory-dialog.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { InventoryService } from '../../../services/inventory-service/inventory.service';
import { MatSnackBar } from '@angular/material/snack-bar';

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
    private dialogRef: MatDialogRef<CreateInventoryDialogComponent>,
    private snackBar: MatSnackBar
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
          this.dialogRef.close(response.data.inventory);
          this.snackBar.open('Inventory item added successfully!', 'Close', {
            duration: 3000,
            panelClass: ['success-snackbar'],
          })
        },
        error: (error) => {
          console.error('Error adding inventory item:', error);
          this.snackBar.open('An error occurred while adding the inventory item.', 'Close', {
            duration: 3000,
            panelClass: ['error-snackbar'],
          })},
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
