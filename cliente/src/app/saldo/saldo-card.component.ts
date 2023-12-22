import {Component, Input, OnInit} from '@angular/core';
import {UsuarioService} from "../_services/usuario.service";

@Component({
  selector: 'app-saldo-card',
  templateUrl: './saldo-card.component.html',
  styleUrls: ['./saldo-card.component.css']
})
export class SaldoCardComponent implements OnInit{
   saldo: number=0;
   constructor(private userService:UsuarioService) {
   }

  ngOnInit(): void {
     this.userService.getSaldo().subscribe({
        next: (saldo) => {
          this.saldo = saldo;
        }
     })
  }
}
