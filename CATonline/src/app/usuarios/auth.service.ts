import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Usuario } from './usuario';
import { Token } from '@angular/compiler';
/* Esta clase service, tiene toda la logica de la seguridad */
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private _usuario: Usuario;//_ porque a parte de ser privado va a tener un metodo accesor
  private _token: string;//metodo getter si es distinto de nulo,
  constructor(private http: HttpClient) { }

  /**Metodos get y set siempre publicos
   */

  public get usuario(): Usuario {
    if (this._usuario != null) {
      return this._usuario;
    } else if (this._usuario == null && sessionStorage.getItem('usuario') != null) {
      this._usuario = JSON.parse(sessionStorage.getItem('usuario')) as Usuario;
      return this._usuario;
    }
    return new Usuario();
    //si no existe el usuario devolvemos un usuario vacio
  }

  public get token(): string {
    if (this._token != null) {
      return this._token;
    } else if (this._token == null && sessionStorage.getItem('token') != null) {
      this._token = sessionStorage.getItem('token');
      return this._token;
    }
    return null;
  }
  //metodo para limitar la vision de los botones (si tiene el role X lo vera o no)

  hasRole(role: string): boolean {
    if (this.usuario.roles.includes(role)) {
      return true;
    }
    return false;
  }


  /*El método login retornara la respuesta, que contiene los datos de acceso, necesitamos el httpclient porque vamos a retornar una respuesta
 en el post requerimos una url, en la cual enviamos los datos para autenticarnos.
  */
  login(usuario: Usuario): Observable<any> {
    const urlEndpoint = 'http://localhost:8080/oauth/token';
    const credenciales = btoa('angularapp' + ':' + '12345');
    const httpHeaders = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded',
      'Authorization': 'Basic ' + credenciales
    });
    let params = new URLSearchParams();
    params.set('grant_type', 'password');
    params.set('username', usuario.username);
    params.set('password', usuario.password);
    /*el endpoint apunta a la ruta de spring para los token.
    los credenciales contiene el cliente con la autentificacion encriptada en base 64.
    Y la cabecera es importante que sea del tipo  application/x-www-form-urlencoded, y el ultimo atributo define el tipo de autentificacion.
    El objeto params almacena los parametros que le indiquemos*/
    console.log(params.toString());

    return this.http.post<any>(urlEndpoint, params.toString(), { headers: httpHeaders });

  }
  /*_ porque a parte de ser privado va a tener un metodo accesor */
  guardarUsuario(accessToken: string): void {
    let payload = this.obtenerDatosToken(accessToken);
    this._usuario = new Usuario();//creamos instancia del objeto usuario y le pasamos los datos a traves del payload.
    this._usuario.nombre = payload.nombre;
    this._usuario.apellido = payload.apellido;
    this._usuario.email = payload.email;
    this._usuario.username = payload.user_name;//nombre de spring
    this._usuario.roles = payload.authorities;//nombre de spring
    sessionStorage.setItem('usuario', JSON.stringify(this._usuario))//nos permite guardar datos en la sesion del ordenador
    //con sessionStorage podemos guardar la sesion, pero el valor es un string no un objeto
  };

  guardarToken(accessToken: string): void {
    /* Muy parecido el metodo anterior, solo que el token equivale al accessToken y lo guardamos en el sessionStorage */
    this._token = accessToken;
    sessionStorage.setItem('token', accessToken)
  };
  /* Este metodo nos permite obtener los datos en estructura JSON */
  obtenerDatosToken(accessToken: string): any {
    if (accessToken != null) {
      return JSON.parse(atob(accessToken.split(".")[1]));
    }
    return null;
  }
//comprobar que el usuario ya se ha logeado
  isAuthenticated():boolean{
    let payload = this.obtenerDatosToken(this.token);//obtenermos el token desde el metodo get
    if(payload != null && payload.user_name && payload.user_name.length >0) {
      return true;
    }
    return false;
  }
  logOut():void{
    this._token=null;
    this._usuario=null;
    sessionStorage.clear();//borramos todas las variables de sesion
    /* Tenemos la opción de borrar las variables por separado en el caso de que lo necesitaramos así:
          sessionStorage.removeItem('usuario'); */
  }


}
