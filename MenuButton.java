import java.awt.image.BufferedImage;
import java.awt.Graphics;

public class MenuButton {
    private int xPos;
    private int yPos;
    private int w;
    private int h;
    private String text;
    public MenuButton(int xPos, int yPos, int w, int h, String text) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.w = w;
        this.h = h;
        this.text = text;
    }

    public void drawButton(BufferedImage image, Graphics g) {
        for(int x = xPos; x < xPos + w; x++) {
            for(int y = yPos; y < yPos + h; y++) {
                if(x == xPos || x == xPos + w - 1 || y == yPos || y == yPos + h - 1) {
                    image.setRGB(x, y, 0); //black outline
                }
                else{
                    image.setRGB(x, y, -4934476); //light gray inside
                }
            }
        }
        g.drawString(text, xPos + Math.abs(g.getFontMetrics().stringWidth(text) - w)/2, yPos + Math.abs(g.getFontMetrics().getHeight() - h) / 2 + g.getFontMetrics().getHeight());
    }

    public boolean clickingInBounds(int x, int y) { //returns true if mouse cursor is clicking within bounds of button
        return (x >= xPos && y >= yPos && x < xPos + w && y < yPos + h);
    }

}