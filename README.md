AI Powered Smart Doorbell System

A smart IoT-based doorbell system built with Raspberry Pi 4, Computer Vision, and Machine Learning, integrated with an Android application for real-time monitoring and control. The system performs face detection, liveness detection (anti-spoofing), and facial recognition to provide secure access to homes and properties.

Features:

Detects human presence using PIR motion sensors and Raspberry Pi Camera.
Sends real-time notifications with visitor images to the Android app.
Prevents spoofing attempts using CNN-based liveness detection and blink detection.
Automatically unlocks the door for authorized visitors.
Allows the user to open the door or trigger an alarm remotely via the app.
Enables adding new authorized visitors directly from the app.

System Architecture

Hardware: 

Raspberry Pi 4 Model B, Camera Module V2, PIR Motion Sensor, Arduino Uno, Solenoid Lock, Alarm Buzzer.

Software:

Raspberry Pi OS (Raspbian)

Python (OpenCV, TensorFlow, face-recognition, dlib, etc.)

Apache, PHP, MySQL for database communication

Android App (Java, XML, PHP scripts)

Installation & Setup (Linux / Raspberry Pi)

1. Install Raspberry Pi OS

Download and flash Raspberry Pi OS onto an SD card:

sudo apt update
sudo apt install rpi-imager
rpi-imager


Select Raspberry Pi OS (32-bit), write it to the SD card, and boot the Pi.

2. Install Dependencies

Update packages:

sudo apt update && sudo apt upgrade -y


Install required packages:

sudo apt install apache2 php libapache2-mod-php mysql-server php-mysql -y
sudo apt install python3 python3-pip python3-opencv -y


Install Python libraries:

pip3 install opencv-python dlib face-recognition tensorflow imutils numpy

3. Setup Database

Start MySQL server:

sudo service mysql start

Create a database and tables for Notifications and Commands.

4. Prepare Dataset for Liveness Detection

Run the provided script to build datasets (If desired to use a self-made dataset):

python3 gather_examples.py --input videos/fake.mov --output dataset/fake --detector face_detector --skip 1
python3 gather_examples.py --input videos/real.mov --output dataset/real --detector face_detector --skip 1


Train the CNN model:

python3 train.py --dataset dataset --model liveness.model --le le.pickle

5. Run the System

Execute the main script:

python3 liveness_demo.py --model liveness.model --le le.pickle --detector face_detector

Android Application

Built using Android Studio (Java + XML).

Connects to the Raspberry Pi server via PHP and MySQL.

Features:

Open Door
Turn On Alarm
Add New Authorized Person
View Recent Visits
