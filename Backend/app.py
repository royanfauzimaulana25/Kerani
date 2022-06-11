import firebase_admin
import pyrebase
import json
import keras
import numpy as np
import io
import h5py

from firebase_admin import credentials, auth
from flask import Flask, request, jsonify
from keras.models import load_model
from PIL import Image

app = Flask(__name__)


#KONEKSI KE FIREBASE
cred = credentials.Certificate('fbAdminConfig.json')
firebase = firebase_admin.initialize_app(cred)
pb = pyrebase.initialize_app(json.load(open('fbConfig.json')))


#HOME
@app.route("/", methods=["GET"])
def welcome():
    return "<h1 style='color:purple'>SELAMAT DATANG!</h1>",200

#REGISTRASI AKUN
@app.route('/register',methods=["GET","POST"])
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
@app.route("/login" ,methods=["GET","POST"])
def login():
    email = request.get_json()["email"]
    password = request.get_json()["password"]

    try:
        user = pb.auth().sign_in_with_email_and_password(email,password)
        return {"msg":"Anda Berhasil Login!"}, 200
    except:
        return {'msg':'Email atau Password Salah'}, 400

#LOAD IMAGE
def load_image(img_path, show=False):
    '''Image Preprocessing Function'''
    img = keras.utils.load_img(img_path, target_size=(300, 300))
    img_tensor = keras.utils.img_to_array(img)  # (height, width, channels)
    img_tensor = np.expand_dims(img_tensor,
                                axis=0)  # (1, height, width, channels), add a dimension because the model expects this shape: (batch_size, height, width, channels)
    img_tensor /= 255.  # imshow expects values in the range [0, 1]

    return img_tensor

#MODEL LOCATION
file = "C:/Asset/v.1.0.h5"
model=load_model("C:/Asset/v.1.0.h5") #Change Model Directory

#PREDICT
@app.route("/predict", methods=["GET","POST"])
def predict():
    #if request.method == "POST":
        #file = request.files.get('file') Still Not Working
        #if file is None or file.filename == "":
           # return jsonify({"error": "no file"})

    #ImageFile = request.files["imagefile"]
    #image_path = "images/" + ImageFile.filename
    
        try:
            #image_bytes = file.read("images/healthy_10.jpg")
            #io_image = io.BytesIO("images/healthy_10.jpg")
            #pillow_img = Image.open("images/healthy_10.jpg")

            # Predict & Response Code Berhasil Tapi Get Request Belum Berhasil
            new_image = load_image(file) # file = file dir
            prediction = np.argmax(model.predict(new_image)[0])
            result = ""
            if prediction == 0:
                result = "Cercosporta"
            elif prediction == 1:
                result = "Healthy"
            elif prediction == 2:
                result = "Miner"
            elif prediction == 3:
                result = "Phoma"
            else:
                result = "Rust"

            data = {"prediction": result}

            return jsonify(data)
        except Exception as e:
            return jsonify({"error": str(e)})


if __name__=="__main__":
    app.run(host='0.0.0.0', port=8080)
