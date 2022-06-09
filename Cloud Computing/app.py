import firebase_admin
import pyrebase
import json

from firebase_admin import credentials, auth
from flask import Flask, request

app = Flask(__name__)


#KONEKSI KE FIREBASE
cred = credentials.Certificate('fbAdminConfig.json')
firebase = firebase_admin.initialize_app(cred)
pb = pyrebase.initialize_app(json.load(open('fbconfig.json')))

#REGISTRASI AKUN
@app.route('/register',methods=['POST'])
def register():
    email = request.get_json()['email']
    password = request.get_json()['password']

    if email is None or password is None:
        return {'msg':'Email atau Password Belum Diisi'},400
    try:
        user = auth.create_user(email=email,password=password)
        return {'msg':'Akun Berhasil Dibuat!'},200
    except:
        return {'msg':'Email Sudah Ada'},400

#LOGIN AKUN
@app.route("/login" ,methods=["POST"])
def login():
    email = request.get_json()["email"]
    password = request.get_json()["password"]

    try:
        user = pb.auth().sign_in_with_email_and_password(email,password)
        return {"msg":"Anda Berhasil Login!"}, 200
    except:
        return {'msg':'Email atau Password Salah'}, 400


if __name__=="__main__":
    app.run(debug=True)
