package model.enemies;

import model.Model;
import model.characters.BungoDarkwood;
import model.classes.Classes;
import model.combat.CombatLoot;
import model.combat.NoCombatLoot;
import model.races.Race;
import view.sprites.Sprite;

public class HalflingMartialArtist extends Enemy {

    private static final Sprite SPRITE = Classes.None.getAvatar(Race.HALFLING, new BungoDarkwood());;

    public HalflingMartialArtist(char a) {
        super(a, "Halfling Martial Artist");
    }

    @Override
    public int getMaxHP() {
        return 5;
    }

    @Override
    public int getSpeed() {
        return 9;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 5;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new NoCombatLoot();
    }
}
