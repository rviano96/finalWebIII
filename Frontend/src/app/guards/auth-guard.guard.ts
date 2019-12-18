import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, CanActivate, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { LoginService } from '../services/login.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardGuard implements  CanActivate {

  constructor(private auth: LoginService,
    private router: Router) {
  }
  canActivate(): boolean {

    if (this.auth.isAuthenticated()) {
      return true;
    } else {
      this.router.navigate(['login']);
      // se puede guardar el url aca y cuando el login sea exitoso redireccionarlo
      // nuevamente a ese url.
      console.log('Guard');
      return false;
    }
  }
}
