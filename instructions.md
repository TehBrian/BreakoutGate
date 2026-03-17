# 7.9 Smartphone App 

By: Prof. Dean R. Johnson, Electrical and Computer Engineering, Western Michigan University

This lab will examine the construction of a smartphone app. This exercise complements the hardware exploration of smartphones in the lecture and other labs.

![SmartphoneApp.png](SmartphoneApp.png)

## Overview

In this experiment, you will tinker with a software application for an **Android** smartphone. The app, called **BreakoutGate**, is a breakout arcade game about defending against alien NAND gates gone rogue. The game utilizes a game engine called **AndEngine** and is written in **Java**.

## Basic Concepts

Software applications may be developed for the Android platform with the Android software development kit (SDK), which is based upon the Java development kit (JDK).

To write code, programmers often use an integrated development environment (IDE), which includes a text editor along with other useful features. **Android Studio** is an IDE specifically built for Android development.

Android Studio makes it easy to test your app by running Android **emulators**, which can mimic Android smartphones.

Games are typically developed with **game engines**, which are software frameworks that provide common game-related functions, such as physics, graphics, and animation. **BreakoutGate** uses code from **AndEngine**.

AndEngine is open source and can be found [on a GitHub repository](https://github.com/nicolasgramlich/AndEngine). The engine is outdated and no longer maintained, but it can still be used for Android game development.

## Tasks

### (1) Load the project and run the app

1. Open [Android Studio](https://developer.android.com/studio).
   * It should already be installed on the lab computers.
   * If you're performing this experiment on your personal computer, you will need to install Android Studio by following the link above. Android Studio is compatible with Windows, macOS, and Linux. The installation may take up to an hour.
2. Download the [BreakoutGate App](https://webwriters.com/ece2500/zybook/BreakoutGate-v3.zip) ZIP file from the link. Extract it into a folder.
3. In Android Studio, open the folder as a project.
4. Wait until the project finishes importing.
5. Click the green play button at the top right to run the app.
   - If the play button is grayed out, ensure that a valid Android emulator exists.
   - To do so, open the Device Manager tab on the right, and check that a Virtual Device exists with this configuration: Pixel 5, "R" (API 30, Android 11), "Software" graphics acceleration.
   - Feel free to ask the TA for help!
6. If it works, you should see NAND gate bricks forming inverter circuits as shown in the lab image on the left.

Additional resource: [Create your first Android app](https://developer.android.com/codelabs/basic-android-kotlin-compose-first-app)

### (2) (Optional) Download the app onto your Android device

If you have an Android device, try downloading the app onto it.

1. Connect your Android device to the computer. 
2. Find `breakout.apk` in the `bin` folder on the computer. 
3. Drag it into your `Dropouts` Android folder. Click it.

### (3) Things to experiment with

Here are things you can modify in the Java code to alter the game, some for \*points.

The Java code can be found in `src/main/java/com/example/ecelab/myapplication/BreakoutGate.java`.

Please refer to the lab image above for emulator screen dimensions and corresponding variables.

1. Alter the velocity of the ball.
   - Change the value of `DEMO_VELOCITY` at the beginning of the code.
2. Put your name on the screen. 
   - Search for `nameBox` (using Ctrl+F) and change “Cody Herring” (the student who originally created this app) with your name. Center it on the line.
3. \* Change the color of the NAND gate brick. 
   - Search for `NANDlargeBlue.png` and change it to `NANDlargeRed.png`.
   - (See the files in the graphics `gfx` assets folder (`src/main/assets/gfx`) also shown on small images above.)
   - Copy the resulting `this.mTexture` line of code to the export tester and validate your statement by running the output tester.
4. \* Make SOP NAND circuits.
   - Now let's try some harder things. Right shifting every other row of NAND bricks by ½ brick will cause the NAND gates to form SOP circuits.
   - Your instructor will show you how to use an if-else statement to do this, shifting every other row of gates (the back gates) by `delX = blockWidth/2`.
   - Copy this section of code to the export tester and validate your statement by running the output tester.
5. \* Make proper form SOP circuits. 
   - The SOP circuits in Step 4 are not drawn in 2-level proper form. Replace the shifted Red NAND gates with Green OR2B2 gates (see `gfx` folder assets image above).
   - Copy the resulting `this.mTexture` line of code to the export tester and validate your statement by running the output tester.
6. \* Increase the number of gates.
   - Now replace all the large gates with small gates from the `gfx` assets folder and increase the number of rows of gates in the display, as shown above on right.
   - (If the ball launch is interfered by the lower rows of the gate bricks, lower the ball launching point on the screen.)
   - Copy the resulting `final Ball ball` line of code to the export tester and validate your statement by running the output tester.

Press **Submit** to record the 12 points for this lab.
