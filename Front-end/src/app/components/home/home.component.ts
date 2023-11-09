import { Component, OnInit } from '@angular/core';
import Typed from 'typed.js';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  private currentSlideIndex = 0;
  private slideshowInterval: any;

  ngOnInit(): void {
    // Initialize Typed.js
    this.initTyped();

    // this.showNextSlide();

    // Start the slideshow
    this.startSlideshow();
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

  private startSlideshow(): void {
    // Set the interval to switch slides every 4 seconds
    this.slideshowInterval = setInterval(() => {
      this.showNextSlide();
    }, 4000);
  }
  private showNextSlide(): void {
    // Get all slides
    const slides = document.querySelectorAll('.slide');

    // Hide all slides
    slides.forEach((slide) => {
      slide.classList.remove('slide-in');
    });

    // Increment the index
    this.currentSlideIndex++;

    // If reached the end, go back to the first slide
    if (this.currentSlideIndex >= slides.length) {
      this.currentSlideIndex = 0;
    }

    // Show the next slide
    slides[this.currentSlideIndex].classList.add('slide-in');
  }
}
