import { Component, OnInit } from '@angular/core';
import { Factura } from './models/factura';
import { ClienteService } from '../clientes/cliente.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormControl } from '@angular/forms';
import { Observable } from 'rxjs';
import { startWith, flatMap, map } from 'rxjs/operators';
import { FacturasService } from './services/facturas.service';
import { Producto } from './models/producto';
import { MatAutocompleteSelectedEvent } from '@angular/material';
import { LineaFactura } from './models/linea-factura';
import swal from 'sweetalert2';


@Component({
  selector: 'app-facturas',
  templateUrl: './facturas.component.html'
})
export class FacturasComponent implements OnInit {
  titulo: string = 'Nueva Factura';
  factura: Factura = new Factura();
  myControl = new FormControl();
  // productos: string[] = ['Rascador', 'Ratón radiocontrol', 'Camita M','Camita S'];
  productosFiltrados: Observable<Producto[]>;
  /*Es necesario tener ClienteService ya que la factura va asignada a un cliente */

  constructor(private clienteService: ClienteService, private activatedRoute: ActivatedRoute, 
    private facturasService: FacturasService,
    private router: Router) { }

  ngOnInit() {
    /*asignamos el clietne al objeto factura, a traves del activatedRoute */
    this.activatedRoute.paramMap.subscribe(params => {
      //con el + convertimos a tipo number ya que por defecto es string
      let clienteId = + params.get('clienteId');
      this.clienteService.getCliente(clienteId).subscribe(cliente => this.factura.cliente = cliente);
      console.log(clienteId);
    });
    /*Angular Material  */
    /*No moostraremos por defecto toda la lista de producto,  */
    this.productosFiltrados = this.myControl.valueChanges
      .pipe(
        map(value => typeof value === 'string' ? value : value.nombre),
        /*Convertimos un observable dentro de otro observable, por lo que tenemos que aplanar con flatmap */
        flatMap(value => value ? this._filter(value) : [])
      );

  }

  private _filter(value: string): Observable<Producto[]> {
    const filterValue = value.toLowerCase();
    //retorna un observable de productos
    return this.facturasService.filtrarProductos(filterValue);
  }
  mostrarNombre(producto?: Producto): string | undefined {
    return producto ? producto.nombre : undefined

  }

  /* Para añadir el producto a la factura, utilizamos el evento de MatAutocompleteSelectedEvent.
  Obtenemos el producto a traves del objeto event, y lo convertimos al tipo producto (as Producto)*/
  seleccionarProducto(event: MatAutocompleteSelectedEvent): void {
    let producto = event.option.value as Producto;
    //mostramos el producto seleccionado
    console.log(producto);
    /*Antes de crear una nueva linea vamos a preguntar si ya esta añadida, para aumentar su cantidad */
    if (this.existeLinea(producto.id)) {
      this.incrementaCantidad(producto.id);
    } else {
      /*Una vez seleccionado el producto lo añadimos a la linea de factura */
      let nuevaLinea = new LineaFactura();
      nuevaLinea.producto = producto;//el producto que seleccionamos
      this.factura.lineas.push(nuevaLinea);//con el metodo push podemos añadir un nuevo elemento   

    }
    this.myControl.setValue('');//limpiamos el autocomplete para poder añadir otro producto
    event.option.focus();//quitamos el focus del evento
    event.option.deselect();//quitamos el producto que ya hemos seleccionado
  }

  actualizarCantidad(id: number, event: any): void {
    let cantidad: number = event.target.value as number;
    if (cantidad == 0) {
      //usamos el return para que no siga ejecutando el resto del metodo, si la cantidad es 0 eliminamos la linea
      return this.eliminarLinea(id);
    }
    this.factura.lineas = this.factura.lineas.map((linea: LineaFactura) => {
      if (id === linea.producto.id) {
        linea.cantidad = cantidad;
      }
      return linea;
    })
  }
  /*Si la linea ya existe devuelve true */
  existeLinea(id: number): boolean {
    let existe = false;
    this.factura.lineas.forEach((linea: LineaFactura) => {
      if (id === linea.producto.id) {
        existe = true;
      }
    });
    return existe;
  }
  /*Aumenta la cantidad si la linea ya existe */
  incrementaCantidad(id: number): void {
    this.factura.lineas = this.factura.lineas.map((linea: LineaFactura) => {
      if (id === linea.producto.id) {
        ++linea.cantidad;
      }
      return linea;
    })
  }
  eliminarLinea(id: number): void {
    /*Filtrar todos los productos cuando tenemos el id que pasamos por argumento */
    this.factura.lineas = this.factura.lineas.filter((linea: LineaFactura) => id !== linea.producto.id);
  }
  create():void {
this.facturasService.create(this.factura).subscribe(factura =>{
  console.log(this.factura);
  //aqui manejamos el exito o la respuesta
  swal(this.titulo, `Factura ${factura.descripcion} creada con éxito`,"success");
  this.router.navigate(['/facturas',factura.id]);

})
  }

}
