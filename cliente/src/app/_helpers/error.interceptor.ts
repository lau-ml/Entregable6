import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError, tap} from 'rxjs/operators';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor() {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(

      catchError(error => {
        if (error.status === 0) {
          console.error('Ocurrio un error:', error.status, error.message);
        } else if (error.status >= 300) {
          console.error(
            `Backend returned code ${error.status}, ` +
            `body was: ${error.error}`);
          return throwError(() => new Error(error.error));
        }
        console.error(error); // Cambiado de alert(error) a console.error(error)
        return new Observable<HttpEvent<any>>();
      })
    );
  }
}
