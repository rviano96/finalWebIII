import { Component, OnInit } from '@angular/core';
import { User } from '../models/user.model';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { LoginService } from '../services/login.service';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  user: User = new User();
  rememberMe = false;


  constructor( private auth: LoginService,
               private router: Router) { }

  ngOnInit() {
    //this.nav.hide();
    if (this.auth.isAuthenticated() ) {
     // console.log("logged")
      this.router.navigate(['sprint','home'])
      return;
    }
  }

  login() {

    //console.log(form);
    //if ( form.invalid ) { return; }
    Swal.fire({
      allowOutsideClick: false,
      type: 'info',
      text: 'Wait please...'
    });
    Swal.showLoading();
    //console.log(this.user.username, this.user.password)
    this.auth.login(this.user.username, this.user.password).subscribe( resp => {

      // Correct Login
      Swal.close();
      /*if ( this.rememberMe ) {
        localStorage.setItem( 'username', this.user.username);
      }*/
      // console.log(resp)
      this.router.navigate(['sprint','home'])

    }, (err) => {
      console.log(err)
      Swal.fire({
        type: 'error',
        title: 'Authentication error',
        text: 'Invalid username or password '
      });
    });
  }

}
