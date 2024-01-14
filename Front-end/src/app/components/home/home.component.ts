import { Component, OnInit } from '@angular/core';
import Typed from 'typed.js';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  customersCount = 7;
  invoicesCount = 12;
  totalBilledAmount = 3500;

  ngOnInit(): void {
    // Initialize Typed.js
    this.initTyped();
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
