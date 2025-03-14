import { Component } from '@angular/core';

@Component({
  selector: 'app-dashboard',
  imports: [],
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
