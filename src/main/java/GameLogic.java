import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GameLogic {
    static GameBoard loadGameBoardFromFile() {
        ClassLoader classLoader = WumpusGame.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream("wumpuszinput.txt");
             Scanner scanner = new Scanner(inputStream)) {

            // Olvassuk be az első sort, amely tartalmazza a pálya méretét és a hős pozícióját
            String firstLine = scanner.nextLine();
            String[] parts = firstLine.split(" ");

            Map<String, Integer> heroColumns = new HashMap<>();
            heroColumns.put("A", 0);
            heroColumns.put("B", 1);
            heroColumns.put("C", 2);
            heroColumns.put("D", 3);
            heroColumns.put("E", 4);
            heroColumns.put("F", 5);

            // Ellenőrizze, hogy minden rész érvényes decimális szám-e
            int boardSize = Integer.parseInt(parts[0]);
            int heroColumn = heroColumns.get(parts[1]);
            int heroRow = Integer.parseInt(parts[2]) - 1;
            char heroDirection = parts[3].charAt(0);

            // Inicializáljuk a játékteret
            GameBoard gameBoard = new GameBoard(boardSize, heroRow, heroColumn, heroDirection);

            // Olvassuk be a pályát soronként
            for (int i = 0; i < boardSize; i++) {
                String line = scanner.nextLine();
                gameBoard.setRow(i, line);
            }

            if (gameBoard.getCell(heroRow, heroColumn) != '_') {
                System.out.println("A jatekos nem ures pozicion van. Kerem adjon meg egy olyan palyat amiben" +
                        " a jatekos ures pozicioban van kezdetlegesen.");
                System.exit(0);
            } else if (gameBoard.getCell(heroRow, heroColumn) == 'G') {
                System.out.println("A jatekos kezdeti pozicioja megegyezik az arany poziciojaval.");
                System.exit(0);
            }

            return gameBoard;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static void playGame(Player player) {
        Scanner scanner = new Scanner(System.in);

        // Játékmenet
        while (player.isAlive() && !player.hasGold()) {
            // Játékállapot kiírása
            player.printStatus();

            // Játékos lépéseinek bekérése
            System.out.print("Valasszon akciot (Lepes - L, Jobbra fordulas - J, Balra fordulas - B, Loves - S, Arany felvetele - A, Kilepes (Feladas) - K): ");
            String action = scanner.nextLine().toUpperCase();

            // Végrehajtjuk a kiválasztott akciót
            switch (action) {
                case "L":
                    player.move();
                    break;
                case "J":
                    player.turnRight();
                    break;
                case "B":
                    player.turnLeft();
                    break;
                case "S":
                    player.shoot();
                    break;
                case "A":
                    player.pickUpGold();
                    break;
                case "K":
                    // Kilépés a játékból
                    System.out.println("Kileptel a jatekbol.");
                    return;
                default:
                    System.out.println("Ervenytelen akcio. Kerem, valasszon ujra.");
            }
        }

        // Játék vége
        if (player.isAlive() && player.hasGold()) {
            System.out.println("Gratulalok, teljesitetted a kuldetest!");
            System.out.println("Pontszamod: " + (player.getScore() + 10) + " pont. 1 pont jar wumpusonkent es 10 pont az arany felszedesert. ");
            System.out.println("Lepeseid szama: " + player.getNumberOfSteps());
        } else {
            System.out.println("Jatek vege. Vagy wumpusra leptel vagy feladtad.");
        }
    }
}
