var express = require('express');
var app = express();
var bodyParser = require ('body-parser');
var mysql = require('mysql');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended: true,
}));

//ROUTE
app.get('/', function(reg, res){
    return res.send({error: true, message: 'Selamat Datang'})
});

//KONFIGURASI KONEKSI KE DATABASE
var dbConn = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: 'root',
    database: 'rest api',
    port: 3000
});

//KONEKSI KE DATABASE
dbConn.connect();

//SET PORT
app.listen(3000, function(){
    console.log('Node app is running on port 3000')
});

module.exports = app;
