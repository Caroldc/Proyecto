import { Component, OnInit, Input, OnChanges, SimpleChange, SimpleChanges } from '@angular/core';

@Component({
  selector: 'paginator-nav',
  templateUrl: './paginator.component.html'
})
export class PaginatorComponent implements OnInit, OnChanges {
  //atributo en la clase hijo, inyecte el valor
  @Input() paginador: any;
  paginas: number[];
  desde: number;
  hasta: number;

  constructor() { }

  ngOnInit() {
    this.initPaginator();
    /*Si implementaramos aqui el calculo de los rangos, no se actualizaría adecuadamente, porque el método onInit, se
    inicia una sola vez.
    Si avanzamos, en los rangos, vemos que no se actualiza el rango en el paginador, pero si esta paginando. Nuestro cvomponente
    cliente contiene el paginador, y se crea y se inicializa una sola vez. Por lo que solo cambia el parametro page.
    Esto se debe a que lo implementamos en el onInit, porque no se vuelve a invocar, por esta razón usamos el OnChanges, para poder asi
    refrescar el objeto paginador, a traves del suscribe.   */

  }
  ngOnChanges(changes: SimpleChanges) {
//para mejorar, creamos el metodo initPaginador, para calcular el rango y despues
// en el onChange recalculamos este.
    let paginadorActualizado = changes['paginador'];
    //el parametro change, obtiene el cambio del input del objeto paginador que inyecta clientes.component
    if(paginadorActualizado.previousValue){
      //si tiene un estado anterior(que haya cambiado)
      this.initPaginator();
    }
  }


  private initPaginator(): void {
        //métodos matematicos para calcular rangos de páginas
    //El primero es el maximo entre 1 y nuestra pagina actual menos 4, y como segundo argumento el total de paginas -5
    this.desde = Math.min(Math.max(1, this.paginador.number - 4), this.paginador.totalPages - 5);
    //El primero es el minimo entre el total de paginas, y la pagina actual +4 y como segundo parametro el 6
    this.hasta = Math.max(Math.min(this.paginador.totalPages, this.paginador.number + 4), 6);
    if (this.paginador.totalPages > 5) {
      this.paginas = new Array(this.hasta - this.desde + 1).fill(0).map((_valor, indice) =>
        indice + this.desde);
    } else {
      //totalpages tiene el total de paginas y existe dentro del paginator  sin tener que definirlo
      //asi nos podemos crear un array con el numero total de paginas
      this.paginas = new Array(this.paginador.totalPages).fill(0).map((_valor, indice) =>
        indice + 1);
      //el metodo fill rellena el arreglo con datos, en este caso 0
      //map dentro de un arreglo sirve para modificar los datos
    }
  }
}
