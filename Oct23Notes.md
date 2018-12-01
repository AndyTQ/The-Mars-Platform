# 22 Oct Notes
* Note taker: Yanxin Ding
## Question that arise during the discussion
* how to connect GameActivity and SlidingTiles?
* Where to calculate the score(BoardManager or ScoreBoardManager)?


## Tasks assignment
| Task | Person |
| ----|:-------:|
| Designing the main game centre  | Tianquan Di, Yuchen Wang |
|PlayerAccount| Yanxin Ding|
|GameCentre|Tianquan Di|
|GameActivity (undo, autosave)|Tingfeng Xia, Yuchen Wang|
|ScoreboardActivity|Zhengdan Li|
|DifficultyChoosable|Yanxin Ding|
|ScoreBoardManager|Tianquan Di|
|ScoreBoard|Zhengdan Li|










## Notes regarding the design of the application
* Figured out that in A2, temporary is always updating when making changes,
  but only saved to file when clicking “save” bottom, and will only load the saved file.
* Game activity also superclass.
* AutoSave method and Undo method should be in GameActivity.
* Every game should have a unique ID and icon
* how to use onPause()
* There is a “Intent” class object that could later passed into switchActivity(...) to switch the screen in a android application.
