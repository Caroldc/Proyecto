import { Component, OnInit } from '@angular/core';
import { Cliente } from './cliente';
import { ClienteService } from './cliente.service';
import { Router, ActivatedRoute } from '@angular/router';
import swal from 'sweetalert2'
import { Region } from './region';
@Component({
  selector: 'app-form',
  templateUrl: './form.component.html'
})
export class FormComponent implements OnInit {
  public cliente: Cliente = new Cliente();
  public title: string = "Crear Cliente";
  constructor(private clienteService: ClienteService, private router: Router,
    private activatedRoute: ActivatedRoute) { }
  public errores: string[];
  regiones: Region[];
  ngOnInit() {
    this.cargarCliente();
    //cada vez que se inicializa va al api rest para desplegarlas en el formulario
    this.clienteService.getRegiones().subscribe(regiones => this.regiones = regiones);
  }
  /*Metodo para listar por id, debemos ver si existe o no el cliente y que se muestre
  de forma automatica en el formulario.Para poder obtener el id debemos importar la clase 
  ActivatedRoute. Suscribimos un observador que observe cuando obtenemos un id.
  Si existe, buscamos el cliente por id, suscribimos para registrar el observador
  que asigna el cliente de la consulta al cliente. En app.module debemos tener la ruta con el id
   */
  cargarCliente(): void {
    this.activatedRoute.params.subscribe(params => {
      let id = params['id']
      if (id) {
        this.clienteService.getCliente(id).subscribe((cliente) => this.cliente = cliente)
      }
    })
  }


  /**
   * metodo para crear 
   */
  public create(): void {
    // console.log(this.cliente)
    this.clienteService.create(this.cliente).subscribe(
      cliente => {
        this.router.navigate(['/clientes'])
        //mensaje con estilo que podemos obtener al tener descargado e importado swalAlert
        swal('Nuevo cliente', `El cliente ${cliente.nombre} ha sido creado con éxito!`, 'success')
        //success es el tipo de mensaje que se mostrará
      },//aqui tenemos la forma de mostrar las validaciones hechas en el backend
      err => {//en el parametro error esta el json
        this.errores = err.error.errors as string[];
        console.error('Código del error desde el backend: ' + err.status);
        console.error(err.error.errors);
      }
    );

  }
  update(): void {
    this.clienteService.update(this.cliente).subscribe(json => {
      this.router.navigate(['/clientes'])
      swal('Cliente Actualizado', `Cliente  ${json.cliente.nombre} actualizado con éxito!`, 'success')
    },//aqui tenemos la forma de mostrar las validaciones hechas en el backend
      //pero al ser un json no accede a los errores
      err => {
        this.errores = err.error.errors as string[];
        console.error('Código del error desde el backend: ' + err.status);
        console.error(err.error.errors);
      }
    );


  }
  //objeto uno interaccion, objeto dos el objeto del  cliente
  compararRegion(o1: Region, o2: Region): boolean {
    //si alguno de los dos objetos es null retorna false, sino, si el o1 y el o2 son iguales, retorna true
    //=== mismo tipo, y mismo valor ==solo por valor
    if (o1 === undefined && o2 === undefined) {
      return true;
    }
    return o1 == null || o2 == null ? false : o1.id === o2.id;
    // return o1 === null || o2 === null || o2 === undefined  || o1 === undefined ? false: o1.id === o2.id;
  }

}
