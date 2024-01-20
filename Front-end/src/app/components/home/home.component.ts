import { Component, OnInit } from '@angular/core';
import { HomeService } from 'src/app/services/home-service/home.service';
import Typed from 'typed.js';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  customersCount = 0;
  invoicesCount = 0;
  totalBilledAmount = 0;

  constructor(private homeService: HomeService) {}

  ngOnInit(): void {
    console.log(this.customersCount)
    // Fetch counts and total billed amount
    this.fetchCounts();
    // Initialize Typed.js
    this.initTyped();
  }

  private fetchCounts(): void {
    this.homeService.getCustomersCount().subscribe((count) => {
      this.customersCount = count;
    });

    this.homeService.getInvoicesCount().subscribe((count) => {
      this.invoicesCount = count;
    });

    this.homeService.getTotalBilledAmount().subscribe((amount) => {
      this.totalBilledAmount = amount;
    });
  }

  private initTyped(): void {
    const typedElement = document.querySelector('.typed') as HTMLElement | null;

    if (typedElement) {
      const typedStringsAttribute =
        typedElement.getAttribute('data-typed-items') || '';
      const typedStrings = typedStringsAttribute.split(',');

      new Typed('.typed', {
        strings: typedStrings, // Pass the array of strings
        typeSpeed: 100,
        backSpeed: 50,
        backDelay: 2000,
        loop: true,
      });
    }
  }
}
