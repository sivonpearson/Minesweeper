import java.util.Random;

import java.awt.image.BufferedImage;
import java.awt.Graphics;

public class Board {
    private int boardHeight = 10;
    private int boardWidth = 20;
    private int numMines = 15;
    private int displayWidth = 42;
    private int displayHeight = 28;
    private int xPos = 0; //of top left part of screen in correspondence with the x pos on board
    private int yPos = 0; //of top left part of screen in correspondence with the y pos on board
    private int numFlagsPlaced = 0; //cannot exceed number of mines on the board
    private boolean newGame = true;
    Tile[][] board;

    public Board(int boardHeight, int boardWidth, int numMines) {
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        this.numMines = numMines;
        board = new Tile[boardHeight][boardWidth];
        if(boardWidth < displayWidth)
            displayWidth = boardWidth;
        if(boardHeight < displayHeight)
            displayHeight = boardHeight;
    }

    private void placeRandomMines() {
        Random rand = new Random();
        int numMinesPlaced = 0;
        while(numMinesPlaced < numMines) {
            int randomRow = rand.nextInt(boardHeight);
            int randomColumn = rand.nextInt(boardWidth);
            if(board[randomRow][randomColumn] == null) {
                board[randomRow][randomColumn] = new Tile(true);
                numMinesPlaced++;
            }
        }
    }

    //Sets the tiles' 'numMinesTouching' value to the correct value
    private void readNumMinesTouching(int r, int c) {
        int count = 0;
        for(int x = c-1; x < c+2; x++) {
            for(int y = r-1; y < r+2; y++) {
                boolean isSameBlock = (x==c) && (y==r);
                if(x >= 0 && x < boardWidth && y >= 0 && y < boardHeight && !isSameBlock) {
                    if(board[y][x].isMine())
                        count++;
                }
            }
        }
        board[r][c].setNumMinesTouching(count);
    }

    public boolean isNewGame() {
        return newGame;
    }

    public void setFlagged(int r, int c) {
        if(r >= 0 && r < boardHeight && c >= 0 && c < boardWidth) {
            if(numFlagsPlaced + 1 <= numMines && !board[r][c].isFlagged() && !board[r][c].isVisible()) {
                board[r][c].setFlagged();
                numFlagsPlaced++;
            }
        }
    }

    public void setUnflagged(int r, int c) {
        if(r >= 0 && r < boardHeight && c >= 0 && c < boardWidth) {
            if(numFlagsPlaced - 1 >= 0 && board[r][c].isFlagged()) {
                board[r][c].setUnflagged();
                numFlagsPlaced--;
            }
        }
    }

    public void toggleFlagged(int r, int c) {
        if(board[r][c].isFlagged())
            setUnflagged(r, c);
        else
            setFlagged(r, c);
    }

    //returns if the player is safe
    public boolean probe(int r, int c) {
        if(newGame)
            newProbe(r, c);
        if(r >= 0 && r < boardHeight && c >= 0 && c < boardWidth && !board[r][c].isVisible() && !board[r][c].isFlagged()) {
            if(board[r][c].isMine()) {
                return false;
            }
            else if(board[r][c].getNumMinesTouching() > 0) {
                board[r][c].setVisible();
                return true;
            }
            else{ //when numMinesTouching == 0
                board[r][c].setVisible();
                probe(r-1, c);
                probe(r-1, c-1);
                probe(r+1, c);
                probe(r+1, c+1);
                probe(r, c-1);
                probe(r+1, c-1);
                probe(r, c+1);
                probe(r-1, c+1);
                return true;
            }
        }
        else
            return true;
    }

    public void newProbe(int r, int c) {
        if(r >= 0 && r < boardHeight && c >= 0 && c < boardWidth) {
            newGame = false;
            emptyTile(r,c);
            if((boardWidth * boardHeight) - numMines >= 9) {
                emptyTile(r-1,c-1);
                emptyTile(r-1,c);
                emptyTile(r-1,c+1);
                emptyTile(r,c-1);
                emptyTile(r,c+1);
                emptyTile(r+1,c-1);
                emptyTile(r+1,c);
                emptyTile(r+1,c+1);
            }
            
            placeRandomMines();
            for(int i = 0; i < boardHeight; i++) {
                for(int j = 0; j < boardWidth; j++) {
                    if(board[i][j] == null)
                        board[i][j] = new Tile(false);
                }
            }
            for(int i = 0; i < boardHeight; i++) {
                for(int j = 0; j < boardWidth; j++) {
                    readNumMinesTouching(i, j);
                }
            }
        }
    }

