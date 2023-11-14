public class Player {

    public int getArrows() {
        return arrows;
    }

    private int arrows;

    public void setScore(int score) {
        this.score = score;
    }

    private int score = 0;

    public int getNumberOfSteps() {
        return numberOfSteps;
    }

    private int numberOfSteps = 0;
    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    private GameBoard gameBoard;

    public Player(GameBoard gameBoard, int arrows) {
        this.gameBoard = gameBoard;
        this.arrows = arrows;
    }


    public void move() {
        // Ellenőrizze, hogy a játékos mozgása lehetséges-e
        int newRow = gameBoard.getPlayerRow();
        int newCol = gameBoard.getPlayerCol();

        switch (gameBoard.getPlayerDirection()) {
            case 'N':
                newRow--;
                break;
            case 'S':
                newRow++;
                break;
            case 'W':
                newCol--;
                break;
            case 'E':
                newCol++;
                break;
        }

        // Ellenőrizze, hogy a célmező érvényes-e
        if (isValidMove(newRow, newCol)) {
            // Ha érvényes, akkor frissítse a játékost a célmezőre
            gameBoard.setPlayerPosition(newRow, newCol, gameBoard.getPlayerDirection());
            numberOfSteps++;
            System.out.println("A játékos sikeresen lépett.");
        } else {
            System.out.println("A játékos nem tud ide lépni, mert fal vagy a pálya szélén van.");
        }
    }

    public void turnRight() {
        // Fordulás jobbra a játékos irányának frissítésével
        switch (gameBoard.getPlayerDirection()) {
            case 'N':
                gameBoard.setPlayerPosition(gameBoard.getPlayerRow(), gameBoard.getPlayerCol(), 'E');
                break;
            case 'S':
                gameBoard.setPlayerPosition(gameBoard.getPlayerRow(), gameBoard.getPlayerCol(), 'W');
                break;
            case 'W':
                gameBoard.setPlayerPosition(gameBoard.getPlayerRow(), gameBoard.getPlayerCol(), 'N');
                break;
            case 'E':
                gameBoard.setPlayerPosition(gameBoard.getPlayerRow(), gameBoard.getPlayerCol(), 'S');
                break;
        }
        numberOfSteps++;
        System.out.println("A játékos jobbra fordult.");
    }


    public void turnLeft() {
        // Fordulás balra a játékos irányának frissítésével
        switch (gameBoard.getPlayerDirection()) {
            case 'N':
                gameBoard.setPlayerPosition(gameBoard.getPlayerRow(), gameBoard.getPlayerCol(), 'W');
                break;
            case 'S':
                gameBoard.setPlayerPosition(gameBoard.getPlayerRow(), gameBoard.getPlayerCol(), 'E');
                break;
            case 'W':
                gameBoard.setPlayerPosition(gameBoard.getPlayerRow(), gameBoard.getPlayerCol(), 'S');
                break;
            case 'E':
                gameBoard.setPlayerPosition(gameBoard.getPlayerRow(), gameBoard.getPlayerCol(), 'N');
                break;
        }
        numberOfSteps++;
        System.out.println("A játékos balra fordult.");
    }

    public void shoot() {
        // Ellenőrizzük, van-e még nyíl a játékosnál
        if (arrows > 0) {
            int row = gameBoard.getPlayerRow();
            int col = gameBoard.getPlayerCol();

            // Lövés a játékos irányába
            switch (gameBoard.getPlayerDirection()) {
                case 'N':
                    row--;
                    break;
                case 'S':
                    row++;
                    break;
                case 'W':
                    col--;
                    break;
                case 'E':
                    col++;
                    break;
            }

            // Ellenőrizzük, hogy a célmező érvényes-e
            if (isValidMoveforShooting(row, col)) {
                // Találat esetén a wumpusz eltávolítása
                if (gameBoard.getCell(row, col) == 'U') {
                    gameBoard.setCell(row, col, '_');
                    score++;
                    System.out.println("Gratulálok, eltaláltad a wumpuszt!");
                } else {
                    System.out.println("A lövedék a semmibe veszett.");
                }

                // Csökkentjük a nyilak számát
                if (arrows > 0) {
                    arrows--;
                }
            } else {
                System.out.println("A lövés nem érvényes, mert a célmező fal vagy a pálya szélén van.");
            }
        } else {
            System.out.println("Nincs több nyílad!");
        }
    }

    public void pickUpGold() {
        int row = gameBoard.getPlayerRow();
        int col = gameBoard.getPlayerCol();

        // Ellenőrizzük, hogy a játékos a G (arany) mezőn áll-e
        if (gameBoard.getCell(row, col) == 'G') {
            // Felvesszük az aranyat és beállítjuk, hogy a játékosnak van aranya
            gameBoard.setCell(row, col, '_');
            System.out.println("Aranyat szereztél!");
        } else {
            System.out.println("Itt nincs arany, nem tudsz felvenni.");
        }
    }

    public void enterWumpus() {
        int row = gameBoard.getPlayerRow();
        int col = gameBoard.getPlayerCol();

        // Ellenőrizzük, hogy a játékos az U (wumpusz) mezőn áll-e
        if (gameBoard.getCell(row, col) == 'U') {
            // A játékos belelépett a wumpusz mezőre, tehát meghal
            System.out.println("A játékos belelépett a wumpusz mezőre és meghalt.");
            // Itt esetleg további teendők (pl. játék vége, stb.)
        } else {
            System.out.println("Itt nincs wumpusz, nem tudsz belelépni.");
        }
    }

    public boolean hasGold() {
        // Ellenőrizzük, hogy a játékosnak van-e aranya
        return gameBoard.getBoard()[gameBoard.getPlayerRow()][gameBoard.getPlayerCol()] == 'G';
    }

    public boolean isAlive() {
        // Ellenőrizzük, hogy a játékos a wumpusz (U) mezőn áll-e
        char currentCell = gameBoard.getBoard()[gameBoard.getPlayerRow()][gameBoard.getPlayerCol()];
        return currentCell != 'U';
    }

    public void printStatus() {
        System.out.println(gameBoard);
        System.out.println("Jatekos allapota:");
        System.out.println("Pozicio: Sor " + (gameBoard.getPlayerRow() + 1) + ", Oszlop " + (gameBoard.getPlayerCol() + 1));
        System.out.println("Irany: " + gameBoard.getPlayerDirection());
        System.out.println("Nyilak szama: " + arrows);
        System.out.println("Van aranya: " + hasGold());
        System.out.println("Eletben van: " + isAlive());
    }

    private boolean isValidMove(int row, int col) {
        // Ellenőrizze, hogy a célmező érvényes

        if (gameBoard.getCell(row,col) == 'P') {
            System.out.println("" + row + " " + col);
            //enterPit();
            if (arrows > 0) {
                arrows--;
            }
        }

        return row >= 0 && row < gameBoard.getSize() && col >= 0 && col < gameBoard.getSize() &&
                gameBoard.getCell(row, col) != 'W'; // Ne lehet legyen wumpusz a célmezőn
    }

    private boolean isValidMoveforShooting(int row, int col) {
        // Ellenőrizzük, hogy a célmező érvényes
        return row >= 0 && row < gameBoard.getSize() && col >= 0 && col < gameBoard.getSize();
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "Player{" +
                "arrows=" + arrows +
                ", score=" + score +
                ", gameBoard=" + gameBoard +
                '}';
    }
}

