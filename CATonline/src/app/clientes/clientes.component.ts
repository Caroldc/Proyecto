import { Component, OnInit, Input } from '@angular/core';
import { Cliente } from './cliente';
import { ClienteService } from './cliente.service';
import { ModalService } from '../clientes/detalle/modal.service';
import swal from 'sweetalert2';
import { tap } from 'rxjs/operators';
import { ActivatedRoute } from '@angular/router';
import { typeWithParameters } from '@angular/compiler/src/render3/util';
import { AuthService } from '../usuarios/auth.service';

@Component({
  selector: 'app-clientes',
  templateUrl: './clientes.component.html'
})
export class ClientesComponent implements OnInit {

  clienteSeleccionado: Cliente;
  paginador: any;
  clientes: Cliente[];
  /* En el constructor estamos utilizando la inyeccion de dependencias
  ya que al utilizar el componente directamente le asignamos un 
  ClienteService, que en nuestro caso contiene un método
  que devuelve la lista de clientes */
  constructor(private clienteService: ClienteService, private activatedRoute: ActivatedRoute, 
    public modalService: ModalService, public authService: AuthService) { 
      //modalService y authService van como publicos porque se usan tambien fuera de la clase
    }
  /* usamos en metodo onInit para asignar el valor al atributo clientes, y así tener cargada la
  lista de clientes desde el json */
  ngOnInit() {

    //El activateRoute paramMap se encarga de suscribir un observador cada vez que cambia
    //el parametro page en la ruta se va a actualizar 
    this.activatedRoute.paramMap.subscribe(params => {
      //el operador suma delante hace que se conviertan en number los parametros
      let page: number = +params.get('page');
      if (!page) {
        page = 0;
      }
      this.clienteService.getClientes(page).pipe(
        //tap nos permite realizar operaciones con los stream siempre y cuando no los transformemos
        tap(response => {
          console.log('ClientesComponent : tap 3');
          (response.content as Cliente[]).forEach(cliente => {
            console.log(cliente.nombre);
          });
        })).subscribe(response => {
          this.clientes = response.content as Cliente[];
          //asignamos los atributos al paginador tb para poder implementarlo
          this.paginador = response;
        });

    })
    this.modalService.notificarUpload.subscribe(cliente => {
      //map nos permite por cada cliente modificar algo
      this.clientes = this.clientes.map(clienteOriginal => {
        if (cliente.id == clienteOriginal.id) {
          clienteOriginal.foto = cliente.foto;
        } return clienteOriginal;
      })
    })
  }
  
  delete(cliente: Cliente): void {
    const swalWithBootstrapButtons = swal.mixin({
      confirmButtonClass: 'btn btn-success',
      cancelButtonClass: 'btn btn-danger',
      buttonsStyling: false,
    })

    swalWithBootstrapButtons({
      title: '¿Estás seguro?',
      text: `¿Seguro que desea eliminarl al cliente ${cliente.nombre} ${cliente.apellido}?`,
      type: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Si, eliminar!',
      cancelButtonText: 'No, cancelar!',
      reverseButtons: true
    }).then((result) => {
      if (result.value) {
        this.clienteService.delete(cliente.id).subscribe(
          response => {
            //el metodo filter() de array nos permite filtrar solo los elementos
            //que deseamos (segun ciertos criterios ) y devolverlos en un nuevo array
            this.clientes = this.clientes.filter(cli => cli !== cliente)
            swalWithBootstrapButtons(
              'Cliente eliminado!',
              `Cliente eliminado con éxito`,
              'success'
            )
          }
        )
      }
    }
    )
  }
  abrirModal(cliente: Cliente) {
    this.clienteSeleccionado = cliente;
    this.modalService.abrirModal();
  }

  
}
