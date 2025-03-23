import {inject} from '@angular/core';
import {CanActivateFn, Router} from '@angular/router';
import {AuthService} from '../services/auth.service';

export const AuthGuard: CanActivateFn = (): boolean => {
  const authService: AuthService = inject(AuthService);
  const router: Router = inject(Router);

  if (authService.getToken()) {
    return true;
  } else {
    router.navigate(['/login']);
    return false;
  }
};
