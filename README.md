# Indoor-Localization-PDR-and-Wardriving
#Some key features of this app



● Implemention of an indoor localization app that will provide coarse-grained accuracy.
● Use of pedestrian dead reckoning (PDR) to compute displacement from accelerometer reading.

○ For this, I compute our stride length 
○ Compute the number of steps taken (by using accelerometer pattern)
○ Determine the direction of moving (by using a magnetometer)

Note that here we assume that the user holds the phone in hand and y-axis is towards north or to your front. You can also assume that the user does not shake his phone, and we know the GPS of the entrance point from where the user started walking.

● Showcase of the steps that we have taken and the direction of our walk. A nice UI to showcase this information in App.

● Reseting of error in PDR using anchor points that we identify using WiFi RSSI

● Get the WiFi scan results to know the list of access points nearby and their RSSI values

● Wardrive inside your home/building/hostel for getting RSSI measurements of the APs from different regions or rooms of your home/respective buildings using WiFi scan results. 

● Given a test scenario, where a new user walks in determine the location of the user separately using PDR and RSSI-based wardriving. Note that for RSSI matching, it will match it to stored information with a single point that is most similar to the test data. Design an appropriate UI to show the location.
