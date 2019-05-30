import { Injectable } from '@angular/core';
import {
  HttpEvent, HttpInterceptor, HttpHandler, HttpRequest
} from '@angular/common/http';

import { Observable, throwError } from 'rxjs';
import { AuthService } from '../auth.service';
import swal from 'sweetalert2';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
/** Pass untouched request through to the next request handler. */
@Injectable()
export class AuthInterceptor implements HttpInterceptor {


  constructor(private authService: AuthService, private router: Router) { }

  intercept(req: HttpRequest<any>, next: HttpHandler):
    Observable<HttpEvent<any>> {

  
    return next.handle(req).pipe(
      catchError(e =>{
          //el error 401 es no autorizado y el 403 es Prohibido
          if (e.status==401){
            if(this.authService.isAuthenticated()){
              this.authService.logOut();
              /*Es importante controlar por el lado de angular la posibilidad de que el token expire */
      
            }
            this.router.navigate(['/login']);
           
          }
          if ( e.status==403){
            swal('Acceso denegado',`${this.authService.usuario.username} no tienes acceso!`,'warning');
            this.router.navigate(['//clientes']);//cuando no tenga permisos
           
          }
         return throwError(e);
      })
    );
  }
}