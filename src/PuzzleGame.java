import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class PuzzleGame extends JFrame implements ActionListener {
    private JButton[][] buttons; //rutnätet med knappar
    private int emptyRowPosition, emptyColumnPosition; //position för den tomma rutan
    private boolean gameWon = false; /// flagga som indikerar om spelet är vunnen.

    public PuzzleGame() {
        setVisible(true);
        setTitle("15-Spel");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        createPuzzleGrid(); // Anropa metoden för att skapa rutnätet

        JButton newGameButton = new JButton("Nytt spel");
        newGameButton.addActionListener(e -> shufflePuzzle());

        add(newGameButton, BorderLayout.SOUTH);
    }

    private void shufflePuzzle() {
        gameWon = false; // börjar på false för att man ska kunna flytta på brickor
        shuffle();
    }

    private void createPuzzleGrid() {
        JPanel buttonPanel = new JPanel(new GridLayout(4, 4));
        buttons = new JButton[4][4];

        int num = 1;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (num == 16) {
                    buttons[i][j] = new JButton("");
                } else {
                    buttons[i][j] = new JButton(String.valueOf(num));
                }
                buttons[i][j].addActionListener(this);
                buttonPanel.add(buttons[i][j]);
                if (num != 16) {
                    num++;
                }
            }
        }

        emptyRowPosition = 3;
        emptyColumnPosition = 3;
        add(buttonPanel, BorderLayout.CENTER);
    }

    private void shuffle() {
        Random randon = new Random();

        for (int i = 0; i < 100; i++) {
            int randomMoveDirection = randon.nextInt(4); // generar en random riktning (0,1,2 eller 3) där den tomma rutan kan flyttas.
            move(randomMoveDirection);                         // skickar till move metoden för att försöka flytta tomma rutan i den random riktningen.
        }
    }



    private void move(int randomMoveDirection) {

        int newRow = emptyRowPosition;
        int newCol = emptyColumnPosition;


        if (randomMoveDirection == 0) {
            newRow = Math.max(emptyRowPosition - 1, 0);
        } else if (randomMoveDirection == 1) {
            newRow = Math.min(emptyRowPosition + 1, 3);
        } else if (randomMoveDirection == 2) {
            newCol = Math.max(emptyColumnPosition - 1, 0);
        } else {
            newCol = Math.min(emptyColumnPosition + 1, 3);
        }


        String buttonText = buttons[newRow][newCol].getText(); // hämtar texten från knappen på den nya positionen som kommer att flyttas till den tomma rutan.
        buttons[emptyRowPosition][emptyColumnPosition].setText(buttonText); // texten från nya position kopieras till den tomma rutan. Simulera draget
        buttons[emptyRowPosition][emptyColumnPosition].setEnabled(true); // knappen på den tidigare tomma rutan återaktiveras.
        buttons[newRow][newCol].setText(""); // texten på den nya positionen nollställs eftersom den är tom nu.
        buttons[newRow][newCol].setEnabled(false); // knapparna på den nya positionen inaktiveras eftersom den nu är den tomma rutan.

        emptyRowPosition = newRow; // uppdaterar position för den tomma rutan till den nya positionen efter draget.
        emptyColumnPosition = newCol;
    }

    public void actionPerformed(ActionEvent e) {
        if (gameWon) {
            return; // Om spelet redan är vunnet, avsluta hanteringen av knappklick.
        }

        JButton buttonClicked = (JButton) e.getSource(); // Hämta knappen som utlöste actionevent e.typkonvertera till en jbutton och har referensen till den knapp som trycktes.
        int row = -1;
        int col = -1; // Variabler för att spara rad och kolumn där knappen klickades.

        // Hitta vilken bricka som klickades på i rutnätet.
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (buttonClicked == buttons[i][j]) {
                    row = i;
                    col = j;
                    break;
                }
            }
        }

        // Kontrollera om den klickade brickan är intill den tomma rutan. Om ja fortsätt med att utföra draget.
        if (isNextTo(row, col, emptyRowPosition, emptyColumnPosition)) {
            buttons[emptyRowPosition][emptyColumnPosition].setText(buttonClicked.getText()); // texten på tomma rutan uppdateras till klickade knappen.
            buttons[emptyRowPosition][emptyColumnPosition].setEnabled(true); //återaktivera den tidigare tomma rutan.
            buttonClicked.setText(""); // Töm texten på klickad bricka.
            buttonClicked.setEnabled(false); //klickad knapp blir inaktiverad eftersom den nu är tom.
            emptyRowPosition = row; // Uppdatera positionen för tom plats.
            emptyColumnPosition = col;

            // Kontrollera om spelet är vunnet och sätt flaggan om det är så.
            if (checkIfGameIsWon()) {
                gameWon = true;
            }
        }
    }





    private boolean isNextTo(int buttonRowPosition, int buttonColumnPosition, int emptyRowPosition, int emptyColumnPosition) {
        if (buttonRowPosition == emptyRowPosition && Math.abs(buttonColumnPosition - emptyColumnPosition) == 1) {
            return true;
        }
        return buttonColumnPosition == emptyColumnPosition && Math.abs(buttonRowPosition - emptyRowPosition) == 1;
    }


    private int[][] expectedLayout = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 0} // 0 representerar den tomma rutan
    };

    private boolean checkIfGameIsWon() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (buttons[i][j].getText().isEmpty()) {
                    continue; // Hoppa över den tomma rutan
                }

                int expectedValue = expectedLayout[i][j];
                int actualValue = Integer.parseInt(buttons[i][j].getText());

                if (expectedValue != actualValue) {
                    return false; // Om någon bricka är på fel plats, är spelet inte vunnet.
                }
            }
        }

        showWinMessage();
        return true;
    }
    private void showWinMessage() {
        JOptionPane.showMessageDialog(this, "Grattis, du löste puzzlet!");

    }



    public static void main(String[] args) {
        PuzzleGame game = new PuzzleGame();

    }
}