import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MystepperComponent } from './mystepper.component';

describe('MystepperComponent', () => {
  let component: MystepperComponent;
  let fixture: ComponentFixture<MystepperComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MystepperComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MystepperComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
