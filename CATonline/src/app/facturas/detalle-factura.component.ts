import { Component, OnInit } from '@angular/core';
import { Factura } from './models/factura';
import { FacturasService } from './services/facturas.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-detalle-factura',
  templateUrl: './detalle-factura.component.html'
})
export class DetalleFacturaComponent implements OnInit {

  factura:Factura;
  titulo:string ='Factura';
  constructor(private facturasService:FacturasService, private activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(params => {
      let id = +params.get('id');//con mas lo convertimos en tipo number
      this.facturasService.getFactura(id).subscribe(factura => this.factura = factura)
    });
  }

}
