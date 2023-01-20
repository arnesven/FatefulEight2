package view.widget;

import view.sprites.Sprite;

import java.awt.*;
import java.io.Serializable;

public class QuestBackground implements Serializable {
    public Point position;
    public Sprite sprite;
    public boolean shifted;

    public QuestBackground(Point point, Sprite sprite, boolean shifted) {
        this.position = point;
        this.sprite = sprite;
        this.shifted = shifted;
    }

    public QuestBackground(Point point, Sprite sprite) {
        this(point, sprite, true);
    }
}
