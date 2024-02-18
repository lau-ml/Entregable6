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
  cargados: Set<number> = new Set<number>();
  seleccionados: Set<string> = new Set<string>();

  constructor(private formBuilder: FormBuilder, private gastoService: GastoService,
              private usuarioService: UsuarioService, private grupoService: GrupoService) {
  }

  ngOnInit() {
    this.formulario = this.formBuilder.group({
      monto: ['', Validators.required],
      fecha: ['', Validators.required],
      imagen: ['', Validators.required],
      grupoBool: [false],
      id_grupo: ["--Seleccione una opción--", Validators.required],
      division: ["--Seleccione una opción--", Validators.required],
      tipo: ["--Seleccione una opción--", Validators.required],
      personas: this.formBuilder.array([]),
    });
    this.usuarioService.getUsuario().subscribe({
      next: (data) => {
        this.usuario = data;
        this.integrantes = data.amigos.slice();
        this.integrantes.push(this.usuario.username)
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
      nombre: ['--Seleccione una opción--', Validators.required],
      monto: ['', Validators.required],
    });
    this.personasFormArray.push(nuevoUsuario);

  }

  eliminarUsuario(index: number) {
    this.getAllIntegrantesOptions().forEach((option: HTMLOptionElement) => {
      if (option.value === this.personasFormArray.at(index).get('nombre')?.value)
        option.disabled = false;
    });
    this.cargados.delete(this.cargados.size - 1);
    this.seleccionados.delete(this.personasFormArray.at(index).get('nombre')?.value);
    this.personasFormArray.removeAt(index);
  }

  guardarRecibo() {

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
          console.log(data);
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
    if (!formulario.value.grupoBool) {
      formulario.value.id_grupo = 0;
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
    let suma: number = 0;
    formulario.value.personas.forEach((persona: any) => {
        suma += Number(persona.monto);

      }
    );
    if (formulario.value.division === 'MONTO') {
      formulario.value.personas.push({id: this.usuario.id, monto: formulario.value.monto - suma});
    } else {
      formulario.value.personas.push({id: this.usuario.id, monto: 100 - suma});
    }


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

  cambioGrupo($event: Event) {

  }


  cambioUsuario($event: any, i: number) {
    const selectedValue = $event.target.value;
    const personasFormArray = this.personasFormArray as FormArray;
    personasFormArray.controls.forEach((control, index) => {
      const otroSelect = control.get('nombre');
      if (otroSelect) {
        const options = this.getIntegrantesOptions(index); // Obtener las opciones
        options.forEach((option: HTMLOptionElement) => {
          if (option.value === selectedValue && index != i) {
            option.disabled = true; // Deshabilitar la opción seleccionada en otros selects
          }
          if (option.value === this.previousValue && this.previousValue !== "--Seleccione una opción--") {
            option.disabled = false; // Habilitar la opción anteriormente seleccionada
          }
        });
      }
    });
    this.seleccionados.add(selectedValue);
    this.seleccionados.delete(this.previousValue);
  }


  getIntegrantesOptions(i: number): NodeListOf<HTMLOptionElement> {
    return document.querySelectorAll(`[id="nombre${i}"] option`);
  }

  getAllIntegrantesOptions(): NodeListOf<HTMLOptionElement> {
    return document.querySelectorAll(`[id^="nombre"] option`);
  }


  antesCambioUsuario($event: any, i: number) {
    if (!this.cargados.has(i)) {
      this.cargados.add(i);
      const options = this.getIntegrantesOptions(i);
      options.forEach((option: HTMLOptionElement) => {
        option.disabled = option.value == "--Seleccione una opción--" || this.seleccionados.has(option.value);
      });
    }
    this.previousValue = $event.target.value;
  }

}
