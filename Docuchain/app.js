'use strict';
var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var fs = require('fs');
var express = require('express');
var http = require('http');
var https = require('https');

var path = require('path');
var key = fs.readFileSync('privatekey.pem');
var cert = fs.readFileSync('certificate.pem');

var https_options = {
  key: key,
  cert: cert,
  passphrase: 'Welcome123',
};
var httpsPort = 7200;
var httpport = process.env.PORT || 7201;

app.use(express.static(__dirname + '/public'));
app.use('/', express.static(path.join(__dirname + '/')));
app.use('/public', express.static(path.join(__dirname + '/public')));
app.use(
  '/public/index.html',
  express.static(path.join(__dirname + '/public/index.html'))
);
app.get('/', function (req, res) {
  res.sendFile(__dirname + '/' + '/public' + '/index.html');
});

https.createServer(https_options, app).listen(httpsPort, function (req, res) {
  console.log('Catch the action at https://localhost:' + httpsPort);
});

http.createServer(app).listen(httpport, function (req, res) {
  console.log('Catch the action at http://localhost:' + httpport);
});
