import os 
from shutil import copyfile
import random

source = "used_datasets"
def rename_file_by_dir_name(source):
    '''Rename file in subfolder of parameter'''
    for dir in os.listdir(source):
        file_dir_path = os.path.join(source, dir)
        for count, file in enumerate(os.listdir(file_dir_path)):
            dst = f"{dir}_{str(count)}.jpg"
            src =f"{file_dir_path}/{file}"  # foldername/filename, if .py file is outside folder
            dst =f"{file_dir_path}/{dst}"
            #rename file
            os.rename(src, dst)

destination_dir_split = "train_val_dir"

def split_data(source, target, split_size):#, TRAINING, TESTING, SPLIT_SIZE   

    for dir in os.listdir(source):
        file_dir_path = os.path.join(source, dir)
        all_files_dir = []
        print(f"adding {dir} to variabel") 

        for file in os.listdir(os.path.join(source,dir)):            
            file_path = os.path.join(file_dir_path,file)
            if os.path.getsize(file_path):
                all_files_dir.append(file)
                print(f"Adding Non-Zero File {file} to variabel")
            else:
                print('{} is zero length, so ignoring'.format(file))

        #Spliting
        sum_files = len(all_files_dir)
        split_point = int(sum_files * split_size)
        shuffled = random.sample(all_files_dir, sum_files)

        train_set = shuffled[:split_point]
        test_set = shuffled[split_point:]

        train_dir = os.path.join(target, os.path.join("train",dir))
        val_dir = os.path.join(target, os.path.join("val",dir))

        os.makedirs(train_dir)
        os.makedirs(val_dir)

        for file in train_set:
            copyfile(os.path.join(file_dir_path, file), os.path.join(train_dir,file))
            print(f"succes adding {file} to {train_dir}")
        
        for file in test_set:
            copyfile(os.path.join(file_dir_path, file), os.path.join(val_dir,file))
            print(f"Succes adding {file} to {val_dir}")

split_data(source, destination_dir_split, 0.9)
'''
    
    n_files = len(all_files)
    split_point = int(n_files * SPLIT_SIZE)
    
    shuffled = random.sample(all_files, n_files)
    
    train_set = shuffled[:split_point]
    test_set = shuffled[split_point:]
    
    for file_name in train_set:
        copyfile(SOURCE + file_name, TRAINING + file_name)
        
    for file_name in test_set:
        copyfile(SOURCE + file_name, TESTING + file_name)
    
    #Check FIle Length and Size
    for file_name in os.listdir(SOURCE):
        file_path = SOURCE + file_name

        if os.path.getsize(file_path):
            all_files.append(file_name)
        else:
            print('{} is zero length, so ignoring'.format(file_name))

CAT_SOURCE_DIR = r"/tmp/PetImages/Cat/"
TRAINING_CATS_DIR = r"/tmp/cats-v-dogs/training/cats/"
TESTING_CATS_DIR = r"/tmp/cats-v-dogs/testing/cats/"
DOG_SOURCE_DIR = r"/tmp/PetImages/Dog/"
TRAINING_DOGS_DIR = r"/tmp/cats-v-dogs/training/dogs/"
TESTING_DOGS_DIR = r"/tmp/cats-v-dogs/testing/dogs/"

split_size = .9
split_data(CAT_SOURCE_DIR, TRAINING_CATS_DIR, TESTING_CATS_DIR, split_size)
split_data(DOG_SOURCE_DIR, TRAINING_DOGS_DIR, TESTING_DOGS_DIR, split_size)
'''


#split_data(source)