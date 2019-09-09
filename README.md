game
====

This project implements the game of Breakout.

Name: Kyle Hong(sh394)

### Timeline

Start Date: 08/31/2019

Finish Date: 09/07/2019

Hours Spent: about 30 hours

### Resources Used
Bouncing Ball : https://stackoverflow.com/questions/20022889/how-to-make-the-ball-bounce-off-the-walls-in-javafx

Collision detection: https://stackoverflow.com/questions/20840587/how-to-use-intersect-method-of-node-class-in-javafx

JavaFx buttons: https://www.geeksforgeeks.org/javafx-button-with-examples/

JavaFx switching scenes : http://www.learningaboutelectronics.com/Articles/How-to-create-multiple-scenes-and-switch-between-scenes-in-JavaFX.php

Reading a text file in java: https://www.geeksforgeeks.org/different-ways-reading-text-file-java/


### Running the Program

Main class: MainScene

Data files needed: text files that contain different type of bricks, Sprite Images

Key/Mouse inputs: 
Use the left and right arrows on your Keyboard to move your paddle, Use the space bar on the keyboard when you have Laser Power to shoot a fireball that can break a brick.

Cheat keys: "1" => to the first level, 
            "2" => to the second level
            "3" => to the last level
            "L" => additional life
            "T" => enable the paddle to move to the different side of the wall.        
                     
  

Known Bugs: multiple collision between the bricks and the ball at the same time. Breaking two bricks at once. Only one of the items of the same kind moves in the screen.

Extra credit: Pong game like versus mode with the computer in level 3. You lose score every time the opponent paddle hits one of the bricks and will eventually lose the game if the score gets 0 before you break every bricks on the screen.


### Notes
I was planning to use JavaFX dialogs to get cheatcode inputs, but I realized that it would disturb the game's flow by pausing the game so I decided to use keycode inputs from the keyboard instead of String inputs from JavaFx dialogs.


### Impressions
Although planning and designing a robust software are still difficult for me, I really gained confidence and learned a lot about JavaFX by programming this game.

