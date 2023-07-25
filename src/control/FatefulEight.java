package control;

import model.Model;
import view.DrawingArea;
import view.MyColors;
import view.sprites.AnimationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Deque;
import java.util.LinkedList;

public class FatefulEight extends JFrame {

    public static final boolean TEST_MODE = false;
    private static final int TIMER_DELAY_MS = 20;
    public static String version = "1.25";
    private DrawingArea drawingArea;
    public static boolean inFullScreenMode = false;
    private Deque<KeyEvent> keyboardEvents = new LinkedList<>();
    private Timer timer;

    public FatefulEight(Model model, DrawingArea drawingArea) {
        super("Fateful Eight");
        this.drawingArea = drawingArea;
        this.setSize(this.drawingArea.getMagnification()*640,
                this.drawingArea.getMagnification()*400+30);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        this.add(this.drawingArea, BorderLayout.CENTER);
        this.setVisible(true);
        this.addKeyListener(new MyKeyListener(model, this));

        timer = new Timer(TIMER_DELAY_MS, new ActionListener() {
            private long lastUpdateTime = System.currentTimeMillis();

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (model.gameExited()) {
                    timer.stop();
                    FatefulEight.this.dispose();
                    return;
                }
                checkKeyboardInput(model);
                long elapsedTime = System.currentTimeMillis() - lastUpdateTime;
                model.update(elapsedTime);
                AnimationManager.stepAnimations(elapsedTime, model);
                lastUpdateTime = System.currentTimeMillis();
                drawingArea.update();
                repaint();
            }
        });
        timer.start();
    }

    public static void main(String[] args) {
        for (int i = 240; i < 256; ++i) {
            System.out.print((char)i + " ");
        }
        MyColors.findClosest(MyColors.PEACH.toAwtColor().getRGB());
        DrawingArea drawingArea = new DrawingArea();
        Model model = new Model(drawingArea.getScreenHandler());
        FatefulEight frame = new FatefulEight(model, drawingArea);
        model.setFrame(frame);
        model.runGameScript();
    }

    private void checkKeyboardInput(Model model) {
        while (!keyboardEvents.isEmpty()) {
            model.handleKeyEvent(keyboardEvents.pop());
        }
    }

    public void toggleFullScreen() {
        if (inFullScreenMode) {
            windowMode();
        } else {
            fullScreen();
        }
    }


    private void fullScreen() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        device.setFullScreenWindow(this);
        inFullScreenMode = true;
    }


    private void windowMode() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        device.setFullScreenWindow(null);
        inFullScreenMode = false;
    }


    public void enqueueKeyEvent(KeyEvent keyEvent) {
        keyboardEvents.addLast(keyEvent);
    }
}
