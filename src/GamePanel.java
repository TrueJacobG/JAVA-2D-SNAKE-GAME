import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 50;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;

    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];

    int bodyParts = 6;
    int fruitsEaten;
    int fruitX;
    int fruitY;
    char direction = 'R';
    boolean running = true;

    Timer timer;
    Random random;


    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        startGame();

    }
    public void startGame(){
        newFruit();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        if(running){
            /*
            g.setColor(Color.WHITE);
            for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE;i++){
                g.drawLine(i*UNIT_SIZE,0,i*UNIT_SIZE,SCREEN_HEIGHT);
                g.drawLine(0,i*UNIT_SIZE,SCREEN_HEIGHT,i*UNIT_SIZE);
            }
            */

            g.setColor(Color.ORANGE);
            g.fillOval(fruitX, fruitY, UNIT_SIZE, UNIT_SIZE);

            g.setColor(Color.GREEN);
            for(int i = 0; i < bodyParts; i++){
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            printString(g, "Score "+fruitsEaten, true, false, 40);

        } else {
            printString(g, "Score "+fruitsEaten, true, false, 40);
            printString(g, "GAME OVER", false, true, 80);
        }

    }

    public void move(){
        for(int i = bodyParts; i > 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction){
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
        }
    }

    public void newFruit(){
        fruitX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        fruitY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    public void checkFruit(){
        if((x[0] == fruitX) && (y[0] == fruitY)){
            bodyParts++;
            fruitsEaten++;
            newFruit();
        }
    }

    public void checkCollisions(){
        for(int i = bodyParts; i > 0; i--){
            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }

        if(x[0] < 0){
            running = false;
        }

        if(x[0] > SCREEN_WIDTH){
            running = false;
        }

        if(y[0] < 0){
            running = false;
        }

        if(y[0] > SCREEN_HEIGHT){
            running = false;
        }

        if(!running){
            timer.stop();
        }

    }

    public void printString(Graphics g, String text, boolean isPositive, boolean isCenter, int fontSize){
        if(isPositive){
            g.setColor(Color.BLUE);
        } else {
            g.setColor(Color.RED);
        }
        g.setFont(new Font("Arial", Font.BOLD, fontSize));
        FontMetrics metrics = getFontMetrics(g.getFont());
        if(isCenter){
            g.drawString(text, (SCREEN_WIDTH - metrics.stringWidth(text))/2, SCREEN_HEIGHT/2);
        } else {
            g.drawString(text, (SCREEN_WIDTH - metrics.stringWidth(text))/2, g.getFont().getSize());
        }


    }

    @Override
    public void actionPerformed(ActionEvent e){
         if(running){
             move();
             checkFruit();
             checkCollisions();
         }
         repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
