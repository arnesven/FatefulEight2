package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.ParalysisCondition;
import model.combat.TimedParalysisCondition;
import model.enemies.Enemy;
import model.items.Item;
import model.states.CombatEvent;
import util.MyRandom;
import view.MyColors;
import view.sprites.CombatSpellSprite;
import view.sprites.Sprite;

import java.util.ArrayList;
import java.util.List;

public class ConjurePhantasmSpell extends CombatSpell {
    private static final Sprite SPRITE = new CombatSpellSprite(15, 8,MyColors.BROWN, MyColors.BLUE, MyColors.WHITE);

    public ConjurePhantasmSpell() {
        super("Conjure Phantasm", 24, MyColors.BLUE, 10, 2);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new ConjurePhantasmSpell();
    }

    @Override
    public String getDescription() {
        return "Terrifies enemies and prevents them from attacking for 2 rounds.";
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return target instanceof Enemy;
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        List<Enemy> targets = new ArrayList<>(combat.getEnemies());
        targets.remove(target);
        while (targets.size() > 2) {
            targets.remove(MyRandom.randInt(targets.size()));
        }
        targets.add((Enemy)target);
        for (Enemy e : targets) {
            combat.println(e.getName() + " has been paralyzed with fear!");
            e.addCondition(new TimedParalysisCondition());
        }
    }
}
