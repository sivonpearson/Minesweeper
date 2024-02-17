import java.awt.event.*;
import java.awt.MouseInfo;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.awt.Graphics;

public class Camera implements KeyListener, MouseListener {
    Random rand = new Random();
    private BufferedImage image;
    private int mouseX = MouseInfo.getPointerInfo().getLocation().x;
    private int mouseY = MouseInfo.getPointerInfo().getLocation().y;
    private TextureUtilities textures = new TextureUtilities();
    private Menu menu;
    private Board board;
    private Graphics g;
    
    public Camera(BufferedImage image, Menu menu) {
        this.image = image;
        this.menu = menu;
    }

    public void keyPressed(KeyEvent k) {
    }
    public void keyReleased(KeyEvent k) {
        switch(k.getKeyCode()) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                if(GameState.gameStatus == GameState.GameStatus.Ingame) {
                    board.moveLeft(); //move left
                    board.display(false, image);
                }
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                if(GameState.gameStatus == GameState.GameStatus.Ingame) {
                    board.moveRight(); //move right
                    board.display(false, image);
                }
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                if(GameState.gameStatus == GameState.GameStatus.Ingame) {
                    board.moveUp(); //move up
                    board.display(false, image);
                }
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                if(GameState.gameStatus == GameState.GameStatus.Ingame) {
                    board.moveDown(); //move down
                    board.display(false, image);
                }
                break;
            case KeyEvent.VK_M:
                if(GameState.gameStatus == GameState.GameStatus.Menu)
                    menu.setMasterDifficultyValues();
                    break;
            case KeyEvent.VK_ESCAPE:
                System.exit(1);
                break;
        }
        
    }
    public void keyTyped(KeyEvent k) {
    }
    public void mouseExited(MouseEvent m) {
    }
    public void mouseReleased(MouseEvent m) {
    }
    public void mouseClicked(MouseEvent m) {
        
    }
    public void mousePressed(MouseEvent m) {
        int mouseInt = m.getButton();
        if(board != null && board.isNewGame() && GameState.gameStatus == GameState.GameStatus.Ingame)
            mouseInt = MouseEvent.BUTTON1; //probe
        switch(mouseInt) {
            case MouseEvent.BUTTON1: //left mouse button
                if(GameState.gameStatus == GameState.GameStatus.Win) {
                    Game.clearScreen(image);
                    menu = new Menu(image);
                    GameState.setGameStatus(GameState.GameStatus.Menu);
                    menu.display(g);
                }
                if(GameState.gameStatus == GameState.GameStatus.Lose) {
                    Game.clearScreen(image);
                    menu = new Menu(image);
                    GameState.setGameStatus(GameState.GameStatus.Menu);
                    menu.display(g);
                }
                if(GameState.gameStatus == GameState.GameStatus.Ingame) {
                    boolean safe = board.probe(coordToBoardIndex_y(mouseY, board.getYPos()), coordToBoardIndex_x(mouseX, board.getXPos())); //probe
                    if(!safe) {
                        GameState.setGameStatus(GameState.GameStatus.Lose);
                        board.display(true, image);
                    }
                    else{
                        board.display(false, image);
                    }
                    if(board.checkIfWon()) {
                        GameState.setGameStatus(GameState.GameStatus.Win);
                    }
                }
                if(GameState.gameStatus == GameState.GameStatus.Menu) {
                    menu.click(mouseX, mouseY);
                    menu.display(g);
                    if(menu.boardIsReady()) {
                        board = menu.getBoard();
                        Game.clearScreen(image);
                        GameState.setGameStatus(GameState.GameStatus.Ingame);
                        board.display(false, image);
                    }
                }
                break;
            case MouseEvent.BUTTON3: //right mouse button
                if(GameState.gameStatus == GameState.GameStatus.Ingame) {
                    board.toggleFlagged(coordToBoardIndex_y(mouseY, board.getYPos()), coordToBoardIndex_x(mouseX, board.getXPos())); //flag or unflag
                    board.display(false, image);
                }
                break;
        }
        //this is it
    }
    public void mouseEntered(MouseEvent m) {
    }

    public void drawImage(BufferedImage texture, int xPosition, int yPosition) {
        for(int x = 0; x < TextureUtilities.textureWidth; x++) {
            for(int y = 0; y < TextureUtilities.textureHeight; y++) {
                if(xPosition+x < image.getWidth() && yPosition+y < image.getHeight()) //Game.gameAreaOffset+xPosition+x < image.getWidth() && Game.gameAreaOffset+yPosition+y < image.getHeight()
                    image.setRGB(xPosition+x, yPosition+y, texture.getRGB(x, y));
                    //image.setRGB(Game.gameAreaOffset+xPosition+x, Game.gameAreaOffset+yPosition+y, texture.getRGB(x, y));
            }
        }
    }

    public void updateMouseLocation(int windowX, int windowY) {
        mouseX = Math.abs(MouseInfo.getPointerInfo().getLocation().x - windowX);
        mouseY = Math.abs(MouseInfo.getPointerInfo().getLocation().y - windowY);
    }

    public int coordToBoardIndex_x(int x, int xPos) {
        return xPos + (int)((x-Game.gameAreaOffset_left) / TextureUtilities.textureWidth); //accounts for offset
    }

    public int coordToBoardIndex_y(int y, int yPos) {
        return yPos + (int)((y-Game.gameAreaOffset_top) / TextureUtilities.textureHeight); //accounts for offset
    }

    public void setGraphics(Graphics g) {
        this.g = g;
    }

    public void displayData(Graphics g) {
        board.displayData(g);
    }
}
