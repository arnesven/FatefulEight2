package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.SpellMasteries;
import model.characters.appearance.AdvancedAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.CompanionEnemy;
import model.enemies.Enemy;
import model.items.Equipment;
import model.items.clothing.MesmersRobes;
import util.MyLists;
import util.MyPair;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MagicianEvent extends MagicExpertGeneralInteractionEvent {
    private AdvancedAppearance portrait;

    public MagicianEvent(Model model) {
        super(model, "Watch", MyRandom.randInt(30, 70));
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        this.portrait = PortraitSubView.makeRandomPortrait(Classes.MAR);
        println("A traveling magician has set up his stage next to the road " +
                "and you can hear him calling to the small crowd that has gathered here.");
        showExplicitPortrait(model, portrait, "Magician");
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        portraitSay("Ladies and gentlemen, step right up! Come into my tent " +
                "to see astounding thaumaturgy. Right before your very " +
                "eyes, I will...");
        randomSayIfPersonality(PersonalityTrait.critical, List.of(model.getParty().getLeader()),
                "This guy's a hack. We should walk away.");
        randomSayIfPersonality(PersonalityTrait.encouraging, List.of(model.getParty().getLeader()),
                "We should watch the show, you know, to show support.");
        randomSayIfPersonality(PersonalityTrait.playful, List.of(model.getParty().getLeader()),
                "Oh goody! Magic tricks!");
        randomSayIfPersonality(PersonalityTrait.stingy, List.of(model.getParty().getLeader()),
                "I doubt this is worth paying for.");
        int cost = model.getParty().size();
        if (cost > model.getParty().getGold()) {
            println("Unfortunately, your purse is so light you cannot even afford to see the show, and you pass by" +
                    " wondering what kind of spectacle you are missing.");
        } else {
            print("Do you wish to pay " + cost + " gold to see show? (Y/N) ");
            if (yesNoInput()) {
                model.getParty().spendGold(cost);
                print("The show is truly very good and you stay a while after it has concluded to complement " +
                        "the magician. He thanks you and offers to give you some pointers in the field of " +
                        "magical showmanship, ");
                ChangeClassEvent changeEvents = new ChangeClassEvent(model, Classes.MAG);
                changeEvents.areYouInterested(model);
                setCurrentTerrainSubview(model);
                showExplicitPortrait(model, portrait, "Magician");
                model.getLog().waitForAnimationToFinish();
                SpellMasteries.offersToTutorSpells(model, this, "the magician",
                        MyLists.filter(Classes.MAG.getSkills(), Skill::isMagic));
            }
        }
        return true;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm a magician. Yes, I happen to be self-taught. So what?";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        GameCharacter gc = new GameCharacter("Magician", "", portrait.getRace(), Classes.MAG, portrait,
                Classes.NO_OTHER_CLASSES, new Equipment(model.getItemDeck().getRandomWand(), new MesmersRobes(),
                model.getItemDeck().getRandomJewelry()));
        gc.setLevel(MyRandom.randInt(1, 4));
        return gc;
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
        List<Enemy> enemies = new ArrayList<>();
        for (int i = MyRandom.randInt(3, 5); i > 0; --i) {
            CharacterClass cls = MyRandom.sample(List.of(Classes.CAP, Classes.BRD, Classes.PRI, Classes.NOB, Classes.None));
            enemies.add(new CompanionEnemy(PortraitSubView.makeRandomPortrait(cls), cls, model.getItemDeck().getRandomWeapon()));
        }
        enemies.addAll(makeBodyGuards(MyRandom.randInt(2), 'C'));
        return enemies;
    }

    @Override
    protected ProvokedStrategy getProvokedStrategy() {
        return ProvokedStrategy.FIGHT_IF_ADVANTAGE;
    }

    @Override
    protected Map<String, MyPair<String, String>> makeSpecificTopics(Model model) {
        return Map.of("show", new MyPair<>("Tell me about your magic show.",
                "My show? Oh it's spectacular, if I say so myself. Genuine, grade-A magic. " +
                        "I hope you enjoy it."));
    }
}
