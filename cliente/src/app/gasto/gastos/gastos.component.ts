import {Component} from '@angular/core';
import {FormArray, FormBuilder, FormGroup} from "@angular/forms";
import {GastoService} from "../../_services/gasto.service";
import {UsuarioService} from "../../_services/usuario.service";
import {GastoResponse} from "../../_responses/gastoResponse";

@Component({
  selector: 'app-gastos',
  templateUrl: './gastos.component.html',
  styleUrl: './gastos.component.css'
})
export class GastosComponent {

  private id: number = 0;
  gastos: GastoResponse[] = [];
  totalItems: number = 0;
  totalPages: number = 0;
  currentPage: number = 0;
  loading: boolean = true;
  itemsPerPage: number = 0;

  constructor(private formBuilder: FormBuilder, private gastoService: GastoService,
              private usuarioService: UsuarioService) {
  }

  ngOnInit() {

    this.usuarioService.getUsuario().subscribe({
      next: (data) => {
        this.id = data.id;
      }
    })
    this.getPage(1);
  }

  getPage(page: number) {

    this.gastoService.getGastos(page).subscribe(
      {
        next: (data) => {
          this.loading = false;
          this.gastos = data.gastos;
          this.totalItems = data.totalItems;
          this.totalPages = data.totalPages;
          this.currentPage = data.currentPage;
          this.itemsPerPage = data.itemsPerPage;

        },
        error: (error) => {
          console.log(error)
        }
      }
    )
  }

  protected readonly Object = Object;
}
