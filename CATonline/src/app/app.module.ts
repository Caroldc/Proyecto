/* Tenemos que agregar el modulo http client, que nos permite
 en nuestra clase service poder conectarnos con el servidor */
import { HttpClientModule, HTTP_INTERCEPTORS } from "@angular/common/http";
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HeaderComponent } from './header/header.component';
import { AppComponent } from './app.component';
import { FooterComponent } from './footer/footer.component';
import { DirectivaComponent } from './directiva/directiva.component';
import { ClientesComponent } from './clientes/clientes.component';
import { ClienteService } from "./clientes/cliente.service";
import { RouterModule, Routes } from "@angular/router";
import { FormComponent } from './clientes/form.component';
import { FormsModule,ReactiveFormsModule } from "@angular/forms";
import { registerLocaleData } from "@angular/common";
import localeES from '@angular/common/locales/es';
import { PaginatorComponent } from './paginator/paginator.component';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { MatNativeDateModule, MatDatepickerModule } from "@angular/material";
import { DetalleComponent } from './clientes/detalle/detalle.component';
import { LoginComponent } from './usuarios/login.component';
import { AuthGuard } from "./usuarios/guards/auth.guard";
import { RoleGuard } from "./usuarios/guards/role.guard";
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';


/*arreglo de rutas, de todos los componentes.
El path contiene una url y lo mapeamos a un componente.
El path vacío es nuestro home*/
registerLocaleData(localeES, 'es');
import { from } from "rxjs";
import { TokenInterceptor } from "./usuarios/interceptors/token.interceptor";
import { AuthInterceptor } from "./usuarios/interceptors/auth.interceptor";
import { DetalleFacturaComponent } from './facturas/detalle-factura.component';
import { FacturasComponent } from './facturas/facturas.component';
const ROUTES: Routes = [
  { path: '', redirectTo: '/clientes', pathMatch: 'full' },
  { path: 'directivas', component: DirectivaComponent },
  { path: 'clientes', component: ClientesComponent },
  //para indicar que vamos a pasar un parametro para la ruta lo hacemos con :parametro
  { path: 'clientes/page/:page', component: ClientesComponent },
  { path: 'clientes/form', component: FormComponent,/*canActivate:[AuthGuard, RoleGuard],*/data:{role: 'ROLE_ADMIN'} },//podemos tener varios guards para una ruta
  { path: 'clientes/form/:id', component: FormComponent,/*canActivate:[AuthGuard, RoleGuard],*/data:{role: 'ROLE_ADMIN'} },
  { path: 'login', component: LoginComponent },
  { path: 'facturas/:id', component: DetalleFacturaComponent,canActivate: [AuthGuard, RoleGuard], data: { role: 'ROLE_USER' }},
  //importantisimo los dos puntos para las variables en las rutas
  { path: 'facturas/form/:clienteId', component: FacturasComponent, canActivate: [AuthGuard, RoleGuard], data: { role: 'ROLE_USER' } },
];
@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    DirectivaComponent,
    ClientesComponent,
    FormComponent,    
    PaginatorComponent, 
    DetalleComponent, 
    LoginComponent,
    DetalleFacturaComponent,
    FacturasComponent

  ],
  imports: [
    BrowserModule,
    /*Debemos pasar todas las rutas al import */
    HttpClientModule,
    FormsModule,
    RouterModule.forRoot(ROUTES),
    BrowserAnimationsModule,
    MatNativeDateModule,
    MatDatepickerModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatAutocompleteModule
  ],
  providers: [ClienteService,
  { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true },
  { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },],
  bootstrap: [AppComponent]
})
export class AppModule { }
