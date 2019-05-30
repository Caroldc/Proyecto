import { Component, OnInit, Input } from '@angular/core';
import { Cliente } from '../cliente';
import { ClienteService } from '../cliente.service';
import { ModalService } from './modal.service';
import { ActivatedRoute } from '@angular/router';
import swal from 'sweetalert2';
import { HttpEventType } from '@angular/common/http';
import { AuthService } from '../../usuarios/auth.service';
import { FacturasService } from '../../facturas/services/facturas.service';
import { Factura } from '../../facturas/models/factura';

/*Creamos este componente para el Upload por el lado de Angular.*/
@Component({
  selector: 'detalle-cliente',
  templateUrl: './detalle.component.html',
  styleUrls: ['./detalle.component.css']
})
export class DetalleComponent implements OnInit {
  titulo: string = "Detalles del cliente";
  progreso: number = 0;
  //inyectamos el cliente en detallesComponent
  @Input() cliente: Cliente;
  //si es un atributo propio de la clase que lo vamos a utilizar solo en ella, lo creamos como private
  private fotoSeleccionada: File;
  /* */
  //inyectamos rl activatedroute para editar un cliente, para poder suscribir cuando cambia el parametro del id
  constructor(private clienteService: ClienteService, private activatedRoute: ActivatedRoute,
    public modalService: ModalService,
    private authService: AuthService,
    private facturasService: FacturasService) { }

  ngOnInit() {
    /*cuando se inicialice el componente, vamos a suscribir cuando cambia el parametro del id, para poder obtener
    el detalle del cliente*/
    /*Como hacemos la inyeccion de la instancia no es necesario esto
    this.activatedRoute.paramMap.subscribe(params => {
      //obtenemos el parametro id y con el signo + convertimos el id en tipo number
      let id: number = +params.get('id');
      //si el id existe
      if (id) {
        //a traves de la clase service obtenemos el cliente y lo suscribimos
        this.clienteService.getCliente(id).subscribe(cliente => {
          //y por ultimo lo asignamos al cliente.
          this.cliente = cliente;
        });
      }
    });
*/
  }

  seleccionarFoto(event) {
    //elegir el primer evento, que es la eleccion de la foto
    this.fotoSeleccionada = event.target.files[0];
    this.progreso = 0;

    console.log(this.fotoSeleccionada);
    //Tenemos que validar que el archivo seleccionada es una imagen
    if (this.fotoSeleccionada.type.indexOf('image') < 0) {
      //indexOf es un metodo de la clase String que lo que hace es buscar en el string si hay alguna coincidencia con image
      //porque es lo que hemos indicado entre comillas. Si lo encuentra devuelve la primera vez que lo encuentre
      swal('Error seleccionar imagen',
        //el mensaje que muestra por pantalla es este
        `Por favor seleccione un archivo de tipo imagen =)`,
        'error');
      this.fotoSeleccionada = null;
    }
  }
  subirFoto() {
    //llamamos al metodo subirFoto del cliente.service, y como tenemos el objeto cliente con todos sus datos añadirmos el id
    if (!this.fotoSeleccionada) {
      swal('Error Upload',
        //el mensaje que muestra por pantalla es este
        `Debe seleccionar una foto  =(`,
        'error')
    } else {

      this.clienteService.subirFoto(this.fotoSeleccionada, this.cliente.id).subscribe(event =>//cliente=>
      {//para cargar un proceso a la imagen que subimos, tenemos que manejar eventos.
        //si el evento es de tipo uploadprogress entonces calculamos el proceso
        if (event.type === HttpEventType.UploadProgress) {
          //calculamos el porcentaje de transferencia
          this.progreso = Math.round((event.loaded / event.total) * 100)
        } else if (event.type === HttpEventType.Response) {
          //si tiene response la pasamos a tipo cliente
          let response: any = event.body;
          this.cliente = response.cliente as Cliente;
          //evento que refresca la imagen en la vista. Pero hay que suscribirse en listado de clientescomponent
          this.modalService.notificarUpload.emit(this.cliente);


          //y añadimos una ventana de alerta para saber que se ha realizado bien
          swal('La foto se ha subido completamente!',
            //el mensaje que muestra por pantalla es este
            response.mensaje,
            'success')
        }
        //lo actualizamos porque viene con la foto ya incluida, por eso lo volvemos a asignar
        // Marca un error aqui, porque al modificar la clase service, y cambiar el tipo de dato
        //de retorno esto da error this.cliente=cliente;

      });

    }

  }
  cerrarModal() {
    this.modalService.cerrarModal();
    //tenemos que resetear fotoSeleccionada y dejarla en null
    //y tambien el progreso a 0
    this.fotoSeleccionada = null;
    this.progreso = 0;
  }

  delete(factura:Factura):void{
        swal({
      title: '¿Estás seguro?',
      text: `¿Seguro que desea eliminarl la factura ${factura.descripcion} del día ${factura.createAt}?`,
      type: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Si, eliminar!',
      cancelButtonText: 'No, cancelar!',
      reverseButtons: true
    }).then((result) => {
      if (result.value) {
        this.facturasService.delete(factura.id).subscribe(
          response => {
            //el metodo filter() de array nos permite filtrar solo los elementos
            //que deseamos (segun ciertos criterios ) y devolverlos en un nuevo array
            this.cliente.facturas = this.cliente.facturas.filter(f => f !== factura)
            swal(
              'Factura eliminada!',
              `Factura ${factura.descripcion} eliminada con éxito`,
              'success'
            )
          }
        )
      }
    }
    )
  }
}
