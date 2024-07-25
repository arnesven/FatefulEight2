package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.SkeletonAppearance;
import model.classes.Classes;
import model.enemies.Enemy;
import model.enemies.FormerPartyMemberEnemy;
import model.enemies.SkeletonEnemy;
import model.enemies.UndeadLordEnemy;
import model.items.Item;
import model.items.ItemDeck;
import model.races.NormalShoulders;
import model.ruins.objects.CorpseObject;
import util.MyRandom;
import util.MyStrings;
import view.MyColors;
import view.sprites.Sprite;
import view.subviews.CollapsingTransition;
import view.combat.CombatTheme;
import view.combat.DungeonTheme;
import view.subviews.SubView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NecromancerRitual extends RitualEvent {

    private final Sprite animation;
    private SubView introSubView;
    private final CharacterAppearance undeadAppearance;

    public NecromancerRitual(Model model) {
        super(model, MyColors.BLACK);
        animation = new SkeletonEnemy('A').getAvatar();
        undeadAppearance = new SkeletonAppearance(new NormalShoulders(false), false);
        undeadAppearance.setClass(Classes.SOR);
    }

    @Override
    protected CombatTheme getTheme() {
        return new DungeonTheme();
    }

    @Override
    protected boolean runEventIntro(Model model, List<GameCharacter> ritualists) {
        this.introSubView = model.getSubView();
        GameCharacter witch = makeRandomCharacter(3);
        witch.setClass(Classes.WIT);
        witch.addToHP(999);
        ritualists.set(0, witch);
        println("The party descents into the crypt and can soon hear voices. Arriving in a large chamber, the party " +
                "encounters " + MyStrings.numberWord(ritualists.size()) + " mages here who standing around a case " +
                "with some mummified remains. They are looking for some extra mages to join them in performing a ritual.");
        model.getLog().waitForAnimationToFinish();
        if (ritualists.size() + model.getParty().size() < 5) {
            println("Unfortunately you do not have enough party members to join the ritual.");
            model.getLog().waitForAnimationToFinish();
            return false;
        }
        showExplicitPortrait(model, ritualists.get(0).getAppearance(), "Necromancer");
        portraitSay("Greetings traveler. Yes, we are necromancers, but please, don't be alarmed. We are " +
                "simply trying to resurrect a great lord and wise leader so " + heOrShe(witch.getGender()) +
                " may divulge even greater arcane secrets to our coven. Unfortunately we are too few magic users " +
                "to perform the ritual. Would you be interested in helping us?");
        return true;
    }

    @Override
    protected void runEventOutro(Model model, boolean success, int power) {
        if (success) {
            println("The mummified remains suddenly come to life");
            portraitSay("Oh great lord, we have awoken you so that you may instruct and educate us!");
            showExplicitPortrait(model, undeadAppearance,"Undead Lord");
            portraitSay("Aah, well done my acolytes. Indeed I shall reward you all.");
            showExplicitPortrait(model, getRitualists().get(0).getAppearance(), "Necromancer");
            portraitSay("Thank you, oh lord!");
            showExplicitPortrait(model, undeadAppearance, "Undead Lord");
            portraitSay("But who are these strangers? They do not wear the garb of our cult.");
            showExplicitPortrait(model, getRitualists().get(0).getAppearance(), "Necromancer");
            portraitSay("Uhm, they are allies... they aided us in the ritual. They have been promised " +
                    "some kind of reward...");
            showExplicitPortrait(model, undeadAppearance, "Undead Lord");
            portraitSay("They are outsiders. Dispose of them immediately.");
            leaderSay("Now wait just a minute here!");
            showExplicitPortrait(model, getRitualists().get(0).getAppearance(), "Necromancer");
            portraitSay("As you wish master.");

            List<Enemy> enemies = new ArrayList<>();
            for (GameCharacter gc : getRitualists()) {
                if (!model.getParty().getPartyMembers().contains(gc)) {
                    enemies.add(new FormerPartyMemberEnemy(gc));
                }
            }
            for (GameCharacter gc : getBench()) {
                if (!model.getParty().getPartyMembers().contains(gc)) {
                    enemies.add(new FormerPartyMemberEnemy(gc));
                }
            }
            enemies.add(new UndeadLordEnemy('A'));
            Collections.shuffle(enemies);
            runCombat(enemies, getTheme(), true);
            if (model.getParty().isWipedOut()) {
                return;
            }
            if (!haveFledCombat()) {
                CollapsingTransition.transition(model, introSubView);
                println("With the necromancer and their undead lord vanquished you help yourselves to the " +
                        "supplies and gear in the crypt.");
                for (int i = MyRandom.randInt(6, 9); i > 0; i--) {
                    Item it = generateCryptLoot(model);
                    println("You find " + it.getName() + ".");
                    it.addYourself(model.getParty().getInventory());
                }
                int gold = MyRandom.randInt(10, 30);
                println("You find " + gold + " gold");
                model.getParty().addToGold(gold);
                int ingredients = MyRandom.randInt(10, 25);
                println("You find " + ingredients + " ingredients.");
                model.getParty().getInventory().addToIngredients(ingredients);
                int rations = MyRandom.randInt(16, 40);
                model.getParty().addToFood(rations);
                println("You find " + rations + " food rations.");
            } else {
                println("You flee the crypt.");
            }
        } else {
            println("The remains stay inanimate.");
            portraitSay("What a shame. Perhaps with more study, preparation and determination we shall be able " +
                    "to resurrect our lord. But for now, I think we all need some rest.");
            println("You part ways with the necromancers.");
        }
    }

    private Item generateCryptLoot(Model model) {
        int roll = MyRandom.rollD10();
        if (roll < 3) {
            return model.getItemDeck().getRandomPotion();
        }
        if (roll < 5) {
            return model.getItemDeck().getRandomJewelry();
        }
        if (roll < 7) {
            return MyRandom.sample(ItemDeck.allScrolls()).copy();
        }
        if (roll < 9) {
            return model.getItemDeck().getRandomWand();
        }
        return model.getItemDeck().getRandomSpell();
    }

    @Override
    public Sprite getCenterSprite() {
        return CorpseObject.SPRITE;
    }

    @Override
    public Sprite getCenterSpriteSuccess() {
        return animation;
    }
}
