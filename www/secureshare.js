var cordova = require('cordova');
var SecureShare = function () { };

function exec (action, args) {
  return new Promise(function (resolve, reject) {
    cordova.exec(
      function (success) { resolve(success); },
      function (error) { reject(error); },
      'SecureShare',
      action,
      args
    );
  });
}

SecureShare.prototype.save = function (data) {
  return exec('save', [ data ]);
};

SecureShare.prototype.save = function (data) {
  return exec('clear', [ {} ]);
};

SecureShare.prototype.retrieve = function () {
  return exec('retrieve', []);
};

SecureShare.prototype.retrieveFrom = function (packageName) {
  return exec('retrieveFrom', [ packageName ]);
};

module.exports = new SecureShare();
