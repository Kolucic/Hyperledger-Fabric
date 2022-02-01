import {Component, OnInit} from '@angular/core';
import {CdkStepper} from '@angular/cdk/stepper';

@Component({
  selector: 'app-mystepper',
  templateUrl: './mystepper.component.html',
  styleUrls: ['./mystepper.component.scss'],
  providers: [{provide: CdkStepper, useExisting: MystepperComponent}]
})
export class MystepperComponent extends CdkStepper implements OnInit {
  ngOnInit(): void {

  }

  onClick(index: number): void {
    if (this.selected && this.selected.stepControl) {
      this.selected.stepControl.markAllAsTouched();
    }
    this.selectedIndex = index;
  }

  next(): void {
    if (this.selected && this.selected.stepControl) {
      this.selected.stepControl.markAllAsTouched();
    }
    super.next();
  }

  previous(): void {
    if (this.selected && this.selected.stepControl) {
      this.selected.stepControl.markAllAsTouched();
    }
    super.previous();
  }
}
