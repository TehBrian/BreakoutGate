# 7.9 Smartphone App

By: Prof. Dean R. Johnson, Electrical and Computer Engineering, Western Michigan University

This lab will examine the construction of a smartphone app. This exercise complements the hardware
exploration of smartphones in the lecture and other labs.

![SmartphoneApp.png](SmartphoneApp.png)

## Overview

In this experiment, you will tinker with a software application for an **Android** smartphone. The
app, called **BreakoutGate**, is a breakout arcade game about defending against alien NAND gates
gone rogue. The game utilizes a game engine called **AndEngine** and is written in **Java**.

## Basic Concepts

Software applications may be developed for the Android platform with the Android **software
development kit** (SDK), which is based upon the Java development kit (JDK).

---

To write code, programmers often use an **integrated development environment** (IDE), which includes
a text editor along with other useful features. **Android Studio** is an IDE specifically built for
Android development.

Android Studio makes it easy to test your app by running Android **emulators**, which can mimic
Android smartphones.

Additional
resource: [Create your first Android app with Android Studio](https://developer.android.com/codelabs/basic-android-kotlin-compose-first-app)

---

Games are typically developed with **game engines**, which are software frameworks that provide
common game-related functions, such as physics, graphics, and animation. **BreakoutGate** uses code
from **AndEngine**.

AndEngine is open source and can be
found [on a GitHub repository](https://github.com/nicolasgramlich/AndEngine). The engine is outdated
and no longer maintained, but it can still be used for Android game development.

## Tasks

### (1) Load the project and run the app

1. Open [Android Studio](https://developer.android.com/studio).
    * It should already be installed on the lab computers.
    * If you're performing this experiment on your personal computer, you will need to install
      Android Studio by following the link above. Android Studio is compatible with Windows, macOS,
      and Linux. The installation may take up to an hour.
2. Download the [BreakoutGate](https://webwriters.com/ece2500/zybook/BreakoutGate-v5.zip) ZIP file
   from the link. Extract it into a folder.
3. In Android Studio, open the folder as a project.
4. Wait until the project finishes importing.
5. Click the green play button at the top right to run the app.
    - If the play button is grayed out, ensure that a valid Android emulator exists.
    - To do so, open the Device Manager tab on the right, and check that a Virtual Device exists.
    - If using the lab computers, ensure the Virtual Device has this configuration: Pixel 5, API
      30 "R" (Android 11), **Software** graphics acceleration.
    - Feel free to ask your instructor for help!
6. If it works, you should see NAND gate bricks forming inverter circuits as shown to the left of
   the lab image above.

### (2) (Optional) Download the app onto your Android device

If you have an Android device, try downloading the app onto it.

1. Connect your Android device to the computer via USB cable.
2. Open the target device menu at the top right of Android Studio.
    - The target device menu is to the left of the green play button and the run configurations
      menu.
3. Select your device from the dropdown.
4. Click the green play button.

### (3) Experiment with the code

Here are things you can modify in the Java code to alter the game, some for \*points.

The Java code can be found in `src/main/java/com/example/ecelab/myapplication/BreakoutGate.java`.

---

Some steps require you to comment/uncomment sections of code. To do so, select a section, then press
Ctrl+/.

Each section of code will be tagged with the corresponding question. For example, Q4 will require
you to
search for `Lab Q4` (using Ctrl+F) then either comment or uncomment the code as instructed.

---

1. (Q1) Alter the velocity of the ball.
    - Change the value of `DEMO_VELOCITY` near the beginning of the code.
2. (Q2) Put your name on the screen.
    - Search for `nameBox` (using Ctrl+F), and change “Cody Herring” (the student who originally
      created this app) to your name.
    - Center it on the screen by adjusting the first argument of `new Text()`, which is initially
      `300`.
3. \* (Q3) Change the color of the NAND gate brick.
    - Search for `NANDlargeBlue.png`, and change it to `NANDlargeRed.png`.
    - (See the images in the GFX assets folder (`src/main/assets/gfx`), shown as small images in the
      lab image above.)
    - Copy the resulting `this.mTexture` line of code to the zyBooks code area, and validate it by
      running the output tester.
4. \* (Q4) Make SOP NAND circuits.
    - Now, let's try something harder. Right shifting every other row of NAND gate bricks by ½ brick
      will cause the NAND gates to form SOP circuits.
    - Comment/uncomment the code sections tagged with `Lab Q4`. This will:
        - Add the `deltaX` field.
        - Introduce an if-else statement to determine whether a row will be shifted.
    - The goal is to shift every other row by `blockWidth / 2` but leave the other rows unchanged
      (i.e., shifted by `0`). Can you figure out what the `???`s should be?
    - Copy the resulting if-else block to the zyBooks code area, and validate it by running the
      output tester.
5. \* (Q5) Make proper form SOP circuits.
    - The SOP circuits from Step 4 are not drawn in proper 2-level form. Let's fix that.
    - Replace the shifted Red NAND gates with Green OR2B2 gates. (Hint: Change
      `this.mGateTextureRegion` to `this.mGate2TextureRegion`.)
    - Copy the resulting if-else block to the zyBooks code area, and validate it by running the
      output tester.
6. \* (Q6) Increase the number of NAND gate bricks.
    - Replace the large gates with small gates from the GFX assets folder.
        - Change `NANDlargeRed.png` to `NAND?????Red.png`.
        - Change `OR2B2largeGreen.png` to `OR2B2?????Green.png`.
    - Comment/uncomment the code sections tagged with `Lab Q6`. This will:
        - Increase the number of rows of gates (`numRows`).
        - Adjust `blockWidth` and `blockHeight` to match the new gate size.
        - Lower the ball's launch point on the screen to prevent the lower rows of the gate bricks
          interfering with the ball launching.
    - The resulting grid should look like the arrangement shown to the right of the lab image above.
    - Copy the resulting `this.mGateTextureRegion = ...` section of code (four lines total) to the
      zyBooks code area, and validate it by running the output tester.
7. (Q7) Launch the ball from the middle of the pong.
    - Comment/uncomment the code sections tagged with `Lab Q7`.
8. (Q8) Make the NAND gate bricks shift down over time.
    - Comment/uncomment the code sections tagged with `Lab Q8`. This will:
      - Move rows of hungry NANDs downward, toward the shooter, like an alien arcade game.
      - Feed more rows down from the top.
9. (Q9) Change the paddle to a curved paddle.
    - Comment/uncomment the code sections tagged with `Lab Q9`. This will:
      - Make the ball deflect at non-45° if struck at paddle edges.
    - Change `pong.png` to `pongCurved.png`.
10. Anything else you'd like! Here are further ideas:
    - Add a defeat screen if the score becomes too negative.
    - Add a victory screen if the player defeats all the aliens.
    - Add a countdown before the game starts.
    - Add a pause menu.

Press **Submit** to record the 12 points for this lab.
