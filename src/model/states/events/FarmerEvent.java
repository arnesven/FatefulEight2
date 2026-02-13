package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.enemies.CompanionEnemy;
import model.enemies.Enemy;
import model.items.Equipment;
import model.items.weapons.*;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public abstract class FarmerEvent extends GeneralInteractionEvent {
    private AdvancedAppearance portrait;

    public FarmerEvent(Model model) {
        super(model, "Talk to", MyRandom.randInt(2, 6));
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        println("The party encounters a farmer.");
        this.portrait = PortraitSubView.makeRandomPortrait(Classes.FARMER);
        showExplicitPortrait(model, portrait, "Farmer");
        return true;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm a farmer. Just trying to work my fields and make a living.";
    }

    protected AdvancedAppearance getPortrait() {
        return portrait;
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        return new GameCharacter("Farmer", "", portrait.getRace(), Classes.FARMER,
                portrait, new Equipment(randomWeapon()));
    }

    private Weapon randomWeapon() {
        return MyRandom.sample(List.of(new WoodenSpear(), new Trident(), new Glaive(), new Warhammer(), new Broadsword()));
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
        List<Enemy> list = new ArrayList<>();
        for (int i = MyRandom.randInt(6); i > 0; --i) {
            list.add(new CompanionEnemy(PortraitSubView.makeRandomPortrait(Classes.FARMER), Classes.FARMER, randomWeapon()));
        }
        return list;
    }

    @Override
    protected ProvokedStrategy getProvokedStrategy() {
        return ProvokedStrategy.FIGHT_IF_ADVANTAGE;
    }
}
