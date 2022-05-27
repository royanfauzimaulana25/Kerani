const express = require('express');
const api = express();
const bodyParser = require ('body-parser');
const mysql = require('mysql');

api.use(bodyParser.json());
api.use(bodyParser.urlencoded({
    extended: true,
}));

//ROUTE
api.get('/', function(reg, res){
    return res.send({error: true, message: 'Selamat Datang'})
});

//KONFIGURASI KONEKSI KE DATABASE
const mysqlConn = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: 'root',
    database: 'rest api',
    port: 3000
});

//KONEKSI KE DATABASE
mysqlConn.connect();

// Menampilkan data all user
api.get('/users', function (req, res) {
    mysqlConn.query('SELECT * FROM users', function (error, results, fields) {
        if (error) throw error;
        return res.send({ error: false, data: results, message: 'Daftar Data Users' });
    });
});

// Menampilkan data user detail
api.get('/user/:id', function (req, res) {
  
    let user_id = req.params.id;
  
    if (!user_id) {
        return res.status(400).send({ error: true, message: 'Silakan Mengisi Data Anda' });
    }
  
    mysqlConn.query('SELECT * FROM users where id=?', user_id, function (error, results, fields) {
        if (error) throw error;
        return res.send({ error: false, data: results[0], message: 'Users Details' });
    });
  
});

// Menambahkan user baru 
api.post('/user', function (req, res) {
  
    let user = req.body.user;
  
    if (!user) {
        return res.status(400).send({ error:true, message: 'Silakan Mengisi Data Anda' });
    }
  
    mysqlConn.query("INSERT INTO users SET ? ", { user: user, name: req.body.name, email: req.body.email }, function (error, results, fields) {
        if (error) throw error;
        return res.status(201).send({ error: false, data: results, message: 'User Baru Berhasil Ditambahkan' });
    });
});

 
//  Update detail user id
api.put('/user', function (req, res) {
  
    let user_id = req.body.user_id;
    let user = req.body.user;
    let name = req.body.name;
    let email = req.body.email;
  
    if (!user_id || !user) {
        return res.status(400).send({ error: user, message: 'Silakan Mengisi Data User' });
    }
  
    mysqlConn.query("UPDATE users SET user = ?, name = ?, email = ? WHERE id = ?", [user, name, email, user_id], function (error, results, fields) {
        if (error) throw error;
        return res.send({ error: false, data: results, message: 'Data User Berhasil Diperbaharui' });
    });
});
 
 
//  Delete user
api.delete('/user', function (req, res) {
  
    let user_id = req.body.user_id;
  
    if (!user_id) {
        return res.status(400).send({ error: true, message: 'Silahkan Mengisi Data User' });
    }
    mysqlConn.query('DELETE FROM users WHERE id = ?', [user_id], function (error, results, fields) {
        if (error) throw error;
        return res.send({ error: false, data: results, message: 'User Berhasil Dihapus.' });
    });
});

//SET PORT
api.listen(3000, function(){
    console.log('Node app is running on port 3000')
});

module.exports = api;
