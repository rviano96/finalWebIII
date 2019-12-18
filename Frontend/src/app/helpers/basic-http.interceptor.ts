import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { LoginService } from '../services/login.service';

@Injectable()
export class BasicHttpInterceptor implements HttpInterceptor {

  constructor(public authService: LoginService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    //console.log("url", request.url)
    //console.log("request", request)
    if (request.url !== environment.urlApiBase + `dologin`) {
      request = request.clone({
        setHeaders: {
            'Authorization': "Bearer " + localStorage.getItem('token')
        }
    });
    }
    //console.log("request 2 ", request)
    return next.handle(request);
  }
}
