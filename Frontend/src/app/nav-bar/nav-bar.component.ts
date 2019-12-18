import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { LoginService } from '../services/login.service';
import Swal from 'sweetalert2';
import { NotifierService } from 'angular-notifier';


@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.scss']
})
export class NavBarComponent implements OnInit {

  public user: any = '';
  oldPassword = "";
  newPassword = ""
  constructor(private router: Router, private authService: LoginService, private notifier: NotifierService) { }

  ngOnInit() {
    if (localStorage.getItem('user')) {
      //this.user = localStorage.getItem('user').charAt(0).toUpperCase() + localStorage.getItem('user').slice(1).toLowerCase();
    }
    console.log(this.user);
    this.authService.isLogged.subscribe(
      data => {
        //console.log(data);
        if (!data.logged)
          this.logout();
      },
      error => {
        console.log(error);
      }
    );
  }

  clearModal() {
    this.oldPassword = "";
    this.newPassword = "";
  }

  changePassword() {
    if (this.isEmpty(this.oldPassword) || this.isEmpty(this.newPassword)) {
      Swal.fire({
        allowOutsideClick: false,
        type: 'error',
        title: 'Oops...',
        text: 'El password no puede estar vacio!',
      })
      this.clearModal();
      return;
    }
    this.authService.changeUserData(this.oldPassword, this.newPassword, "password")
      .subscribe(resp => {
        console.log(resp)
        this.notifier.notify("succes", "Password cambiado correctamente" );
      }, err => {
        if (err.status == 401) {
          Swal.fire({
            allowOutsideClick: false,
            type: 'error',
            title: 'Oops...',
            text: 'Su token ha expirado!',
          }).then(result => {
            this.logout();
           // this.router.navigateByUrl('login');
            this.notifier.notify("info", "Logging out. Su token ha expirado" );
          })
        } else {
          Swal.fire({
            allowOutsideClick: false,
            type: 'error',
            title: 'Oops...',
            text: err.error,
          })
          this.clearModal();
        }


      })
  }
  isEmpty(elem) {
    return !elem || elem.trim().length == 0;
  }

  navigateTo(destination) {
    //this.sharedService.navigateTo(destination);
  }
  navigateHome() {
    this.router.navigateByUrl('sprint/home')
  }


  logout() {
    this.authService.logout();

    //this.router.navigate(['login']);
    this.router.navigateByUrl('login');
  }

}
