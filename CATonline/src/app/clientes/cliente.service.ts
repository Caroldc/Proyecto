import { Injectable } from '@angular/core';
import { Cliente } from './cliente';
import { Observable, of, throwError } from "rxjs";
import { HttpClient, HttpRequest, HttpEvent } from "@angular/common/http";
import { map, catchError, tap } from 'rxjs/operators';// para usar pipe y cambiar la respuesta del observable

import { Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { Region } from './region';


@Injectable(//{
  // providedIn: 'root'}
)
export class ClienteService {
  //esta es la url que usamos para enlazar. el puerto 8080 es donde esta el servidor de spring
  private urlEndPoint: string = 'http://localhost:8080/api/clientes';
  //atributo con las cabeceras
//  private httpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' })
  //Tenemos que entender que el objeto HttpHeaders es inmutable, por lo tanto cada vez que agregamos un atributo
  //con el método append es retornar una nueva instancia con ese cambio pero la instancia original, se mantiene
  //inalterable

  //Por lo tanto hay que implementar un metodo que agregue a las cabeceras el atributo autorithation con el token
 /* private agregarAuthHeader(){
    let token = this.authService.token;
    if(token !=null){
      return this.httpHeaders.append('Authorization','Bearer '+ token);//importante el Bearer(Petición servicio con autorización)
      //devuelve una nueva instancia con el cambio

    }
    return this.httpHeaders;
  }*/

 //inyectamos la dependencia 
 constructor(private http: HttpClient, private router: Router) { }

/*Este metodo crea error en las autorizaciones */

  getRegiones(): Observable<Region[]>{
    //añadimos el headers a cada uno de los métodos protegidos
    return this.http.get<Region[]>(this.urlEndPoint+'/regiones');
    
  }
 
  /*Con Observable utilizamos los Stream, en este caso le decimos al
  metodo getClientes que va a recibir un array de clientes 
  y va a devolverlo, pero debemos utilizar observable y of para ello.
  flujo reactivo */
   /*Una vez configurado el cors, debemos modificar nuestros métodos para las peticiones http */
     /*si queremos un tipo de dato concreto debemos castear observable,
     puesto que nos devuelve any: ej Observable< Cliente[] >*/
    //return this.http.get<Cliente[]>(this.urlEndPoint);
  getClientes(page: number): Observable<any> {
    return this.http.get(this.urlEndPoint + '/page/' + page).pipe(
      tap((response: any) => {
        console.log('ClienteService: tap 1');
        (response.content as Cliente[]).forEach(cliente => {
          console.log(cliente.nombre);
        });
      })
      , map((response: any) => {
        //map del flujo
        (response.content as Cliente[]).map(cliente => {
          cliente.nombre = cliente.nombre;//.toUpperCase();
          let datePipe = new DatePipe('es');
          cliente.createAt = datePipe.transform(cliente.createAt, 'EEEE dd, MMMM yyyy');//formatDate(cliente.createAt,'EEEE dd, MMMM yyyy','en-US');
          return cliente;
        });
        return response;
      }),
      tap(response => {
        (response.content as Cliente[]).forEach(cliente => {
          console.log(cliente.nombre);
        })
      })
    );
    //la respuesta viene un formato json, por eso es importante castear
  }
  create(cliente: Cliente): Observable<Cliente> {
    return this.http.post(this.urlEndPoint, cliente).pipe(
      //existen dos metodos de recoger los errores, uno creando los tipos de objeto en any
      //y la otra es conviertiendo el objeto json en un cliente, como es este caso
      //como en el back tenemos un map hay que convertir la parte del map cliente en un objeto tipo cliente
      map((response: any) => response.cliente as Cliente),
      catchError(e => {
                //en el if tenemos la forma de mostrar las validaciones hechas en el backend
        if (e.status == 400) {
          return throwError(e);
        }
        if (e.error.mensaje){
          console.error(e.error.mensaje);

        }
              
        return throwError(e);
      }
      )
    )
  }

  getCliente(id): Observable<Cliente> {
    //así navegamos al id del cliente
    //Además añadiendo catchError, podemos capturar los errores que retorne el servidor
    return this.http.get<Cliente>(`${this.urlEndPoint}/${id}`).pipe(
      //detecta los codigos de errores de la respuesta del servidor y los encapsula dentro
      //del error (e) y dentro de la funcion lamda lo manejamos
      catchError(e => {

        if(e.status!=401 && e.error.mensaje){
          this.router.navigate(['/clientes']);
        }
        //Lo primero que hacemos es redireccionar a la vista
        this.router.navigate(['/clientes']);
        //primero el titulo para el error, el segundo argumento el mensaje de error que contiene
        //la excepcion(e) que recibimos desde el backend. Y el mensaje es el que pasamos desde el backend
        //y por ultimo el tipo de mensaje, en este caso error
        console.error(e.error.mensaje);
        //nos permite convertir el error en un observable
        return throwError(e);

      })
    );
  }
  //metodo para actualizar los datos, importante en la url poner el identificativo de cada uno
  update(cliente: Cliente): Observable<any> {
    return this.http.put<any>(`${this.urlEndPoint}/${cliente.id}`, cliente
   ).pipe(
        catchError(e => {

          if (e.status == 400) {
            return throwError(e);
          }
          if (e.error.mensaje){
            console.error(e.error.mensaje);
  
          }
         
          return throwError(e);
        }
        )
      )
  }
  delete(id: number): Observable<Cliente> {
    //así navegamos al id del cliente
    return this.http.delete<Cliente>(`${this.urlEndPoint}/${id}`).pipe(
      catchError(e => {
        if (e.error.mensaje){
          console.error(e.error.mensaje);

        }     
        return throwError(e);
      }
      )
    )
  }
  /*Para poder subir nuestra imagen al cliente necesitamos un nuevo metodo que nos permita
  enviar FormData con soporte multipart/form. Para eso debemos utilizar la clase de javascript
  formData */
  subirFoto(archivo: File, id): Observable<HttpEvent<{}>> {
    //clase nativa de javascript por lo que no es necesaria importarla.
    let formData = new FormData();
    //debemos poner el mismo nombre que pusimos en el backend: @RequestParam("archivo")
    formData.append("archivo", archivo);
    //lo mismo para el id
    formData.append("id", id);

    const req = new HttpRequest('POST', `${this.urlEndPoint}/upload`, formData, {
      reportProgress: true
    });
    //con esto retornamos un httpEvent, pero aun asi contiene el objeto response por lo que podemos recuperar el cliente
    return this.http.request(req);//.pipe(
    //enlugar de enviar el cliente como en los metodos anteriores, aqui estamos enviado formData
    //con el archivo y el id
    //Enviando una peticion del tipo post, vamos a pasar la url a nuestro endpoint con el upload, y como segundo parametro el formdata
    // Como hemos cambiado la forma por el request --> return this.http.post(`${this.urlEndPoint}/upload`, formData).pipe(
      //De esta forma enviamos el formdata con los dos parametros, archivo e id
      //pero debemos manejar el observable con el cliente modificado, para ello debenos utilizar el operador pipe
     // map((response: any) => response.cliente as Cliente),
      //manejamos los errores, y con el map emitimos el response en forma de cliente,
      //pero debemos tener en cuenta que en el back la respuesta es un json con cliente, y un mensaje
      //por esta razon tenemos que acceder response.cliente
     // catchError(e => {
      //  console.error(e.error.mensaje);
     //   swal(e.error.mensaje, e.error.error, 'error');
     //   return throwError(e);
     // }
    //  )
  //  );



  }




}
