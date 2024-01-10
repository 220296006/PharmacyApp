import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateUserDialogComponent } from './update-user-dialog.component';

describe('UpdateUserDialogComponent', () => {
  let component: UpdateUserDialogComponent;
  let fixture: ComponentFixture<UpdateUserDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UpdateUserDialogComponent]
    });
    fixture = TestBed.createComponent(UpdateUserDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
