package Participants.BitterDivernois;

import Othello.Move;

public class AI {

	public Move getBestMove(GameBoard gameBoard, int depth, int playerID)
	{
		this.gameBoard = gameBoard;
		this.playerID = playerID;
		return null;
	}
	
	private int alphaBeta(Node root, int depth, int minOrMax, int parentValue)
	{
		if(depth == 0 || root.isLeaf())
		{
			return root.getEvaluation();
		}
		
		int optVal = (int) (minOrMax * Double.NEGATIVE_INFINITY);
		//Move optOp = none; 
		
		for(Move movement : gameBoard.getPossibleMoves(playerID))
		{
			Node newElement = new Node(movement);
			root.addChildNode(newElement);
			
			int val = alphaBeta(newElement, depth-1, -minOrMax, optVal);
			if(val * minOrMax > optVal * minOrMax)
			{
				if(optVal * minOrMax > parentValue * minOrMax)
				{
					break;
				}
			}
		}
		return optVal; //, optOp;
	}
	
	private GameBoard gameBoard;
	private int playerID;

}
