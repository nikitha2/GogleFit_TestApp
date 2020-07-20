# GogleFit_TestApp
## Important Note:
I used a test account that did not have any data points so inserted dummy date. if your account has data, change line 124 in class OAuthAuthorization as shown below so that if a particular day doesnt have steps it will show 0 else display steps for the day.

            // if your account has data. Please change the line to
            // dataPoint0=dataPoint0.setIntValues(0);

## Description:
GogleFit_TestApp integrates with Google Fit. Fetch the daily step count data and display this information in a scrollable table with a single day's worth of total steps per 
row starting with the current day. Only the last two weeks’ worth of steps are displayed.Toggles the order of the list between chronological and reverse chronological order when clicked in the row header. 

Topics covered: MVVM architecture, adaptors, RecyclerView, GoogleFit API, GoogleSignIn API, material design pattern and other basic concepts.

## Installation
1. Unzip the github project to a folder
2. Open Android Studio. Go to File -> New -> Import Project
3. choose GogleFit_TestApp to import and then click Next->Finish.
4. Run the project in android studio with emulator or on android phone to see the App running.
 
## Dependencies
make sure you have the below dependencies in build.gradle.app file
* implementation fileTree(dir: "libs", include: ["*.jar"])
* implementation 'androidx.appcompat:appcompat:1.1.0'
* implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
* implementation 'com.google.android.material:material:1.1.0'
* implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0'
* testImplementation 'junit:junit:4.13'
* androidTestImplementation 'androidx.test.ext:junit:1.1.1'
* androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'
* implementation 'com.google.android.gms:play-services-fitness:18.0.0'
* implementation 'com.google.android.gms:play-services-auth:18.1.0'

## Final App
http://youtu.be/V9JzEPstH1Y?hd=1

## App architecture
<p align="center">
  <img src="https://github.com/nikitha2/images_for_ReadMe/issues/1#issue-661831156" width="350" title="App architecturet">
</p>

## TestCredentials for testing:
test.fit.gogle@gmail.com
test_googlefit123

## License
GogleFit_TestApp is distributed under the MIT license.
