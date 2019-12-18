import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ListSprintComponent } from './list-sprint/list-sprint.component';



const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: ListSprintComponent },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class sprintRoutingModule { }


