import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {AuthenticationService} from "../_services";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent implements OnInit {
  userLoginOn:boolean=false;
  constructor(private loginService:AuthenticationService, private router:Router) { }

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
