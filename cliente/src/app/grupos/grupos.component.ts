import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, Validators} from "@angular/forms";
import {GrupoService} from "../_services/grupo.service";
import {GrupoCreateRequest} from "../_requests/grupoCreateRequest";
import {SweetalertService} from "../_services/sweetalert.service";
import {GrupoResponse} from "../_responses/grupoResponse";

@Component({
  selector: 'app-grupos',
  templateUrl: './grupos.component.html',
  styleUrl: './grupos.component.css'
})
export class GruposComponent implements OnInit {

  groupForm = this.formBuilder.group({
    usuario: ['', Validators.required],
    categoria: ['', Validators.required],
  });

  grupos: GrupoResponse[] = [];
  loading: boolean = false;
  perPage: number= 6;
  totalPages: number = 0;
  currentPage: number = 0;
  totalItems: number = 0;
  itemsPerPage: number = 0;
  nombre: string = "";
  categoria: string = "";

  constructor(private formBuilder: FormBuilder, private router: Router, private grupoService: GrupoService, private sweetAlertService: SweetalertService,
              private route: ActivatedRoute) {
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
    const nombre = this.groupForm.get('usuario')?.value ?? '';

    this.grupoService.getGroupsPaginated(page, categoria, nombre, this.perPage).subscribe({
      next: ({ grupos, totalItems, totalPages, currentPage, itemsPerPage }) => {
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
        this.updateQueryParams(page, nombre, categoria);
      },
    });
  }
  updateQueryParams(page: number, nombre: string, categoria: string) {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { page, nombre, categoria },
      queryParamsHandling: 'merge',
    });
  }


  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.groupForm.patchValue({
        categoria: params['categoria'] || '',
        usuario: params['nombre']|| ''
      });
    });
    this.getPage(1);
  }
}
