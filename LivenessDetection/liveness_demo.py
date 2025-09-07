# import the necessary packages
import imutils
from imutils.video import VideoStream
from tensorflow.keras.preprocessing.image import img_to_array
from tensorflow.keras.models import load_model
import numpy as np
import argparse
import pickle
import time
import cv2
import os
import face_recognition
import base64
import mysql.connector


myDB = mysql.connector.connect(host="localhost", user="root", password="cenG@495", database="CENG495")
myCursor = myDB.cursor()

	
# below function sends a notification to the user
def send_notification(personImage):
 myDB = mysql.connector.connect(host="localhost", user="root", password="cenG@495", database="CENG495")
 myCursor = myDB.cursor()
 file = "/home/legion/Desktop/LivenessDetection/Test_Image/testImage.jpg"
 visitorImage = cv2.imread(file)
 with open(file, "rb") as f:
  data12 = f.read()
  stringImage = base64.b64encode(data12)
  sqlStatement = "INSERT INTO Notifications (image, description) VALUES (%s, %s)"
  myCursor.execute(sqlStatement, (stringImage, "Unknown Visitor"))
  myDB.commit()

def send_message():
 myDB = mysql.connector.connect(host="localhost", user="root", password="cenG@495", database="CENG495")
 myCursor = myDB.cursor()
 sqlStatement = "INSERT INTO Notifications (image, description) VALUES (%s, %s)"
 myCursor.execute(sqlStatement, ("No Image", "Spoof Attempt"))
 myDB.commit()
# =================================================================
# get the max ID every time the program is run (for returning last command).	
getIdStatement = "SELECT MAX(id) from commands"
myCursor.execute(getIdStatement)
ids = myCursor.fetchall()[0][0]
MAXID = ids
#===================================================================

# return all images saved in Authorized_People for facial recognition.
path = '/home/legion/Desktop/LivenessDetection/Authorized_People'
images = []
classNames = []
myList = os.listdir(path)
print(myList)
for cls in myList:
	currentImage = cv2.imread(f'{path}/{cls}')
	images.append(currentImage)
	classNames.append(os.path.splitext(cls)[0])

# ===============================================================

# extract the encodings of all returned images.
def findEncodings(images):
    encodeList = []
    for img in images:
        img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB) # Convert from BGR to RGB
        encode = face_recognition.face_encodings(img)[0]
        encodeList.append(encode)
    return encodeList
encodeListIsKnown = findEncodings(images)
print('Encoding Complete')
#===============================================================

# facial recognition starts here
def run_face_recognition(img):
 file = "/home/legion/Desktop/LivenessDetection/Test_Image/testImage.jpg"
 cv2.imwrite(file, img)
 testImage = face_recognition.load_image_file(file)
 testImage = cv2.cvtColor(testImage, cv2.COLOR_BGR2RGB)
	
 img2 = cv2.resize(img, (0, 0), None, 0.25, 0.25)
 img2 = cv2.cvtColor(img2, cv2.COLOR_BGR2RGB)

 facesCurFrame = face_recognition.face_locations(img2)
 encodesCurFrame = face_recognition.face_encodings(img2, facesCurFrame)
 recognized = False
 for encodeFace, faceLoc in zip(encodesCurFrame, facesCurFrame):
  matches = face_recognition.compare_faces(encodeListIsKnown, encodeFace)
  faceDis = face_recognition.face_distance(encodeListIsKnown, encodeFace)
  print(faceDis)
  matchIndex = np.argmin(faceDis) # get the minimum distance, the most matched image
  print(matches)
  if matches[matchIndex]:
   print("matched")
   name = classNames[matchIndex].upper()
   print("Authorized Visitor: ", name)
   print("Door Opened")
   recognized = True

 if(not recognized):
  send_notification(img)  
# ===================================================================================================   


# construct the argument parser and parse the arguments
ap = argparse.ArgumentParser()
ap.add_argument("-m", "--model", type=str, required=True,
	help="path to trained model")
ap.add_argument("-l", "--le", type=str, required=True,
	help="path to label encoder")
ap.add_argument("-d", "--detector", type=str, required=True,
	help="path to OpenCV's deep learning face detector")
ap.add_argument("-c", "--confidence", type=float, default=0.5,
	help="minimum probability to filter weak detections")
args = vars(ap.parse_args())

# load our serialized face detector from disk
print("[INFO] loading face detector...")
protoPath = os.path.sep.join([args["detector"], "deploy.prototxt"])
modelPath = os.path.sep.join([args["detector"],
	"res10_300x300_ssd_iter_140000.caffemodel"])
net = cv2.dnn.readNetFromCaffe(protoPath, modelPath)
# load the liveness detector model and label encoder from disk
print("[INFO] loading liveness detector...")
model = load_model(args["model"])
#model = load_model(args["model"], custom_objects={'ArcFace': ArcFace})
le = pickle.loads(open(args["le"], "rb").read())
# initialize the video stream and allow the camera sensor to warmup
print("[INFO] starting video stream...")
vs = VideoStream(src=0).start()
time.sleep(2.0)

