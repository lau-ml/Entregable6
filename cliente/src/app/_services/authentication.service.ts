import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {BehaviorSubject, Observable, throwError} from 'rxjs';
import {catchError, map, tap} from 'rxjs/operators';
import {environment, environment as env} from '../../environments/environment';

import {User} from '../_models';
import {LoginRequest} from "../_requests/loginRequest";

@Injectable({providedIn: 'root'})
export class AuthenticationService {

    public currentUser: BehaviorSubject<String> = new BehaviorSubject<String>("");
    private isLogged: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
    private isResendEmail: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
    private isRecoverPass: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
    constructor(private http: HttpClient) {
        this.isLogged = new BehaviorSubject<boolean>(sessionStorage.getItem("token") != null);
        this.currentUser = new BehaviorSubject<String>(sessionStorage.getItem("token") || "");
    }

    public get currentUserValue(): Observable<String> {
        return this.currentUser.asObservable();
    }

    public get isLoggedValue(): Observable<boolean> {
        return this.isLogged.asObservable();
    }

    public get isResendEmailValue(): Observable<boolean> {
        return this.isResendEmail.asObservable();
    }

    public get isRecoverPassValue(): Observable<boolean> {
        return this.isRecoverPass.asObservable();
    }

    login(credenciales: LoginRequest) {
        return this.http.post<any>(environment.urlHost + "auth/login", credenciales).pipe(
            tap((userData) => {
                sessionStorage.setItem("token", userData.token);
                this.currentUser.next(userData.token);
                this.isLogged.next(true);
            }),
            map((userData) => userData.token),
            catchError(this.handleError)
        );
    }

    resendEmail(email: String) {
        return this.http.post<any>(environment.urlHost + "auth/resend", {email: email}).pipe(
          tap((userData) => {
            this.isResendEmail.next(true);
          }),
          map((userData) => userData.message),
          catchError(this.handleError)
        );
    }

    recoverPassword(email: String) {
        return this.http.post<any>(environment.urlHost + "auth/recover", {email: email}).pipe(
            tap((userData) => {
                this.isRecoverPass.next(true);
            }),
            map((userData) => userData.message),
            catchError(this.handleError)
        );
    }
    private handleError(error: HttpErrorResponse) {
        if (error.status === 0) {
            console.error('Ocurrio un error:', error.status, error.message);
        } else {
            console.error(
                `Backend returned code ${error.status}, ` +
                `body was: ${error.error}`);
            return throwError(() => new Error(error.error));
        }
        return throwError(() => new Error('Algo malo sucedio; por favor intente mas tarde.'));
    }

    logout() {
        sessionStorage.removeItem('token');
        this.isLogged.next(false);
    }

    get userToken(): String {
        return this.currentUser.value;
    }

}
