package Participants.BitterDivernois;

public class EvalMatrix {
	
	public EvalMatrix(int i, int j, int player){

		this.matrix = new int[i][j];
		this.player = player;
		init();

		// Adapt corner-neighboor values depending if corner is taken
		adaptCornerNeighboors();
	}
	
	/**
	 * Initialize the position value matrix
	 */
	private void init() {

		// Set coin values
		this.matrix[0][0] = this.matrix[0][7] = this.matrix[7][0] = this.matrix[7][7] = 500;

		// Set next to coins values
		this.matrix[1][1] = this.matrix[1][6] = this.matrix[6][1] = this.matrix[6][6] = -250;
		this.matrix[0][1] = this.matrix[1][0] = this.matrix[7][6] = this.matrix[6][7] = -150;
		this.matrix[0][6] = this.matrix[1][7] = this.matrix[6][0] = this.matrix[7][1] = -150;

		// set border values
		this.matrix[0][2] = this.matrix[2][0] = this.matrix[0][5] = this.matrix[5][0] = 30;
		this.matrix[5][2] = this.matrix[2][5] = this.matrix[7][5] = this.matrix[5][7] = 30;
		this.matrix[0][3] = this.matrix[0][4] = this.matrix[7][3] = this.matrix[7][4] = 10;
		this.matrix[3][0] = this.matrix[4][0] = this.matrix[3][7] = this.matrix[4][7] = 10;

		// Set center values
		this.matrix[3][3] = this.matrix[3][4] = this.matrix[4][3] = this.matrix[4][4] = 16;
		this.matrix[2][3] = this.matrix[2][4] = this.matrix[5][3] = this.matrix[5][4] = 2;
		this.matrix[3][2] = this.matrix[3][5] = this.matrix[4][2] = this.matrix[4][5] = 2;
		this.matrix[2][2] = this.matrix[5][2] = this.matrix[2][5] = this.matrix[5][5] = 1;
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
			this.matrix[0][1] = this.matrix[1][0] = 150;
			this.matrix[1][1] = 250;
		}
		if(matrix[7][7] == this.player){
			this.matrix[6][7] = this.matrix[7][6] = 150;
			this.matrix[6][6] = 250;
		}
		if(matrix[0][7] == this.player){
			this.matrix[0][6] = this.matrix[1][7] = 150;
			this.matrix[1][6] = 250;
		}
		if(matrix[7][0] == this.player){
			this.matrix[7][1] = this.matrix[6][0] = 150;
			this.matrix[6][1] = 250;
		}
		
	}

	public void setMiddleGameValues() {
		int i;
		int j;
		 
		//First column
		i = 0;
		for(j = 0;i <= 7; i++){
			matrix[i][j] += 250;
		}

		// Last column
		i = 7;
		for(j = 0;i <= 7; i++){
			matrix[i][j] += 250;
		}
		
		// Top line
		j = 0;
		for(i = 1; i <= 6; i++){
			matrix[i][j] += 250;
		}
		// Bottom line
		j = 7;
		for(i = 1; i <= 6; i++){
			matrix[i][j] += 250;
		}

		
	}
	
	// TOOLS
	private int[][] matrix;
	private int player;

}
