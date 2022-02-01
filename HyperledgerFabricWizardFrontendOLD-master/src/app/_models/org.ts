import {Entity} from './entity';
import {Ca} from './ca';

export class Org {

  constructor(name?: string, domain?: string, entities?: Entity[], ca?: Ca) {
    this.name = name;
    this.domain = domain;
    this.entities = entities;
    this.ca = ca;
  }

  name: string;
  domain: string;
  entities: Entity[];
  ca: Ca;
  fullName: string;

  static parse(obj: any): Org {
    const org = new Org();
    if (obj.name) {
      org.name = obj.name;
    }
    if (obj.domain) {
      org.domain = obj.domain;
    }
    if (obj.fullName) {
      org.fullName = obj.fullName;
    }
    if (Array.isArray(obj.entities)) {
      org.entities = [];
      obj.entities.forEach(e => {
        org.entities.push(Entity.parse(e, org));
      });
    }
    if (obj.ca) {
      org.ca = Ca.parse(obj.ca);
    }
    return org;
  }

  toJSON(): object {
    return {
      name: this.name,
      domain: this.domain,
      fullName: this.fullName,
      entities: this.entities,
      ca: this.ca
    };
  }
}
