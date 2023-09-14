import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MedicationComponent } from './medication.component';

describe('MedicationComponent', () => {
  let component: MedicationComponent;
  let fixture: ComponentFixture<MedicationComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MedicationComponent]
    });
    fixture = TestBed.createComponent(MedicationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
