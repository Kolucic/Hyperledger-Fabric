import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsortiumsComponent } from './consortiums.component';

describe('ConsortiumsComponent', () => {
  let component: ConsortiumsComponent;
  let fixture: ComponentFixture<ConsortiumsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConsortiumsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConsortiumsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
