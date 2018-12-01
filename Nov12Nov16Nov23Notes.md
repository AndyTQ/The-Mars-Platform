# CSC207 Phase 2 <br/>
* Note that this note is copied from Google docs, which contains notes from Nov12nd, Nov16th and Nov23rd.
* Handout link: ```https://q.utoronto.ca/courses/67371/assignments/67584```
## Games
1. Game 1: A flavour of 2048, with undo functionality
2. Game 2: Sudoku, with auto save functionality

## Blueprint(for tasks):

* Game 1: Tianquan Di, Tingfeng Xia
* Game 2: Zhengdan Li, Yanxin Ding, Yuchen Wang
* Testing: All group_0616 members
* Refactoring: All group_0616 members
* SlidingTiles solvable board: Yanxin Ding

### Team Sudoku

* Board size should always be 9 x 9
* Number of missing digits is 60 by default (medium difficulty)
* May add complexity later if all works finish early

#### Notes:
1. Tests
2. game activity, movement controller, gridview refactoring
3. controller implementation for sudoku
4. first the user taps a missing cell
5. then the missing cell will be highlighted
6. then the user taps a digit to fill in
7. the digit will fill in automatically
8. If the player taps a digit first, then throw a message
9. The mechanism for calculating positions will be different
add digit button listeners
10. constructor for board manager is missing (this is a minor problem)


### Team 2048

* Board size can vary, from 3-by-3 to 7-by-7
* The score should be incremented whenever there is a tile that has been merged with another, by two times the amount of the number on the tile.
* We consider add a ```Animation``` to the tiles, both when refreshing the board at the start of the game, and when generating a new tile on an existing board.

#### Notes:

1. The game will randomly generate 2s and 4s at the start of the each new game
2. Using class Canvas to create the tiles in the game and in analogue to other 2048 that are available in the play store. Canvas enables us to draw the tile with increasing “depth” of color as the number goes up.
3. A game is lost if the board is full
4. The size of the board can be chosen from the starting activity, with seek bar.
