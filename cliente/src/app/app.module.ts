import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule, HTTP_INTERCEPTORS} from '@angular/common/http';
import {MatSelectModule} from '@angular/material/select';

import {HttpParams} from "@angular/common/http";

import {MiPerfilVisualizarComponent} from "./mi-perfil/mi-perfil-visualizar/mi-perfil-visualizar.component";
import {AppComponent} from './app.component';
import {routing} from './app.routes';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import {AmigosComponent} from "./amigos/amigos.component";
import {JwtInterceptor, ErrorInterceptor} from './_helpers';
import {LoginComponent} from './login';
import {NavComponent} from "./nav/nav.component";
import {FooterComponent} from "./footer/footer.component";
import {TopnavComponent} from "./topnav/topnav.component";
import {RegisterComponent} from "./register/register.component";
import { SweetAlert2Module } from '@sweetalert2/ngx-sweetalert2';
import {VerificarComponent} from "./verificar/verificar.component";
import {RecuperarContraComponent} from "./recuperar-contra/recuperar-contra.component";
import {GruposComponent} from "./grupos/grupos.component";
import {AuthGuard} from "./_guards";
import {NoAuthGuard} from "./_guards/noAuth.guard";
import {BienvenidaComponent} from "./bienvenida/bienvenida.component";
import {NgxPaginationModule} from "ngx-pagination";
import {GrupoComponent} from "./grupos/grupo.component";
import {SaldoCardComponent} from "./saldo/saldo-card.component";
import {HomeComponent} from "./home/home.component";
import {GastosComponent} from "./gasto/gastos/gastos.component";
import {GastoCreacionComponent} from "./gasto/gasto-creacion/gasto-creacion.component";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatSelectModule,
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
    SaldoCardComponent,
    HomeComponent,
    GastosComponent,
    FooterComponent,
    VerificarComponent,
    RecuperarContraComponent,
    GruposComponent,
    BienvenidaComponent,
    GrupoComponent,
    AmigosComponent,
    MiPerfilVisualizarComponent,
    GastoCreacionComponent
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true},
    AuthGuard,
    NoAuthGuard
  ],
  exports: [
    SaldoCardComponent,
    SaldoCardComponent
  ],
  bootstrap: [AppComponent]
})

export class AppModule {
}
