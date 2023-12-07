import {Component, OnInit} from '@angular/core';
import {Router, ActivatedRoute} from '@angular/router';
import {UntypedFormBuilder, UntypedFormGroup, Validators} from '@angular/forms';
import {catchError, finalize, first, tap} from 'rxjs/operators';

import {AuthenticationService} from '../_services';
import {throwError} from "rxjs";

@Component({templateUrl: 'login.component.html'})
export class LoginComponent implements OnInit {
  loginForm: UntypedFormGroup = new UntypedFormGroup({})
  loading = false;
  submitted = false;
  returnUrl: string = '';
  error = '';

  constructor(
    private formBuilder: UntypedFormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService
  ) {
  }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });

    // elimino las credenciales del usuario, si es que existen
    this.authenticationService.logout();

    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  get f() {
    return this.loginForm.controls;
  }

  onSubmit() {
    this.submitted = true;

    // Valido que el formulario sea valido antes del submit
    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    this.authenticationService.login(this.f['username'].value, this.f['password'].value)
      .pipe(
        first(),
        tap((data) => {
          this.router.navigate([this.returnUrl]).then(r => console.log(r));
        }),
        catchError((error) => {
          this.error = 'Nombre de usuario o Contraseña incorrectas';
          this.loading = false;
          // You can handle the error further if needed
          return throwError(error); // Rethrow the error or return a default value
        }),
        finalize(() => {
          console.log('LoginComponent.onSubmit() completed');
          // Any cleanup code you want to execute, regardless of success or error
        })
      )
      .subscribe();
  }
}
