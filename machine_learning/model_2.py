import csv
from random import shuffle
import string
import numpy as np
import tensorflow as tf
from tensorflow import keras
import matplotlib.pyplot as plt
from tensorflow.keras.preprocessing.image import ImageDataGenerator, array_to_img
import pathlib

train_dir = "train_val_dir/train"
val_dir =  "train_val_dir/val"

def image_generator(train_dir, val_dir):    
    train_datagen = ImageDataGenerator( rescale = 1./255.,
                                        rotation_range = 90,
                                        width_shift_range = 0.2,
                                        height_shift_range = 0.2,
                                        shear_range = 0.2,
                                        zoom_range = 0.2,
                                        horizontal_flip = True)
    
    test_datagen  = ImageDataGenerator( rescale = 1./255. )

    # --------------------
    # Flow training images in batches of 32 using train_datagen generator
    # --------------------
    train_generator = train_datagen.flow_from_directory(train_dir,
                                                        batch_size = 32,
                                                        class_mode = 'categorical',
                                                        target_size = (300, 300))     
    # --------------------
    # Flow validation images in batches of 32 using test_datagen generator
    # --------------------
    validation_generator =  test_datagen.flow_from_directory(val_dir,
                                                            batch_size = 32,
                                                            class_mode  = 'categorical',
                                                            target_size = (300, 300))

    return train_generator, validation_generator

train_generator, validation_generator = image_generator(train_dir, val_dir)

model = tf.keras.models.Sequential([
    tf.keras.layers.Conv2D(32, (3,3), activation='relu', input_shape=(300, 300, 3)),
    tf.keras.layers.MaxPooling2D(2, 2),
   
    tf.keras.layers.Conv2D(64, (3,3), activation='relu'),
    tf.keras.layers.MaxPooling2D(2,2),

    tf.keras.layers.Conv2D(128, (3,3), activation='relu'),
    tf.keras.layers.MaxPooling2D(2,2),

    tf.keras.layers.Conv2D(256, (3,3), activation='relu'),
    tf.keras.layers.MaxPooling2D(2,2),

    tf.keras.layers.Conv2D(512, (3,3), activation='relu'),
    tf.keras.layers.MaxPooling2D(2,2),
    
    # Flatten
    tf.keras.layers.Flatten(),
    tf.keras.layers.Dropout(0.5),
    tf.keras.layers.Dense(1024, activation='relu'),
    tf.keras.layers.Dense(5, activation='softmax')
])

model.compile(optimizer = tf.optimizers.Adam(learning_rate = 0.0001),
              loss = 'categorical_crossentropy',
              metrics=['accuracy'])

print("========== Summary ==========")
print(model.summary())

print("========== Start Training =========")
history = model.fit(train_generator,
                    epochs = 15,
                    validation_data = validation_generator,
                    verbose = 1,
                    shuffle = True,
                    use_multiprocessing=False)
print("========= Visualize Accuracy and Loss =========")
acc = history.history['accuracy']
val_acc = history.history['val_accuracy']
loss = history.history['loss']
val_loss = history.history['val_loss']

epochs = range(len(acc))

plt.plot(epochs, acc, 'r', label='Training accuracy')
plt.plot(epochs, val_acc, 'b', label='Validation accuracy')
plt.title('Training and validation accuracy')
plt.legend()
plt.figure()

plt.plot(epochs, loss, 'r', label='Training Loss')
plt.plot(epochs, val_loss, 'b', label='Validation Loss')
plt.title('Training and validation loss')
plt.legend()

plt.show()

# Prediksi
y_pred = model.predict(validation_generator)
y_pred

y_pred = np.argmax(y_pred, axis=1)
print(y_pred)

# Test predict image
from google.colab import files
from keras.preprocessing import image

uploaded = files.upload()

for fn in uploaded.keys():
 
  # predicting images
  path = fn
  img = image.load_img(path, target_size=(300, 300))
  x = image.img_to_array(img)
  x = np.expand_dims(x, axis=0)

  images = np.vstack([x])
  classes = model.predict(images, batch_size=10)
  print(fn)
  print(classes)

#Export Model
export_dir = '/model_save/1'
tf.saved_model.save(new_model, export_dir)

# Convert the model to tflite
converter = tf.lite.TFLiteConverter.from_saved_model(export_dir)
tflite_model = converter.convert()

tflite_model_file = pathlib.Path('/model_save/model.tflite')
tflite_model_file.write_bytes(tflite_model)
