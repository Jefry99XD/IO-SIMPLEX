import { Routes } from '@angular/router';
import { EntradaComponent } from './entrada/entrada.component';
import { SolucionComponent } from './solucion/solucion.component';
import { ZRestriccionesComponent } from './z-restricciones/z-restricciones.component';


export const routes: Routes = [
    {path : '', redirectTo: 'Entrada', pathMatch : 'full'},
    
    {path : 'Entrada', component : EntradaComponent},
    {path : 'Z-Restricciones', component : ZRestriccionesComponent},
    {path : 'Solucion', component : SolucionComponent}    
];
