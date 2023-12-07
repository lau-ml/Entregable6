import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./login";

export const routes: Routes = [


  {
    path: 'login',
    component: LoginComponent
  },

  // otherwise redirect to home
  { path: '**', redirectTo: '' }
];



export const routing = RouterModule.forRoot(routes, {});
