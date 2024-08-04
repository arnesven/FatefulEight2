package model.states.events;

import model.Model;
import model.enemies.Enemy;
import model.enemies.GelatinousBlobEnemy;
import model.enemies.SmallScorpionEnemy;
import model.states.DailyEventState;
import util.MyRandom;
import view.combat.WastelandNightCombatTheme;

import java.util.ArrayList;
import java.util.List;

public class BlobNightAttackEvent extends NightTimeAttackEvent {
    public BlobNightAttackEvent(Model model) {
        super(model, 6, "the ground suddenly seems sponge. Gelatinous Blobs are rising out of the ground",
                new WastelandNightCombatTheme(), "Gelatinous Blobs are rising out of the ground");
    }

    @Override
    protected List<Enemy> getEnemies(Model model) {
        GelatinousBlobEnemy blobTemplate = GelatinousBlobEvent.makeRandomBlob();
        List<Enemy> enemies = new ArrayList<>();
        int numberOfEnemies = Math.max(1, model.getParty().partyStrength() / (blobTemplate).getThreat());
        for (int i = numberOfEnemies; i > 0; --i) {
            enemies.add(blobTemplate.copy());
        }
        return enemies;
    }
}
