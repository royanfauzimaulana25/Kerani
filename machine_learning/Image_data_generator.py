import tensorflow as tf
from tensorflow.keras.preprocessing.image import ImageDataGenerator
import numpy
import os

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
    # Flow training images in batches of 20 using train_datagen generator
    # --------------------
    train_generator = train_datagen.flow_from_directory(train_dir,
                                                        batch_size = 32,
                                                        class_mode = 'cateforical',
                                                        target_size = (150, 150))     
    # --------------------
    # Flow validation images in batches of 20 using test_datagen generator
    # --------------------
    validation_generator =  test_datagen.flow_from_directory(val_dir,
                                                            batch_size = 32,
                                                            class_mode  = 'categorical',
                                                            target_size = (150, 150))

    return train_generator, validation_generator