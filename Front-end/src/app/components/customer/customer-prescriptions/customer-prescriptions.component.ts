import { Component, Input } from '@angular/core';
import { Prescription } from 'src/app/interface/prescription';

@Component({
  selector: 'app-customer-prescriptions',
  templateUrl: './customer-prescriptions.component.html',
  styleUrls: ['./customer-prescriptions.component.scss']
})
export class CustomerPrescriptionsComponent {
  @Input() prescriptions: Prescription[] | undefined;

}
