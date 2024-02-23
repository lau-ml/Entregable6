import {Component} from '@angular/core';
import {FormBuilder} from "@angular/forms";
import {GastoService} from "../../_services/gasto.service";
import {UsuarioService} from "../../_services/usuario.service";
import {GastoResponse} from "../../_responses/gastoResponse";
import {ActivatedRoute, Router} from "@angular/router";

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
  groupForm = this.formBuilder.group({
    nombreGrupo: [''],
    tipoGasto: [''],
    fechaDesde: [''],
    fechaHasta: ['']
  });
  username: any;
  grupoId: any;

  constructor(private formBuilder: FormBuilder, private gastoService: GastoService,
              private usuarioService: UsuarioService,
              private route: ActivatedRoute,
              private router: Router) {
  }

  ngOnInit() {

    this.usuarioService.getUsuario().subscribe({
      next: (data) => {
        this.id = data.id;
        this.username = data.usuario;

      }
    })
    this.route.queryParams.subscribe(params => {
      this.groupForm.patchValue({
        nombreGrupo: params['nombreGrupo'] || '',
        tipoGasto: params['tipoGasto'] || '',
        fechaDesde: params['fechaDesde'] || '',
        fechaHasta: params['fechaHasta'] || '',

      });
      this.grupoId = params['grupoId'] || '';
    });
    this.getPage(1);


  }

  getPage(page: number) {
    const tipoGasto = this.groupForm.get('tipoGasto')?.value ?? '';
    const fechaDesde = this.groupForm.get('fechaDesde')?.value ?? '';
    const fechaHasta = this.groupForm.get('fechaHasta')?.value ?? "";
    const nombreGrupo = this.groupForm.get('nombreGrupo')?.value ?? '';

    this.gastoService.getGastos(page, tipoGasto, fechaDesde, fechaHasta, nombreGrupo,this.grupoId).subscribe(
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
        },
        complete: () => {
          this.loading = false;
          this.updateQueryParams(page, nombreGrupo, tipoGasto, fechaDesde, fechaHasta, this.grupoId);
        }
      }
    )
  }

  updateQueryParams(page: number, nombreGrupo: string, tipoGasto: string, fechaDesde: string, fechaHasta: string, grupoId: number) {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {page, nombreGrupo, tipoGasto, fechaDesde, fechaHasta, grupoId},
      queryParamsHandling: 'merge',
    });
  }

  protected readonly Object = Object;
}
