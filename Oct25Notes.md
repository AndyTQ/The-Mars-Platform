# 25 Oct Notes
* Note Taker: Yuchen Wang

## Game Centre Dev Team:
* Attempting to connect the centre’s interface to the login information.
* e.g. “Good Evening, {UserName}” !
* Need to extend the user database: we gotta store more information.

## Game Development Team:
* Try to make it able to simply undo before connect undo to the player accounts.
* To know who is playing the game, two choices:
* 1. Player Account in boardManager ✅
* 2. BoardManager in PlayerAccount
* If the user undo once and make one or two more steps, then we should let them only be able to undo one or two steps at that moment.
* Use built-in Stack() class to save file names (String).
* Need to revise the old “save”:
* Originally, save to temp file when start(starting activity), and saveToFile when onPause() (GameActivity)
* Now, don’t save when start, save once before the user take the first move, and from then on save each time the user make a step

## Question:
* How to pass the users’ information to “Starting Activity”?
