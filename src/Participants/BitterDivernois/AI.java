package Participants.BitterDivernois;

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
		// Count coins
		int myCoins = l_gameBoard.getCoinCount(playerID);
		int hisCoins = l_gameBoard.getCoinCount(Math.abs(playerID - 1));
		// Turn count
		int l_turn = (myCoins + hisCoins) - 4;

		EvalMatrix evalMatrix = new EvalMatrix(GameBoard.BOARD_SIZE, GameBoard.BOARD_SIZE, l_playerID);

		//evalMatrix.displayEvalMatrix(l_gameBoard);
		// Adapt matrix according game state
//		System.out.println(
//				"MOVES = " + moves + " / EVALMATRIX = " + evalMatrix.getValue(node.getMove().i, node.getMove().j));
		if (l_turn <= gameOpeningstate) {
			// Game opening state
			return 2 * moves + evalMatrix.getValue(node.getMove().i, node.getMove().j);
		} else if (l_turn > this.gameOpeningstate && l_turn < this.gameEndState) {
			// Game middle state
			evalMatrix.setMiddleGameValues();
			return 2 * moves + evalMatrix.getValue(node.getMove().i, node.getMove().j);
		} else {
			// Game end state
			return (myCoins - hisCoins) * 10 + evalMatrix.getValue(node.getMove().i, node.getMove().j);
		}
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
