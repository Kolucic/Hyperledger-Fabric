import {Consortium} from './consortium';
import {Org} from './org';

export class Channel {
  name: string;
  consortium: Consortium;
  orgs: Org[];

  constructor(name?: string, consortium?: Consortium, orgs?: Org[]) {
    this.name = name;
    this.consortium = consortium;
    this.orgs = orgs;
  }

  static parse(orgs: Org[], consortiums: Consortium[], obj: any): Channel {
    const channel = new Channel();
    if (obj.name) {
      channel.name = obj.name;
    }
    if (obj.consortium) {
      channel.consortium = consortiums.find(consortium => consortium.name === obj.consortium);
    }
    if (Array.isArray(obj.orgs)) {
      channel.orgs = [];
      obj.orgs.forEach(o => {
        const org = orgs.find(o1 => o1.fullName === o);
        if (org) {
          channel.orgs.push(org);
        }
      });
    }
    return channel;
  }

  toJSON(): object {
    return {
      name: this.name,
      consortium: this.consortium?.name,
      orgs: this.orgs?.map(o => o.fullName)
    };
  }
}
