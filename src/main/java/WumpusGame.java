import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WumpusGame {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Felhasználónév bekérése
        System.out.print("Kerem, adja meg a felhasznalo nevet: ");
        String felhasznaloNev = scanner.nextLine();

        // Játék inicializálása
        GameBoard gameBoard = loadGameBoardFromFile();
        Player player = new Player(gameBoard, gameBoard.getArrows());

        // Adatbázis kezelő inicializálása
        DbHandler dbHandler = new DbHandler();

        // Főmenü
        while (true) {
            // Alapmenü kiírása
            System.out.println("1. Fájlból beolvasás");
            System.out.println("2. Adatbázisból betöltés");
            System.out.println("3. Adatbázisba mentés");
            System.out.println("4. Játszás");
            System.out.println("5. Kilépés");

            // Felhasználó választása bekérése
            int selectedOption;
            try {
                System.out.print("Kérem, válasszon egy opciót (1-5): ");
                selectedOption = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Érvénytelen választás. Kérem, adjon meg egy érvényes számot.");
                continue;
            }

            // Végrehajtjuk a kiválasztott műveletet
            switch (selectedOption) {
                case 1:
                    // Fájlból beolvasás
                    gameBoard = loadGameBoardFromFile();
                    player.setGameBoard(gameBoard);
                    player.setArrows(gameBoard.getArrows());
                    break;
                case 2:
                    // Adatbázisból betöltés
                    System.out.println("Felhasználói pontszám betöltése az adatbázisból...");
                    int loadedScore = dbHandler.loadFromDatabase(felhasznaloNev);
                    System.out.println("Felhasználói pontszám: " + loadedScore);
                    break;
                case 3:
                    // Adatbázisba mentés
                    System.out.println("Felhasználói pontszám mentése az adatbázisba...");
                    dbHandler.saveToDatabase(felhasznaloNev, player.getScore());
                    break;
                case 4:
                    // Játszás
                    playGame(player);
                    break;
                case 5:
                    // Kilépés
                    System.exit(0);
                    break;
                default:
                    System.out.println("Érvénytelen választás. Kérem, válasszon újra.");
            }
        }
    }

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
            System.out.print("Válasszon akciót (Lépés - L, Jobbra fordulás - J, Balra fordulás - B, Lövés - S, Arany felvétele - A, Kilépés (Feladas) - K): ");
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
                    System.out.println("Kiléptél a játékból.");
                    return;
                default:
                    System.out.println("Érvénytelen akció. Kérem, válasszon újra.");
            }
        }

        // Játék vége
        if (player.isAlive() && player.hasGold()) {
            System.out.println("Gratulálok, teljesítetted a küldetést!");
            System.out.println("Pontszamod: " + (player.getScore() + 10) + " pont. 1 pont jar wumpusonkent es 10 pont az arany felszedesert. ");
            System.out.println("Lepeseid szama: " + player.getNumberOfSteps());
        } else {
            System.out.println("Játék vége. Vagy wumpusra leptel vagy feladtad.");
        }
    }
}