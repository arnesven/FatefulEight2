package model.quests;

import model.Model;
import model.enemies.Enemy;
import model.states.QuestState;
import view.ScreenHandler;
import view.sprites.Sprite;

import java.awt.*;
import java.util.List;

public class MovingEnemyGroup {
    private final List<Enemy> enemies;
    private final List<Point> path;
    private int pathIndex;
    private ChooseNode node;
    private boolean defeated = false;

    public MovingEnemyGroup(List<Enemy> enemies, List<Point> path, int pathIndex) {
        this.enemies = enemies;
        this.path = path;
        this.pathIndex = pathIndex;
    }

    public MovingEnemyGroup(List<Enemy> enemies, List<Point> path) {
        this(enemies, path, 0);
    }

    public Sprite getSprite() {
        return enemies.get(0).getAvatar();
    }

    public void drawYourself(ScreenHandler screenHandler, Point point) {
        Sprite sprite = getSprite();
        screenHandler.register(sprite.getName(), point, sprite, 2);
    }

    public void setNode(ChooseNode chooseNode) {
        this.node = chooseNode;
    }

    public ChooseNode getNode() {
        return node;
    }

    public QuestEdge getEdgeToMoveTo(Model model, QuestState state, QuestNode[][] nodeGrid) {
        if (defeated) {
            return null;
        }
        pathIndex = (pathIndex + 1) % path.size();
        QuestNode destination = nodeGrid[path.get(pathIndex).x][path.get(pathIndex).y];
        for (QuestEdge edge : getNode().getConnections()) {
            if (edge.getNode() == destination) {
                return edge;
            }
        }
        return null;
    }

    public void setOnPath(QuestNode[][] nodeGrid) {
        int x = path.get(pathIndex).x;
        int y = path.get(pathIndex).y;
        ((ChooseNode)nodeGrid[x][y]).addEnemyGroup(this);
        setNode((ChooseNode)nodeGrid[x][y]);
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setDefeated(boolean allDead) {
        this.defeated = allDead;
    }

    public boolean isDefeated() {
        return defeated;
    }
}
