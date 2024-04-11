package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.enemies.CompanionEnemy;
import model.enemies.Enemy;
import model.items.Equipment;
import model.items.clothing.FurArmor;
import model.items.weapons.Spear;
import model.states.DailyEventState;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class AmazonEvent extends CombatExpertDarkDeedsEvent {
    private AdvancedAppearance portrait;

    public AmazonEvent(Model model) {
        super(model, "Talk to", MyRandom.randInt(5, 15));
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        this.portrait = PortraitSubView.makeRandomPortrait(Classes.AMZ);
        println("A large, handsome and scantily clad person stands " +
                "before the party. From the way that they are talking you " +
                "understand that this is a native warrior of great skill.");
        showExplicitPortrait(model, portrait, "Amazon");
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        ChangeClassEvent change = new ChangeClassEvent(model, Classes.AMZ);
        print("The native warrior offers to instruct you in the ways of being an Amazon, ");
        change.areYouInterested(model);
        setCurrentTerrainSubview(model);
        showExplicitPortrait(model, portrait, "Amazon");
        return true;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm a native warrior of these lands.";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        GameCharacter gc = new GameCharacter("Amazon", "", portrait.getRace(), Classes.AMZ, portrait,
                Classes.NO_OTHER_CLASSES, new Equipment(new Spear(), new FurArmor(), model.getItemDeck().getRandomShoes()));
        gc.setLevel(MyRandom.randInt(2, 6));
        return gc;
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
        List<Enemy> list = new ArrayList<>();
        for (int i = MyRandom.randInt(3); i > 0; --i) {
            list.add(new CompanionEnemy(PortraitSubView.makeRandomPortrait(Classes.AMZ), Classes.AMZ,
                    model.getItemDeck().getRandomWeapon()));
        }
        return list;
    }

    @Override
    protected ProvokedStrategy getProvokedStrategy() {
        return ProvokedStrategy.FIGHT_TO_DEATH;
    }
}
