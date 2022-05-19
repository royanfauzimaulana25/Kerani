import matplotlib.pyplot as plt
import matplotlib.image as mpimg
import numpy as np
import os

main_dir = "used_datasets" #initialize dataset base directory

def bar_plot (x, y):
    x_value = np.array(x)
    y_value = np.array(y)

    plt.xlabel('Type Of Coffe Leaf Disease ')
    plt.ylabel('Qty')
    plt.title('Datasets Distribution')
    plt.bar(x,y)
    plt.show()

def count_image_in_dir (base_dir):
    x = []
    y = []
    for dir in os.listdir(base_dir):
        x.append(os.path.basename(os.path.join(base_dir,dir)))
        y.append(len(os.listdir(os.path.join(base_dir,dir))))
    return x, y

def bar_subplot(labels,value):
    sum_labels = np.arange(len(labels))  # the label locations
    width = 0.8  # the width of the bars

    fig, ax = plt.subplots()
    bar = ax.bar(sum_labels, value, width)
    
    # Add some text for labels, title and custom x-axis tick labels, etc.
    ax.set_title('Image Distributions')
    ax.set_ylabel('Quantity')
    ax.set_xticks(sum_labels, labels)    
    # Label the number each bar 
    ax.bar_label(bar, padding=3)    

    fig.tight_layout()
    plt.show()

x, y = count_image_in_dir(main_dir) 

bar_subplot(x,y)

#--------------
#Visualize Distribution Train Val Image 
#--------------

def map_image_distribution ():
# Parameters for our graph; we'll output images in a 4x4 configuration
nrows = 4
ncols = 4

pic_index = 0 # Index for iterating over images

# Set up matplotlib fig, and size it to fit 4x4 pics
fig = plt.gcf()
fig.set_size_inches(ncols*4, nrows*4)

pic_index+=8

next_cat_pix = [os.path.join(train_cats_dir, fname) 
                for fname in train_cat_fnames[ pic_index-8:pic_index] 
               ]

next_dog_pix = [os.path.join(train_dogs_dir, fname) 
                for fname in train_dog_fnames[ pic_index-8:pic_index]
               ]

for i, img_path in enumerate(next_cat_pix+next_dog_pix):
  # Set up subplot; subplot indices start at 1
  sp = plt.subplot(nrows, ncols, i + 1)
  sp.axis('Off') # Don't show axes (or gridlines)

  img = mpimg.imread(img_path)
  plt.imshow(img)

plt.show()