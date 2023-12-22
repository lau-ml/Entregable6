import { Component } from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-gastos',
  templateUrl: './gastos.component.html',
  styleUrl: './gastos.component.css'
})
export class GastosComponent {
  personas: string[] = ['Usuario 1', 'Usuario 2', 'Usuario 3'];
  tuFormulario: FormGroup= new FormGroup({});

  constructor(private formBuilder: FormBuilder) {}

  ngOnInit() {
    this.tuFormulario = this.formBuilder.group({
      monto: ['', Validators.required],
      fecha: ['', Validators.required],
      imagen: [''],
      grupoBool: [false],

      formaDividir: ['igual'],
      categoria: ['comida', Validators.required],
      quienHizoCompra: [''],
      personas: this.formBuilder.array([]),
    });
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

  eliminarUsuario(index: number) {
    this.personasFormArray.removeAt(index);
  }

  guardarRecibo() {
    // Lógica para guardar el recibo aquí
    console.log(this.tuFormulario.value);
  }
}
