import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';

import { TaskManagerComponent } from './task-manager/task-manager.component';
import { AuthGuardGuard } from './guards/auth-guard.guard';


const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },

  { path: 'manager/:sprintId', component: TaskManagerComponent, canActivate: [AuthGuardGuard] },
  {
    path: 'sprint',
    loadChildren: './sprint-module/sprint-module.module#sprintModule',
    canActivate: [AuthGuardGuard]
  },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
