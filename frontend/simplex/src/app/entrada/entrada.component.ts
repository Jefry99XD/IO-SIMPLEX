import { Component, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-entrada',
  standalone: true,
  imports: [CommonModule,
    FormsModule ],
  templateUrl: './entrada.component.html',
  styleUrl: './entrada.component.css'
})
export class EntradaComponent {
  variables = 0
  restricciones = 0
  objetivo = ''

}
