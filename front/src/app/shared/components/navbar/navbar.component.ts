import {Component} from '@angular/core';
import {NgOptimizedImage} from "@angular/common";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {MatIcon} from "@angular/material/icon";

@Component({
  selector: 'app-navbar',
  imports: [
    NgOptimizedImage,
    RouterLink,
    RouterLinkActive,
    MatIcon
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
