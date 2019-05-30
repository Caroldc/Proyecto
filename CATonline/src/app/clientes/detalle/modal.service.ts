import { Injectable,EventEmitter } from '@angular/core';


@Injectable({
  providedIn: 'root'
})
/*necesitaremos una variable que sea como una bandera, con un valor true o false, parqa mostrar o ocultar 
el modal. Para ello creamos un modal.service, que nos muestre o no. Con un par de metodos que cambien el
 valor del modal. */
export class ModalService {
  modal: boolean = false;
  //necesita el _ para diferenciarlo del metodo get
private _notificarUpload= new EventEmitter<any>();
  constructor() { }
  get notificarUpload(): EventEmitter<any>{
    return this._notificarUpload;
  }
  abrirModal() {
    this.modal = true;
  }
  cerrarModal() {
    this.modal = false;
  }
}
