Tic tac toe:

requirements:
1. multiple games running
2. two players in each game
3. code allows n*n grid size, extensible wrt to this.
4. tic tac toe rules followed
5. only allow valid moves.
6. the final states can be winner, looser or draw.

out of scope:
1. maintaining the score of each player

Entities:

GameManager: registry of all games.
TicTacToeGame: a single game instance, with co-ordination handled.
Player: actual player playing the game.
Board: the real grid, will be present in teh ticTacToeGame
GameState: ongoing, draw or winner

win condition: a game can check it, or even board can also do. Both are fine. It is mostly related to game,
board is dumb entity.

PlayingSymbol: X or O. it is specific to a player. Later it can be changed also.

Board: can use char[][] or Symbol[][] both are fine, I will go with second.

-----
TicTacToeGame {
- Board
- player1
- player2
- currentPlayer
+ getCurrentPlayer(): player
+ isTerminalState()
+ makeMove(int r, int c): GameState
- getStateOfGame(int r, int c): GameState // as each move have a potential of changing state.
- changeTurn(): Player
  }

Board {
- size
+ isValidMove(int r, int c): boolean
  }

GameManager {
- List<TicTacToeGame> games
+ createGame(int n, Player p1, Player p2): TicTacToeGame
  }


private GameState getStateOfGame(int r, int c) {
char currChar = this.board[r][c];
// check in same row
boolean rowFound = true;
for(int j=0; j<n; j++) {
rowFound = rowFound && (board[r][j]==currChar);
}
boolean colFound = true;
for(int i=0; i<n; i++) {
colFound = colFound && (board[i][c]==currChar);
}
int[] r = new int[]{-1,1};
int[] c= new int[]{-1,1};
boolean diagFound = true;
for(int i=0; i<2; i++) {
int dx = r[i], dy= c[i];
int x = r, y = c;
while(true) {
x+=dx;
y+=dy;
if(x<0 || y<0 || x==n || y==n) {
break;
}
diagFound = diagFound &&(board[x][y]==currChar);
}
return diagFound || rowFound|| colFound;
}
}

----

createGame {
try {
lock.lock();
TicTacToeGame game = new TicTacToeGame(player1, plaeyr2);
this.list.add(game);
}
finally {
this.lock.unlock();
}
}