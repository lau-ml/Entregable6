import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {GrupoResponse} from "../_responses/grupoResponse";
import {ActivatedRoute, Router} from "@angular/router";
import {GrupoService} from "../_services/grupo.service";
import {SweetalertService} from "../_services/sweetalert.service";
import {GrupoCreateRequest} from "../_requests/grupoCreateRequest";
import {UsuarioService} from "../_services/usuario.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

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
  usuario: string = '';
  amigos: string[] = [];
  solicitudesEnviadas: string[] = [];
  responsable: string = '';

  constructor(private formBuilder: FormBuilder, private router: Router, private grupoService: GrupoService, private sweetAlertService: SweetalertService,
              private route: ActivatedRoute, private usuarioService: UsuarioService,
              private modalService: NgbModal) {

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
          if (error.message.includes("nombre")){
          this.groupForm.get('nombreGrupo')?.setErrors({error: true});
          }
          if (error.message.includes("categoria")) {
            this.groupForm.get('categoria')?.setErrors({error: true});
          }
          this.sweetAlertService.showAlert("error", "¡Error!", "No se pudo actualizar el grupo");
        },
        complete: () => {

          this.groupForm.reset()
          this.obtenerGrupo();
          this.solicitudesEnviadasGrupo();
        }


      }
    )


  }

  solicitudesEnviadasGrupo(){
    this.grupoService.solicitudesEnviadasGrupoUsuarios(this.groupId).subscribe(
      {
        next: (data) => {
          this.solicitudesEnviadas = data;
        }
      })
  }

  obtenerGrupo(){
    this.grupoService.getGroup(this.groupId).subscribe(
      {
        next: (data) => {
          const participantes = data.participantes.filter((participante: any) => participante != this.user.usuario);
          this.groupForm.setValue({
            nombreGrupo: data.nombreGrupo,
            categoria: data.categoria,
            participantes: participantes
          });
          this.responsable = data.responsable;
          this.categoria = data.categoria;
          this.nombre = data.nombreGrupo;
          this.saldo = data.saldo;
          this.id = data.id;
        },
        error: (error) => {
          this.sweetAlertService.showAlert("error", "¡Error!", "No se pudo actualizar el grupo");
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
        this.usuario = data.usuario;
        this.amigos = data.amigos.slice();
      }
    })
    this.obtenerGrupo();
    this.solicitudesEnviadasGrupo();
    this.groupForm.valueChanges.subscribe(() => {
      this.eliminarErrores();
    });
  }

  private eliminarErrores() {
    this.groupForm.get('nombreGrupo')?.setErrors(null);
    this.groupForm.get('categoria')?.setErrors(null);
  }
}
