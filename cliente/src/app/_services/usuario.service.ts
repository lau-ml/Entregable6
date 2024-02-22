import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject, map} from "rxjs";
import {tap} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private saldo: BehaviorSubject<number> = new BehaviorSubject<number>(0);
  private nombre: BehaviorSubject<string> = new BehaviorSubject<string>("");
  private apellido: BehaviorSubject<string> = new BehaviorSubject<string>("");
  private email: BehaviorSubject<string> = new BehaviorSubject<string>("");
  private id: BehaviorSubject<number> = new BehaviorSubject<number>(0);
  private amigos: BehaviorSubject<any> = new BehaviorSubject<any>("")
  private usuario:BehaviorSubject<any> = new BehaviorSubject<any>("")
  constructor(private http: HttpClient) {
  }

  getUsuario() {
    return this.http.get<any>("http://localhost:8080/user/").pipe(
      tap((usuarioData) => {
        this.saldo.next(usuarioData.saldo);
        this.nombre.next(usuarioData.nombre);
        this.apellido.next(usuarioData.apellido);
        this.amigos.next(usuarioData.amigos)
        this.email.next(usuarioData.email);
        this.usuario.next(usuarioData.usuario);
        this.id.next(usuarioData.id);
      }),
      map((usuarioData) => usuarioData)
    );
  }

  getSaldo() {
    return this.saldo.asObservable();
  }

  getAmigos() {
    return this.amigos.asObservable();
  }

  getNombre() {
    return this.nombre.asObservable();
  }

  getApellido() {
    return this.apellido.asObservable();
  }

  getEmail() {
    return this.email.asObservable();
  }

  getId() {
    return this.id.asObservable();
  }

  updateSaldo(saldo: number) {
    this.saldo.next(saldo);
  }

  updateNombre(nombre: string) {
    this.nombre.next(nombre);
  }

}
