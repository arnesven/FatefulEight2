package model.quests;

import model.Model;
import model.combat.CombatAdvantage;
import model.enemies.Enemy;
import model.states.CombatEvent;
import model.states.QuestState;
import util.MyRandom;
import view.MyColors;
import view.sprites.SmokeBallAnimation;
import view.sprites.Sprite32x32;
import view.combat.MansionTheme;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChooseNode extends QuestJunction {
    private static final Sprite32x32 SPRITE = new Sprite32x32("choosenode", "quest.png", 0x03,
            MyColors.BLACK, MyColors.WHITE, MyColors.GRAY, MyColors.BROWN);
    private List<MovingEnemyGroup> enemyGroups;
    private SmokeBallAnimation poof = null;

    public ChooseNode(int col, int row, List<QuestEdge> questEdges) {
        super(col, row);
        for (QuestEdge edge : questEdges) {
            connectTo(edge);
        }
        enemyGroups = new ArrayList<>();
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE, 1);
        if (hasEnemies()) {
            enemyGroups.get(enemyGroups.size()-1).drawYourself(model.getScreenHandler(), new Point(xPos, yPos));
        }
        if (poof != null) {
            model.getScreenHandler().register(poof.getName(), new Point(xPos, yPos), poof);
        }
    }

    public boolean hasEnemies() {
        return !enemyGroups.isEmpty();
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public QuestEdge run(Model model, QuestState state) {
        boolean didFlee = preRunHook(model, state);
        QuestEdge finalEdge;
        if (!didFlee) {
            finalEdge = questNodeInput(model, state, getConnections());
        } else {
            finalEdge = MyRandom.sample(getConnections());
        }
        return finalEdge;
    }

    protected boolean preRunHook(Model model, QuestState state) {
        return false;
    }

    public void addEnemyGroup(MovingEnemyGroup movingEnemyGroup) {
        this.enemyGroups.add(movingEnemyGroup);
    }

    public void removeEnemyGroup(MovingEnemyGroup group) {
        enemyGroups.remove(group);
    }

    public boolean runCombat(Model model, QuestState state) {
        List<Enemy> enemies = new ArrayList<>();
        String groupName = "";
        for (MovingEnemyGroup group : enemyGroups) {
            for (Enemy e : group.getEnemies()) {
                if (!e.isDead()) {
                    groupName = group.getName();
                    enemies.add(e);
                }
            }
        }
        state.println("You encounter a group of " + groupName + "!");
        CombatEvent combat = new CombatEvent(model, enemies, new MansionTheme(), true, CombatAdvantage.Neither);
        combat.run(model);
        state.transitionToQuestView(model);
        for (MovingEnemyGroup group : enemyGroups) {
            boolean allDead = true;
            for (Enemy e : group.getEnemies()) {
                if (!e.isDead()) {
                    allDead = false;
                }
            }
            group.setDefeated(allDead);
        }
        enemyGroups.removeIf(MovingEnemyGroup::isDefeated);
        return combat.fled();
    }

    public void clearEnemyGroups() {
        enemyGroups.clear();
    }

    public void addPoof() {
        this.poof = new SmokeBallAnimation();
    }

    public QuestEdge questNodeInput(Model model, QuestState state, List<QuestEdge> connections) {
        do {
            state.waitForReturnSilently();
            for (QuestEdge edge : connections) {
                if (edge.getNode() == state.getSelectedElement()) {
                    if (edge.getNode().isEligibleForSelection(model, state)) {
                        return edge;
                    }
                }
            }
            state.println("You cannot move there.");
        } while (true);
    }
}
