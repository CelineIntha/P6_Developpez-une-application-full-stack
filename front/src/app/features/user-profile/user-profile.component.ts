import {Component, inject, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {UserService} from '../../core/services/user.service';
import {AuthService} from '../../core/services/auth.service';
import {User} from '../../core/models/user';

import {NavbarComponent} from '../../shared/components/navbar/navbar.component';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {UpdateUser} from "../../core/models/update-user";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-user-profile',
  standalone: true,
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.scss',
  imports: [
    ReactiveFormsModule,
    NavbarComponent,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
  ]
})
export class UserProfileComponent implements OnInit {
  private fb: FormBuilder = inject(FormBuilder);
  private userService: UserService = inject(UserService);
  private authService: AuthService = inject(AuthService);
  private router: Router = inject(Router);

  profileForm!: FormGroup<{
    username: FormControl<string>;
    email: FormControl<string>;
    password: FormControl<string>;
  }>;

  errorMessage: string | null = null;

  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe({
      next: (user: User) => {
        this.profileForm = this.fb.group({
          username: this.fb.control(user.username, {
            nonNullable: true,
            validators: [Validators.minLength(3)]
          }),
          email: this.fb.control(user.email, {
            nonNullable: true,
            validators: [Validators.email]
          }),
          password: this.fb.control('', {
            nonNullable: true,
            validators: [
              Validators.minLength(8),
              Validators.pattern(
                '^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$'
              )
            ]
          })
        });
      },
      error: () => {
        this.errorMessage = 'Impossible de charger le profil utilisateur.';
      }
    });
  }

  onSubmit(): void {
    if (this.profileForm.invalid) return;
    if (this.profileForm.pristine) {
      this.errorMessage = 'Aucune modification détectée.';
      return;
    }

    const payload: UpdateUser = this.profileForm.value;

    this.userService.updateUser(payload).subscribe({
      next: (): void => {
        this.authService.logout();
        this.router.navigate(['/']);
      },
      error: (err: HttpErrorResponse): void => {
        this.errorMessage =
          err.error?.message || 'Erreur lors de la mise à jour.';
      }
    });
  }

}
