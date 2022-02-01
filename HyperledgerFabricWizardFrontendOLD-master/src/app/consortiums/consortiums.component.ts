import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {Network} from '../_models/network';
import {Consortium} from '../_models/consortium';
import {Org} from '../_models/org';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-consortiums',
  templateUrl: './consortiums.component.html',
  styleUrls: ['./consortiums.component.scss']
})
export class ConsortiumsComponent implements OnInit {
  @Input() network: Network;

  form: FormGroup;
  orgs: Org[];
  consortiums: Consortium[];

  consortiumControl(i: number): string {
    return `consortium-${i}`;
  }

  nameControl(i?: number): string {
    const name = 'name';
    if (i !== undefined) {
      return `${this.consortiumControl(i)}.${name}`;
    }
    return name;
  }

  orgsControl(i?: number): string {
    const name = 'orgs';
    if (i !== undefined) {
      return `${this.consortiumControl(i)}.${name}`;
    }
    return name;
  }

  constructor(private formBuilder: FormBuilder) {
    this.form = this.formBuilder.group({});
  }

  addConsortium(): void {
    const length = this.consortiums.length;
    this.consortiums.push(new Consortium());
    this.addConsortiumControl(length);
  }

  private addConsortiumControl(i: number): void {
    const innerForm = this.formBuilder.group({});
    const consortiumName = this.formBuilder.control('', [Validators.required]);
    const consortiumOrgs = this.formBuilder.control('', [Validators.required]);
    innerForm.addControl(this.nameControl(), consortiumName);
    innerForm.addControl(this.orgsControl(), consortiumOrgs);
    this.form.addControl(this.consortiumControl(i), innerForm);
    consortiumName.valueChanges.subscribe(value => {
      this.consortiums[i].name = value?.trim();
    });
    consortiumOrgs.valueChanges.subscribe((values: string[]) => {
      if (values) {
        this.consortiums[i].orgs = this.orgs.filter(o => {
          return values.find(v => {
            return o.fullName === v;
          });
        });
      }
    });
  }

  removeConsortium(): void {
    const length = this.consortiums.length;
    if (length > 1) {
      this.form.removeControl(this.consortiumControl(length - 1));
      this.consortiums.pop();
    }
  }

  ngOnInit(): void {
    this.orgs = this.network.orgs;
    this.consortiums = this.network.consortiums;
    if (!this.consortiums) {
      this.consortiums = [];
      this.network.consortiums = this.consortiums;
    }
    if (this.consortiums.length > 0) {
      this.consortiums.forEach((consortium, index) => {
        this.addConsortiumControl(index);
        this.form.get(this.nameControl(index)).setValue(consortium.name);
        this.form.get(this.orgsControl(index)).setValue(consortium.orgs?.map(o => o.fullName));
      });
    } else {
      this.addConsortium();
    }
    this.form.setValidators((control: FormGroup) => {
      const consortiumNames = Object.values(control.controls).filter(c => {
        const nameControl = c.get(this.nameControl());
        return c instanceof FormGroup && nameControl != null && nameControl.valid;
      }).map(o => o.get(this.nameControl()).value.trim());
      const set = new Set(consortiumNames);
      if (consortiumNames.length > set.size) {
        return {error: true};
      }
      return null;
    });
  }

}
