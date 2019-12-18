import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { SprintService } from 'src/app/services/sprint.service';
import { LoginService } from 'src/app/services/login.service';
import { Sprint } from 'src/app/models/sprint.model';
import Swal from 'sweetalert2';
import { NotifierService } from 'angular-notifier';

@Component({
  selector: 'app-list-sprint',
  templateUrl: './list-sprint.component.html',
  styleUrls: ['./list-sprint.component.scss']
})
export class ListSprintComponent implements OnInit {

  constructor(private router: Router, private sprintSrv: SprintService,
    private auth: LoginService, private notifier: NotifierService) { }
  private sprints: Sprint[] = [];
  private nameMaxLength: number = 30;
  private charsRemainingName: number = this.nameMaxLength;
  private newSprint: Sprint = new Sprint();
  private oldSprint: Sprint = new Sprint();


  ngOnInit() {
    if (this.auth.isAuthenticated()) {
      this.auth.setLogged(true);
    } else {
      this.auth.setLogged(false);
      this.auth.logout();
    }
    //console.log("init")
    this.newSprint.startDate = new Date();
    this.listSprints();
  }

  listSprints() {
    this.sprintSrv.getAllSprints()
      .subscribe(sprints => {
        console.log(sprints);
        this.sprints = sprints;
        console.log(this.sprints);
      }, err => {
        if (err.status == 401) {
          Swal.fire({
            allowOutsideClick: false,
            type: 'error',
            title: 'Oops...',
            text: 'Su token ha expirado!',
          }).then(result => {
            this.auth.logout();
            this.router.navigateByUrl('login');
          })
        } else {
          Swal.fire({
            allowOutsideClick: false,
            type: 'error',
            title: 'Oops...',
            text: 'Something went wrong!',
          })
            this.ngOnInit();

        }
      });
  }

  /*editSprint() {
    console.log(this.oldSprint);
    if (this.checkIfEmpty(this.oldSprint.name)) {
      Swal.fire({
        allowOutsideClick: false,
        type: 'error',
        title: 'Oops...',
        text: 'El nombre no puede estar vacio!',
      })
      this.oldSprint = new Sprint();
      return;
    }
    this.sprintSrv.editSprint(this.oldSprint)
      .subscribe(resp => {
        console.log(resp);
      }, err => {
        console.log(err);
        Swal.fire({
          allowOutsideClick: false,
          type: 'error',
          title: 'Oops...',
          text: 'Something went wrong! ' + err.error,
        })
      })
  }*/

  clearModal() {
    this.oldSprint = new Sprint();
    this.newSprint = new Sprint();
  }

  goToSprintLists(item) {
    // this.router.navigate(['manager', 'home'], { state: { data: item } })
    let url = "manager/" + item.id;
    this.router.navigateByUrl(url)
  }

  private computeCharsRemainingName() {
    this.charsRemainingName = this.nameMaxLength - this.newSprint.name.length;
  }

  checkIfEmpty(elem): boolean {
    return !elem || elem.trim().length == 0
  }

  createSprint() {

    if (this.checkIfEmpty(this.newSprint.name)) {
      Swal.fire({
        allowOutsideClick: false,
        type: 'error',
        title: 'Oops...',
        text: 'El nombre no puede estar vacio!',
      })
      this.newSprint = new Sprint();
      return;
    }
    this.sprintSrv.addSprint(this.newSprint)
      .subscribe(resp => {
        console.log(resp);
        this.newSprint = new Sprint();
        this.ngOnInit();
        this.notifier.notify("success", "Se creó el sprint " + this.newSprint.name);
      }, err => {
        console.log(err);
        if (err.status == 401) {
          Swal.fire({
            allowOutsideClick: false,
            type: 'error',
            title: 'Oops...',
            text: 'Su token ha expirado!',
          }).then(result => {
            this.notifier.notify("info", "Logging out. Su token ha expirado" );
            this.auth.logout();
            this.router.navigateByUrl('login');

          })
        } else {
          Swal.fire({
            allowOutsideClick: false,
            type: 'error',
            title: 'Oops...',
            text: 'Something went wrong!',
          })
        }
      })
  }

  openDeleteModal(sprint) {
    Swal.fire({
      allowOutsideClick: false,
      title: "Esta seguro de eliminar este elemento: " + sprint.name + "?",
      type: 'warning',
      text: "Esta acción no se puede revertir",
      showCancelButton: true,
      confirmButtonText: 'Si, Eliminar!',
      cancelButtonText: 'No, cancelar!',
      reverseButtons: true
    }).then((result) => {
      if (result.value) {
        Swal.fire(
          'Eliminado!',
          'Es sprint se eliminó.',
          'success'
        )
        this.sprintSrv.deleteSprint(sprint.id)
          .subscribe(resp => {
            console.log(resp);
            this.ngOnInit();
            this.notifier.notify("success", "Se eliminó el sprint " + sprint.name);
          }, err => {
            if (err.status == 401) {
              Swal.fire({
                allowOutsideClick: false,
                type: 'error',
                title: 'Oops...',
                text: 'Su token ha expirado!',
              }).then(result => {
                this.notifier.notify("info", "Logging out. Su token ha expirado" );
                this.auth.logout();
                this.router.navigateByUrl('login');
              })
            } else {
              Swal.fire({
                allowOutsideClick: false,
                type: 'error',
                title: 'Oops...',
                text: 'Something went wrong!',
              })
            }
          })

      } else if (
        /* Read more about handling dismissals below */
        result.dismiss === Swal.DismissReason.cancel
      ) {
        Swal.fire(
          'Cancelado',
          'Tu sprint esta a salvo ;)',
          'error'
        )
      }
    });
  }
}
