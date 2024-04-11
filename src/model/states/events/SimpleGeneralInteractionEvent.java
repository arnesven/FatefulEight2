package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.enemies.Enemy;
import model.items.Equipment;
import model.races.Race;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public abstract class SimpleGeneralInteractionEvent extends GeneralInteractionEvent {
    private final CharacterClass charClass;
    private final Race race;
    private final String name;
    private final String introText;
    private AdvancedAppearance portrait;

    public SimpleGeneralInteractionEvent(Model model, CharacterClass cls, Race race, String name, String introText) {
        super(model, "Talk to", MyRandom.randInt(5, 30));
        this.charClass = cls;
        this.race = race;
        this.name = name;
        this.introText = introText;
    }

    public AdvancedAppearance getPortrait() {
        return portrait;
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        this.portrait = PortraitSubView.makeRandomPortrait(charClass, race);
        println(introText);
        showExplicitPortrait(model, portrait, name);
        return true;
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        GameCharacter gc = new GameCharacter(name, "", race, charClass, portrait,
                Classes.NO_OTHER_CLASSES,
                new Equipment(model.getItemDeck().getRandomWeapon(),
                        model.getItemDeck().getRandomApparel(), null));
        gc.setLevel(MyRandom.randInt(1, 4));
        return gc;
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
        return new ArrayList<>();
    }

    @Override
    protected ProvokedStrategy getProvokedStrategy() {
        return ProvokedStrategy.FIGHT_IF_ADVANTAGE;
    }
}
