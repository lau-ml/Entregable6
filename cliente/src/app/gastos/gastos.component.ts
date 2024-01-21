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
  formulario: FormGroup = new FormGroup({});
  imagen: File = new File([], "");
  private id: number = 0;
  gastos: GastoResponse[] = [];
  totalItems: number = 0;
  totalPages: number = 0;
  currentPage: number = 0;
  loading: boolean = true;
  itemsPerPage: number = 0;

  constructor(private formBuilder: FormBuilder, private gastoService: GastoService,
              private usuarioService: UsuarioService) {
  }

  ngOnInit() {
    this.formulario = this.formBuilder.group({
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
    return this.formulario.get('personas') as FormArray;
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
          this.itemsPerPage = data.itemsPerPage;

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
          this.getPage(this.currentPage);
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
      formulario.value.personas.push({id: this.id, monto: formulario.value.monto - suma});
    } else {
      formulario.value.personas.push({id: this.id, monto: 100 - suma});
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
}
