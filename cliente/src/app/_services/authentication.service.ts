import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {environment as env} from '../../environments/environment';

import {User} from '../_models';

@Injectable({providedIn: 'root'})
export class AuthenticationService {
  private currentUserSubject: BehaviorSubject<User | null>;
  public currentUser: Observable<User | null>;

  constructor(private http: HttpClient) {
    const storedUser = localStorage.getItem('currentUser');
    const initialUser = storedUser ? JSON.parse(storedUser) : null;
    this.currentUserSubject = new BehaviorSubject<User | null>(initialUser);
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }

  login(username: string, password: string) {
    return this.http.post<any>(`${env.url}/auth`, {username, password})
      .pipe(map(credentials => {
        // login successful si hay un token en la respuesta
        if (credentials && credentials.token) {
          // store user details and jwt token in local storage to keep user logged in between page refreshes
          localStorage.setItem('currentUser', JSON.stringify(credentials));
          this.currentUserSubject.next(credentials);
        }

        return credentials;
      }));
  }

  logout() {
    // elimino las credenciales del localstorage al deslogearme
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
  }
}
