import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreatePrescriptionDialogComponent } from './create-prescription-dialog.component';

describe('CreatePrescriptionDialogComponent', () => {
  let component: CreatePrescriptionDialogComponent;
  let fixture: ComponentFixture<CreatePrescriptionDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CreatePrescriptionDialogComponent]
    });
    fixture = TestBed.createComponent(CreatePrescriptionDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
