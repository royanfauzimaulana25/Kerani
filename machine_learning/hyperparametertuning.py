#=============== HyperParameter Tuning =====================#
import tensorflow as tf
import kerastuner as kt
from tensorflow import keras
import pandas as pd

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

# Build baseline model with Sequential API
model = keras.Sequential()
model.add(keras.layers.Flatten(input_shape=(300,300)))
model.add(keras.layers.Dense(units=1024, activation='relu', name='dense_1'))
model.add(keras.layers.Dropout(0.2))
model.add(keras.layers.Dense(5, activation='softmax'))

# Print model summary
model.summary()

def evaluate_model(model, X_test, y_test):
    """
    evaluate model on test set and show results in dataframe.
    
    Parameters
    ----------
    model : keras model
        trained keras model.
    X_test : numpy array
        Features of holdout set.
    y_test : numpy array
        Labels of holdout set.
        
    Returns
    -------
    display_df : DataFrame
        Pandas dataframe containing evaluation results.
    """
    eval_dict = model.evaluate(X_test, y_test, return_dict=True)
    
    display_df = pd.DataFrame([eval_dict.values()], columns=[list(eval_dict.keys())])
    
    return display_df

results = evaluate_model(model, X_test, y_test)

results.index = ['Baseline']

results.head()

def build_model(hp):
    """
    Builds model and sets up hyperparameter space to search.
    
    Parameters
    ----------
    hp : HyperParameter object
        Configures hyperparameters to tune.
        
    Returns
    -------
    model : keras model
        Compiled model with hyperparameters to tune.
    """
    # Initialize sequential API and start building model.
    model = keras.Sequential()
    model.add(keras.layers.Flatten(input_shape=(28,28)))
    
    # Tune the number of hidden layers and units in each.
    # Number of hidden layers: 1 - 5
    # Number of Units: 32 - 512 with stepsize of 32
    for i in range(1, hp.Int("num_layers", 2, 6)):
        model.add(
            keras.layers.Dense(
                units=hp.Int("units_" + str(i), min_value=32, max_value=1024, step=32),
                activation="relu")
            )
        
        # Tune dropout layer with values from 0 - 0.3.
        model.add(keras.layers.Dropout(hp.Float("dropout_" + str(i), 0, 0.3, step=0.1)))
    
    # Add output layer.
    model.add(keras.layers.Dense(units=10, activation="softmax"))
    
    # Tune learning rate for Adam optimizer with values from 0.01, 0.001, or 0.0001
    hp_learning_rate = hp.Choice("learning_rate", values=[1e-2, 1e-3, 1e-4])
    
    model.compile(optimizer=keras.optimizers.Adam(learning_rate=hp_learning_rate),
                  loss=keras.losses.SparseCategoricalCrossentropy(),
                  metrics=["accuracy"])
    
    return model

# Instantiate the tuner
tuner = kt.Hyperband(build_model,
                     objective="val_accuracy",
                     max_epochs=20,
                     factor=3,
                     hyperband_iterations=1,
                     directory="kt_dir",
                     project_name="kt_hyperband",
                     overwrite=True)

tuner.search_space_summary()

# This cell takes a long time to run when hyperband_iterations is large

stop_early = tf.keras.callbacks.EarlyStopping(monitor='val_loss', patience=5)

tuner.search(X_train, y_train, epochs=NUM_EPOCHS, validation_split=0.2, callbacks=[stop_early], verbose=0)

# Get the optimal hyperparameters from the results
best_hps=tuner.get_best_hyperparameters()[0]

h_model = tuner.hypermodel.build(best_hps)
h_model.summary()

h_model.fit(X_train, y_train, epochs=NUM_EPOCHS, validation_split=0.2, callbacks=[stop_early], verbose=2)

hyper_df = evaluate_model(h_model, X_test, y_test)

hyper_df.index = ["Hypertuned"]

results.append(hyper_df)
