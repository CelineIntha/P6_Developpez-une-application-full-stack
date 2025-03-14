import {Component, inject} from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import {Router, RouterLink} from '@angular/router';
import {MatButton, MatIconButton} from "@angular/material/button";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, MatButton, MatIconButton, NgOptimizedImage],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  private router = inject(Router);
  goBack() {
    this.router.navigate(['..']);
  }
}
