import { Routes } from '@angular/router';
import { HomeComponent } from './features/home/home.component';

// consider a guard combined with canLoad / canActivate route option
// to manage unauthenticated user to access private routes
export const routes: Routes = [
  { path: '', component: HomeComponent }
];
