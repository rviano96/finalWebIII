import { Component, OnInit, ViewChild } from '@angular/core';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { ListService } from '../services/list.service';
import { Task } from '../models/task.model';
import { TaskService } from '../services/task.service';
import Swal from 'sweetalert2';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginService } from '../services/login.service';
import { NotifierService } from "angular-notifier";
import { FormControl } from '@angular/forms';
import { DatePipe } from '@angular/common'
@Component({
  selector: 'app-task-manager',
  templateUrl: './task-manager.component.html',
  styleUrls: ['./task-manager.component.scss']
})
export class TaskManagerComponent implements OnInit {
  @ViewChild('updateTaskModal', { static: true }) updateTaskModal;
  notifier: NotifierService;
  todos: any[] = undefined;
  doing: any[] = undefined;// in progress
  done: any[] = undefined;
  backlog: any[] = undefined;
  waiting: any[] = undefined;
  backlogId = -1;
  todosId = -1;
  doingId = -1;
  doneId = -1;
  waitingId = -1;
  canLoad = true;
  listsMapper: Map<string, number> = new Map<string, number>();
  priorityMapper: Map<string, number> = new Map<string, number>();
  newTask: Task = new Task();
  private textAreaMaxLength: number = 300;
  private charsRemainingTextArea: number = this.textAreaMaxLength;
  private nameMaxLength: number = 30;
  private charsRemainingName: number = this.nameMaxLength;
  listName = "";
  sprintId = -1;
  private modification;
  private creation;
  date = new FormControl(new Date());
  private priorities: any = [
    { value: 0, label: 'Alta' },
    { value: 1, label: 'Media' },
    { value: 2, label: 'Baja' }];

  private selectedPriority: number = 0;

  constructor(private listSrv: ListService, private auth: LoginService, private router: Router,
    private taskSrv: TaskService, private route: ActivatedRoute, notifierSrv: NotifierService, private datepipe: DatePipe) {
    this.notifier = notifierSrv;
  }

  ngOnInit() {
    /**
     *  this.date=new Date();
 let latest_date =this.datepipe.transform(this.date, 'yyyy-MM-dd');
     */
    this.newTask.creation = new Date();
    this.newTask.modification = new Date();
    this.selectedPriority = 0;
    this.priorityMapper.set('alta', 0);
    this.priorityMapper.set('media', 1);
    this.priorityMapper.set('baja', 2);
    this.sprintId = Number(this.route.snapshot.paramMap.get('sprintId'));
    this.todos = undefined;
    this.doing = undefined;// in progress
    this.done = undefined;
    this.backlog = undefined;
    this.waiting = undefined;
    this.listName = "";

    this.listSrv.getList(this.sprintId)
      .subscribe((resp) => {
        if (this.listSrv.filter(resp, 'backlog')[0] != null) {
          this.backlog = this.listSrv.filter(resp, 'backlog')[0].task;
          this.listsMapper.set('backlog', this.listSrv.filter(resp, 'backlog')[0].id);
        }
        if (this.listSrv.filter(resp, 'todo')[0] != null) {
          this.todos = this.listSrv.filter(resp, 'todo')[0].task;
          this.listsMapper.set('todo', this.listSrv.filter(resp, 'todo')[0].id);
        }
        if (this.listSrv.filter(resp, 'in progress')[0] != null) {
          this.doing = this.listSrv.filter(resp, 'in progress')[0].task;
          this.listsMapper.set('inprogress', this.listSrv.filter(resp, 'in progress')[0].id);
        }
        if (this.listSrv.filter(resp, 'done')[0] != null) {
          this.done = this.listSrv.filter(resp, 'done')[0].task;
          this.listsMapper.set('done', this.listSrv.filter(resp, 'done')[0].id);
        }
        if (this.listSrv.filter(resp, 'waiting')[0] != null) {
          this.waiting = this.listSrv.filter(resp, 'waiting')[0].task;
          this.listsMapper.set('waiting', this.listSrv.filter(resp, 'waiting')[0].id);
        }
        this.canLoad = true;
      }, err => {
        console.log(err)
        let status = Math.trunc(err.status / 100);
        console.log(status)
        if (err.status == 401) {
          Swal.fire({
            allowOutsideClick: false,
            type: 'error',
            title: 'Oops...',
            text: 'Su token ha expirado!',
          }).then(result => {
            this.notifier.notify("info", "Logging out. Su token ha expirado");
            this.auth.logout();
            this.router.navigateByUrl('login');
          })
          return;
        }
        switch (status) {
          case 4:
            this.canLoad = false;
            Swal.fire({
              allowOutsideClick: false,
              type: 'error',
              title: 'Oops...',
              text: 'Something went wrong!',
            })
            break;
          default:
            this.canLoad = false;
            Swal.fire({
              allowOutsideClick: false,
              type: 'error',
              title: 'Oops...',
              text: 'Something went wrong!',
            }).then(result => {
              this.ngOnInit();
            })
        }

      })
  }

