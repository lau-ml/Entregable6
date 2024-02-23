import { Component } from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {GastoResponse} from "../_responses/gastoResponse";
import {GastoService} from "../_services/gasto.service";
import {UsuarioService} from "../_services/usuario.service";
import {AmigosService} from "../_services/amigos.service";
import {UserResponse} from "../_responses/userResponse";

@Component({
  selector: 'app-amigos',
  templateUrl: './amigos.component.html',
  styleUrl: './amigos.component.css'
})
export class AmigosComponent {
  formulario: FormGroup = new FormGroup({});
  imagen: File = new File([], "");
  private id: number = 0;
  gastos: GastoResponse[] = [];
  totalItems: number = 0;
  totalPages: number = 0;
  currentPage: number = 0;
  loading: boolean = true;
  perPage: number = 0;
  amigos: UserResponse[]= [];

  constructor(private formBuilder: FormBuilder, private gastoService: GastoService,
              private usuarioService: UsuarioService,
              private amigosService: AmigosService) {
  }

  ngOnInit() {


    this.getPage(1);
  }


  getPage(page: number, perPage?: number, usuario?: string) {

    this.amigosService.getAmigos(page, perPage, usuario).subscribe({
      next: (data) => {
        this.amigos=data.amigos;
        this.totalItems = data.totalItems;
        this.totalPages = data.totalPages;
        this.currentPage = data.currentPage;
        this.perPage = data.itemsPerPage;
      }
    })
    }



}
