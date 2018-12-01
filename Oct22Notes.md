# 22 Oct Notes
* Note taker: Tingfeng Xia

## Question that arise during the discussion
* When GameCentre receives the username and password input from player, how do we access the database and check if both are correct?
* How to save the game status (into a user account)? How do we store a user account?(including all related info for a user)
* Insight to the previous line: The users’ data should stored locally, or otherwise there is no point to login with username and password.
* slidingTiles as a subclass of Game, but it’s a package
* Should chooseDifficulty() be remained in Game abstract class?
* (Bonus) Consider using Stack in implementing the undo method, default=3, user could choose the number of steps that the user can undo in the game. Remember to check the condition (Stack.isEmpty()), should print “Reached end” at such time.
* How to start the game?

## Notes regarding the design of the application
* Game centre uses other game classes, no inheritance at this point
* All games might extend an abstract game class, which has a scoreboard , undo and auto save feature.
* Undo and auto-save is together
* “StartingActivity” is general, can be used for all games

* More notes may be added later.
