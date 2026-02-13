package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.SpellMasteries;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.normal.SorcererClass;
import model.enemies.ApprenticeEnemy;
import model.enemies.Enemy;
import model.items.Equipment;
import model.items.Item;
import model.items.clothing.MagesRobes;
import model.items.weapons.MorningStar;
import model.items.weapons.OldWand;
import model.states.ShopState;
import util.MyLists;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class SorcerersTowerEvent extends MagicExpertGeneralInteractionEvent {
    private AdvancedAppearance portrait;

    public SorcerersTowerEvent(Model model) {
        super(model, "Talk to", MyRandom.randInt(50, 110));
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Visit Sorcerer", "We're close to " + getDistantDescription() + ", a sorcerer lives within");
    }

    @Override
    public String getDistantDescription() {
        return "a stone tower";
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        this.portrait = PortraitSubView.makeRandomPortrait(Classes.SOR);
        showExplicitPortrait(model, portrait, "Sorcerer");
        showEventCard("A tall stone structure stands silently in front of the party. " +
                "Within is a sorcerer who only accepts persons who are " +
                "acquainted with magic.");
        randomSayIfPersonality(PersonalityTrait.diplomatic, new ArrayList<>(),
                "Open your tower Sorcerer! We have come a long way to meet with you!");
        boolean success = model.getParty().doSoloSkillCheck(model, this, Skill.MagicAny, 7);
        if (success) {
            print("The sorcerer admits you into the tower.");
            return true;
        }
        println("The sorcerer sneers at your feeble attempts to impress him. The door to his tower remains firmly shut.");
        randomSayIfPersonality(PersonalityTrait.irritable, new ArrayList<>(),
                "That really sets my teeth on edge. Grrr...");
        return false;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        print("The sorcerer admits you into the tower. He offers to train you further in the ways of sorcery, ");
        ChangeClassEvent changeClassEvent = new ChangeClassEvent(model, Classes.SOR);
        changeClassEvent.areYouInterested(model);
        println("The sorcerer also offers to sell you a spell at a discount of 5 gold.");
        List<Item> items = new ArrayList<>(List.of(model.getItemDeck().getRandomSpell()));
        ShopState shop = new ShopState(model, "Sorcerer", items,
                new int[]{Math.max(1, items.get(0).getCost() - 5)}, new boolean[]{true});
        shop.setSellingEnabled(false);
        shop.run(model);
        setCurrentTerrainSubview(model);
        showExplicitPortrait(model, portrait, "Sorcerer");
        model.getLog().waitForAnimationToFinish();
        SpellMasteries.offersToTutorSpells(model, this, "the sorcerer", MyLists.filter(Classes.SOR.getSkills(), Skill::isMagic));
        return true;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm a sorcerer. Now please, I am quite busy.";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        GameCharacter gc = new GameCharacter("Sorcerer", "", portrait.getRace(), Classes.SOR, portrait,
                Classes.NO_OTHER_CLASSES, new Equipment(new MorningStar(), new MagesRobes(), model.getItemDeck().getRandomJewelry()));
        gc.setLevel(MyRandom.randInt(3, 6));
        return gc;
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
        List<Enemy> enemies = new ArrayList<>();
        for (int i = MyRandom.randInt(3); i > 0; --i) {
            enemies.add(new ApprenticeEnemy(PortraitSubView.makeRandomPortrait(Classes.SOR), new OldWand()));
        }
        return enemies;
    }

    @Override
    protected ProvokedStrategy getProvokedStrategy() {
        return ProvokedStrategy.FIGHT_TO_DEATH;
    }
}
