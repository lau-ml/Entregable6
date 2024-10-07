import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, Validators} from "@angular/forms";
import {GrupoService} from "../_services/grupo.service";
import {GrupoCreateRequest} from "../_requests/grupoCreateRequest";
import {SweetalertService} from "../../../../../../dssd/cliente/src/app/services/sweetalert.service";
import {GrupoResponse} from "../_responses/grupoResponse";
import {UsuarioService} from "../_services/usuario.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

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
              private usuarioService: UsuarioService,
              private modalService: NgbModal) {
  }


  get f() {
    return this.groupForm.controls;
  }
  onSubmit() {
    if (this.groupForm.invalid) {
      this.groupForm.markAllAsTouched();
      return;
    }
    this.grupoService.createGroup(this.groupForm.value as GrupoCreateRequest).subscribe(
      {
        next: (data) => {
          this.sweetAlertService.showAlert("success", "¡Éxito!", "Grupo creado");
          this.getPage(1);

        },
        error: (error) => {
          this.sweetAlertService.showAlert("error", "¡Error!", "No se pudo crear el grupo");
          if (error.message.includes("nombre")){
            const nombreGrupoControl = this.groupForm.get('nombreGrupo');
            if (nombreGrupoControl) {
              nombreGrupoControl.setErrors({ unique: true });
              nombreGrupoControl.markAsDirty();
              nombreGrupoControl.markAsTouched();
            }
          }
        },
        complete: () => {

          this.groupForm.reset();

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
        if (error.message.includes("nombre")){
          this.groupForm.get('nombreGrupo')?.setErrors({error: true});
        }
        if (error.message.includes("categoria")) {
          this.groupForm.get('categoria')?.setErrors({error: true});
        }
        this.sweetAlertService.showAlert("error", "¡Error!", "No se pudo obtener los grupos");
      },
      complete: () => {
        this.loading = false;
        this.updateQueryParams(page, nombreGrupo, categoria);
      },
    });
  }

  updateQueryParams(page: number, nombreGrupo: string, categoria: string) {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {page, nombreGrupo, categoria},
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
    this.groupForm.valueChanges.subscribe(() => {
      this.eliminarErrores();
    });
    this.getPage(1);
  }
  private eliminarErrores() {
    this.groupForm.get('nombreGrupo')?.setErrors(null);
    this.groupForm.get('categoria')?.setErrors(null);
  }
}
