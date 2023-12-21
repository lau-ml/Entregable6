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
@NgModule({
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModule,
    routing,
    SweetAlert2Module.forRoot()
  ],
  declarations: [
    AppComponent,
    LoginComponent,
    NavComponent,
    TopnavComponent,
    RegisterComponent,
    FooterComponent,
    VerificarComponent,
    RecuperarContraComponent

  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true}
  ],
  bootstrap: [AppComponent]
})

export class AppModule {
}
