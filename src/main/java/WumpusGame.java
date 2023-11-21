import java.util.List;
import java.util.Scanner;

public class WumpusGame {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Felhasználónév bekérése
        System.out.print("Kerem, adja meg a felhasznalo nevet: ");
        String felhasznaloNev = scanner.nextLine();

        // Játék inicializálása
        GameBoard gameBoard = GameLogic.loadGameBoardFromFile();
        Player player = new Player(gameBoard, gameBoard.getArrows());

        // Adatbázis kezelő inicializálása
        DbHandler dbHandler = new DbHandler();

        // Főmenü
        while (true) {
            // Alapmenü kiírása
            System.out.println("1. Fajlbol beolvasas");
            System.out.println("2. Adatbazisbol betoltes");
            System.out.println("3. Adatbazisba mentes");
            System.out.println("4. Jatszas");
            System.out.println("5. High score tablazat megjelenitese");
            System.out.println("6. Kilepes");

            // Felhasználó választása bekérése
            int selectedOption;
            try {
                System.out.print("Kerem, valasszon egy opciot (1-5): ");
                selectedOption = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ervenytelen valasztas. Kerem, adjon meg egy ervenyes szamot.");
                continue;
            }

            // Végrehajtjuk a kiválasztott műveletet
            switch (selectedOption) {
                case 1:
                    // Fájlból beolvasás
                    gameBoard = GameLogic.loadGameBoardFromFile();
                    player.setGameBoard(gameBoard);
                    player.setArrows(gameBoard.getArrows());
                    break;
                case 2:
                    // Adatbázisból betöltés
                    System.out.println("Felhasznaloi pontszam betoltese az adatbazisbol...");
                    int loadedScore = dbHandler.loadFromDatabase(felhasznaloNev);
                    System.out.println("Felhasznaloi pontszam: " + loadedScore);
                    break;
                case 3:
                    // Adatbázisba mentés
                    System.out.println("Felhasznaloi pontszam mentese az adatbazisba...");
                    dbHandler.saveToDatabase(felhasznaloNev, player.getScore());
                    break;
                case 4:
                    // Játszás
                    player.setScore(0);
                    GameLogic.playGame(player);
                    break;
                case 5:
                    // High score táblázat megjelenítése
                    List<PlayerScore> highScores = dbHandler.getHighScores();
                    System.out.println("High Score Tablazat:");
                    System.out.println("------------------------------");
                    System.out.println("Felhasznalo név | Pontszam");
                    System.out.println("------------------------------");
                    for (PlayerScore score : highScores) {
                        System.out.printf("%-15s | %-8d%n", score.getUsername(), score.getScore());
                    }
                    System.out.println("------------------------------");
                    break;
                case 6:
                    // Kilépés
                    System.exit(0);
                    break;
                default:
                    System.out.println("Ervenytelen valasztas. Kerem, valasszon ujra.");
            }
        }
    }
}