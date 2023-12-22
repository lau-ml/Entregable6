import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule, HTTP_INTERCEPTORS} from '@angular/common/http';

import {AppComponent} from './app.component';
import {routing} from './app.routes';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import {JwtInterceptor, ErrorInterceptor} from './_helpers';
import {LoginComponent} from './login';
import {NavComponent} from "./nav/nav.component";
import {FooterComponent} from "./footer/footer.component";
import {TopnavComponent} from "./topnav/topnav.component";
import {RegisterComponent} from "./register/register.component";
import { SweetAlert2Module } from '@sweetalert2/ngx-sweetalert2';
import {VerificarComponent} from "./verificar/verificar.component";
import {RecuperarContraComponent} from "./recuperar-contra/recuperar-contra.component";
import {GruposComponent} from "./grupos/grupos/grupos.component";
import {AuthGuard} from "./_guards";
import {NoAuthGuard} from "./_guards/noAuth.guard";
import {BienvenidaComponent} from "./bienvenida/bienvenida/bienvenida.component";
import {NgxPaginationModule} from "ngx-pagination";
@NgModule({
    imports: [
        BrowserModule,
        ReactiveFormsModule,
        HttpClientModule,
        NgbModule,
        routing,
        SweetAlert2Module.forRoot(),
        NgxPaginationModule
    ],
  declarations: [
    AppComponent,
    LoginComponent,
    NavComponent,
    TopnavComponent,
    RegisterComponent,
    FooterComponent,
    VerificarComponent,
    RecuperarContraComponent,
    GruposComponent,
    BienvenidaComponent
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true},
    AuthGuard,
    NoAuthGuard
  ],
  bootstrap: [AppComponent]
})

export class AppModule {
}
