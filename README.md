# Mars Game Centre
A game centre that contains fun games and user accounts. <br />

#### Instructions for setting up the environment:
1. Download Android Studio 3.2.1 from https://developer.android.com/studio/
2. Install Android Studio and create a new project by choosing New ->
Project From Version Control -> Git and provide
https://markus.teach.cs.toronto.edu/git/csc207-2018-09-reg/group_0616
as the URL and choose a local directory that you want to store it on your device.
3. Create an Android Virtual Device within Android Studio.
Select a Pixel2 smartphone as the device to emulate, specifiying the device
OS as Android 8.1 API 27. You may need to download this specific build of Android
at this step if you're not using the CDF computers. Leave all other settings
to default values. Create and launch the virtual device and ensure it loads correctly.
4. Close the Android Studio entirely, now you need to open the correct folder based on your need
    * If you wish to view the discussion notes, README.md and TEAM.md, open the project,
     with Android Studio, at directory ```/your/local/path/group_0616/Phase2```,
     you should be able to see the files that you are looking for to the left
     \(under the tab ```1: Project```) then choose ```Project``` in the drop down menu.
    * If you wish to play the game on an emulator or modify the code, open the project ,
    with Android Studio, at directory ```/your/local/path/group_0616/Phase2/GameCentre```.
     On the top right, click the green triangle to run the app,
     and select the pixel 2 emulator that you have already installed on your
     machine to run the app.

#### Instructions for using the Mars Game Centre:
1. First, make sure your device is connected to the Internet.<br/>
2. Sign up with a username, an email address and a password.
Notice that we have cleaned our database recently, so even i
f you have registered before for the Mars Game Centre<br/>
3. Log in with the account you have created.<br/>
     * You can choose to let the Mars game centre remember your email
     and password by ticking "Remember Me".
4. You will see your profile page first. At the bottom of the screen, there is a navigation bar.<br/>
     * On the profile page you can see your username and email, and some other information.
     * You can edit your profile picture, your username, your location and your bio by clicking "Edit Profile".
        * After you click "Edit Profile", you will be redirected to a "Edit My Profile" page.
        * By clicking your existing profile picture at the top, you will be prompted with a dialog that allows you to choose an input source for your new profile image.
        * After you confirmed your selection of picture, you will be redirected to a "Crop" page where you can choose the desired piece of your picture as your new profile image.
        * By clicking on "CROP" on the upper right of the screen, you can get back to your "Edit My Profile" page, where you can preview your change to the profile picture.
        * You can edit your "User Name", "Location" and "My Bio" by clicking the already existing words following the them. A key board will be automatically prompt for you to type.
        * You can choose to save the changes you made by clicking "SAVE"
        or discard them by clicking "CANCEL".
     * You can log out Mars by clicking "Logout".
     * By clicking "Games", you will switch to the game selecting page and choose which game you would like to play.
     * By clicking "Scores", you will be switch the Score Board of the centre
        * On the top left of the page, it shows the name of the game that you are currently viewing.
        * For the games that support multiple levels of difficulty/board sizes, you can click either directly click on the level that you wish to see the scoreboard about, or left/right swipe on the screen to view the scoreboard of the level to the left/right.
        * If you wish to change the game that you want to know scores about, click on the "Select Game" icon to the upper right of the page, where you will be prompt with a dialog to switch between games.
        * By clicking on "My Scores" right next to the "Select Game", you will be directed to a page showing all your highest scores for each level for each game.

#### New Feature included since last update:
* The sliding tiles game now generates only solvable boards, hooray!
* The sliding tiles game now support customizing the tiles!
    * By clicking "CUSTOMIZE TILES", you will be prompted with a dialog for you to select your image from one of the four sources, it works just like the game centre change profile picture functionality!
* Two new games are now available in the Mars game centre, they are: <br />
    1. The Real 2048, which supports in-game undo!
    2. Quick Sudoku, which supports auto saving and loading!
* The Mars game centre now supports profile editing, this includes your profile picture, your username, your location and a short bio. You can do this by clicking "Edit Profile" under the tab "Me".
* You can now sign out of the Mars game centre with out actually quitting the app in android by clicking "Logout" under the tab "Me".
* All the scoreboards have been moved to the Mars Game Centre, under "Scores"

