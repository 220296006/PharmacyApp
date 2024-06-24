import { Medication } from 'src/app/interface/medication';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-customer-medications',
  templateUrl: './customer-medications.component.html',
  styleUrls: ['./customer-medications.component.scss']
})
export class CustomerMedicationsComponent {
  @Input() medications: Medication [] | undefined;

}
