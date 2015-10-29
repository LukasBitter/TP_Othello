package Participants.BitterDivernois;

/**
 * 
 * @author lukas.bitter / margaux.divernois
 *
 */
public class EvalMatrix {

	/**
	 * Constructor of evaluation matrix
	 * 
	 * @param i
	 * @param j
	 * @param player
	 */
	public EvalMatrix(int i, int j) {

		this.matrix = new int[i][j];
		this.column = i;
		this.line = j;
		init();

		// Adapt corner-neighboor values depending if corner is taken by player
		// or not
		// adaptCornerNeighboors();
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

		this.matrix[1][3] = this.matrix[1][4] = this.matrix[3][1] = this.matrix[4][1] = 0;
		this.matrix[3][6] = this.matrix[4][6] = this.matrix[6][3] = this.matrix[6][4] = 0;
	}

	/**
	 * return position value of position
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	public int getValue(int i, int j) {
		return this.matrix[i][j];
	}

	/**
	 * If middle game stat is reached, borders have stronger weight
	 */
	public void setMiddleGameValues() {
		// First and last column
		for (int j = 0; j <= 7; j++) {
			matrix[0][j] += 250;
			matrix[7][j] += 250;
		}

		// Top and bottom line
		for (int i = 1; i <= 6; i++) {
			matrix[i][0] += 250;
			matrix[i][7] += 250;
		}
	}

	/**
	 * Diplay current evaluation matrix in conlole
	 * 
	 * @param gameboard
	 */
	public void displayEvalMatrix(GameBoard gameboard) {
		System.out.println("************ Eval Matrix ************");
		System.out.println();
		for (int l = 0; l < this.line; l++) {
			for (int c = 0; c < this.column; c++) {
				int coin = gameboard.getPlayerIDAtPos(l, c);
				if (coin != GameBoard.NO_COIN) {
					String playerColor;
					if (coin == 0) {
						playerColor = "R";
					} else {
						playerColor = "B";
					}

					System.out.print(playerColor + "\t");
				} else {
					System.out.print(this.matrix[c][l] + "\t");
				}
			}
			System.out.print("\n");
		}
	}

	// TOOLS
	private int[][] matrix;
	private int column;
	private int line;

}
