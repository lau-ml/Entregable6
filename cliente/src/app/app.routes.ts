import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./login";
import {HomeComponent} from "./home/home.component";
import {RegisterComponent} from "./register/register.component";
import {VerificarComponent} from "./verificar/verificar.component";

export const routes: Routes = [


  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'home',
    component: HomeComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path:'verificar',
    component: VerificarComponent
  }
];


export const routing = RouterModule.forRoot(routes, {});
