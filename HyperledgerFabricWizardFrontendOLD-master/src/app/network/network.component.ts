import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Org} from '../_models/org';
import {Network} from '../_models/network';

@Component({
  selector: 'app-network',
  templateUrl: './network.component.html',
  styleUrls: ['./network.component.scss']
})
export class NetworkComponent implements OnInit {
  form: FormGroup;
  networkName: FormControl;

  @Input() network: Network;
  orgs: Org[];

  constructor(private formBuilder: FormBuilder) {
    this.networkName = this.formBuilder.control('', [Validators.required, Validators.pattern('^\\S+$')]);
    this.networkName.valueChanges.subscribe(e => {
      this.network.name = e?.trim();
    });

    this.form = this.formBuilder.group({
      network_name: this.networkName
    });
  }

  orgControl(i: number): string {
    return `org-${i}`;
  }

  nameControl(i?: number): string {
    if (i === undefined) {
      return 'name';
    } else {
      return this.orgControl(i) + '.name';
    }
  }

  domainControl(i?: number): string {
    if (i === undefined) {
      return 'domain';
    } else {
      return this.orgControl(i) + '.domain';
    }
  }

  addOrg(): void {
    const length = this.orgs.length;
    this.orgs.push(new Org());
    this.addOrgControl(length);
  }

  private addOrgControl(i: number): FormGroup {
    const innerForm = this.formBuilder.group({});
    const nameControl = this.formBuilder.control('', [Validators.required, Validators.pattern('^\\S+$')]);
    const domainControl = this.formBuilder.control('', [Validators.required, Validators.pattern('^\\S+$')]);
    innerForm.addControl(this.nameControl(), nameControl);
    innerForm.addControl(this.domainControl(), domainControl);
    nameControl.valueChanges.subscribe(value => {
      this.orgs[i].name = value?.trim();
    });
    domainControl.valueChanges.subscribe(value => {
      this.orgs[i].domain = value?.trim();
    });
    innerForm.valueChanges.subscribe(value => {
      if (value) {
        this.orgs[i].fullName = Object.values(value).join('.');
      }
    });
    this.form.addControl(this.orgControl(i), innerForm);
    return innerForm;
  }

  removeOrg(): void {
    const length = this.orgs.length;
    if (length > 2) {
      this.form.removeControl(this.orgControl(length - 1));
      this.orgs.pop();
    }
  }

  invalidNetworkName(): string {
    if (this.networkName.invalid && (this.networkName.value as string).length > 0) {
      return 'Il nome della rete non può contenere spazi';
    } else {
      return '';
    }
  }

  ngOnInit(): void {
    if (this.network.name) {
      this.networkName.setValue(this.network.name);
    }
    if (!this.network.orgs) {
      this.network.orgs = [];
    }
    this.orgs = this.network.orgs;
    if (this.orgs.length > 0) {
      this.orgs.forEach((org, index) => {
        const form = this.addOrgControl(index);
        form.get(this.nameControl()).setValue(org.name);
        form.get(this.domainControl()).setValue(org.domain);
      });
    } else {
      this.addOrg();
      this.addOrg();
    }

    this.form.setValidators((control: FormGroup) => {
      const fullNames = Object.values(control.controls).filter(c => {
        return c instanceof FormGroup && c.valid;
      }).map(o => Object.values(o.value).join('.'));
      const d = new Set(fullNames);
      if (d.size < fullNames.length) {
        return {error: true};
      }
      return null;
    });
  }
}
