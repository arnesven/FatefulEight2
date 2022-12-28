package model.quests.scenes;

import model.enemies.VampireEnemy;
import view.sprites.LoopingSprite;
import view.sprites.SkeletonEnemySprite;

import java.util.List;

public class VampireCombatSubScene extends CombatSubScene {

    public VampireCombatSubScene(int col, int row) {
        super(col, row, List.of(new VampireEnemy('A')));
    }

    @Override
    protected String getCombatDetails() {
        return "Vampire Guardian";
    }
}
