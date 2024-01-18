import {Component} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {GastoService} from "../_services/gasto.service";
import {UsuarioService} from "../_services/usuario.service";
import {GastoResponse} from "../_responses/gastoResponse";

@Component({
  selector: 'app-gastos',
  templateUrl: './gastos.component.html',
  styleUrl: './gastos.component.css'
})
export class GastosComponent {
  personas: string[] = ['Usuario 1', 'Usuario 2', 'Usuario 3'];
  tuFormulario: FormGroup = new FormGroup({});
  imagen: File = new File([], "");
  private id: number = 0;
  gastos: GastoResponse[] = [];
  totalItems: number = 0;
  totalPages: number = 0;
  currentPage: number = 0;
  loading: boolean = true;
  perPage: number = this.gastoService.getPerPages();

  constructor(private formBuilder: FormBuilder, private gastoService: GastoService,
              private usuarioService: UsuarioService) {
  }

  ngOnInit() {
    this.tuFormulario = this.formBuilder.group({
      monto: ['', Validators.required],
      fecha: ['', Validators.required],
      imagen: ['', Validators.required],
      grupoBool: [false],
      id_grupo: ['', Validators.required],
      division: ['', Validators.required],
      tipo: ['', Validators.required],
      personas: this.formBuilder.array([]),
    });
    this.usuarioService.getUsuario().subscribe({
      next: (data) => {
        this.id = data.id;
      }
    })
    this.getPage(1);
  }


  get personasFormArray() {
    return this.tuFormulario.get('personas') as FormArray;
  }

  agregarUsuario() {
    this.personasFormArray.push(this.formBuilder.group({
      nombre: ['', Validators.required],
      monto: ['', Validators.required],
    }));
  }

  getPage(page: number) {

    this.gastoService.getGastos(page).subscribe(
      {
        next: (data) => {
          this.loading = false;
          this.gastos = data.gastos;
          this.totalItems = data.totalItems;
          this.totalPages = data.totalPages;
          this.currentPage = data.currentPage;

        },
        error: (error) => {
          console.log(error)
        }
      }
    )
  }

  eliminarUsuario(index: number) {
    this.personasFormArray.removeAt(index);
  }

  guardarRecibo() {

    const formData = new FormData();
    this.armarGastoRequest(this.tuFormulario);
    const blob = new Blob([JSON.stringify(this.tuFormulario.value)], {
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
          this.tuFormulario.reset();
          this.getPage(this.currentPage);
        }
      }
    );
  }


  armarGastoRequest(tuFormularo: FormGroup) {
    if (!tuFormularo.value.grupoBool) {
      tuFormularo.value.id_grupo = 0;
    }
    if (tuFormularo.value.division === 'MONTOIGUAL') {
      tuFormularo.value.personas.forEach((persona: any) => {
        persona.monto = tuFormularo.value.monto / tuFormularo.value.personas.length;
      });
      tuFormularo.value.division = 'MONTO';
    }
    if (tuFormularo.value.division === 'PORCENTAJEIGUAL') {
      tuFormularo.value.personas.forEach((persona: any) => {
        persona.monto = (tuFormularo.value.monto / tuFormularo.value.personas.length) * 100 / tuFormularo.value.monto;
      });
      tuFormularo.value.division = 'PORCENTAJE';
    }
    let suma: number = 0;
    tuFormularo.value.personas.forEach((persona: any) => {
        suma += Number(persona.monto);

      }
    );
    if (tuFormularo.value.division === 'MONTO') {
      tuFormularo.value.personas.push({nombre: this.id, monto: tuFormularo.value.monto - suma});
    } else {
      tuFormularo.value.personas.push({nombre: this.id, monto: 100 - suma});
    }


  }

  resetearForm() {
    this.tuFormulario.reset();
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
}
