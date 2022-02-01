import {Component, Input, OnInit, OnDestroy} from '@angular/core';
import {Org} from '../_models/org';
import {Client, Entity, Orderer, Peer, Type} from '../_models/entity';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Ca} from '../_models/ca';
import {states} from '../_models/states';
import {CdkStepper} from '@angular/cdk/stepper';


@Component({
  selector: 'app-organization',
  templateUrl: './organization.component.html',
  styleUrls: ['./organization.component.scss']
})
export class OrganizationComponent implements OnInit, OnDestroy {
  @Input() rootStepper: CdkStepper;
  @Input() parentForm: FormGroup;
  @Input() last: boolean;
  @Input() i: number;
  form: FormGroup;
  hide = true;
  caName: FormControl;
  caUsername: FormControl;
  caPassword: FormControl;
  caURL: FormControl;
  caPort: FormControl;
  caState: FormControl;

  constructor(private formBuilder: FormBuilder) {
    this.caName = this.formBuilder.control('', [Validators.required]);
    this.caUsername = this.formBuilder.control('', [Validators.required]);
    this.caPassword = this.formBuilder.control('', [Validators.required]);
    this.caURL = this.formBuilder.control('', [Validators.required]);
    this.caPort = this.formBuilder.control('', [Validators.required]);

    this.caState = this.formBuilder.control('', []);
    this.caState.valueChanges.subscribe((value: string) => {
      if (!value) {
        return;
      }
      this.ca.state = states.find(s => s.code === value);
    });

    this.form = this.formBuilder.group({
      ca: this.formBuilder.group({
        caName: this.caName,
        caUsername: this.caUsername,
        caPassword: this.caPassword,
        caURL: this.caURL,
        caPort: this.caPort,
        caState: this.caState
      }),
    });

    this.caName.valueChanges.subscribe(n => {
      this.ca.name = n?.trim();
    });
    this.caUsername.valueChanges.subscribe(n => {
      this.ca.username = n?.trim();
    });
    this.caPassword.valueChanges.subscribe(n => {
      this.ca.password = n?.trim();
    });
    this.caURL.valueChanges.subscribe(n => {
      this.ca.url = n?.trim();
    });
    this.caPort.valueChanges.subscribe(n => {
      this.ca.port = n;
    });
  }

  memberTypologies = Object.keys(Type).filter(i => isNaN(Number(i)));

  @Input() org: Org;
  ca: Ca;
  entities: Entity[];

  states = states;

  memberControl(i: number): string {
    return `member-${i}`;
  }

  stateControl(i?: number): string {
    const name = 'state';
    if (i === undefined) {
      return name;
    } else {
      return this.memberControl(i) + `.${name}`;
    }
  }

  nameControl(i?: number): string {
    const name = 'name';
    if (i === undefined) {
      return name;
    } else {
      return this.memberControl(i) + `.${name}`;
    }
  }

  typeControl(i?: number): string {
    const name = 'type';
    if (i === undefined) {
      return name;
    } else {
      return this.memberControl(i) + `.${name}`;
    }
  }

  urlControl(i?: number): string {
    const name = 'url';
    if (i === undefined) {
      return name;
    } else {
      return this.memberControl(i) + `.${name}`;
    }
  }

  portControl(i?: number): string {
    const name = 'port';
    if (i === undefined) {
      return name;
    } else {
      return this.memberControl(i) + `.${name}`;
    }
  }

  anchorControl(i?: number): string {
    const name = 'anchor';
    if (i === undefined) {
      return name;
    } else {
      return this.memberControl(i) + `.${name}`;
    }
  }

  isAnchor(entity: Entity): boolean {
    if (entity instanceof Peer) {
      return entity.isAnchor;
    }
    return false;
  }

  removeMember(): void {
    const length = this.entities.length;
    if (length > 1) {
      this.form.removeControl(this.memberControl(length - 1));
      this.entities.pop();
    }
  }

  isHost(entity: Entity): boolean {
    return entity instanceof Peer || entity instanceof Orderer;
  }

  isPeer(entity: Entity): boolean {
    return entity instanceof Peer;
  }

  addMember(): void {
    const length = this.entities.length;
    // @ts-ignore
    this.entities.push(new Entity(undefined, this.org));
    this.addMemberControl(length);
  }

