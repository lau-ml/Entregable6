import {Component} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {GastoResponse} from "../../_responses/gastoResponse";
import {GastoService} from "../../_services/gasto.service";
import {UsuarioService} from "../../_services/usuario.service";
import {GrupoResponse} from "../../_responses/grupoResponse";
import {GrupoService} from "../../_services/grupo.service";

@Component({
  selector: 'app-gasto-creacion',

  templateUrl: './gasto-creacion.component.html',
  styleUrl: './gasto-creacion.component.css'
})
export class GastoCreacionComponent {
  formulario: FormGroup = new FormGroup({});
  imagen: File = new File([], "");
  private usuario: any;
  integrantes: string[] = [];
  gastos: GastoResponse[] = [];
  grupos: GrupoResponse[] = [];

  totalItems: number = 0;
  totalPages: number = 0;
  currentPage: number = 0;
  loading: boolean = true;
  itemsPerPage: number = 0;
  previousValue: string = "";
  amigosConUsuario: string[] = [];
  seleccionCargado: Map<string, number> = new Map<string, number>();

  constructor(private formBuilder: FormBuilder, private gastoService: GastoService,
              private usuarioService: UsuarioService, private grupoService: GrupoService) {
  }

  ngOnInit() {
    this.formulario = this.formBuilder.group({
      monto: ['', Validators.required],
      fecha: ['', Validators.required],
      imagen: ['', Validators.required],
      grupoBool: [false],
      id_grupo: [0, Validators.required],
      division: ["--Seleccione una opción--", Validators.required],
      tipo: ["--Seleccione una opción--", Validators.required],
      personas: this.formBuilder.array([]),
      responsable: ["--Seleccione una opción--", Validators.required],
    });
    this.usuarioService.getUsuario().subscribe({
      next: (data) => {
        this.usuario = data;
        this.integrantes = data.amigos.slice();
        this.integrantes.push(this.usuario.username)
        this.amigosConUsuario = this.integrantes.slice();
      }
    })
    this.grupoService.getGroupsPaginated().subscribe({
      next: (data) => {
        this.grupos = data.grupos;
      }
    })
  }


  get personasFormArray() {
    return this.formulario.get('personas') as FormArray;
  }

  agregarUsuario() {
    // Agregar un nuevo usuario al FormArray
    const nuevoUsuario = this.formBuilder.group({
      usuario: ['--Seleccione una opción--', Validators.required],
      monto: ['0', Validators.required],
    });
    this.personasFormArray.push(nuevoUsuario);

  }

  eliminarUsuario(index: number) {
    // Eliminar la entrada en el Map correspondiente al índice
    this.seleccionCargado.forEach((value, key) => {
      if (value === index) {
        this.seleccionCargado.delete(key);
      } else if (value > index) {
        this.seleccionCargado.set(key, value - 1);
      }
    });

    // Eliminar el usuario del FormArray
    this.personasFormArray.removeAt(index);
  }

  crearGasto() {

    const formData = new FormData();
    this.armarGastoRequest(this.formulario);
    const blob = new Blob([JSON.stringify(this.formulario.value)], {
      type: 'application/json'
    });
    formData.append('imagen', this.imagen);
    formData.append('gastoRequest', blob);
    alert(JSON.stringify(this.formulario.value));
    // Realiza la solicitud HTTP POST
    this.gastoService.crearGasto(formData).subscribe(
      {
        next: (data) => {
          this.resetearForm();
        },
        error: (error) => {
          console.log(error);
        }
        ,
        complete: () => {
          this.formulario.reset();
        }
      }
    );
  }


  armarGastoRequest(formulario: FormGroup) {
    if (formulario.value.division === 'MONTOIGUAL') {
      formulario.value.personas.forEach((persona: any) => {
        persona.monto = formulario.value.monto / formulario.value.personas.length;
      });
      formulario.value.division = 'MONTO';
    }
    if (formulario.value.division === 'PORCENTAJEIGUAL') {
      formulario.value.personas.forEach((persona: any) => {
        persona.monto = (formulario.value.monto / formulario.value.personas.length) * 100 / formulario.value.monto;
      });
      formulario.value.division = 'PORCENTAJE';
    }
    formulario.value.fecha = new Date(formulario.value.fecha).toISOString();
  }

  resetearForm() {
    this.formulario.reset();
    this.personasFormArray.clear();
  }

  onChange(event: any) {

    const file: File = event.target.files[0];

    if (file) {
      this.imagen = file;
    } else {
      this.imagen = new File([], "");
    }
  }

  cambioGrupo($event: any) {
    const foundGroup = this.grupos.find((grupo) => grupo.id == $event.target.value);

    if (foundGroup) {
      this.integrantes = foundGroup.participantes;
      this.seleccionCargado.clear();
      this.formulario.get('responsable')?.setValue("--Seleccione una opción--");
      this.personasFormArray.clear();
    } else {
      // Handle the case where no matching group is found
      console.error("No matching group found");
    }

  }


  cambioUsuario($event: any, i: number) {
    const selectedValue = $event.target.value;
    this.seleccionCargado.delete(this.previousValue);
    this.seleccionCargado.set(selectedValue, i);
  }

  antesCambioUsuario($event: any, i: number) {
    this.previousValue = $event.target.value;
  }

  chequearSeleccionado(persona: string, i: number) {
    return this.seleccionCargado.get(persona) != i && this.seleccionCargado.get(persona) != undefined;
  }

  gastoGrupalCheck($event: any) {
    this.personasFormArray.clear();
    this.seleccionCargado.clear();
    this.formulario.get('responsable')?.setValue("--Seleccione una opción--");
    this.formulario.get('id_grupo')?.setValue(0);
    if (!$event.target.checked) {
      this.integrantes = this.usuario.amigos.slice();
      this.integrantes.push(this.usuario.username);
      return;
    }
    this.integrantes = [];
  }
}
