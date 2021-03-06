# Chain-reaction

This is a game created using java,javaFx and fxml.
About the game :

1. The gameplay takes place in a m x n board.
2. For each cell in the board, we define a critical mass. The critical mass is equal to the number of orthogonally adjacent cells. That would be 4 for usual cells, 3 for cells in the edge and 2 for cells in the corner. 
3. All cells are initially empty. The Red and the Green player take turns to place "orbs" of their corresponding colors. The Red player can only place an (red) orb in an empty cell or a cell which already contains one or more red orbs. When two or more orbs are placed in the same cell, they stack up. 
4. When a cell is loaded with a number of orbs equal to its critical mass, the stack immediately explodes. As a result of the explosion, to each of the orthogonally adjacent cells, an orb is added and the initial cell loses as many orbs as its critical mass. The explosions might result in overloading of an adjacent cell and the chain reaction of explosion continues until every cell is stable. 
5. When a red cell explodes and there are green cells around, the green cells are turned to red and the other rules of explosions still follow.The same rule is applicable for other colors. 
6. The winner is the one who eliminates every other player's orbs.
