import { Component } from '@angular/core';
import {NgOptimizedImage} from "@angular/common";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-navbar',
  imports: [
    NgOptimizedImage,
    RouterLink
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

  logout() {
    localStorage.removeItem('token');
    window.location.href = '/login';
  }

}
