import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router:Router){}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
      if(this.authService.isAuthenticated()){
        if(this.tokenExpirado()){
          this.authService.logOut();
          this.router.navigate(['/login']);
          return false;

        }
        return true;
      }
      this.router.navigate(['/login']);
    return false;
  }

  tokenExpirado():boolean{
    let token = this.authService.token;
    let payload = this.authService.obtenerDatosToken(token);
    let now = new Date().getTime()/1000;//fecha actual en segundos
    if(payload.exp<now){//atributo de la fecha cuando expira el token
      return true;
    }
    return false;
  }

}
