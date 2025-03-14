import { Routes } from '@angular/router';
import { HomeComponent } from './features/home/home.component';
import {LoginComponent} from "./features/auth/login/login.component";
import {RegisterComponent} from "./features/auth/register/register.component";

// consider a guard combined with canLoad / canActivate route option
// to manage unauthenticated user to access private routes
export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent }
];
