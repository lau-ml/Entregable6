import {Component, OnInit} from '@angular/core';
import {Router, ActivatedRoute} from '@angular/router';
import {FormBuilder, Validators} from '@angular/forms';


import {AuthenticationService} from '../_services';
import {throwError} from "rxjs";
import {LoginRequest} from "../_requests/loginRequest";

@Component({templateUrl: 'login.component.html', styleUrl: "./login.css"})
export class LoginComponent implements OnInit {
    loading = false;
    submitted = false;
    error = '';
    loginForm = this.formBuilder.group({
        username: ['', Validators.required],
        password: ['', Validators.required]
    })

    constructor(
        private formBuilder: FormBuilder,
        private router: Router,
        private authenticationService: AuthenticationService
    ) {
    }

    ngOnInit() {

    }


    get f() {
        return this.loginForm.controls;
    }

    onSubmit() {
        this.submitted = true;
        this.loading = true;
        if (this.loginForm.invalid) {
            this.loginForm.markAllAsTouched();

        } else {

            this.loading = true;
            this.authenticationService.login(this.loginForm.value as LoginRequest).subscribe({
                next: (userData) => {
                    console.log(userData);
                },
                error: (errorData) => {
                    this.error = errorData;
                    console.error(errorData);
                },
                complete: () => {
                    console.info("Login completo");
                    this.router.navigateByUrl('/').then(r => console.log(r));
                    this.loginForm.reset();
                }
            })
        }
        this.loading = false;
    }
}
