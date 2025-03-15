import { Component } from '@angular/core';
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";

@Component({
  selector: 'app-dashboard',
  imports: [
    NavbarComponent
  ],
  standalone: true,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent {
  logout() {
    localStorage.removeItem('token');
    window.location.href = '/login';
  }
}
