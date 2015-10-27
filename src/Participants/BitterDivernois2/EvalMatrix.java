package Participants.BitterDivernois2;

public class EvalMatrix {
	
	public EvalMatrix(int i, int j, int player){

		this.matrix = new int[i][j];
		this.player = player;
		this.column = i;
		this.line = j;
		init();

		// Adapt corner-neighboor values depending if corner is taken
		adaptCornerNeighboors();
	}
	
	/**
	 * Initialize the position value matrix
	 */
	private void init() {

		// Set coin values   	
    	this.matrix[0][0] = this.matrix[0][7] = this.matrix[7][0] = this.matrix[7][7] = 20;

		// Set next to coins values
		this.matrix[1][1] = this.matrix[1][6] = this.matrix[6][1] = this.matrix[6][6] = -7;
		this.matrix[0][1] = this.matrix[1][0] = this.matrix[7][6] = this.matrix[6][7] = -3;
		this.matrix[0][6] = this.matrix[1][7] = this.matrix[6][0] = this.matrix[7][1] = -3;

		// set border values
		this.matrix[0][2] = this.matrix[2][0] = this.matrix[0][5] = this.matrix[5][0] = 11;
		this.matrix[5][2] = this.matrix[2][5] = this.matrix[7][5] = this.matrix[5][7] = 11;
		this.matrix[0][3] = this.matrix[0][4] = this.matrix[7][3] = this.matrix[7][4] = 8;
		this.matrix[3][0] = this.matrix[4][0] = this.matrix[3][7] = this.matrix[4][7] = 8;

		// Set center values
		this.matrix[3][3] = this.matrix[3][4] = this.matrix[4][3] = this.matrix[4][4] = -3;
		this.matrix[2][3] = this.matrix[2][4] = this.matrix[5][3] = this.matrix[5][4] = 2;
		this.matrix[3][2] = this.matrix[3][5] = this.matrix[4][2] = this.matrix[4][5] = 2;
		this.matrix[2][2] = this.matrix[5][2] = this.matrix[2][5] = this.matrix[5][5] = 2;
		
		// Faut-il les ajouter?
		//this.matrix[1][3] = this.matrix[1][4] = this.matrix[3][1] = this.matrix[4][1] = 1;
		//this.matrix[3][6] = this.matrix[4][6] = this.matrix[6][3] = this.matrix[6][4] = 1;
	
	}
	
	public int getValue(int i, int j){
		return this.matrix[i][j];
	}
	
	/**
	 * Adapt neighboor cells to corners. If the corners are of the player
	 * their neighboors are interesting also
	 */
	private void adaptCornerNeighboors() {
		if(matrix[0][0] == this.player){
			System.out.println("ADAPT CORNERNEIGHBOORS: 0, 0");
			this.matrix[0][1] = this.matrix[1][0] = 150;
			this.matrix[1][1] = 250;
		}
		if(matrix[7][7] == this.player){
			System.out.println("ADAPT CORNERNEIGHBOORS: 7, 7");
			this.matrix[6][7] = this.matrix[7][6] = 150;
			this.matrix[6][6] = 250;
		}
		if(matrix[0][7] == this.player){
			System.out.println("ADAPT CORNERNEIGHBOORS: 0, 7");
			this.matrix[0][6] = this.matrix[1][7] = 150;
			this.matrix[1][6] = 250;
		}
		if(matrix[7][0] == this.player){
			System.out.println("ADAPT CORNERNEIGHBOORS: 7, 0");
			this.matrix[7][1] = this.matrix[6][0] = 150;
			this.matrix[6][1] = 250;
		}		
	}

	public void setMiddleGameValues() {
		//System.out.println("MIDDLEGAME VALUES");
		int i;
		int j;
		 
		//First and last column
		for(j = 0;j <= 7; j++){
			matrix[0][j] += 250;
			matrix[7][j] += 250;
		}
		
		// Top and bottom line
		j = 0;
		for(i = 1; i <= 6; i++){
			matrix[i][0] += 250;
			matrix[i][7] += 250;
		}
	}
	
	public void displayEvalMatrix(GameBoard gameboard) {
		System.out.println("************ Eval Matrix ************");
		System.out.println();
		for (int l = 0; l < this.line; l++) {
			for (int c = 0; c < this.column; c++) {
				int coin = gameboard.getPlayerIDAtPos(l, c); 
				if(coin != GameBoard.NO_COIN){
					String playerColor;
					if(coin == 0){
						playerColor = "R";
					}
					else {
						playerColor = "B";					
					}
					 
					System.out.print(playerColor + "\t");
				}
				else{
					System.out.print(this.matrix[c][l] + "\t");
				}
			}
			System.out.print("\n");
		}
	}

	
	// TOOLS
	private int[][] matrix;
	private int player;
	private int column;
	private int line;

}
