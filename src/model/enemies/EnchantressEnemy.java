package model.enemies;

import model.Model;
import model.characters.EnchantressCharacter;
import model.classes.Classes;
import model.combat.CombatLoot;
import model.combat.PersonCombatLoot;
import model.races.Race;
import model.states.CombatEvent;
import util.MyRandom;
import view.sprites.Sprite;

import java.util.List;

public class EnchantressEnemy extends Enemy {

    private static EnchantressCharacter character = new EnchantressCharacter();
    private static Sprite avatar = Classes.ENCHANTRESS.getAvatar(Race.WOOD_ELF, character.getAppearance());

    public EnchantressEnemy(char a) {
        super(a, "Enchantress");
    }

    @Override
    public int getMaxHP() {
        return 4;
    }

    @Override
    public int getSpeed() {
        return 5;
    }

    @Override
    protected Sprite getSprite() {
        return avatar;
    }

    @Override
    public int getDamage() {
        return 0;
    }


    @Override
    public void takeCombatTurn(Model model, CombatEvent combatEvent) {
        combatEvent.printQuote("Enchantress", MyRandom.sample(List.of("Please, no! don't kill me!",
                "No don't hurt me", "No!", "Please stop!", "Help!")));
        combatEvent.println(getName() + "'s turn is skipped.");
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new PersonCombatLoot(model);
    }

    @Override
    public String getDeathSound() {
        return "female_scream";
    }
}
