## Game Centre Team:
* Right now E-mail is the unique identifier key for users. Consider change the unique identifier to username.
*  Need to smooth the transition between login and gamecentre -- load all information before presenting the front-end to users
*  We gotta finish implementing all the remaining front-end. Including, but not limited, to:
  * User’s profile
  * Game page
  * Rankings page
*  After implementation of front end, we set up the game icons; allow them to start the game after being clicked.


## Undo and Auto save team:
* We should use different systems for undo and autosave
  * undo: save states,
  i.e., save positions in a stack and save the stack into a file
  * autosave: save file(boardmanager object)every single step
  Undo and Autosave Team:
  * A plausible plan for implementation of such functionality(undo):
    * define class SizableStack() that extends Java stack, we add another functionality: getSize(). This function should be useful for the implementation of calculating the score of the game(the number of steps taken?). Though not sure about how we should account for the undo-ed steps for the total number of steps.
    * As mentioned above, save such stack using the naming format <playerId> + <nameOfGame> + “history.tmp/.ser”. In such way, each player only have one copy of each game saved on the device. When a player start a new game that has a existing history, the old file gets overwritten, and the new empty stack takes place, and on and on and on.
    * Note that the stack contains all the non-popped elements at everytime one try to read from the file, so it basically contains the full history of moves that a player made.
    * Undoing would be process that we pop from the respective stack for that specific player and specific game, and use the already implemented swap_tiles(int, int, int, int) to “unwind” the whole operation.

## Scoreboard team:
* Create per-user and per-game scoreboard activity and layout.
* Decide where to call updateScore method.
* Improve the algorithm for calculating score.
* Add a timer to board.
* Extract superclass of scoreboard activity.

