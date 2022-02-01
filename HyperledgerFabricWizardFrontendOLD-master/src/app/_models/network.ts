import {Org} from './org';
import {Consortium} from './consortium';
import {Channel} from './channel';

export class Network {
  name: string;
  orgs: Org[];
  consortiums: Consortium[];
  channels: Channel[];


  constructor(name?: string, orgs?: Org[], consortiums?: Consortium[], channels?: Channel[]) {
    this.name = name;
    this.orgs = orgs;
    this.consortiums = consortiums;
    this.channels = channels;
  }

  static parse(obj: any): Network {
    const network = new Network();
    if (obj.name) {
      network.name = obj.name;
    }
    if (Array.isArray(obj.orgs)) {
      network.orgs = [];
      obj.orgs.forEach(o => {
        network.orgs.push(Org.parse(o));
      });
    }
    if (Array.isArray(obj.consortiums)) {
      network.consortiums = [];
      obj.consortiums.forEach(c => {
        network.consortiums.push(Consortium.parse(network.orgs, c));
      });
    }
    if (Array.isArray(obj.channels)) {
      network.channels = [];
      obj.channels.forEach(c => {
        network.channels.push(Channel.parse(network.orgs, network.consortiums, c));
      });
    }
    return network;
  }
}
