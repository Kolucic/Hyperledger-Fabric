import {Address} from './address';
import {Org} from './org';
import {State} from './ca';

export enum Type {
  Client, Peer, Orderer, Admin
}

export abstract class Entity {
  name: string;
  org: Org;
  state: State;

  constructor(name: string, org: Org, state?: State) {
    this.name = name;
    this.org = org;
    this.state = state;
  }

  static parse(obj: any, org: Org): Entity {
    // @ts-ignore
    let entity = new Entity();
    entity.org = org;
    if (obj.name) {
      entity.name = obj.name;
    }
    if (obj.state && obj.state.code && obj.state.name) {
      entity.state = obj.state;
    }
    if (obj.isAdmin !== undefined) {
      if (obj.isAdmin) {
        entity = entity.toEntityInstance(Type.Admin);
      } else {
        entity = entity.toEntityInstance(Type.Client);
      }
    } else {
      if (obj.isAnchor) {
        entity = entity.toEntityInstance(Type.Peer);
        entity.isAnchor = obj.isAnchor;
        if (obj.url) {
          entity.url = obj.url;
        }
        if (obj.port) {
          entity.port = obj.port;
        }
      } else {
        if (obj.url) {
          entity = entity.toEntityInstance(Type.Orderer);
          entity.url = obj.url;
        }
        if (obj.port) {
          entity = entity.toEntityInstance(Type.Orderer);
          entity.port = obj.port;
        }
      }
    }
    return entity;
  }

  toEntityInstance(type: Type): Entity {
    switch (type) {
      case Type.Admin:
        if (this instanceof Client) {
          if (!this.isAdmin) {
            this.isAdmin = true;
          }
          return this;
        }
        return new Client(this.name, this.org, true, this.state);
      case Type.Client:
        // @ts-ignore
        if (this instanceof Client) {
          if (this.isAdmin) {
            this.isAdmin = false;
          }
          return this;
        }
        return new Client(this.name, this.org, false, this.state);
      case Type.Orderer:
        if (this instanceof Orderer) {
          return this;
        }
        if (this instanceof Peer) {
          return new Orderer(this.name, this.org, this.url, this.port, this.state);
        }
        return new Orderer(this.name, this.org, undefined, undefined, this.state);
      case Type.Peer:
        if (this instanceof Peer) {
          return this;
        }
        if (this instanceof Orderer) {
          return new Peer(this.name, this.org, false, this.url, this.port);
        }
        return new Peer(this.name, this.org, false, undefined, undefined, this.state);
    }
  }

  toJSON(): any {
    const entity = {
      ...this,
      domain: this.org.fullName
    };
    delete entity.org;
    return entity;
  }
}

export class Client extends Entity {
  isAdmin: boolean;

  constructor(name: string, org: Org, isAdmin: boolean, state?: State) {
    super(name, org, state);
    this.isAdmin = isAdmin;
  }
}

export class Peer extends Entity implements Address {
  isAnchor: boolean;
  port: number;
  url: string;


  constructor(name: string, org: Org, isAnchor: boolean, url: string, port: number, state?: State) {
    super(name, org, state);
    this.isAnchor = isAnchor;
    this.port = port;
    this.url = url;
  }
}

export class Orderer extends Entity implements Address {
  port: number;
  url: string;


  constructor(name: string, org: Org, url: string, port: number, state?: State) {
    super(name, org, state);
    this.port = port;
    this.url = url;
  }
}
