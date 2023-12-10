import { Component } from '@angular/core';
import {AuthenticationService} from "./_services";
import {Router} from "@angular/router";

@Component({ selector: 'app-root', templateUrl: 'app.component.html', styleUrls: ['./app.component.css'] })
export class AppComponent {
  title: string = 'cliente';

    userLoginOn:boolean=false;
    constructor(private loginService:AuthenticationService) { }

    ngOnInit(): void {
        this.loginService.isLoggedValue.subscribe(
            {
                next:(userLoginOn) => {
                    this.userLoginOn=userLoginOn;
                }
            }
        )
    }
}
