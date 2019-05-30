import { LineaFactura } from "./linea-factura";
import { Cliente } from "../../clientes/cliente";

export class Factura {
    id: number;
    descripcion: string;
    observacion: string;
    lineas: Array<LineaFactura> = [];
    cliente: Cliente;
    total: number;
    createAt: string;

    calcularTotal(): number {
        this.total = 0;
        this.lineas.forEach((linea: LineaFactura) => {
            this.total = this.total + linea.calcularImporte();
        });
        return this.total;
    }
}
