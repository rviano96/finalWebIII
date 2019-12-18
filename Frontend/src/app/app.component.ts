import { Component, OnInit } from '@angular/core';
import { LoginService } from './services/login.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'productosFrontend';
  logged = false;

  constructor(private authService: LoginService) {
    if (this.authService.isAuthenticated()) {
      this.authService.setLogged(true);
    } else {
      this.authService.setLogged(false);
    }
  }

  ngOnInit() {
    this.authService.isLogged.subscribe(
      data => {
        this.logged = data.logged === undefined ? false : data.logged;
      },
      error => {
        console.log(error);
      }
    );
  }
}
