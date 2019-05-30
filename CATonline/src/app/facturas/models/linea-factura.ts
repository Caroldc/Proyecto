import { Producto } from "./producto";

export class LineaFactura {
    producto:Producto;
    cantidad:number = 1;
    importe:number;

/*este metodo es necesario para crear las facturas en el form, 
ya que el metodo que tenemos creado en el backend es para consultar, no para insertar */
    public calcularImporte():number{
        return this.cantidad * this.producto.precio;
    }
}
