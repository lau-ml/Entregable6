import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { AuthenticationService } from './_services';
import { User } from './_models';
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({ selector: 'app-root', templateUrl: 'app.component.html', styleUrls: ['./app.component.scss'], })
export class AppComponent {
  currentUser: User | null = null;
  title: string = 'cliente';
  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
    private modalService: NgbModal
  ) {
    this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
  }


  public open(modal: any): void {
    this.modalService.open(modal);
  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/login']).then(r => console.log(r));
  }
}
