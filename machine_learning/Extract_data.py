import pandas as pd
import shutil
import matplotlib.pyplot as plt
import numpy as np
df = pd.read_csv("datasets/raw_datasets/datasets_2/dataset.csv")

#inisialisasi column name
id = df["id"]
miner = df['miner']
phoma = df['phoma']
cercospora = df['cercospora']
#variabel count file 
count_m = 0
count_r = 0
count_p = 0
count_h = 0

# iteration for copy file
for index, row in df.iterrows():
    name_file = row["id"]
    miner_file = row["miner"]
    rust_file = row["rust"]
    phoma_file = row["phoma"]
    cercospora_file = row["cercospora"]

    src = f"datasets/raw_datasets/datasets_2/images/images/{str(name_file)}.jpg"
    #split file to specific folder based on type images 
    if miner_file == 1:    
        shutil.copyfile(src ,f"datasets/raw_datasets/datasets_2/images/miner/{str(name_file)}.jpg")
        print("Copy Success on miner ")
        count_m += 1
    elif rust_file == 1:
        shutil.copyfile(src,f"datasets/raw_datasets/datasets_2/images/rust/{str(name_file)}.jpg")
        print("Copy Success on rust ")
        count_r += 1
    elif phoma_file == 1:
        shutil.copyfile(src,f"datasets/raw_datasets/datasets_2/images/phoma/{str(name_file)}.jpg")
        print("Copy Success on phoma ")
        count_p += 1
    else:
        shutil.copyfile(src,f"datasets/raw_datasets/datasets_2/images/healthy/{str(name_file)}.jpg")
        print("Copy Success on healthy ")
        count_h += 1

print(f"Number of miner file : {count_m}")
print(f"Number of rust file : {count_r}")
print(f"Number of phoma file : {count_p}")
print(f"Number of healthy file : {count_h}")
print("Total :",count_m+count_p+count_h+count_r)

x = np.array(["Rust", "Miner", "Phoma"])
y = np.array([count_r, count_m, count_p])

plt.xlabel('Type Of Coffe Leaf Disease ')
plt.ylabel('Qty')
plt.title('Datasets Distribution')

plt.bar(x,y)
plt.show()