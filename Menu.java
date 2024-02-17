import java.awt.image.BufferedImage;
import java.awt.Graphics;

public class Menu {
    private int boardWidth = 20;
    private int boardHeight = 10;
    private int numMines = 15;
    private double minePercentage = ((double)numMines / (boardWidth * boardHeight))*100;
    private BufferedImage image;
    private Graphics g;
    private Board board;
    private boolean gameCreation = false;
    private MenuButton boardWidth_minus;
    private MenuButton boardWidth_plus;
    private MenuButton boardHeight_minus;
    private MenuButton boardHeight_plus;
    private MenuButton numMines_minus;
    private MenuButton numMines_plus;
    private MenuButton minePercentage_minus;
    private MenuButton minePercentage_plus;
    private MenuButton playGame;
    

    public Menu(BufferedImage image) {
        this.image = image;
    }

    public void display(Graphics g) {
        this.g = g;
        drawLabel(375, 290, "Board Width");
        boardWidth_minus = new MenuButton(300, 300, 50, 50, "-");
        boardWidth_minus.drawButton(image, g);
        drawLabel(375, 320, ""+boardWidth);
        boardWidth_plus = new MenuButton(400, 300, 50, 50, "+");
        boardWidth_plus.drawButton(image, g);

        drawLabel(375, 360, "Board Height");
        boardHeight_minus = new MenuButton(300, 370, 50, 50, "-");
        boardHeight_minus.drawButton(image, g);
        drawLabel(375, 390, ""+boardHeight);
        boardHeight_plus = new MenuButton(400, 370, 50, 50, "+");
        boardHeight_plus.drawButton(image, g);

        drawLabel(375, 430, "Number of Mines");
        numMines_minus = new MenuButton(300, 440, 50, 50, "-");
        numMines_minus.drawButton(image, g);
        drawLabel(375, 460, ""+numMines);
        numMines_plus = new MenuButton(400, 440, 50, 50, "+");
        numMines_plus.drawButton(image, g);

        drawLabel(375, 500, "Mine Percentage");
        minePercentage_minus = new MenuButton(300, 510, 50, 50, "-");
        minePercentage_minus.drawButton(image, g);
        drawLabel(375, 530, String.format("%.2f", minePercentage)+"%");
        minePercentage_plus = new MenuButton(400, 510, 50, 50, "+");
        minePercentage_plus.drawButton(image, g);

        playGame = new MenuButton(500, 380, 100, 30, "Play Game");
        playGame.drawButton(image, g);
    }

    public void drawLabel(int xPos, int yPos, String text) { //xPos correlates to the center of the text
        g.drawString(text, xPos - g.getFontMetrics().stringWidth(text) / 2, yPos + g.getFontMetrics().getHeight() / 2);
    }

    public void click(int x, int y) {
        if(!playGame.clickingInBounds(x, y)) {
            if(boardWidth_minus.clickingInBounds(x, y)) {
                if(boardWidth > 3) {
                    boardWidth--;
                    minePercentage = ((double)numMines / (boardWidth * boardHeight))*100;
                }
            }
            if(boardWidth_plus.clickingInBounds(x, y)) {
                if(boardWidth < 150) {
                    boardWidth++;
                    minePercentage = ((double)numMines / (boardWidth * boardHeight))*100;
                }
            }
            if(boardHeight_minus.clickingInBounds(x, y)) {
                if(boardHeight > 3) {
                    boardHeight--;
                    minePercentage = ((double)numMines / (boardWidth * boardHeight))*100;
                }
            }
            if(boardHeight_plus.clickingInBounds(x, y)) {
                if(boardHeight < 150) {
                    boardHeight++;
                    minePercentage = ((double)numMines / (boardWidth * boardHeight))*100;
                }
            }
            if(numMines_minus.clickingInBounds(x, y)) {
                if(numMines > 1) {
                    numMines--;
                    minePercentage = ((double)numMines / (boardWidth * boardHeight))*100;
                }
            }
            if(numMines_plus.clickingInBounds(x, y)) {
                if(numMines < (boardWidth * boardHeight) - 1) {
                    numMines++;
                    minePercentage = ((double)numMines / (boardWidth * boardHeight))*100;
                }
            }
            if(minePercentage_minus.clickingInBounds(x, y)) {
                if(minePercentage > (1.0/(boardWidth * boardHeight))*100) {
                    minePercentage -= 1 - (minePercentage - (int)minePercentage);
                    if(minePercentage == 0.0) {
                        minePercentage = (1.0/(boardWidth * boardHeight))*100;
                    }
                    numMines = (int)((minePercentage * boardWidth * boardHeight) / 100);
                }
            }
            if(minePercentage_plus.clickingInBounds(x, y)) {
                if(minePercentage < ((double)((boardWidth * boardHeight) - 1)/(boardWidth * boardHeight))*100) {
                    minePercentage += 1 - (minePercentage - (int)minePercentage);
                    numMines = (int)((minePercentage * boardWidth * boardHeight) / 100);
                }
            }
            fixNumMines();
            display(g);
        }
        else{
            startGame();
        }
    }

    public void fixNumMines() {
        if(numMines > (boardWidth * boardHeight) - 1) {
            numMines = (boardWidth * boardHeight) - 1;
            minePercentage = ((double)numMines / (boardWidth * boardHeight))*100;
        }
    }

    public boolean boardIsReady() {
        return gameCreation;
    }

    public void startGame() {
        board = new Board(boardHeight, boardWidth, numMines);
        gameCreation = true;
    }

    public Board getBoard() {
        return board;
    }

    public void setMasterDifficultyValues() {
        boardWidth = 150;
        boardHeight = 150;
        minePercentage = 25;
        numMines = (int)((minePercentage * boardWidth * boardHeight) / 100);
    }
}
