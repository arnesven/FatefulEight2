package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.Enemy;
import model.items.Equipment;
import model.items.clothing.MesmersRobes;
import model.items.weapons.PineWand;
import model.items.weapons.YewWand;
import model.races.Race;
import model.states.DailyEventState;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class WizardsAbodeEvent extends MagicExpertDarkDeedsEvent {
    private boolean admitted;
    private AdvancedAppearance portrait;

    public WizardsAbodeEvent(Model model) {
        super(model, "Talk to", MyRandom.randInt(10, 30));
        admitted = false;
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        this.portrait = PortraitSubView.makeRandomPortrait(Classes.WIZ, Race.randomRace(), false);
        showExplicitPortrait(model, portrait, "Wizard");
        println("A white stone structure pokes out from the surrounding " +
                "hills. A wizard lives here and gladly accepts some " +
                "company.");
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        println("He seems a jovial fellow and presents the party " +
                "with an enticing riddle. No visitor has been able to solve " +
                "it for many years and he offers a beautiful golden trinket " +
                "if the party can solve the riddle.");
        randomSayIfPersonality(PersonalityTrait.playful, new ArrayList<>(), "I just love riddles!3");
        boolean result = model.getParty().doCollaborativeSkillCheck(model, this, Skill.Logic, 8);
        if (result) {
            admitted = true;
            println("The wizard admits the party into his home and hands over the trinket (the party receives 10 gold).");
            model.getParty().addToGold(10);
            ChangeClassEvent change = new ChangeClassEvent(model, Classes.WIZ);
            println("After a scrumptious supper, the wizard offers to teach you about his trade, ");
            change.areYouInterested(model);
            setCurrentTerrainSubview(model);
            showExplicitPortrait(model, portrait, "Wizard");
            return true;
        }
        model.getParty().randomPartyMemberSay(model, List.of("Agh, I don't get it. Let's just give up.#"));
        println("The wizard is annoyed and refuses to accept you into his abode.");
        return false;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm a wizard. I love all things magical.";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        GameCharacter gc = new GameCharacter("Wizard", "", portrait.getRace(), Classes.WIZ, portrait,
                Classes.NO_OTHER_CLASSES, new Equipment(new PineWand(), new MesmersRobes(), model.getItemDeck().getRandomJewelry()));
        gc.setLevel(MyRandom.randInt(3, 6));
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

    @Override
    protected boolean isFreeRations() {
        return admitted;
    }
}
