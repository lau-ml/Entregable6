import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {AuthenticationService} from "../_services";
import {Router} from "@angular/router";

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private authenticationService: AuthenticationService,
              private router: Router) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError(error => {
        if (error.status === 0) {
          console.error('Ocurrio un error:', error.status, error.message);
        }
        if (error.status === 401 && error.error.message=="El token ha expirado.") {
          this.authenticationService.logout();
          this.router.navigateByUrl("/").then(r => console.log(r));
        }
        if (error.status >= 300) {
          return throwError(() => new Error(error.error));
        }
        return new Observable<HttpEvent<any>>();
      })
    );
  }
}
