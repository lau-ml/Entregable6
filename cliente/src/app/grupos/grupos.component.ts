import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, Validators} from "@angular/forms";
import {GrupoService} from "../_services/grupo.service";
import {GrupoCreateRequest} from "../_requests/grupoCreateRequest";
import {SweetalertService} from "../_services/sweetalert.service";
import {GrupoResponse} from "../_responses/grupoResponse";
import {UsuarioService} from "../_services/usuario.service";

@Component({
  selector: 'app-grupos',
  templateUrl: './grupos.component.html',
  styleUrl: './grupos.component.css'
})
export class GruposComponent implements OnInit {

  groupForm = this.formBuilder.group({
    nombreGrupo: ['', Validators.required],
    categoria: ['--Seleccione una opción--', Validators.required],
    participantes: this.formBuilder.control([])
  });

  grupos: GrupoResponse[] = [];
  loading: boolean = false;
  perPage: number = 6;
  totalPages: number = 0;
  currentPage: number = 0;
  totalItems: number = 0;
  itemsPerPage: number = 0;
  nombre: string = "";
  categoria: string = "";
  amigos: string[] = [];

  constructor(private formBuilder: FormBuilder, private router: Router, private grupoService: GrupoService, private sweetAlertService: SweetalertService,
              private route: ActivatedRoute,
              private usuarioService: UsuarioService) {
  }

  onSubmit() {
    if (this.groupForm.invalid) {
      this.groupForm.markAllAsTouched();
      return;
    }
    alert(this.groupForm.value.participantes)
    this.grupoService.createGroup(this.groupForm.value as GrupoCreateRequest).subscribe(
      {
        next: (data) => {
          this.sweetAlertService.showAlert("success", "¡Éxito!", "Grupo creado");
        },
        error: (error) => {
          this.sweetAlertService.showAlert("error", "¡Error!", "No se pudo crear el grupo");
        },
        complete: () => {
          this.groupForm.reset();
          if (this.itemsPerPage % this.totalItems == 0) {
            this.getPage(this.currentPage + 1);
          } else {
            this.getPage(this.currentPage);
          }
        }
      }
    )


  }

  getPage(page: number) {
    this.loading = true;

    const categoria = this.groupForm.get('categoria')?.value ?? '';
    const nombreGrupo = this.groupForm.get('nombreGrupo')?.value ?? '';
    this.grupoService.getGroupsPaginated(page, this.perPage, categoria, nombreGrupo).subscribe({
      next: ({grupos, totalItems, totalPages, currentPage, itemsPerPage}) => {
        this.grupos = grupos;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.itemsPerPage = itemsPerPage;
      },
      error: (error) => {
        console.error(error);
      },
      complete: () => {
        this.loading = false;
        this.updateQueryParams(page, nombreGrupo, categoria);
      },
    });
  }

  updateQueryParams(page: number, usuario: string, categoria: string) {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {page, usuario, categoria},
      queryParamsHandling: 'merge',
    });
  }


  ngOnInit(): void {
    this.usuarioService.getAmigos().subscribe({
      next: (data) => {
        this.amigos = data;
      }
    });
    this.route.queryParams.subscribe(params => {
      this.groupForm.patchValue({
        categoria: params['categoria'] || '',
        nombreGrupo: params['nombreGrupo'] || ''
      });
    });
    this.getPage(1);
  }
}
