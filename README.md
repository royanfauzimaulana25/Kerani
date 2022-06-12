# Bangkit Team C22-PS164, Kerani (Kerabat Tani)

Product-based Capstone Bangkit Academy 2022
--
# Members:
1. Machine Learning:
- Muh.Royan Fauzi Maulana (M2107F1425) - Institut Informatika Dan Bisnis Darmajaya
- Bagas Arya Ariyanto (M2119F1495) - Institut Teknologi Sumatera

2. Cloud Computing:
- Aprian Herwansyah (C2107F1424) - Institut Informatika Dan Bisnis Darmajaya
- Rehan Maulana Ibrahim (C2009F0943) - Universitas Gunadarma

3. Mobile Development:
- Husain Ahmad Faiq (A2107F1426) - Institut Informatika Dan Bisnis Darmajaya
- Assad All Fajar (A2425F3000) - Universitas Bandar Lampung

Final Selected Themes:
--
Environmental Conservation, Disaster Resilience, and Climate Change

Title of the Project: 
--
Kerani (Kerabat Tani): Fast and Easy Image-Based Plant Disease Detection Application

Summary of Project: 
--
Identification of plant diseases is the key to prevent loss of yield and quantity of agricultural products. Health monitoring and disease detection in crops are very important for sustainable agriculture to reduce losses due to various disease attacks on crops which can reach 20-35%. Monitoring of plants is very difficult to do manually because it requires a lot of work, expertise in plant diseases, and takes a lot of time. Plant disease detection is usually done by visual inspection of leaves or some chemical process by experts. Based on these problems, the treatment that can be done is to use image processing and machine learning models to help in monitoring large fields of crops to be faster, cheaper and more accurate because it uses statistical machine learning and image processing algorithms. We can identify plant diseases by the special characteristics of the leaves of these plants. The question is " How to design a fast and easy plant diseases identifier android application that also can provide recommendations for prevention and control on agricultural land?". We hope that by building this app, we can contribute to decreasing the percentage losses due to various disease attacks and help for the welfare of farmers. 

Step to Replicate : 
--

1. Machine Learning:
- Dataset ingestion (from Kaggle)
- Feature exploration
- Preprocessing (binary encoding, dividing data, check numbers of data, and scaling the data to prepare for the ML training)
- Define deep learning model using TensorFlow 
- Hyperparameter tuning 
- Save and load model to evaluate model performance

2. Mobile Development:
- Design UI layout (optional: Figma)
- Dependencies (see Technology used part)
- Navigation
- Connecting local database to UI (using ViewModel, Room, optional: Flow, Koin, Clean Architecture)
- Implement external feature (accessing camera)
- Connecting to remote (using Firestore for database and Firebase storage for file)
- Implement machine learning using TFLite

3. Cloud Computing:
- Create a project on Google Cloud Platform
- Set default region as asia-southeast2(Jakarta)
> go to gcp console and write this command : $gcloud config set compute/region asia-southeast2
- Create a project on Firebase
- Create storage with records and profile folders
- Cloud Storage Browser page
  - Create bucket
  - Name your bucket : "-----"
  - Location type : region
  - Choose where to store your data = asia-southeast2
  - Leave the default setting
  - create
- Create a firestore for the database 

Technology Used : 
--
- [Firebase](https://firebase.google.com)
- [TensorFlow](https://www.tensorflow.org/lite/guide/android)
- [Keras]([https://keras.io/])
- [Google Cloud Platform](https://cloud.google.com/gcp)

Project Resources : 
--
### Budget
Google Cloud Platform Subscription : **$200**

### Dataset:
- [Disease and pest in coffee leaves]([https://www.kaggle.com/datasets/alvarole/coffee-leaves-disease])

### Paper / Journals / articles:
- [Identifikasi Penyakit pada Daun Kopi Menggunakan Metode Deep Learning VGG16](https://jurnal.yudharta.ac.id/v2/index.php/EXPLORE-IT/article/view/2689)

### Design Apps :
[Design](https://www.figma.com/file/j81lA9sBapgNRp8tfqKYZG/Kerani-(Kerabat-Tani)?node-id=0%3A1)




