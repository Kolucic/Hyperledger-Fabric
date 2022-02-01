import {Org} from './org';

export class Consortium {
  name: string;
  orgs: Org[];


  constructor(name?: string, orgs?: Org[]) {
    this.name = name;
    this.orgs = orgs;
  }

  static parse(orgs: Org[], obj: any): Consortium {
    const consortium = new Consortium();
    if (obj.name) {
      consortium.name = obj.name;
    }
    if (Array.isArray(obj.orgs)) {
      consortium.orgs = [];
      obj.orgs.forEach(o => {
        const org = orgs.find(o1 => o1.fullName === o);
        if (org) {
          consortium.orgs.push(org);
        }
      });
    }
    return consortium;
  }

  toJSON(): object {
    return {
      name: this.name,
      orgs: this.orgs?.map(o => o.fullName)
    };
  }
}
