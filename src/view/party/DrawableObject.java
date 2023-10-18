package view.party;

import model.Model;

import java.awt.*;

public abstract class DrawableObject {
    public Point position;

    public DrawableObject(int x, int y) {
        this.position = new Point(x, y);
    }

    public abstract void drawYourself(Model model, int x, int y);
}
