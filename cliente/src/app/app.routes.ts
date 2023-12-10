import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./login";
import {HomeComponent} from "./home/home.component";
import {RegisterComponent} from "./register/register.component";

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
  }
];


export const routing = RouterModule.forRoot(routes, {});
