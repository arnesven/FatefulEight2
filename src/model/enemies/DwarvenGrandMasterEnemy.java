package model.enemies;

import model.Model;
import model.characters.preset.TorhildAmbershard;
import model.classes.Classes;
import model.combat.loot.CombatLoot;
import model.combat.loot.NoCombatLoot;
import model.races.Race;
import view.sprites.Sprite;

public class DwarvenGrandMasterEnemy extends HumanoidEnemy {
    private static final Sprite SPRITE = Classes.PAL.getAvatar(Race.DWARF, new TorhildAmbershard());;

    public DwarvenGrandMasterEnemy(char a) {
        super(a, "Dwarven Grand Master");
    }

    @Override
    public int getMaxHP() {
        return 18;
    }

    @Override
    public int getSpeed() {
        return 5;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 7;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new NoCombatLoot();
    }
}
