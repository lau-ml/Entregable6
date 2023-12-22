import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./login";
import {HomeComponent} from "./home/home.component";
import {RegisterComponent} from "./register/register.component";
import {VerificarComponent} from "./verificar/verificar.component";
import {RecuperarContraComponent} from "./recuperar-contra/recuperar-contra.component";
import {GruposComponent} from "./grupos/grupos/grupos.component";
import {AuthGuard} from "./_guards";
import {NoAuthGuard} from "./_guards/noAuth.guard";
import {BienvenidaComponent} from "./bienvenida/bienvenida/bienvenida.component";

export const routes: Routes = [

  {
    path: 'home',
    component: HomeComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [NoAuthGuard]
  }
  ,
  {
    path: 'register',
    component: RegisterComponent,
    canActivate: [NoAuthGuard]

  },
  {
    path: 'verify',
    component: VerificarComponent,
    canActivate: [NoAuthGuard]

  },
  {
    path: 'reset',
    component: RecuperarContraComponent,
    canActivate: [NoAuthGuard]
  },
  {
    path: 'grupos',
    component: GruposComponent,
    canActivate: [AuthGuard]
  },
  {
    path: '',
    component: BienvenidaComponent,
    canActivate: [NoAuthGuard]
  },
  {
    path: '**',
    redirectTo: ''
  }

];


export const routing = RouterModule.forRoot(routes, {});
