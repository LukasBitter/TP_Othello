package Participants.BitterDivernois;

import java.io.IOException;
import java.util.ArrayList;

import Othello.Move;

public class AI {

	public Move getBestMove(GameBoard gameBoard, int depth, int playerID) {
		System.out.println("I am player: " + playerID);
		this.gameBoard = gameBoard;
		this.playerID = playerID;
		this.gameOpeningstate = 12;
		this.gameEndState = 60 - depth;
		this.evalMatrix = new int[8][8];
		this.noMovePossible = true;
		this.maxDepth = depth;
		init();
		int[] result = alphaBeta(gameBoard, new Node(new Move()), depth, 1, Double.NEGATIVE_INFINITY);

		if (noMovePossible) {
			return null;
		}
		return new Move(result[1], result[2]);
	}

	private void init() {
		// Turn count
		this.turn = (gameBoard.getCoinCount(playerID) - 2) / 2;

		// Set coin values
		this.evalMatrix[0][0] = this.evalMatrix[0][7] = this.evalMatrix[7][0] = this.evalMatrix[7][7] = 500;

		// Set next to coins values
		this.evalMatrix[1][1] = this.evalMatrix[1][6] = this.evalMatrix[6][1] = this.evalMatrix[6][6] = -250;
		this.evalMatrix[0][1] = this.evalMatrix[1][0] = this.evalMatrix[7][6] = this.evalMatrix[6][7] = -150;
		this.evalMatrix[0][6] = this.evalMatrix[1][7] = this.evalMatrix[6][0] = this.evalMatrix[7][1] = -150;

		// set border values
		this.evalMatrix[0][2] = this.evalMatrix[2][0] = this.evalMatrix[0][5] = this.evalMatrix[5][0] = 30;
		this.evalMatrix[5][2] = this.evalMatrix[2][5] = this.evalMatrix[7][5] = this.evalMatrix[5][7] = 30;
		this.evalMatrix[0][3] = this.evalMatrix[0][4] = this.evalMatrix[7][3] = this.evalMatrix[7][4] = 10;
		this.evalMatrix[3][0] = this.evalMatrix[4][0] = this.evalMatrix[3][7] = this.evalMatrix[4][7] = 10;

		// Set center values
		this.evalMatrix[3][3] = this.evalMatrix[3][4] = this.evalMatrix[4][3] = this.evalMatrix[4][4] = 16;
		this.evalMatrix[2][3] = this.evalMatrix[2][4] = this.evalMatrix[5][3] = this.evalMatrix[5][4] = 2;
		this.evalMatrix[3][2] = this.evalMatrix[3][5] = this.evalMatrix[4][2] = this.evalMatrix[4][5] = 2;
		this.evalMatrix[2][2] = this.evalMatrix[5][2] = this.evalMatrix[2][5] = this.evalMatrix[5][5] = 1;
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
		
		int evaluation = eval(root, possibleMoves.size());
		
		root.setEvaluation(evaluation);

		/*
		 * System.out.print(indent(depth) + "Possible moves: "); for(Move
		 * move:possibleMoves){ System.out.print(move.i + ", " + move.j + " / "
		 * ); } System.out.println("");
		 */

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

			// System.out.println(indent(depth) + "Test move: " + op.i + ", " +
			// op.j + ": ");
			
			// Create new node with op, attach it to current node as child and play the turn
			Node newElement = new Node(op);
			root.addChildNode(newElement);
			l_gameBoard.addCoin(op, l_PlayerID);

			int val[] = alphaBeta(l_gameBoard, newElement, depth - 1, -minOrMax, optVal);

			// Detect if returning value is better than previous
			if (val[0] * minOrMax > optVal * minOrMax) {
				optVal = val[0];
				optOp = op;

				// Detect if we can stop searching in the rest of the childs of current node
				if ((optVal * minOrMax > parentValue * minOrMax) && (parentValue != Double.NEGATIVE_INFINITY)) {
					break;
				}
			}
		}
		return new int[] { optVal, optOp.i, optOp.j }; // , optOp;
	}

	private int eval(Node node, int moves) {
		// Turn count
		int l_turn = (gameBoard.getCoinCount(playerID) + gameBoard.getCoinCount(Math.abs(playerID-1))) - 2;
		
		// Adapt matrix according game state
		if (l_turn <= gameOpeningstate) {
			// Game opening state
			return moves + this.evalMatrix[node.getMove().i][node.getMove().j];
		} else if (l_turn > this.gameOpeningstate && l_turn < this.gameEndState) {
			// Game middle state
			return moves + this.evalMatrix[node.getMove().i][node.getMove().j];
		} else {
			// Game end state
			return this.evalMatrix[node.getMove().i][node.getMove().j];
		}

	}

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
	private int turn;
	private Boolean noMovePossible;
	private int gameOpeningstate;
	private int gameEndState;
	private int[][] evalMatrix;
	private int maxDepth;

}
