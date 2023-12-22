import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from "../_services";
import {Router} from "@angular/router";

@Component({
    selector: 'app-topnav',
    templateUrl: './topnav.component.html',
    styleUrl: './topnav.component.css'
})
export class TopnavComponent implements OnInit {
    userLoginOn: boolean = false;

    constructor(private loginService: AuthenticationService, private router: Router) {
    }

    ngOnInit(): void {
        this.loginService.isLoggedValue.subscribe(
            {
                next: (userLoginOn) => {
                    this.userLoginOn = userLoginOn;
                }
            }
        )
    }

    logout() {
        this.loginService.logout();
        this.router.navigateByUrl("/").then(r => console.log(r));
    }
}
