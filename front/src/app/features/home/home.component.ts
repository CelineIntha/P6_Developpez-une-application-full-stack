import { Component, OnInit } from '@angular/core';
import {CommonModule, NgOptimizedImage} from "@angular/common";
import {MatButtonModule} from "@angular/material/button";
import {RouterLink} from "@angular/router";

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss'],
    standalone: true,
  imports: [CommonModule, MatButtonModule, RouterLink, NgOptimizedImage],
})
export class HomeComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}

}
