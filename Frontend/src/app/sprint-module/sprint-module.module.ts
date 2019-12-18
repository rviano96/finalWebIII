import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

// TERCEROS
import { MDBBootstrapModule } from 'angular-bootstrap-md';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AngularFontAwesomeModule } from 'angular-font-awesome';
import { SidebarModule } from 'ng-sidebar';
import { MatCardModule } from '@angular/material';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
// PROPIAS
import { sprintRoutingModule } from './sprint-module-routing.module';
import { ListSprintComponent } from './list-sprint/list-sprint.component';



@NgModule({
  declarations: [
    ListSprintComponent

  ],
  imports: [
    CommonModule,
    sprintRoutingModule,
    MDBBootstrapModule,
    SidebarModule.forRoot(),
    MatCardModule,
    MatDividerModule,
    MatIconModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class sprintModule {}



