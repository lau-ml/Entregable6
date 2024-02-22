import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from "../_services";
import {Router} from "@angular/router";
import {UsuarioService} from "../_services/usuario.service";
import {UserResponse} from "../_responses/userResponse";

@Component({
  selector: 'app-topnav',
  templateUrl: './topnav.component.html',
  styleUrl: './topnav.component.css'
})
export class TopnavComponent implements OnInit {
  userLoginOn: boolean = false;
  user: UserResponse = {} as UserResponse;

  constructor(private loginService: AuthenticationService, private router: Router,
              private usuarioService: UsuarioService) {
  }

  ngOnInit(): void {
    this.loginService.isLoggedValue.subscribe(
      {
        next: (userLoginOn) => {
          this.userLoginOn = userLoginOn;
        }
      }
    )
    this.usuarioService.getUsuario().subscribe({
      next: (data) => {
        this.user = data as UserResponse;
      }
    })
  }


  logout() {
    this.loginService.logout();
    this.router.navigateByUrl("/").then(r => console.log(r));
  }
}
