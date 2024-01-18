import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
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
    nombre: ['', Validators.required],
    categoria: ['', Validators.required],
  })
  grupos: GrupoResponse[] = [];
  loading: boolean = false;
  totalPages: number = 0;
  currentPage: number = 0;
  totalItems: number = 0;
  perPage: number = this.grupoService.getPerPages();
  constructor(private formBuilder: FormBuilder, private router: Router, private grupoService: GrupoService, private sweetAlertService: SweetalertService
  ) {
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
          if (this.grupoService.getPerPages() % this.totalItems == 0) {
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
    this.grupoService.getGroups(page).subscribe(
      {
        next: (data) => {
          this.grupos = data.grupos;
          this.totalItems = data.totalItems;
          this.totalPages = data.totalPages;
          this.currentPage = data.currentPage;
          console.log(data);

        },
        error: (error) => {
          console.log(error)
        }
      }
    )
  }

  ngOnInit(): void {
    this.getPage(1);
  }
}
