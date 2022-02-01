import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Network} from '../_models/network';
import {Channel} from '../_models/channel';
import {Org} from '../_models/org';

@Component({
  selector: 'app-channels',
  templateUrl: './channels.component.html',
  styleUrls: ['./channels.component.scss']
})
export class ChannelsComponent implements OnInit {
  form: FormGroup;
  @Input() network: Network;
  channels: Channel[];

  constructor(private formBuilder: FormBuilder) {
    this.form = this.formBuilder.group({});
  }

  channelControl(i: number): string {
    return `channel-${i}`;
  }

  nameControl(i?: number): string {
    const name = 'name';
    if (i !== undefined) {
      return `${this.channelControl(i)}.${name}`;
    }
    return name;
  }

  consortiumControl(i?: number): string {
    const name = 'consortium';
    if (i !== undefined) {
      return `${this.channelControl(i)}.${name}`;
    }
    return name;
  }

  orgsControl(i?: number): string {
    const name = 'orgs';
    if (i !== undefined) {
      return `${this.channelControl(i)}.${name}`;
    }
    return name;
  }

  addChannel(): void {
    const length = this.channels.length;
    this.channels.push(new Channel());
    this.addChannelControls(length);
  }

  isPartOfConsortium(i: number, org: Org): boolean {
    if (this.channels[i].consortium) {
      return !!this.channels[i].consortium.orgs.find(o => o.fullName === org.fullName);
    } else {
      return false;
    }
  }

  private addChannelControls(i: number): void {
    const innerForm = this.formBuilder.group({});
    const channelName = this.formBuilder.control('', [Validators.required]);
    const channelConsortium = this.formBuilder.control('', [Validators.required]);
    const channelOrgs = this.formBuilder.control('', [Validators.required]);

    innerForm.addControl(this.nameControl(), channelName);
    innerForm.addControl(this.consortiumControl(), channelConsortium);
    innerForm.addControl(this.orgsControl(), channelOrgs);

    this.form.addControl(this.channelControl(i), innerForm);

    channelName.valueChanges.subscribe(v => {
      this.channels[i].name = v?.trim();
    });
    channelConsortium.valueChanges.subscribe(v => {
      if (v) {
        this.channels[i].consortium = this.network.consortiums.find(c => {
          return c.name === v;
        });
      }
      // channelOrgs.setValue(this.channels[i].consortium.orgs.map(o => o.fullName));
    });
    channelOrgs.valueChanges.subscribe((orgs: string[]) => {
      if (orgs) {
        this.channels[i].orgs = this.network.orgs.filter(o => {
          return orgs.find(j => {
            return o.fullName === j;
          });
        });
      }
    });
  }

  removeChannel(): void {
    const length = this.channels.length;
    if (length > 1) {
      this.form.removeControl(this.channelControl(length - 1));
      this.channels.pop();
    }
  }

  ngOnInit(): void {
    this.channels = this.network.channels;
    if (!this.channels) {
      this.channels = [];
      this.network.channels = this.channels;
    }
    if (this.channels.length > 0) {
      this.channels.forEach((c, index) => {
        this.addChannelControls(index);
        this.form.get(this.nameControl(index)).setValue(c.name);
        this.form.get(this.consortiumControl(index)).setValue(c.consortium?.name);
        this.form.get(this.orgsControl(index)).setValue(c.orgs?.map(o => o.fullName));
      });
    } else {
      this.addChannel();
    }
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
}
