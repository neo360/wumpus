import java.util.Arrays;

public class GameBoard {
    private char[][] board;
    private int size;
    private int playerRow;
    private int playerCol;
    private char playerDirection;
    private int arrows = 0;

    public GameBoard(int size, int playerRow, int playerCol, char playerDirection) {
        this.size = size;
        this.playerRow = playerRow;
        this.playerCol = playerCol;
        this.playerDirection = playerDirection;
        this.board = new char[size][size];
    }

    public int getSize() {
        return size;
    }

    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }

    public char getPlayerDirection() {
        return playerDirection;
    }

    public int getArrows() {
        return arrows;
    }

    public char[][] getBoard() {
        return board;
    }

    public void setPlayerPosition(int row, int col, char direction) {
        this.playerRow = row;
        this.playerCol = col;
        this.playerDirection = direction;
    }

    public char getCell(int row, int col) {
        return board[row][col];
    }

    public void setCell(int row, int col, char value) {
        board[row][col] = value;
    }

    public void setRow(int rowIndex, String row) {
        if (rowIndex >= 0 && rowIndex < size && row.length() == size) {
            for (int i = 0; i < size; i++) {
                board[rowIndex][i] = row.charAt(i);
                if (row.charAt(i) == 'U') {
                    arrows++;
                }
            }
        } else {
            System.out.println("Érvénytelen sor vagy méret.");
        }
    }

    @Override
    public String toString() {
        return "GameBoard{" +
                "board=" + Arrays.toString(board) +
                ", size=" + size +
                ", playerRow=" + playerRow +
                ", playerCol=" + playerCol +
                ", playerDirection=" + playerDirection +
                ", arrows=" + arrows +
                '}';
    }
}
