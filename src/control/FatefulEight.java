package control;

import model.Model;
import test.Balancing;
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
import java.util.Locale;

public class FatefulEight extends JFrame {

    public static final boolean TEST_MODE = false;
    private static final int TIMER_DELAY_MS = 20;
    public static String version = "1.44";
    private static boolean debug = false;
    private final DrawingArea drawingArea;
    public static boolean inFullScreenMode = false;
    private final Deque<KeyEvent> keyboardEvents = new LinkedList<>();
    private Timer timer;
    private MyKeyListener keyListener;

    public FatefulEight(DrawingArea drawingArea) {
        super("Fateful Eight");
        this.drawingArea = drawingArea;
        this.setSize(DrawingArea.getMagnification()*640,
                DrawingArea.getMagnification()*400+30);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setUndecorated(true);
        this.add(this.drawingArea, BorderLayout.CENTER);
        this.setVisible(true);
    }

    private void setModel(Model model) {
        this.keyListener = new MyKeyListener(model, this);
        this.addKeyListener(keyListener);
        timer = new Timer(TIMER_DELAY_MS, new ActionListener() {
            private long lastUpdateTime = System.currentTimeMillis();

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (model.gameExited()) {
                    timer.stop();
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
        Locale.setDefault(Locale.US);
        if (args.length == 1 && args[0].equals("--debug")) {
            debug = true;
        }
        for (int i = 240; i < 256; ++i) {
            System.out.print((char)i + " ");
        }
        MyColors.findClosest(MyColors.PEACH.toAwtColor().getRGB());

        DrawingArea drawingArea = new DrawingArea();
        FatefulEight frame = new FatefulEight(drawingArea);
        while (true) {
            Model model = new Model(drawingArea.getScreenHandler(), frame);
            frame.setModel(model);
            if (args.length == 1 && args[0].equals("--balancing")) {
                Balancing.runWeaponValueAnalysis(model);
                Balancing.runClothingAnalysis(model);
                Balancing.runAccessoryAnalysis(model);
                Balancing.runClassesAnalysis(model);
            }
            try {
                model.runGameScript();
            } catch (GameExitedException gee) {
                System.out.println("Game exited via exception.");
            }
            frame.tearDown();
            if (!model.gameAbandoned()) {
                break;
            }
        }
        frame.dispose();
        System.exit(0);
    }

    private void tearDown() {
        this.timer.stop();
        removeKeyListener(keyListener);
        drawingArea.clear();
        repaint();
    }

    public static boolean inDebugMode() {
        return debug;
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
