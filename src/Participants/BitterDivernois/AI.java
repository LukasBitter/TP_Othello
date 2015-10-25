package Participants.BitterDivernois;

import java.io.IOException;
import java.util.ArrayList;

import Othello.Move;

public class AI {

	public Move getBestMove(GameBoard gameBoard, int depth, int playerID)
	{
		System.out.println("I am player: " + playerID);
		this.gameBoard = gameBoard;
		this.playerID = playerID;
		this.gameOpeningstate = 12;
		this.gameEndState = 60 - depth;
		this.evalMatrix = new int [8][8];
		this.noMovePossible = true;
		this.maxDepth = depth;
		init();
		int[] result = alphaBeta( gameBoard, new Node(new Move()), depth, 1, Double.NEGATIVE_INFINITY);
		
		if(noMovePossible){
			return null;
		}
		return new Move(result[1], result[2]);
	}
	
	private void init(){
		// Turn count
		this.turn = (gameBoard.getCoinCount(playerID) - 2)/2;
				
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
	
	private int[] alphaBeta(GameBoard gameBoard, Node root, int depth, int minOrMax, double parentValue)
	{
		// minOrMax = 1 : maximize
		// minOrMax = −1 : minimize
		int l_PlayerID = (minOrMax==1) ? this.playerID : Math.abs(this.playerID - 1);
		
		//System.out.println(indent(depth) + "Player: " + l_PlayerID + " Node name: " + root.name);
		ArrayList<Move> possibleMoves = new ArrayList<>(gameBoard.getPossibleMoves(l_PlayerID));
		
		/*System.out.print(indent(depth) + "Possible moves: ");
		for(Move move:possibleMoves){
			System.out.print(move.i + ", " + move.j + " / ");
		}
		System.out.println("");*/
		
		//if(depth == 0 || root.isLeaf())
		if(depth == 0 || possibleMoves.isEmpty())
		{
			//System.out.println(" --> Depth == 0");
			return new int[] {eval(root), 0, 0};
		}
		
		int optVal = (int) (minOrMax * Double.NEGATIVE_INFINITY);
		Move optOp = new Move(); 
		
		for(Move op : possibleMoves)
		{
			GameBoard l_gameBoard = gameBoard.clone();
			this.noMovePossible = false;
			
			//System.out.println(indent(depth) + "Test move: " + op.i + ", " + op.j + ": ");
			Node newElement = new Node(op);
			root.addChildNode(newElement); 
			l_gameBoard.addCoin(op, l_PlayerID);
			
			//System.out.print(indent(depth) + "Coin count: " + l_gameBoard.getCoinCount(l_PlayerID) + " / ");
			//System.out.println("PlayerId at this point: " + l_gameBoard.getPlayerIDAtPos(op.j, op.i));

			//l_gameBoard.displayGameBoard();
			
			/*try {
				System.in.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			
			int val[] = alphaBeta(l_gameBoard, newElement, depth-1, -minOrMax, optVal);
			
			//System.out.println(indent(depth) + "Value = " + val[0]);
			
			if(val[0] * minOrMax > optVal * minOrMax)
			{
				//System.out.print(indent(depth) + "new best move: " + optOp.i + ", " + optOp.j + " / Val = " + val[0]+ ", ");
				//System.out.println("Matrix = " + this.evalMatrix[op.i][op.j]);
				optVal = val[0];
				optOp = op;
				if((optVal * minOrMax > parentValue * minOrMax) && (parentValue != Double.NEGATIVE_INFINITY))
				{
					//System.out.print(indent(depth) + " minOrMax = " + minOrMax + ", optVal = " + optVal + ", parentValue = " + parentValue);
					//System.out.println(" --> cut!");
					break;
				}
			}
			//System.out.println("");
		}
		return new int[] {optVal, optOp.i, optOp.j}; //, optOp;
	}
	
	private int eval(Node node) {
		if(this.turn < gameOpeningstate){
			// Game opening state
			//System.out.println("GAME OPENING: i=" + node.getMove().i + ", j=" + node.getMove().j + " / Matrix val=" + this.evalMatrix[node.getMove().i][node.getMove().j]);
			return this.evalMatrix[node.getMove().i][node.getMove().j];
		}
		else if(this.turn > this.gameOpeningstate && this.turn < this.gameEndState){
			// Game middle state
			System.out.println("GAME MIDDLE: i=" + node.getMove().i + ", j=" + node.getMove().j + " / Matrix val=" + this.evalMatrix[node.getMove().i][node.getMove().j]);
			return this.evalMatrix[node.getMove().i][node.getMove().j];
		}
		else{
			// Game end state
			System.out.println("GAME ENDING: i=" + node.getMove().i + ", j=" + node.getMove().j + " / Matrix val=" + this.evalMatrix[node.getMove().i][node.getMove().j]);
			return this.evalMatrix[node.getMove().i][node.getMove().j];
		}
	}
	
	private String indent(int d){
		String indentString = "";
		for(int i=d; i<this.maxDepth; i++ ){
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
