import { Injectable } from '@angular/core';
import{HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs';
import { Factura } from '../models/factura';
import { Producto } from '../models/producto';

@Injectable({
  providedIn: 'root'
})
export class FacturasService {
private urlEndPoint: string ='http://localhost:8080/api/facturas';

  constructor(private http: HttpClient) { }
  getFactura(id:number): Observable<Factura>{
    return this.http.get<Factura>(`${this.urlEndPoint}/${id}`);
  }
  delete(id:number):Observable<void>{
    return this.http.delete<void>(`${this.urlEndPoint}/${id}`);
  }
  filtrarProductos(texto:string):Observable<Producto[]>{
    //metodo en el backend que devuelve los productos buscados por el nombre(@GetMapping)
    //http://localhost:8080/api/facturas/filtrar-productos/nombreproducto
    return this.http.get<Producto[]>(`${this.urlEndPoint}/filtrar-productos/${texto}`);

  }
  /*Para utilizar el metodo create, accedemos al endPoint y le pasamos
  la factura */
  create (factura: Factura): Observable<Factura>{
    return this.http.post<Factura>(this.urlEndPoint, factura);
  }
}
