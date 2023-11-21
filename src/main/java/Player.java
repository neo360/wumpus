public class Player {
    private int arrows;
    private int score = 0;

    public void setNumberOfSteps(int numberOfSteps) {
        this.numberOfSteps = numberOfSteps;
    }

    private int numberOfSteps = 0;

    private boolean isAlive;

    public void setHasGold(boolean hasGold) {
        this.hasGold = hasGold;
    }

    private boolean hasGold;
    private GameBoard gameBoard;
    public Player(GameBoard gameBoard, int arrows) {
        this.gameBoard = gameBoard;
        this.arrows = arrows;
        this.isAlive = true;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void setArrows(int arrows) {
        this.arrows = arrows;
    }

    public int getArrows() {
        return arrows;
    }

    public void setScore(int score) {
        this.score = score;
    }
    public int getScore() {
        return score;
    }
    public int getNumberOfSteps() {
        return numberOfSteps;
    }
    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public GameBoard getGameBoard() { return this.gameBoard; }

    public void move() {
        // Ellenőrizzuk, hogy a játékos mozgása lehetséges-e
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

        // Ellenőrizzuk, hogy a célmező érvényes-e
        if (isValidMove(newRow, newCol)) {
            // Ha érvényes, akkor frissítse a játékost a célmezőre
            gameBoard.setPlayerPosition(newRow, newCol, gameBoard.getPlayerDirection());
            numberOfSteps++;
            System.out.println("A jatekos sikeresen lepett.");
        } else {
            System.out.println("A jatekos nem tud ide lepni, mert fal vagy a palya szelen van.");
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
        System.out.println("A jatekos jobbra fordult.");
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
        System.out.println("A jatekos balra fordult.");
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
                    System.out.println("Gratulalok, eltalaltad a wumpuszt!");
                } else {
                    System.out.println("A lovedek a semmibe veszett.");
                }

                // Csökkentjük a nyilak számát
                if (arrows > 0) {
                    arrows--;
                }
            } else {
                System.out.println("A loves nem ervenyes, mert a celmezo fal vagy a palya szelen van.");
            }
        } else {
            System.out.println("Nincs tobb nyilad!");
        }
    }

    public void pickUpGold() {
        int row = gameBoard.getPlayerRow();
        int col = gameBoard.getPlayerCol();

        // Ellenőrizzük, hogy a játékos a G (arany) mezőn áll-e
        if (gameBoard.getCell(row, col) == 'G') {
            // Felvesszük az aranyat és beállítjuk, hogy a játékosnak van aranya
            gameBoard.setCell(row, col, '_');
            System.out.println("Aranyat szereztel!");
        } else {
            System.out.println("Itt nincs arany, nem tudsz felvenni.");
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

        // Itt ellenorizzuk hogy a hos nem-e lepett wumpusra
        if (gameBoard.getCell(row,col) == 'P' && arrows > 0) {
                arrows--;
        }

        // Ellenőrizuk, hogy a célmezőre valo lepes érvényes
        return row >= 0 && row < gameBoard.getSize() && col >= 0 && col < gameBoard.getSize() &&
                gameBoard.getCell(row, col) != 'W'; // Ne lehet legyen wumpusz a célmezőn
    }

    private boolean isValidMoveforShooting(int row, int col) {
        // Ellenőrizzük, hogy a célmező érvényes-e a lovesre
        return row >= 0 && row < gameBoard.getSize() && col >= 0 && col < gameBoard.getSize();
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

