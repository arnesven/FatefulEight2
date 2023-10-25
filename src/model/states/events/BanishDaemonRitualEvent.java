package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.enemies.FiendEnemy;
import model.races.Race;
import model.states.DailyEventState;
import util.MyStrings;
import view.MyColors;
import view.sprites.SmokeBallAnimation;
import view.sprites.Sprite;
import view.subviews.CombatTheme;

import java.util.List;

public class BanishDaemonRitualEvent extends RitualEvent {
    private final Sprite fiendSprite;
    private SmokeBallAnimation flash;

    public BanishDaemonRitualEvent(Model model) {
        super(model, MyColors.RED);
        this.fiendSprite = new FiendEnemy('A').getAvatar();
    }

    @Override
    protected CombatTheme getTheme() {
        return getModel().getCurrentHex().getCombatTheme();
    }

    @Override
    protected boolean runEventIntro(Model model, List<GameCharacter> ritualists) {
        GameCharacter sorcerer = makeRandomCharacter(3);
        sorcerer.setClass(Classes.SOR);
        sorcerer.addToHP(999);
        ritualists.set(0, sorcerer);
        println("The party encounters a large group of dwarves here, gathered around a large pit. " +
                "In the pit stands an enormous daemon bound in chains. It seems somewhat subdued. The dwarves are shouting " +
                "and arguing amongst themselves.");
        println("There are also " + MyStrings.numberWord(ritualists.size()) + " mages here who are about to do a banishing ritual. " +
                "They are looking for some extra mages to join them in performing it.");
        model.getLog().waitForAnimationToFinish();
        if (ritualists.size() + model.getParty().size() < 5) {
            println("Unfortunately you do not have enough party members to join the ritual.");
            model.getLog().waitForAnimationToFinish();
            return false;
        }
        showExplicitPortrait(model, ritualists.get(0).getAppearance(), "Sorcerer");
        portraitSay("These miners here have been plagued for some time by a daemon. " +
                "We've finally managed to trap it in the pit below, but to be rid of it once and for all " +
                "we need to banish it from this world. We're a few mages short, and the dwarves know nothing but mining. " +
                "Say, you seem like you could be skilled in magic... Would " +
                "you lend us a hand?");
        return true;
    }

    @Override
    protected void runEventOutro(Model model, boolean success, int power) {
        if (success) {
            println("The daemon disappears in a flash of light.");
            portraitSay("Thank goodness. Please let me heal you.");
            healParty(model);
            showRandomPortrait(model, Classes.MIN, Race.DWARF, "Miner");
            portraitSay("Our greatest thanks for helping us with this nuisance. Now we are free to return to our " +
                    "mines once more. Please accept this gift as a token of our gratitude.");
            println("The party gains " + 20*power + " materials!");
            model.getParty().getInventory().addToMaterials(20*power);
        } else {
            println("The daemon remains in the pit.");
            portraitSay("Aaa... what a drag. Now we'll have to figure out something else instead. Perhaps " +
                    "if we made a very durable cage...");
        }
        println("You part ways with the mages and the dwarves.");
    }

    @Override
    public Sprite getCenterSprite() {
        return fiendSprite;
    }

    @Override
    public Sprite getCenterSpriteSuccess() {
        if (flash == null) {
            flash = new SmokeBallAnimation();
        }
        return flash;
    }
}
