import { Component, HostListener, OnInit } from '@angular/core';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss']
})
export class NavigationComponent {
  isSticky: boolean = false;

    // Implement your scroll logic here
    @HostListener('window:scroll', ['$event'])
    checkScroll() {
      const scrollPosition = window.scrollY;
  
      if (scrollPosition >= 100) {
        this.isSticky = true;
      } else {
        this.isSticky = false;
      }
    }

  // Add other component logic here
  

}


