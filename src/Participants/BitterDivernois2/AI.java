package Participants.BitterDivernois2;

import java.util.ArrayList;

import Othello.Move;

public class AI {

	public Move getBestMove(GameBoard gameBoard, int depth, int playerID) {
		System.out.println("I am player: " + playerID);
		this.gameBoard = gameBoard;
		this.playerID = playerID;
		this.gameOpeningstate = 12;
		this.gameEndState = 60 - depth;
		this.noMovePossible = true;
		this.maxDepth = depth;
		int[] result = alphaBeta(gameBoard, new Node(new Move()), depth, 1, Double.NEGATIVE_INFINITY);

		if (noMovePossible) {
			return null;
		}
		return new Move(result[1], result[2]);
	}

	private int[] alphaBeta(GameBoard gameBoard, Node root, int depth, int minOrMax, double parentValue) {
		// minOrMax = 1 : maximize
		// minOrMax = −1 : minimize

		// Get playerID for this tree-level
		int l_PlayerID = (minOrMax == 1) ? this.playerID : Math.abs(this.playerID - 1);

		// System.out.println(indent(depth) + "Player: " + l_PlayerID + " Node
		// name: " + root.name);

		// List possible moves
		ArrayList<Move> possibleMoves = new ArrayList<>(gameBoard.getPossibleMoves(l_PlayerID));

//		System.out.print(indent(depth) + "Possible moves: ");
//		for (Move move : possibleMoves) {
//			System.out.print(move.i + ", " + move.j + " / ");
//		}
//		System.out.println("");

		int evaluation = eval(root, possibleMoves.size(), l_PlayerID, gameBoard);

		root.setEvaluation(evaluation);

		// Detect if we reach a leaf or max depth research
		if (depth == 0 || possibleMoves.isEmpty()) {
			return new int[] { root.getEvaluation(), 0, 0 };
		}

		// We'll do at least one move
		this.noMovePossible = false;

		// Set base value for current node
		int optVal = (int) (minOrMax * Double.NEGATIVE_INFINITY);
		Move optOp = new Move();

		// Loop through all possible ops and apply alphaBeta to each of them
		for (Move op : possibleMoves) {
			// Clone gameboard to apply each op
			GameBoard l_gameBoard = gameBoard.clone();

			//System.out.println(indent(depth) + "Test move: " + op.i + ", " + op.j + ": ");

			// Create new node with op, attach it to current node as child and
			// play the turn
			Node newElement = new Node(op);
			root.addChildNode(newElement);
			l_gameBoard.addCoin(op, l_PlayerID);

			int val[] = alphaBeta(l_gameBoard, newElement, depth - 1, -minOrMax, optVal);

			//System.out.println(indent(depth) + "VALUE: " + val[0]);
			// Detect if returning value is better than previous
			if (val[0] * minOrMax > optVal * minOrMax) {
				optVal = val[0];
				optOp = op;
				//System.out.println(indent(depth) + "OPTVAL: " + optVal);

				// Detect if we can stop searching in the rest of the childs of
				// current node
				// Check also if we have no parent
				if ((optVal * minOrMax > parentValue * minOrMax) && (parentValue != Double.NEGATIVE_INFINITY)) {
					break;
				}
			}
		}
		return new int[] { optVal, optOp.i, optOp.j }; // , optOp;
	}

	private int eval(Node node, int moves, int l_playerID, GameBoard l_gameBoard) {
		
		EvalMatrix evalMatrix = new EvalMatrix(GameBoard.BOARD_SIZE, GameBoard.BOARD_SIZE, l_playerID);
		
		int myCoins = l_gameBoard.getCoinCount(playerID);
		int hisCoins = l_gameBoard.getCoinCount(Math.abs(playerID - 1));

		double d = 0, p = 0, c = 0, l = 0, m = 0;
		
		// ??
		/*int x, y;
		int X1[] = {-1, -1, 0, 1, 1, 1, 0, -1};
		int Y1[] = {0, 1, 1, 1, 0, -1, -1, -1};*/
		
		// Piece difference, frontier disks and disk squares
		for(int i=0; i<GameBoard.BOARD_SIZE; i++)
		{
			for(int j=0; j<GameBoard.BOARD_SIZE; j++) 
			{
				boolean hasCoin = false;
				if(l_gameBoard.getPlayerIDAtPos(i,j) == l_playerID)
				{
					hasCoin = true;
					d += evalMatrix.getValue(i, j);
				} 
				else if(l_gameBoard.getPlayerIDAtPos(i,j) == Math.abs(playerID - 1)) 
				{
					hasCoin = true;
					d -= evalMatrix.getValue(i, j);
				}
				// ??
				/*if(hasCoin)
				{
					for(int k=0; k<GameBoard.BOARD_SIZE; k++)
					{
						x = i + X1[k]; y = j + Y1[k];
						if(x >= 0 && x < bound_x && y >= 0 && y < bound_y && grid[x][y] == '-') {
							if(grid[i][j] == my_color)  my_front_tiles++;
							else opp_front_tiles++;
							break;
						}
					}*/
				}
			}
			
		if(myCoins > hisCoins)
		{
			p = (100.0 * myCoins)/(myCoins + hisCoins);
		}
		else if(myCoins < hisCoins)
		{
			p = -(100.0 * hisCoins)/(myCoins + hisCoins);
		}
		else
		{
			p = 0;
		}

		/*if(my_front_tiles > opp_front_tiles)
			f = -(100.0 * my_front_tiles)/(my_front_tiles + opp_front_tiles);
		else if(my_front_tiles < opp_front_tiles)
			f = (100.0 * opp_front_tiles)/(my_front_tiles + opp_front_tiles);
		else f = 0;*/
		
		// Corner occupancy
		int myCornerCoin = l_gameBoard.getCornerCoinCount(playerID);
		int hisCornerCoin = l_gameBoard.getCornerCoinCount(Math.abs(playerID - 1));
		c = 25 * (myCornerCoin - hisCornerCoin);
		
		// Corner closeness
		int myBorderCoin = l_gameBoard.getEdgeCoinCount(playerID);
		int hisBorderCoin = l_gameBoard.getEdgeCoinCount(Math.abs(playerID - 1));
		l = -12.5 * (myBorderCoin - hisBorderCoin);
		
		// Mobility
		int myMobility = l_gameBoard.getPossibleMoves(playerID).size();
		int hisMobility = l_gameBoard.getPossibleMoves(Math.abs(playerID - 1)).size();
		
		if(myMobility > hisMobility)
		{
			m = (100.0 * myMobility)/(myMobility + hisMobility);
		}
		else if(myMobility < hisMobility)
		{
			m = -(100.0 * hisMobility)/(myMobility + hisMobility);
		}
		else 
		{
			m = 0;
		}
		
		// final weighted score
		double score = (10 * p) + (801.724 * c) + (382.026 * l) + (78.922 * m) + (10 * d); // + (74.396 * f) 
		return (int) score;
	}

	/**
	 * For debug display purpose
	 * 
	 * @param d
	 * @return
	 */
	private String indent(int d) {
		String indentString = "";
		for (int i = d; i < this.maxDepth; i++) {
			indentString += "   ";
		}
		return indentString;
	}

	// INPUTS
	private GameBoard gameBoard;
	private int playerID;

	// TOOLS
	private Boolean noMovePossible;
	private int gameOpeningstate;
	private int gameEndState;
	private int maxDepth;

}
