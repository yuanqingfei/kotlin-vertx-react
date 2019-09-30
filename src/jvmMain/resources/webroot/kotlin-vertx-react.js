(function (root, factory) {
  if (typeof define === 'function' && define.amd)
    define(['exports', 'kotlin'], factory);
  else if (typeof exports === 'object')
    factory(module.exports, require('kotlin'));
  else {
    if (typeof kotlin === 'undefined') {
      throw new Error("Error loading module 'kotlin-vertx-react'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'kotlin-vertx-react'.");
    }
    root['kotlin-vertx-react'] = factory(typeof this['kotlin-vertx-react'] === 'undefined' ? {} : this['kotlin-vertx-react'], kotlin);
  }
}(this, function (_, Kotlin) {
  'use strict';
  var Kind_CLASS = Kotlin.Kind.CLASS;
  var Kind_OBJECT = Kotlin.Kind.OBJECT;
  var Unit = Kotlin.kotlin.Unit;
  function Country(name, code) {
    this.name = name;
    this.code = code;
  }
  Country.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Country',
    interfaces: []
  };
  Country.prototype.component1 = function () {
    return this.name;
  };
  Country.prototype.component2 = function () {
    return this.code;
  };
  Country.prototype.copy_puj7f4$ = function (name, code) {
    return new Country(name === void 0 ? this.name : name, code === void 0 ? this.code : code);
  };
  Country.prototype.toString = function () {
    return 'Country(name=' + Kotlin.toString(this.name) + (', code=' + Kotlin.toString(this.code)) + ')';
  };
  Country.prototype.hashCode = function () {
    var result = 0;
    result = result * 31 + Kotlin.hashCode(this.name) | 0;
    result = result * 31 + Kotlin.hashCode(this.code) | 0;
    return result;
  };
  Country.prototype.equals = function (other) {
    return this === other || (other !== null && (typeof other === 'object' && (Object.getPrototypeOf(this) === Object.getPrototypeOf(other) && (Kotlin.equals(this.name, other.name) && Kotlin.equals(this.code, other.code)))));
  };
  function Island(name, country) {
    this.name = name;
    this.country = country;
  }
  Island.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Island',
    interfaces: []
  };
  Island.prototype.component1 = function () {
    return this.name;
  };
  Island.prototype.component2 = function () {
    return this.country;
  };
  Island.prototype.copy_jo78kg$ = function (name, country) {
    return new Island(name === void 0 ? this.name : name, country === void 0 ? this.country : country);
  };
  Island.prototype.toString = function () {
    return 'Island(name=' + Kotlin.toString(this.name) + (', country=' + Kotlin.toString(this.country)) + ')';
  };
  Island.prototype.hashCode = function () {
    var result = 0;
    result = result * 31 + Kotlin.hashCode(this.name) | 0;
    result = result * 31 + Kotlin.hashCode(this.country) | 0;
    return result;
  };
  Island.prototype.equals = function (other) {
    return this === other || (other !== null && (typeof other === 'object' && (Object.getPrototypeOf(this) === Object.getPrototypeOf(other) && (Kotlin.equals(this.name, other.name) && Kotlin.equals(this.country, other.country)))));
  };
  function hello() {
    return 'Hello from JS';
  }
  function Sample() {
  }
  Sample.prototype.checkMe = function () {
    return 12;
  };
  Sample.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Sample',
    interfaces: []
  };
  function Platform() {
    Platform_instance = this;
    this.name = 'JS';
  }
  Platform.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'Platform',
    interfaces: []
  };
  var Platform_instance = null;
  function Platform_getInstance() {
    if (Platform_instance === null) {
      new Platform();
    }
    return Platform_instance;
  }
  function helloWorld(salutation) {
    var tmp$;
    var message = salutation + ' from Kotlin.JS ' + hello() + ', check me value: ' + (new Sample()).checkMe();
    (tmp$ = document.getElementById('js-response')) != null ? (tmp$.textContent = message) : null;
  }
  function main$lambda(it) {
    helloWorld('Hi!');
    return Unit;
  }
  function main() {
    document.addEventListener('DOMContentLoaded', main$lambda);
  }
  var package$sample = _.sample || (_.sample = {});
  package$sample.Country = Country;
  package$sample.Island = Island;
  package$sample.hello = hello;
  package$sample.Sample = Sample;
  Object.defineProperty(package$sample, 'Platform', {
    get: Platform_getInstance
  });
  package$sample.helloWorld = helloWorld;
  package$sample.main = main;
  main();
  Kotlin.defineModule('kotlin-vertx-react', _);
  return _;
}));

//# sourceMappingURL=kotlin-vertx-react.js.map
