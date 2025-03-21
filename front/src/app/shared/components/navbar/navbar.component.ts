import {Component} from '@angular/core';
import {NgOptimizedImage} from "@angular/common";
import {RouterLink, RouterLinkActive} from "@angular/router";

@Component({
  selector: 'app-navbar',
  imports: [
    NgOptimizedImage,
    RouterLink,
    RouterLinkActive
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

  logout(): void {
    localStorage.removeItem('token');
    window.location.href = '/login';
  }

}