  drop(event: CdkDragDrop<string[]>) {

    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex);
      const todo = event.item.data;
      todo.state = event.container.element.nativeElement.classList[0];
      let listId = this.getListId(todo.state);
      this.taskSrv.moveTask(todo.id, listId).subscribe(resp => {
        console.log(resp);
        this.notifier.notify("success", "Se movió la tarea " + todo.name);
      }, err => {
        if (err.status == 401) {
          Swal.fire({
            allowOutsideClick: false,
            type: 'error',
            title: 'Oops...',
            text: 'Su token ha expirado!',
          }).then(result => {
            this.notifier.notify("info", "Logging out. Su token ha expirado");
            this.auth.logout();
            this.router.navigateByUrl('login');

          })
          return;
        }
        Swal.fire({
          allowOutsideClick: false,
          type: 'error',
          title: 'Oops...',
          text: 'Something went wrong!',
        })
      })
    }
  }

  getListId(key: string): number {
    console.log(this.listsMapper.get(key));
    return this.listsMapper.get(key);
  }

  getPriority(key: string): number {
    return this.priorityMapper.get(key);
  }

  private computeCharsRemainingTextArea() {
    this.charsRemainingTextArea = this.textAreaMaxLength - this.newTask.description.length;
  }

  private computeCharsRemainingName() {
    this.charsRemainingName = this.nameMaxLength - this.newTask.name.length;
  }

  createTask() {
    this.newTask.priority = this.priorities[this.selectedPriority].label
    this.newTask.listId = this.getListId("backlog");
    this.newTask.modification = this.newTask.creation;
    this.newTask.name = this.newTask.name.trim();
    console.log(this.newTask)
    if (this.newTask.name.length == 0) {
      console.log("error");
      Swal.fire({
        allowOutsideClick: false,
        type: 'error',
        title: 'Oops...',
        text: 'El nombre no puede estar vacio!',
      })

      return;
    }
    if(this.newTask.estimation < 0){
      Swal.fire({
        allowOutsideClick: false,
        type: 'error',
        title: 'Oops...',
        text: 'La estimacion debe ser mayor a 0!',
      })
      return;
    }

    this.taskSrv.addTask(this.newTask).subscribe(resp => {
      console.log(resp);
      this.ngOnInit();
      this.clearModal();
      this.notifier.notify("success", "Se creó la tarea " + this.newTask.name);
    }, err => {
      console.log(err);
      if (err.status == 401) {
        Swal.fire({
          allowOutsideClick: false,
          type: 'error',
          title: 'Oops...',
          text: 'Su token ha expirado!',
        }).then(result => {
          this.notifier.notify("info", "Logging out. Su token ha expirado");
          this.auth.logout();
          this.router.navigateByUrl('login');

        })
        return;
      }
      Swal.fire({
        allowOutsideClick: false,
        type: 'error',
        title: 'Oops...',
        text: 'Something went wrong!',
      })
    })
  }

  openUpdateModal(task, list) {
    this.newTask.creation = task.creation;
    this.newTask.modification = new Date();
    this.modification =  this.newTask.modification.toISOString().substr(0, 10);
    this.creation = task.creation;
    this.newTask.name = task.name;
    this.newTask.description = task.description;
    this.newTask.estimation = task.estimation;
    this.selectedPriority = this.getPriority(task.priority);
    this.newTask.priority = task.priority;
    this.newTask.id = task.id;
    this.newTask.listId = this.getListId(list);
    this.computeCharsRemainingTextArea();
    this.computeCharsRemainingName();
    this.updateTaskModal.show()
  }

  createList() {
    this.listName = this.listName.trim();
    if (this.listName.length == 0) {
      console.log("error");
      Swal.fire({
        allowOutsideClick: false,
        type: 'error',
        title: 'Oops...',
        text: 'El nombre no puede estar vacio!',
      })

      return;
    }
    this.listSrv.addList(this.listName, this.sprintId)
      .subscribe(resp => {
        console.log(resp);
        this.ngOnInit();
        this.notifier.notify("success", "Se creó la lista " + this.listName);
      }, err => {
        if (err.status == 401) {
          Swal.fire({
            allowOutsideClick: false,
            type: 'error',
            title: 'Oops...',
            text: 'Su token ha expirado!',
          }).then(result => {
            this.notifier.notify("info", "Logging out. Su token ha expirado");
            this.auth.logout();
            this.router.navigateByUrl('login');

          })
          return;
        }
        console.log(err);
        Swal.fire({
          allowOutsideClick: false,
          type: 'error',
          title: 'Oops...',
          text: err.error,
        })
      })

  }
  clearModal() {
    this.newTask.creation = null;
    this.newTask.modification = null;
    this.newTask.name = "";
    this.newTask.description = "";
    this.newTask.estimation = 0;
  }

  deleteList(list) {

    Swal.fire({
      allowOutsideClick: false,
      title: "Esta seguro de eliminar esta lista: " + list + "?",
      type: 'warning',
      text: "Esta acción no se puede revertir",
      showCancelButton: true,
      confirmButtonText: 'Si, Eliminar!',
      cancelButtonText: 'No, cancelar!',
      reverseButtons: true
    }).then((result) => {
      if (result.value) {
        this.listSrv.deleteList(this.getListId(list))
          .subscribe(resp => {
            console.log(resp);
            this.ngOnInit();
            this.notifier.notify("success", "Se eliminó la lista " + list);
            Swal.fire(
              'Eliminado!',
              'La lista se eliminó.',
              'success'
            )
          }, err => {
            if (err.status == 401) {
              Swal.fire({
                allowOutsideClick: false,
                type: 'error',
                title: 'Oops...',
                text: 'Su token ha expirado!',
              }).then(result => {
                this.notifier.notify("info", "Logging out. Su token ha expirado");
                this.auth.logout();
                this.router.navigateByUrl('login');

              })
              return;
            }
            Swal.fire({
              allowOutsideClick: false,
              type: 'error',
              title: 'Oops...',
              text: 'Something went wrong! ' + err.error,
            })
          })
      } else if (
        /* Read more about handling dismissals below */
        result.dismiss === Swal.DismissReason.cancel
      ) {
        Swal.fire(
          'Cancelado',
          'Tu lista esta a salvo :)',
          'error'
        )
      }
    });
  }

  openDeleteModal(todo) {
    Swal.fire({
      allowOutsideClick: false,
      title: "Esta seguro de eliminar este elemento: " + todo.name + "?",
      type: 'warning',
      text: "Esta acción no se puede revertir",
      showCancelButton: true,
      confirmButtonText: 'Si, Eliminar!',
      cancelButtonText: 'No, cancelar!',
      reverseButtons: true
    }).then((result) => {
      if (result.value) {
        this.taskSrv.deleteTask(todo.id)
          .subscribe(resp => {
            Swal.fire(
              'Eliminado!',
              'La tarea se eliminó.',
              'success'
            )
            console.log(resp);
            this.notifier.notify("success", "Se eliminó la tarea " + todo.name);
            this.ngOnInit();
          }, err => {
            if (err.status == 401) {
              Swal.fire({
                allowOutsideClick: false,
                type: 'error',
                title: 'Oops...',
                text: 'Su token ha expirado!',
              }).then(result => {
                this.notifier.notify("info", "Logging out. Su token ha expirado");
                this.auth.logout();
                this.router.navigateByUrl('login');

              })
              return;
            }
            Swal.fire({
              allowOutsideClick: false,
              type: 'error',
              title: 'Oops...',
              text: 'Something went wrong! ' + err.error,
            })
          })
      } else if (
        /* Read more about handling dismissals below */
        result.dismiss === Swal.DismissReason.cancel
      ) {
        Swal.fire(
          'Cancelado',
          'Tu tarea esta a salvo :)',
          'error'
        )
      }
    });
  }

  updateTask() {
    //console.log(this.newTask);
    this.newTask.priority = this.priorities[this.selectedPriority].label;
    this.newTask.name = this.newTask.name.trim();
    if (this.newTask.name.length == 0) {
      console.log("error");
      Swal.fire({
        allowOutsideClick: false,
        type: 'error',
        title: 'Oops...',
        text: 'El nombre no puede estar vacio!',
      })

      return;
    }
    if(this.newTask.estimation < 0){
      Swal.fire({
        allowOutsideClick: false,
        type: 'error',
        title: 'Oops...',
        text: 'La estimacion debe ser mayor a 0!',
      })
      return;
    }
    this.taskSrv.editTask(this.newTask).subscribe(resp => {
      console.log(resp);
      this.ngOnInit();
      this.clearModal();
      this.notifier.notify("success", "Se modificó la tarea " + this.newTask.name);
    }, err => {
      if (err.status == 401) {
        Swal.fire({
          allowOutsideClick: false,
          type: 'error',
          title: 'Oops...',
          text: 'Su token ha expirado!',
        }).then(result => {
          this.notifier.notify("info", "Logging out. Su token ha expirado");
          this.auth.logout();
          this.router.navigateByUrl('login');

        })
        return;
      }
      Swal.fire({
        allowOutsideClick: false,
        type: 'error',
        title: 'Oops...',
        text: 'Something went wrong! ' + err.error,
      })
    })
  }
}