    private boolean emptyTile(int r, int c) {
        if(r >= 0 && r < boardHeight && c >= 0 && c < boardWidth) {
            board[r][c] = new Tile(false);
        }
        return (r >= 0 && r < boardHeight && c >= 0 && c < boardWidth);
    }

    public void moveLeft() {
        if(xPos > 0) {
            xPos--;
        }
    }

    public void moveRight() {
        if(xPos + displayWidth < boardWidth) {
            xPos++;
        }
    }

    public void moveUp() {
        if(yPos > 0) {
            yPos--;
        }
    }

    public void moveDown() {
        if(yPos + displayHeight < boardHeight) {
            yPos++;
        }
    }

    public void display(boolean gameover, BufferedImage image) {
        //Game.clearScreen(image);
        for(int r = 0; r < displayHeight; r++) { //int r = 0; r < boardHeight; r++
            for(int c = 0; c < displayWidth; c++) { //int c = 0; c < boardWidth; c++
                if(board[r+yPos][c+xPos] != null && !gameover && board[r+yPos][c+xPos].isFlagged()) {
                    drawImage(c, r, image, TextureUtilities.getTexture(TextureUtilities.TextureTag.flagged)); //flag
                }
                else if(board[r+yPos][c+xPos] != null && gameover && board[r+yPos][c+xPos].isMine()) {
                    drawImage(c, r, image, TextureUtilities.getTexture(TextureUtilities.TextureTag.mine)); //is displayed during gameover
                }
                else if(board[r+yPos][c+xPos] == null || (!gameover && !board[r+yPos][c+xPos].isVisible())) {
                    drawImage(c, r, image, TextureUtilities.getTexture(TextureUtilities.TextureTag.undiscovered)); //is displayed for tiles that are not visible
                }
                else if(board[r+yPos][c+xPos].getNumMinesTouching() > 0) {
                    switch(board[r+yPos][c+xPos].getNumMinesTouching()) {
                        case 1:
                            drawImage(c, r, image, TextureUtilities.getTexture(TextureUtilities.TextureTag.discovered_1));
                            break;
                        case 2:
                            drawImage(c, r, image, TextureUtilities.getTexture(TextureUtilities.TextureTag.discovered_2));
                            break;
                        case 3:
                            drawImage(c, r, image, TextureUtilities.getTexture(TextureUtilities.TextureTag.discovered_3));
                            break;
                        case 4:
                            drawImage(c, r, image, TextureUtilities.getTexture(TextureUtilities.TextureTag.discovered_4));
                            break;
                        case 5:
                            drawImage(c, r, image, TextureUtilities.getTexture(TextureUtilities.TextureTag.discovered_5));
                            break;
                        case 6:
                            drawImage(c, r, image, TextureUtilities.getTexture(TextureUtilities.TextureTag.discovered_6));
                            break;
                        case 7:
                            drawImage(c, r, image, TextureUtilities.getTexture(TextureUtilities.TextureTag.discovered_7));
                            break;
                        case 8:
                            drawImage(c, r, image, TextureUtilities.getTexture(TextureUtilities.TextureTag.discovered_8));
                            break;
                    }
                }
                else if(board[r+yPos][c+xPos].getNumMinesTouching() == 0) { //board[r][c].getNumMinesTouching() == 0
                    drawImage(c, r, image, TextureUtilities.getTexture(TextureUtilities.TextureTag.discovered_empty)); //is displayed for tiles that have a 'numMinesTouching' value of 0
                }
            }
        }
    }

    public boolean checkIfWon() {
        int counter = (boardWidth * boardHeight) - numMines;
        for(int r = 0; r < boardHeight; r++) {
            for(int c = 0; c < boardWidth; c++) {
                if(board[r][c].isVisible())
                    counter--;
            }
        }
        return counter==0;
    }

    private void drawImage(int xPosition, int yPosition, BufferedImage image, BufferedImage texture) {
        xPosition *= TextureUtilities.textureWidth;
        yPosition *= TextureUtilities.textureHeight;
        for(int x = 0; x < TextureUtilities.textureWidth; x++) {
            for(int y = 0; y < TextureUtilities.textureHeight; y++) {
                image.setRGB(Game.gameAreaOffset_left+xPosition+x, Game.gameAreaOffset_top+yPosition+y, texture.getRGB(x, y));
            }
        }
    }

    public int getXPos() {
        return xPos;
    }
    public int getYPos() {
        return yPos;
    }

    public void displayData(Graphics g) {
        g.drawString("Flags: "+(numMines-numFlagsPlaced)+"                          "+xPos+", "+yPos, 10, 3 * g.getFontMetrics().getHeight());
    }

}