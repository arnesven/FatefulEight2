package control;

import model.Model;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MyKeyListener implements KeyListener {
    private final Model model;
    private final FatefulEight fatefulEight;

    public MyKeyListener(Model model, FatefulEight fatefulEight) {
        this.model = model;
        this.fatefulEight = fatefulEight;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER && keyEvent.isAltDown()) {
            fatefulEight.toggleFullScreen();
        } else if (keyEvent.getKeyCode() != KeyEvent.VK_ALT) {
            fatefulEight.enqueueKeyEvent(keyEvent);
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
