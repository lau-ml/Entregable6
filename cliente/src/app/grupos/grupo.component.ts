import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {GrupoResponse} from "../_responses/grupoResponse";
import {ActivatedRoute, Router} from "@angular/router";
import {GrupoService} from "../_services/grupo.service";
import {SweetalertService} from "../_services/sweetalert.service";
import {GrupoCreateRequest} from "../_requests/grupoCreateRequest";

@Component({
  selector: 'app-grupo',
  templateUrl: './grupo.component.html',
  styleUrl: './grupo.component.css'
})
export class GrupoComponent implements OnInit {
  groupForm = this.formBuilder.group({
    nombre: ['', Validators.required],
    categoria: ['', Validators.required],
  })
  grupos: GrupoResponse[] = [];
  loading: boolean = false;
  groupId: number = 0;
  nombre: string='';
  categoria: string='';
  saldo: number=0;
  id: number=0;


  constructor(private formBuilder: FormBuilder, private router: Router, private grupoService: GrupoService, private sweetAlertService: SweetalertService,
              private route: ActivatedRoute
  ) {
  }

  onSubmit() {
    if (this.groupForm.invalid) {
      this.groupForm.markAllAsTouched();
      return;
    }
    this.grupoService.updateGroup(this.id,this.groupForm.value as GrupoCreateRequest).subscribe(
      {
        next: (data) => {
          this.sweetAlertService.showAlert("success", "¡Éxito!", "Grupo actualizado");
          this.categoria = data.categoria;
          this.nombre = data.nombre;
        },
        error: (error) => {
          this.sweetAlertService.showAlert("error", "¡Error!", "No se pudo crear el grupo");
        }
      }
    )


  }

  ngOnInit(): void {
    this.groupId = +this.route.snapshot.paramMap.get('id')!;
    this.grupoService.getGroup(this.groupId).subscribe(
      {
        next: (data) => {
          this.groupForm.patchValue(data);
          this.categoria = data.categoria;
          this.nombre = data.nombre;
          this.saldo = data.saldo;
          this.id = data.id;
        },
        error: (error) => {
          this.sweetAlertService.showAlert("error", "¡Error!", "No se pudo cargar el grupo");
        }
      }
    )
  }
}
