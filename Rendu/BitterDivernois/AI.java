package Participants.BitterDivernois;

import java.util.ArrayList;

import Othello.Move;

/**
 * 
 * @author lukas.bitter / margaux.divernois
 *
 */
public class AI {

	/**
	 * returns best move for player or null if no move is possible
	 * 
	 * @param gameBoard
	 * @param depth
	 * @param playerID
	 * @return
	 */
	public Move getBestMove(GameBoard gameBoard, int depth, int playerID) {
		System.out.println("I am player: " + playerID);
		this.playerID = playerID;
		this.gameOpeningstate = 12;
		this.gameEndState = 60 - depth;
		this.maxDepth = depth;
		int[] result = alphaBeta(gameBoard, new Node(new Move()), depth, 1, Double.NEGATIVE_INFINITY);

		if (result[1] == -1) {
			return null;
		}
		return new Move(result[1], result[2]);
	}

	/**
	 * AlphaBeta logic
	 * 
	 * @param gameBoard
	 * @param root
	 * @param depth
	 * @param minOrMax
	 * @param parentValue
	 * @return
	 */
	private int[] alphaBeta(GameBoard gameBoard, Node root, int depth, int minOrMax, double parentValue) {
		// minOrMax = 1 : maximize
		// minOrMax = −1 : minimize

		// Get playerID for this tree-level
		int l_PlayerID = (minOrMax == 1) ? this.playerID : Math.abs(this.playerID - 1);

		// List possible moves
		ArrayList<Move> possibleMoves = new ArrayList<>(gameBoard.getPossibleMoves(l_PlayerID));

		int evaluation = eval(root, possibleMoves.size(), l_PlayerID, gameBoard);
		root.setEvaluation(evaluation);

		// Detect if we reach a leaf or max depth research
		if (depth == 0 || possibleMoves.isEmpty()) {
			return new int[] { root.getEvaluation(), -1, -1 };
		}

		// Set base value for current node
		int optVal = (int) (minOrMax * Double.NEGATIVE_INFINITY);
		Move optOp = new Move();

		// Loop through all possible ops and apply alphaBeta to each of them
		for (Move op : possibleMoves) {

			// Clone gameboard to apply each op
			GameBoard l_gameBoard = gameBoard.clone();

			// Create new node with op, attach it to current node as child and
			// play the turn
			Node newElement = new Node(op);
			root.addChildNode(newElement);
			l_gameBoard.addCoin(op, l_PlayerID);

			int val[] = alphaBeta(l_gameBoard, newElement, depth - 1, -minOrMax, optVal);

			// Detect if returning value is better than previous
			if (val[0] * minOrMax > optVal * minOrMax) {
				optVal = val[0];
				optOp = op;

				// Detect if we can stop searching in the rest of the childs of
				// current node. Check also if we have no parent
				if ((optVal * minOrMax > parentValue * minOrMax) && (parentValue != Double.NEGATIVE_INFINITY)) {
					break;
				}
			}
		}
		return new int[] { optVal, optOp.i, optOp.j };
	}

	/**
	 * Node evaluation, depending on gamestate Three parameters influence the
	 * nodes strength: - the position, given by the evaluation matrix - the
	 * mobility, given by the quantity of possible moves - the number of coins
	 * Each of these parameters vary depending on the game state
	 * 
	 * @param node
	 * @param moves
	 * @param l_playerID
	 * @param l_gameBoard
	 * @return
	 */
	private int eval(Node node, int moves, int l_playerID, GameBoard l_gameBoard) {
		// Count coins
		int myCoins = l_gameBoard.getCoinCount(playerID);
		int hisCoins = l_gameBoard.getCoinCount(Math.abs(playerID - 1));
		// Turn count
		int l_turn = (myCoins + hisCoins) - 4;

		EvalMatrix evalMatrix = new EvalMatrix(GameBoard.BOARD_SIZE, GameBoard.BOARD_SIZE);

		int mobility = l_gameBoard.getPossibleMoves(playerID).size() - l_gameBoard.getPossibleMoves(Math.abs(playerID - 1)).size(); 
		int materiel = 	l_gameBoard.getCoinCount(playerID)-l_gameBoard.getCoinCount(Math.abs(playerID - 1));
		int coins = l_gameBoard.getCornerCoinCount(playerID) - l_gameBoard.getCornerCoinCount(Math.abs(playerID - 1));
		
		// Adapt matrix according game state
		if (l_turn <= gameOpeningstate) {
			// Game opening state: moves have a lot of importance and position
			return (int) 2 * mobility + materiel + evalMatrix.getValue(node.getMove().i, node.getMove().j);
		} else if (l_turn > this.gameOpeningstate && l_turn < this.gameEndState) {
			// Game middle state: same as in opening, but we set stronger weigth to borders 
			evalMatrix.setMiddleGameValues();
			return (int) 0.5 * mobility + materiel + 3 * coins + 6 * evalMatrix.getValue(node.getMove().i, node.getMove().j);
		} else {
			// Game end state: here the most important factor is the amount of coins
			return (int) 0.1 * mobility + 3 * materiel + 3 * evalMatrix.getValue(node.getMove().i, node.getMove().j);
		}
	}

	// INPUTS
	private int playerID;

	// TOOLS
	private int gameOpeningstate;
	private int gameEndState;
	private int maxDepth;

}