# loop over the frames from the video stream
while True:

#check the command received by the user and execute it..
	myDB = mysql.connector.connect(host="localhost", user="root", password="cenG@495", database="CENG495")
	myCursor = myDB.cursor()
	getIdStatement = "SELECT MAX(id) from commands"
	myCursor.execute(getIdStatement)
	ids = myCursor.fetchall()[0][0]	
	#return the command associated with the maximum id.
	command = """SELECT command from commands where id = %s"""
	myCursor.execute(command, (ids,))
	cmd = myCursor.fetchall()[0][0]
	#if ids == MAXID: # No new command has been received yet
		#print("No command has been received yet ", ids)
	if ids > MAXID:# A new command has been received
		MAXID = ids
		if cmd == "open": # Resume functioning (break from malfunction loop)
			print("Door Opened")
		elif cmd == "alarm":
			print("Alarming System On")
		elif cmd == "addImage":#if command add image is received, return the last image stored in the database and save it to authorized people dataset
			returnImage = """SELECT image,name from commands where id = %s"""
			myCursor.execute(returnImage, (ids,))
			result = myCursor.fetchall()
			image = result[0][0]
			name = result[0][1]
			print("name goes here: ", name)
			file = os.path.join("/home/legion/Desktop/LivenessDetection/Authorized_People/", "{}-A1.jpg".format(name, ))
			with open(file, "wb") as fh:
   			 fh.write(base64.b64decode(image))
   			 print("Image saved to file")
	
	# grab the frame from the threaded video stream and resize it
	# to have a maximum width of 600 pixels
	frame = vs.read()
	frame = imutils.resize(frame, width=600)
	# grab the frame dimensions and convert it to a blob
	(h, w) = frame.shape[:2]
	blob = cv2.dnn.blobFromImage(cv2.resize(frame, (300, 300)), 1.0,
		(300, 300), (104.0, 177.0, 123.0))
	# pass the blob through the network and obtain the detections and
	# predictions
	net.setInput(blob)
	detections = net.forward()
	
	# loop over the detections
	for i in range(0, detections.shape[2]):
		# extract the confidence (i.e., probability) associated with the
		# prediction
		confidence = detections[0, 0, i, 2]
		# filter out weak detections
		if confidence > args["confidence"]:
			# compute the (x, y)-coordinates of the bounding box for
			# the face and extract the face ROI
			box = detections[0, 0, i, 3:7] * np.array([w, h, w, h])
			(startX, startY, endX, endY) = box.astype("int")
			# ensure the detected bounding box does fall outside the
			# dimensions of the frame
			startX = max(0, startX)
			startY = max(0, startY)
			endX = min(w, endX)
			endY = min(h, endY)
			# extract the face ROI and then preproces it in the exact
			# same manner as our training data
			face = frame[startY:endY, startX:endX]
			face = cv2.resize(face, (32, 32))
			face = face.astype("float") / 255.0
			face = img_to_array(face)
			face = np.expand_dims(face, axis=0)
			# pass the face ROI through the trained liveness detector
			# model to determine if the face is "real" or "fake"
			preds = model.predict(face)[0]
			j = np.argmax(preds)
			label = le.classes_[j]
			
			if label == "fake":
				send_message()
				while True:
				#return the maximum command id from the database
					myDB = mysql.connector.connect(host="localhost", user="root", password="cenG@495", database="CENG495")
					myCursor = myDB.cursor()
					getIdStatement = "SELECT MAX(id) from commands"
					myCursor.execute(getIdStatement)
					ids = myCursor.fetchall()[0][0]	
				#return the command associated with the maximum id.
					command = """SELECT command from commands where id = %s"""
					myCursor.execute(command, (ids,))
					cmd = myCursor.fetchall()[0][0]
				
					if ids == MAXID: # No new command has been received yet
						print("Fake Face Detected")
					elif ids > MAXID:# A new command has been received
						MAXID = ids
						if cmd == "reset": # Resume functioning (break from malfunction loop)
							print("Resumed")
							break
						else:# Stay in this loop (keep malfunctioning)
							print("Stay here")
			else:
					run_face_recognition(frame)
			
			# draw the label and bounding box on the frame
			label = "{}: {:.4f}".format(label, preds[j])
			cv2.putText(frame, label, (startX, startY - 10),
				cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255), 2)
			cv2.rectangle(frame, (startX, startY), (endX, endY),
				(0, 0, 255), 2)
				
				# show the output frame and wait for a key press
	cv2.imshow("Frame", frame)
	key = cv2.waitKey(1) & 0xFF
	# if the `q` key was pressed, break from the loop
	if key == ord("q"):
		break		
		
		# do a bit of cleanup
cv2.destroyAllWindows()
vs.stop()
	