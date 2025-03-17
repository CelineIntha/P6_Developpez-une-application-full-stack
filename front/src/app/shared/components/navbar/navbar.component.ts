import { Component } from '@angular/core';
import {NgClass, NgOptimizedImage} from "@angular/common";

@Component({
  selector: 'app-navbar',
  imports: [
    NgClass,
    NgOptimizedImage
  ],
  standalone: true,
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {

  isNavbarOpen: boolean = false;

  toggleNavbar(): void {
    this.isNavbarOpen = !this.isNavbarOpen;
  }
}
