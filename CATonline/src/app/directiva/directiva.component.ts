import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-directiva',
  templateUrl: './directiva.component.html'
})
export class DirectivaComponent {
  listaCurso:string[]=['Peluquería','Adiestramiento','Guardería','Nutrición','Información'];
  habilitar:boolean=false;
  
  constructor() { }
  /*Este método es mejor opcion que poner directamente la funcionalidad en el boton. Si creamos
  aqui el metodo que cambia el valor, luego en el (click) solo tenemos que hacer la llamada
  al método (click)='mostrar()'
  
  mostrar():void {
    this.habilitar= (this.habilitar==true)? false:true

  }*/
}
