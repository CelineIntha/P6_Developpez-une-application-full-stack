import {Component} from '@angular/core';
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";

@Component({
  selector: 'app-user-profile',
  imports: [
    NavbarComponent
  ],
  standalone: true,
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.scss'
})
export class UserProfileComponent {

}
