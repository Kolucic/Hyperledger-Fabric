import {Address} from './address';

export class Ca implements Address {
  name: string;
  username: string;
  password: string;
  port: number;
  url: string;
  state: State;

  constructor(name?: string, username?: string, password?: string, url?: string, port?: number, state?: State) {
    this.name = name;
    this.username = username;
    this.password = password;
    this.url = url;
    this.port = port;
    this.state = state;
  }

  static parse(obj: any): Ca {
    const ca = new Ca();
    if (obj.name) {
      ca.name = obj.name;
    }
    if (obj.username) {
      ca.username = obj.username;
    }
    if (obj.password) {
      ca.password = obj.password;
    }
    if (obj.url) {
      ca.url = obj.url;
    }
    if (obj.port) {
      ca.port = obj.port;
    }
    if (obj.state) {
      // @ts-ignore
      ca.state = {};
      if (obj.state.name && obj.state.code) {
        ca.state.name = obj.state.name;
        ca.state.code = obj.state.code;
      }
    }
    return ca;
  }
}

export interface State {
  name: string;
  code: string;
}
