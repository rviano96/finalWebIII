import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';

import { NavBarComponent } from './nav-bar/nav-bar.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AngularFontAwesomeModule } from 'angular-font-awesome';
import { SidebarModule } from 'ng-sidebar';
import { BasicHttpInterceptor } from './helpers/basic-http.interceptor';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { NavbarModule, DropdownModule, MDBBootstrapModule, IconsModule } from 'angular-bootstrap-md';
import { CommonModule } from '@angular/common';
import { TaskManagerComponent } from './task-manager/task-manager.component';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { NotifierModule, NotifierOptions } from 'angular-notifier';
import { DatePipe } from '@angular/common'
/**
 * Custom angular notifier options
 */
const customNotifierOptions: NotifierOptions = {
  position: {
		horizontal: {
			position: 'left',
			distance: 12
		},
		vertical: {
			position: 'bottom',
			distance: 12,
			gap: 10
		}
	},
  theme: 'material',
  behaviour: {
    autoHide: 5000,
    onClick: 'hide',
    onMouseover: 'pauseAutoHide',
    showDismissButton: true,
    stacking: 4
  },
  animations: {
    enabled: true,
    show: {
      preset: 'slide',
      speed: 300,
      easing: 'ease'
    },
    hide: {
      preset: 'fade',
      speed: 300,
      easing: 'ease',
      offset: 50
    },
    shift: {
      speed: 300,
      easing: 'ease'
    },
    overlap: 150
  }
};
@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    NavBarComponent,
    TaskManagerComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    AngularFontAwesomeModule,
    BrowserAnimationsModule,
    CommonModule,
    NavbarModule,
    DropdownModule.forRoot(),
    MDBBootstrapModule.forRoot(),
    FormsModule,
    ReactiveFormsModule ,
    SidebarModule.forRoot(),
    IconsModule,
    NgbModule,
    DragDropModule,
    NotifierModule.withConfig(customNotifierOptions),

  ],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: BasicHttpInterceptor, multi: true },DatePipe],
  bootstrap: [AppComponent]
})
export class AppModule { }
