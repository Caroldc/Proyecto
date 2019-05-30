import { Component } from "@angular/core";
import { AuthService } from "../usuarios/auth.service";
import { Router } from "@angular/router";
import swal from 'sweetalert2';
@Component({
    selector:'app-header',
    templateUrl:'header.component.html'
})
export class HeaderComponent{
    title:string ='CATonline';



    constructor(public authService: AuthService, private router: Router){}

    logOut():void{
        swal('Logout',`Has cerrado correctamente la sesion ${this.authService.usuario.username}`,'success');
        this.authService.logOut();       
        this.router.navigate(['/login']);
    }




}