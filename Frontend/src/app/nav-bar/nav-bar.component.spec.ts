import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NavBarComponent } from './nav-bar.component';
import { NavbarModule, DropdownModule, MDBBootstrapModule } from 'angular-bootstrap-md';
import { RouterModule, Router } from '@angular/router';
import { RouterLinkDirectiveStub } from 'src/testing/router-link-directive-stub';
import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
let mockRouter:any;
    class MockRouter {
        //noinspection TypeScriptUnresolvedFunction
        navigate = jasmine.createSpy('navigate');
    }

describe('NavBarComponent', () => {
  let component: NavBarComponent;
  let fixture: ComponentFixture<NavBarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        NavbarModule,
        DropdownModule.forRoot(),
        MDBBootstrapModule.forRoot(),
        RouterTestingModule,
        HttpClientModule,
        HttpClientTestingModule
      ],
      declarations: [
        NavBarComponent
      ]
    }).overrideModule(NavbarModule, {
      remove: {
          imports: [ RouterModule ],
      },
      add: {
          declarations: [ RouterLinkDirectiveStub ],
          providers: [ { provide: Router, useValue: mockRouter } ],
    }}).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NavBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
