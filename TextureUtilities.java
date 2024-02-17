import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TextureUtilities {
    public static BufferedImage textureMap;
    public static int textureWidth = 32;
    public static int textureHeight = 32;
    public static enum TextureTag {
        undiscovered,
        discovered_empty,
        flagged,
        mine,
        discovered_1,
        discovered_2,
        discovered_3,
        discovered_4,
        discovered_5,
        discovered_6,
        discovered_7,
        discovered_8
    }

    public TextureUtilities() {
        loadImages();
    }

    public void loadImages() {
        try{
            textureMap = ImageIO.read(new File("Data/TextureMap.png"));
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage getTexture(TextureTag texture) {
        switch(texture) {
            case undiscovered:
                return textureMap.getSubimage(0, 0, textureWidth, textureHeight);
            case discovered_empty:
                return textureMap.getSubimage(32, 0, textureWidth, textureHeight);
            case flagged:
                return textureMap.getSubimage(64, 0, textureWidth, textureHeight);
            case mine:
                return textureMap.getSubimage(96, 0, textureWidth, textureHeight);
            case discovered_1:
                return textureMap.getSubimage(0, 32, textureWidth, textureHeight);
            case discovered_2:
                return textureMap.getSubimage(32, 32, textureWidth, textureHeight);
            case discovered_3:
                return textureMap.getSubimage(64, 32, textureWidth, textureHeight);
            case discovered_4:
                return textureMap.getSubimage(96, 32, textureWidth, textureHeight);
            case discovered_5:
                return textureMap.getSubimage(0, 64, textureWidth, textureHeight);
            case discovered_6:
                return textureMap.getSubimage(32, 64, textureWidth, textureHeight);
            case discovered_7:
                return textureMap.getSubimage(64, 64, textureWidth, textureHeight);
            case discovered_8:
                return textureMap.getSubimage(96, 64, textureWidth, textureHeight);
            default: //should never happen
                return textureMap;
        }
    }

}