package model.states.events;

import model.Model;
import model.Party;
import model.RecruitInfo;
import model.RecruitableCharacter;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.enemies.Enemy;
import model.enemies.FormerPartyMemberEnemy;
import model.enemies.ServantEnemy;
import model.items.Equipment;
import model.items.accessories.GreatHelm;
import model.items.accessories.LeatherBoots;
import model.items.accessories.LeatherGloves;
import model.items.clothing.LeatherArmor;
import model.items.clothing.LeatherTunic;
import model.items.clothing.PlateMailArmor;
import model.items.weapons.BastardSword;
import model.items.weapons.Spear;
import model.items.weapons.Warhammer;
import model.states.DailyEventState;
import model.states.RecruitState;
import view.subviews.PortraitSubView;

import java.util.List;

public class WhiteKnightEvent extends GeneralInteractionEvent {
    private GameCharacter pala;

    public WhiteKnightEvent(Model model) {
        super(model, "Talk to", 20, true);
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        AdvancedAppearance app = PortraitSubView.makeRandomPortrait(Classes.PAL);
        pala = new GameCharacter("Paladin", "", app.getRace(), Classes.PAL, app,
                new Equipment(new BastardSword(), new PlateMailArmor(), new GreatHelm()));
        showExplicitPortrait(model, pala.getAppearance(), "Paladin");
        showEventCard("You meet a white knight, a paladin.");
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        portraitSay("We are the Paladins, the protectors of our sacred " +
                "temple!");
        println("The paladin seems almost comically serious as he " +
                "says this with a solemn face. The party members dare not " +
                "make fun of him however. With a little encouragement " +
                "the Paladin tells all about the order and his martial skills " +
                "and duties");

        if (pala != null) {
            pala.setLevel((int)Math.ceil(calculateAverageLevel(model)));
            println("The paladin offers to join your party.");
            RecruitState recruit = new RecruitState(model, RecruitableCharacter.makeOneRecruitable(pala, RecruitInfo.none));
            recruit.run(model);
        }
        println("The paladin offers to instruct you in the way of his order, ");
        ChangeClassEvent change = new ChangeClassEvent(model, Classes.PAL);
        change.areYouInterested(model);
        return true;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm a paladin. I belong to the order that protects the Temple.";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        return pala;
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
        AdvancedAppearance squireApp = PortraitSubView.makeRandomPortrait(Classes.CAP);
        Enemy squire = new FormerPartyMemberEnemy(new GameCharacter("Squire", "", squireApp.getRace(), Classes.CAP,
                squireApp, new Equipment(new Spear(), new LeatherArmor(), new LeatherBoots())));
        AdvancedAppearance armorerApp = PortraitSubView.makeRandomPortrait(Classes.ART);
        Enemy armorer = new FormerPartyMemberEnemy(new GameCharacter("Armorer", "", armorerApp.getRace(), Classes.ART,
                armorerApp, new Equipment(new Warhammer(), new LeatherTunic(), new LeatherGloves())));
        Enemy servant = new ServantEnemy(PortraitSubView.makeRandomPortrait(Classes.None));
        return List.of(squire, armorer, servant);
    }

    @Override
    protected ProvokedStrategy getProvokedStrategy() {
        return ProvokedStrategy.FIGHT_TO_DEATH;
    }
}
