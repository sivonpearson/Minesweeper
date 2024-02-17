import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class Game extends JFrame implements Runnable{
    private static final long serialVersionUID = 1L;
    public int windowWidth = 1408; //640 , 1100 , 1300
    public int windowHeight = 992; //480 , 825 , 975
    public static int gameAreaOffset_top = 64;
    public static int gameAreaOffset_left = 32;
    public static int gameAreaOffset_right = 32;
    public static int gameAreaOffset_bottom = 32;
    private Thread thread;
    private boolean running;
    private BufferedImage image;
    public Camera camera;
    public static Graphics g;
    private Menu menu;

    public Game() {
        thread = new Thread(this);
        image = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_RGB);
        clearScreen(image);
        menu = new Menu(image);
        camera = new Camera(image, menu);
        addKeyListener(camera);
        addMouseListener(camera);
        setSize(windowWidth, windowHeight);
        setResizable(false);
        setTitle("Minesweeper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        start();
    }

    private synchronized void start() {
        running = true;
        thread.start();
    }
    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if(bs == null) {
            createBufferStrategy(3);
            return;
        }
        g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
        camera.setGraphics(g);
        if(GameState.gameStatus == GameState.GameStatus.Menu) {
            menu.display(g);
        }
        if(GameState.gameStatus == GameState.GameStatus.Ingame) {
            camera.displayData(g);
        }
        bs.show();
    }

    public void run() {
        long lastTime = System.nanoTime();
        final double ns = 1000000000.0 / 60.0;//60 times per second
        double delta = 0;
        requestFocus();
        while(running) {
            long now = System.nanoTime();
            delta = delta + ((now-lastTime) / ns);
            lastTime = now;
            while (delta >= 1)//Make sure update is only happening 60 times a second
            {
                //handles all of the logic restricted time
                camera.updateMouseLocation(getLocation().x, getLocation().y);
                delta--;
            }
            render();//displays to the screen unrestricted time
        }
    }

    public static void clearScreen(BufferedImage image) {
        for(int x = 0; x < image.getWidth(); x++) {
            for(int y = 0; y < image.getHeight(); y++) {
                image.setRGB(x, y, -4934476); //light gray
            }
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
    }

}