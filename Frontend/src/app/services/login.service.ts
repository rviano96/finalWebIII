import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { HttpParams, HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { map, catchError } from 'rxjs/operators';
@Injectable({
  providedIn: 'root'
})
export class LoginService {

  userToken = '';
  public isLogged: Subject<any> = new Subject<any>();
  private logged: false;
  constructor(private http: HttpClient) { }

  login(username, password): Observable<any> {
    const body = new HttpParams()
      .set('username', username)
      .set('password', password);

    return this.http.post(environment.urlApiBase + `dologin`,
      body.toString(),
      {
        headers: new HttpHeaders()
          .set('Content-Type', 'application/x-www-form-urlencoded')
      }
    ).pipe(map(response=>{
       // store user details and basic auth credentials in local storage to keep user logged in between page refreshes
                // user.authdata = window.btoa(username + ':' + password);
               // this.currentUserSubject.next(response);
                this.saveToken(response['jwtToken']);
                localStorage.setItem('userData', JSON.stringify(response))
                localStorage.setItem('roles', response['roles']);
                localStorage.setItem('actualRole', response['roles'][0]);
                localStorage.setItem('username', response['username'])
                //localStorage.setItem('username', response['username'])
                console.log('users: ', response);
                return response;
    }));
  }

  changeUserData(oldPassword, newPassword, field):Observable<any>{
    const data = {
        newValue: newPassword,
        password: oldPassword
    }
    let username = localStorage.getItem("username");
    //username
    return this.http.put<any>(environment.urlApiRest +`usuarios/${username}/${field}`, data )
    .pipe(map( resp =>{
      return resp;
    }))
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('roles');
    localStorage.removeItem('expires');
    localStorage.removeItem('actualRole');
    localStorage.removeItem('userData');
    localStorage.removeItem('username');
    this.setLogged(false);
  }
  saveToken(tokenId: string) {
    localStorage.setItem('token', tokenId);
    const today = new Date();
    today.setSeconds( 3600 * 24 ); // 24 hs
    localStorage.setItem('expires', today.getTime().toString());
  }

  readToken() {
    if (!!localStorage.getItem('token')) {
      this.userToken = localStorage.getItem('token');
    } else {
      this.userToken = '';
    }

    return this.userToken;
  }

  isAuthenticated(): boolean {
    if (localStorage.getItem('token') == null ) {
      console.log('not authenticated');
      return false;
    }

    const expires = Number(localStorage.getItem('expires'));
    // console.log(expires);
    const expirationDate = new Date ();
    expirationDate.setTime(expires);

    if ( expirationDate > new Date() ) {
      // console.log("authenticated");
      return true;
    } else {
      console.log('not authenticated');
      return false;
    }

  }
  setLogged(logged) {
    this.logged = logged;

    setTimeout(() => {
      this.refreshAll();
    }, 0);
  }

  public refreshAll() {
    this.isLogged.next({ logged: this.logged });
  }

}
