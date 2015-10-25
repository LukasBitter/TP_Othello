package Participants.BitterDivernois;

import java.io.IOException;

import Othello.Move;


/**
 * Managages the player
 * @author Ellenberger Patrick and Moll Adrian
 *
 */
public class Joueur extends Othello.Joueur{

	private int depth;
	private int enemyID;
	private GameBoard gameBoard;
	
	public Joueur(){
		
	}
	
	public Joueur(int depth, int playerID) {
		super();
		System.out.println("depth: " + depth);
		this.depth = depth;
		this.playerID = playerID;
		this.enemyID = 1-playerID;
		this.gameBoard = new GameBoard();
	}


	/**
	 * Method called every time the player has to play
	 */
	public Move nextPlay(Move move) {
		
		// Add enemy coin to the gameboard
		if(move != null){
			System.out.println("His move: " + move.i + ", " + move.j);
		}
		gameBoard.addCoin(move, enemyID);
		System.out.println("nextPlay: " + this.playerID);

		/*try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("no input!");
		}*/
		
		// Get the best move (null if no move possible)
		Move bestMove = new AI().getBestMove(gameBoard, depth, this.playerID);
		//Move bestMove = new Move(2, 3);

		if(bestMove != null){
			System.out.println("My move: " + bestMove.i + ", " + bestMove.j);
		}
		
		// Add player coin to the gameboard
		gameBoard.addCoin(bestMove, playerID);
		
		/*try {
			System.in.read();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			System.out.println("no input!");
		}*/
		
		// Return the played move
		return bestMove;
	}

}