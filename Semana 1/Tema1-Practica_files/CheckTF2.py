from tensorflow.keras.datasets import mnist
import numpy as np
import matplotlib.pyplot as plt
import tensorflow.keras as keras
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense

# Load data
(x_train, y_train), (x_valid, y_valid) = mnist.load_data()

# Display sample images
n_to_show = 10
input_idx = np.random.choice(range(len(x_train)), n_to_show)
input_images = x_train[input_idx]
input_labels = y_train[input_idx]

fig = plt.figure(figsize=(15, 2))
fig.subplots_adjust(hspace=0.1, wspace=0.4)
for i in range(n_to_show):
    ax = fig.add_subplot(1, n_to_show, i+1)
    ax.axis('off')
    ax.set_title(input_labels[i])
    fig.suptitle('MNIST digits')
    ax.imshow(input_images[i], cmap='binary')
plt.show()

# Normalize the data
x_train = x_train.astype('float32') / 255.0
x_valid = x_valid.astype('float32') / 255.0

# Reshape data
x_train = x_train.reshape(60000, 784)
x_valid = x_valid.reshape(10000, 784)
print(x_train.shape)

# One-hot encode labels
num_categories = 10
y_train = keras.utils.to_categorical(y_train, num_categories)
y_valid = keras.utils.to_categorical(y_valid, num_categories)

# Build the model
model = Sequential([
    Dense(512, activation='relu', input_shape=(784,)),
    Dense(512, activation='relu'),
    Dense(10, activation='softmax')
])

model.summary()

# Compile the model
model.compile(optimizer='adam', loss='categorical_crossentropy', metrics=['accuracy'])

# Train the model
model.fit(x_train, y_train, epochs=10, validation_data=(x_valid, y_valid))
