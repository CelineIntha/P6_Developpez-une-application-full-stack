import { Component } from '@angular/core';
import {NgOptimizedImage} from "@angular/common";

@Component({
  selector: 'app-navbar',
  imports: [
    NgOptimizedImage
  ],
  standalone: true,
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {

  isNavbarOpen: boolean = false;


  toggleNavbar() {
    this.isNavbarOpen = !this.isNavbarOpen;
  }

}
