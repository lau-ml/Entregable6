import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {GrupoResponse} from "../_responses/grupoResponse";
import {ActivatedRoute, Router} from "@angular/router";
import {GrupoService} from "../_services/grupo.service";
import {SweetalertService} from "../_services/sweetalert.service";
import {GrupoCreateRequest} from "../_requests/grupoCreateRequest";
import {UsuarioService} from "../_services/usuario.service";

@Component({
  selector: 'app-grupo',
  templateUrl: './grupo.component.html',
  styleUrl: './grupo.component.css'
})
export class GrupoComponent implements OnInit {
  groupForm = this.formBuilder.group({
    nombreGrupo: ['', Validators.required],
    categoria: ['', Validators.required],
    participantes: this.formBuilder.control([])
  })
  grupos: GrupoResponse[] = [];
  loading: boolean = false;
  groupId: number = 0;
  nombre: string = '';
  categoria: string = '';
  saldo: number = 0;
  id: number = 0;
  user: any;
  amigos: string[] = [];

  constructor(private formBuilder: FormBuilder, private router: Router, private grupoService: GrupoService, private sweetAlertService: SweetalertService,
              private route: ActivatedRoute, private usuarioService: UsuarioService) {

  }

  onSubmit() {
    if (this.groupForm.invalid) {
      this.groupForm.markAllAsTouched();
      return;
    }
    this.grupoService.updateGroup(this.id, this.groupForm.value as GrupoCreateRequest).subscribe(
      {
        next: (data) => {
          this.sweetAlertService.showAlert("success", "¡Éxito!", "Grupo actualizado");
          this.categoria = data.categoria;
          this.nombre = data.nombreGrupo;
          this.saldo = data.saldo;
          this.id = data.id;

        },
        error: (error) => {
          this.sweetAlertService.showAlert("error", "¡Error!", "No se pudo crear el grupo");
        }
      }
    )


  }

  get f() {
    return this.groupForm.controls;
  }

  ngOnInit(): void {
    this.groupId = +this.route.snapshot.paramMap.get('id')!;

    this.usuarioService.getUsuario().subscribe({
      next: (data) => {
        this.user = data;
        this.amigos = data.amigos.slice();
      }
    })
    this.grupoService.getGroup(this.groupId).subscribe(
      {
        next: (data) => {
          const participantes = data.participantes.filter((participante: any) => participante != this.user.usuario);
          this.groupForm.setValue({
            nombreGrupo: data.nombreGrupo,
            categoria: data.categoria,
            participantes: participantes
          });

          this.categoria = data.categoria;
          this.nombre = data.nombreGrupo;
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
