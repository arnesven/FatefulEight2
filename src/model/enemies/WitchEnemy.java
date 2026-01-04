package model.enemies;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.combat.loot.CombatLoot;
import model.combat.loot.PersonCombatLoot;
import model.enemies.behaviors.WitchAttackBehavior;
import model.items.ItemDeck;
import model.items.Prevalence;
import model.races.Race;
import view.sprites.AvatarSprite;
import view.sprites.Sprite;

public class WitchEnemy extends SummonerHumanoidEnemy {
    private final AvatarSprite avatar;

    public WitchEnemy(char group, Race race, CharacterAppearance app) {
        super(group, "Witch", new WitchAttackBehavior());
        this.avatar = Classes.WIT.getAvatar(race, app);
    }

    @Override
    protected Sprite getSprite() {
        return avatar;
    }

    @Override
    public int getDamage() {
        return 3;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new WitchLoot(model);
    }

    @Override
    public int getMaxHP() {
        return 8;
    }

    @Override
    public int getSpeed() {
        return 7;
    }

    private static class WitchLoot extends PersonCombatLoot {
        public WitchLoot(Model model) {
            super(model);
            getItems().addAll(model.getItemDeck().draw(ItemDeck.allPotions(),
                    3, Prevalence.unspecified, 0.2));
        }
    }
}