#### About Firebase:
* We use Firebase as the tool for authentication.
* All the users' account information (not including the game scores), including email, display name, and password, are stored in Firebase Database.
* The Firebase Database is managed at https://console.firebase.google.com
* To access Firebase:
	* Provide cscteam0500@gmail.com as username and 1Q2W3e4r as password.
	* Click "Authentication" to see the users that registered into Firebase Authentication.
	* Click "Database" - "Real-Time Database" to see the additional user information that are stored in the database.

## Sliding Tiles
After choosing the Sliding Tiles game, you will be directed to the
 starting page for sliding tiles.
- You can use the spinner to choose a difficulty: 3x3, 4x4 or 5x5. (3x3 by default).
- You can use the seek bar to select the undo limit(i.e.,
how many steps you can undo) form 0 to 9, or *infinity*(unlimited) (3 by default).
- You can customize your tiles bu clicking "CUSTOMIZE TILES" -> "Customize Images",
you will be prompted with a dialog to choose your image source from one of the following:
Camera, Files, Drive or Photos. After selecting and cropping the image, you can start a
fresh new game to see your changes to the tiles! Try to arrange them back in the way of
the original picture! If you wish, you could switch back to the default number tiles by
clicking "Use Default Number" under "CUSTOMIZE TILES" on the starting menu.
- By clicking "NEW GAME", you will be directed to the game playing page.
The timer shows the elapsed time. You can click "UNDO"
button to revert the number of steps up to the undo limit you chose.
- If a new user ,who has no previous saved game, clicks "LOAD GAME",
 a new game with complexity of 3 will start.
- Every swap you make will be auto-saved. If you quit in progress
and click "LOAD GAME" the next time,
you will continue your last saved game regardless of the complexity
your selected this time.
Everything will be loaded: the tile board, elapsed time, the record
of your number of swaps, and you can undo your previous steps.
- By clicking "SAVE GAME", it also saves your game state.
- After you win, you will see a congratulation page with a score calculated
based on the time you used and the number of swaps. On this page, you can click "QUIT GAME"
to return to the game selecting page, or click "RETURN TO MENU" to
return to the starting page of the sliding tile game.

## The Real 2048
After choosing The Real 2048 game, you will be directed to the starting page of the game. <br/>
- You can use the seek bar on the starting page to select the size of board that you wish to play.
- By clicking "NEW GAME" you will enter the game with the selected board size.
- By swiping on the grid of the 2048 tiles, you will be able to "snap" all the tiles
toward that direction, at which time tiles with same number will be merged together if they are the same!
- If you have filled the board, at which time you have not yet reached 2048,
and there is no more move that can be made, i.e. you lost the game,
you will be prompted with a dialog where you can choose to restart the
game with the same size of board or quit to The Real 2048 game, and return to the game centre
- If you have one of your tile 2048, you win the game. At which time you
will be redirected to a winning page. You can choose to return to the starting page of
game The Real 2048 by clicking "RETURN TO MENU" or quit The Real 2048 game and
return to the game centre by clicking "QUIT GAME"

## Quick Sudoku
After choosing Quick Sudoku game, you will be directed to the starting page of the game. <br/>
- By clicking "LOAD GAME" you will continue your last saved game.
- By clicking "NEW GAME" you will enter the game.
- There is a 9x9 grid on the screen, which contains a valid Sudoku puzzle with 50 digits missing. Below the grid
there are 9 buttons that represents the digit 1-9.
- First select a cell in the grid that you want to fill in. As you do so, the cell turns red.
- Then tap a digit button to fill in the cell you have chosen. The cell will
be filled and turns back to the original colour.
- After filling in a cell, you can still change the numbers.
- Every time you fill in a cell, your state of game will be saved automatically
- Once you correctly fill in all the cells with digits, you win the game.
- After you win, you will see a congratulation page with a score calculated
based on the time you used. On this page, you can click "QUIT GAME"
to return to the game selecting page, or click "RETURN TO MENU" to
return to the starting page of the sudoku game.