  private addMemberControl(i: number): FormGroup {
    const innerForm = this.formBuilder.group({});
    const nameControl = this.formBuilder.control('', Validators.required);
    const typeControl = this.formBuilder.control('', Validators.required);
    const urlControl = this.formBuilder.control('', []);
    const portControl = this.formBuilder.control('', []);
    const anchorControl = this.formBuilder.control('', []);
    const stateControl = this.formBuilder.control('', []);

    innerForm.addControl(this.nameControl(), nameControl);
    innerForm.addControl(this.typeControl(), typeControl);
    innerForm.addControl(this.urlControl(), urlControl);
    innerForm.addControl(this.portControl(), portControl);
    innerForm.addControl(this.anchorControl(), anchorControl);
    innerForm.addControl(this.stateControl(), stateControl);

    this.form.addControl(this.memberControl(i), innerForm);

    nameControl.valueChanges.subscribe(e => {
      this.entities[i].name = e?.trim();
    });
    typeControl.valueChanges.subscribe(e => {
      this.entities[i] = this.entities[i].toEntityInstance(Type[e] as unknown as Type);
      const url = this.form.get(this.urlControl(i));
      const port = this.form.get(this.portControl(i));
      if (this.isHost(this.entities[i])) {
        url.setValidators(Validators.required);
        port.setValidators(Validators.required);
      } else {
        url.setValidators([]);
        url.updateValueAndValidity();
        port.setValidators([]);
        port.updateValueAndValidity();
      }
    });
    urlControl.valueChanges.subscribe(e => {
      const entity = this.entities[i];
      if (entity instanceof Peer || entity instanceof Orderer) {
        entity.url = e?.trim();
      }
    });
    portControl.valueChanges.subscribe(e => {
      const entity = this.entities[i];
      if (entity instanceof Peer || entity instanceof Orderer) {
        entity.port = e;
      }
    });
    anchorControl.valueChanges.subscribe(e => {
      const entity = this.entities[i];
      if (entity instanceof Peer) {
        entity.isAnchor = e;
      }
    });
    stateControl.valueChanges.subscribe(e => {
      const entity = this.entities[i];
      if (!e) {
        return;
      }
      entity.state = states.find(s => s.code === e);
    });
    return innerForm;
  }

  ngOnInit(): void {
    this.ca = this.org.ca;
    if (!this.ca) {
      this.ca = new Ca();
      this.org.ca = this.ca;
    } else {
      this.caName.setValue(this.ca.name);
      this.caUsername.setValue(this.ca.username);
      this.caPassword.setValue(this.ca.password);
      this.caURL.setValue(this.ca.url);
      this.caPort.setValue(this.ca.port);
      if (this.ca.state) {
        this.caState.setValue(this.ca.state.code);
      }
    }
    if (!this.org.entities) {
      this.org.entities = [];
    }
    this.entities = this.org.entities;
    if (this.entities.length > 0) {
      this.entities.forEach((value, index) => {
        let type: string;
        if (value instanceof Peer) {
          type = 'Peer';
        } else if (value instanceof Orderer) {
          type = 'Orderer';
        } else {
          if ((value as Client).isAdmin) {
            type = 'Admin';
          } else {
            type = 'Client';
          }
        }
        const controls = this.addMemberControl(index);
        controls.get(this.nameControl()).setValue(value.name);
        controls.get(this.typeControl()).setValue(type);
        if (value.state) {
          controls.get(this.stateControl()).setValue(value.state.code);
        }
        if (value instanceof Peer || value instanceof Orderer) {
          controls.get(this.urlControl()).setValue(value.url);
          controls.get(this.portControl()).setValue(value.port);
          if (value instanceof Peer) {
            controls.get(this.anchorControl()).setValue(value.isAnchor);
          }
        }
      });
    } else {
      // this.addMember();
      this.entities.push(new Client(undefined, this.org, true, null));
      const typeControl = this.addMemberControl(0).get(this.typeControl());
      typeControl.setValue('Admin');
      typeControl.disable();
    }
    this.parentForm.addControl(String(this.i), this.form);
    this.form.setValidators((control: FormGroup) => {
      const memberNames = Object.values(control.controls).filter(c => {
        const nameControl = c.get(this.nameControl());
        return c instanceof FormGroup && nameControl != null && nameControl.valid;
      }).map(o => o.get(this.nameControl()).value.trim());
      const set = new Set(memberNames);
      if (memberNames.length > set.size) {
        return {error: true};
      }
      return null;
    });
  }

  ngOnDestroy(): void {
    this.parentForm.removeControl(String(this.i));
  }
}
