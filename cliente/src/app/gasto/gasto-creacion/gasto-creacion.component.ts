import {Component} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {GastoResponse} from "../../_responses/gastoResponse";
import {GastoService} from "../../_services/gasto.service";
import {UsuarioService} from "../../_services/usuario.service";
import {GrupoResponse} from "../../_responses/grupoResponse";
import {GrupoService} from "../../_services/grupo.service";
import {SweetalertService} from "../../../../../../../dssd/cliente/src/app/services/sweetalert.service";
import {Router} from "@angular/router";

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
  previousValue: Map<number, string> = new Map<number, string>();
  amigosConUsuario: string[] = [];
  seleccionCargado: Map<string, number> = new Map<string, number>();


  constructor(private formBuilder: FormBuilder, private gastoService: GastoService,
              private sweetAlertService: SweetalertService,
              private usuarioService: UsuarioService, private grupoService: GrupoService,
              private router: Router) {
  }

  ngOnInit() {
    this.formulario = this.formBuilder.group({
      monto: ['', Validators.required],
      fecha: ['', Validators.required],
      imagen: [''],
      grupoBool: [false],
      id_grupo: ['', this.requiredIfGrupoBool()],
      responsable: ["", Validators.required],
      division: ["", Validators.required],
      tipo: ["", Validators.required],
      personas: this.formBuilder.array([]),
    });
    this.usuarioService.getUsuario().subscribe({
      next: (data) => {
        this.usuario = data;
        this.integrantes = data.amigos.slice();
        this.integrantes.push(this.usuario.usuario)
        this.amigosConUsuario = this.integrantes.slice();
      }
    })
    this.grupoService.getGroupsPaginated().subscribe({
      next: (data) => {
        this.grupos = data.grupos;
      }
    })
    this.formulario.valueChanges.subscribe(() => {
      this.eliminarErrores();
    });
  }

  private requiredIfGrupoBool() {
    if (this.formulario.get('grupoBool')?.value) {
      return {required: true};
    }
    return null;
  };


  get f() {
    return this.formulario.controls;
  }

  get personasFormArray() {
    return this.formulario.get('personas') as FormArray;
  }

  get fp() {
    return (this.formulario.get('personas') as FormArray).controls;
  }

  agregarUsuario() {
    // Agregar un nuevo usuario al FormArray
    const nuevoUsuario = this.formBuilder.group({
      usuario: ['', Validators.required],
      monto: ['0', Validators.required],
    });
    this.personasFormArray.push(nuevoUsuario);

  }

  eliminarUsuario(index
                    :
                    number
  ) {
    // Eliminar la entrada en el Map correspondiente al índice
    this.seleccionCargado.forEach((value, key) => {
      if (value === index) {
        this.seleccionCargado.delete(key);
      } else if (value > index) {
        this.seleccionCargado.set(key, value - 1);
      }
    });
    this.previousValue.forEach((value, key) => {
      if (key > index) {
        this.previousValue.set(key - 1, value);
      } else if (key === index) {
        this.previousValue.delete(key);
      }
    });
    this.formulario.get('responsable')?.setValue("");
    // Eliminar el usuario del FormArray
    this.personasFormArray.removeAt(index);
  }

  eliminarErrores() {
    this.formulario.get('monto')?.setErrors(null);
    this.formulario.get('imagen')?.setErrors(null);
    this.formulario.get('id_grupo')?.setErrors(null);
    this.formulario.get('responsable')?.setErrors(null);
    this.formulario.get('division')?.setErrors(null);
    this.formulario.get('tipo')?.setErrors(null);
    this.formulario.get('fecha')?.setErrors(null);
    this.formulario.get('personas')?.setErrors(null);
    this.formulario.get('personas')?.setErrors(null);
  }
  crearGasto() {

    const formData = new FormData();
    this.armarGastoRequest(this.formulario);
    const blob = new Blob([JSON.stringify(this.formulario.value)], {
      type: 'application/json'
    });
    formData.append('imagen', this.imagen);
    formData.append('gastoRequest', blob);

    // Realiza la solicitud HTTP POST
    this.gastoService.crearGasto(formData).subscribe(
      {
        next: (data) => {
          this.resetearForm();
          this.sweetAlertService.showAlert("success", "¡Éxito!", "Gasto creado");
          this.router.navigate(['/gastos']);
        },
        error: (error) => {
          if (error.message.includes("monto")) {
            this.formulario.get('monto')?.setErrors({error: true});
          }
          if (error.message.includes("imagen")) {
            this.formulario.get('imagen')?.setErrors({error: true});
          }
          if (error.message.includes("grupo")) {
            this.formulario.get('id_grupo')?.setErrors({error: true});
          }

          if (error.message.includes("responsable")) {
            this.formulario.get('responsable')?.setErrors({error: true});
          }
          if (error.message.includes("division")) {
            this.formulario.get('division')?.setErrors({error: true});
          }
          if (error.message.includes("tipo")) {
            this.formulario.get('tipo')?.setErrors({error: true});
          }
          if (error.message.includes("fecha")) {
            this.formulario.get('fecha')?.setErrors({error: true});
          }

          if (error.message.includes("participantes")) {
            this.formulario.get('personas')?.setErrors({error: true});

          }

          if (error.message.includes("suma")) {
            this.formulario.get('personas')?.setErrors({error2: true});
          }


          this.sweetAlertService.showAlert("error", "¡Error!", "No se pudo crear el gasto");
        }
        ,
        complete: () => {
        }
      }
    );
  }


  armarGastoRequest(formulario
                      :
                      FormGroup
  ) {
    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }
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

  onChange(event
             :
             any
  ) {

    const file: File = event.target.files[0];

    if (file) {
      this.imagen = file;
    } else {
      this.imagen = new File([], "");
    }
  }

  cambioGrupo($event
                :
                any
  ) {
    const foundGroup = this.grupos.find((grupo) => grupo.id == $event);

    if (foundGroup) {
      this.integrantes = foundGroup.participantes;
      this.seleccionCargado.clear();
      this.formulario.get('responsable')?.setValue("");
      this.personasFormArray.clear();
    } else {
      // Handle the case where no matching group is found
      console.error("No matching group found");
    }

  }


  cambioUsuario($event
                  :
                  any, i
                  :
                  number
  ) {
    const selectedValue = $event;
    this.seleccionCargado.delete(<string>this.previousValue.get(i));
    this.previousValue.set(i, selectedValue);
    this.seleccionCargado.set(selectedValue, i);
  }

  antesCambioUsuario(i
                       :
                       number
  ) {
    this.previousValue.set(i, this.personasFormArray.at(i).get('usuario')?.value);
  }

  eliminarSeleccion(i
                      :
                      number
  ) {

    this.seleccionCargado.forEach((value, key) => {
      if (value === i) {
        this.seleccionCargado.delete(key);
      }
    });
    this.previousValue.delete(i);
    this.personasFormArray.at(i).get('usuario')?.setValue("");
  }

  chequearSeleccionado(persona
                         :
                         string, i
                         :
                         number
  ) {
    return this.seleccionCargado.get(persona) != i && this.seleccionCargado.get(persona) != undefined;
  }

  gastoGrupalCheck($event
                     :
                     any
  ) {
    this.personasFormArray.clear();
    this.seleccionCargado.clear();
    this.formulario.get('responsable')?.setValue("");
    this.formulario.get('id_grupo')?.setValue('');
    if (!$event.target.checked) {
      this.integrantes = this.usuario.amigos.slice();
      this.integrantes.push(this.usuario.usuario);
      this.formulario.get('id_grupo')?.setValidators(null);
      return;
    } else {
      this.integrantes = [];
      this.formulario.get('id_grupo')?.setValidators(Validators.required);
    }
  }
}
