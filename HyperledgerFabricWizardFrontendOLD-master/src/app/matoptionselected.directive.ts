import {Directive, Input, OnChanges, SimpleChanges} from '@angular/core';
import {MatOption} from '@angular/material/core';

@Directive({
  // tslint:disable-next-line:directive-selector
  selector: '[matoptionselected]'
})
export class MatoptionselectedDirective implements OnChanges {
  @Input() selected: boolean;

  constructor(private option: MatOption) {
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.selected.currentValue) {
      this.option.select();
    } else {
      this.option.deselect();
    }
  }
}
