package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.CompanionEnemy;
import model.enemies.Enemy;
import model.enemies.ServantEnemy;
import model.items.Equipment;
import model.items.weapons.Longsword;
import model.items.weapons.ShortSword;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.List;

public class NoblemanEvent extends DarkDeedsEvent {
    private AdvancedAppearance portrait;
    private boolean success;

    public NoblemanEvent(Model model) {
        super(model, "Talk to", MyRandom.randInt(10, 100));
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        this.portrait = PortraitSubView.makeRandomPortrait(Classes.NOB);
        println("You meet with a nobleman and " + hisOrHer(portrait.getGender()) + " entourage.");
        showExplicitPortrait(model, portrait, "Noble");
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        this.success = model.getParty().doSoloSkillCheck(model, this, Skill.Entertain, 8);
        if (success) {
            println("After telling the great story of the party's exploits, the nobleman gladly gives you a small stipend.");
            int amount = Math.min(10 * model.getParty().size(), 50);
            model.getParty().addToGold(amount);
            println("The party receives " + amount + " gold from the nobleman.");
            model.getParty().partyMemberSay(model, MyRandom.sample(model.getParty().getPartyMembers()),
                    List.of("Much obliged.", "This extra coin will come in handy.", "It's for a worthy cause.",
                            "A good deed sir.", "Perhaps we can pay you back one day...",
                            "How very generous of you!3"));
            ChangeClassEvent change = new ChangeClassEvent(model, Classes.NOB);
            print("The nobleman also offers to inspire you to take on the high life, ");
            change.areYouInterested(model);
        } else {
            println("You try to explain why supporting the party would be a good deed, but the noble seems unimpressed " +
                    "by your exploits.");
        }
        setCurrentTerrainSubview(model);
        showExplicitPortrait(model, portrait, "Noble");
        return true;
    }

    @Override
    protected void doEnding(Model model) {
        if (success) {
            println("You part ways with the nobleman.");
            model.getParty().partyMemberSay(model, MyRandom.sample(model.getParty().getPartyMembers()),
                    List.of("What a sucker...", "Such a nice person.", "We should hang out with noblemen more often."));
        } else {
            println("You part ways with the nobleman.");
        }
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        GameCharacter chara = new GameCharacter("Noble", "", portrait.getRace(), Classes.NOB, portrait,
                Classes.NO_OTHER_CLASSES,
                new Equipment(new Longsword()));
        chara.setLevel(MyRandom.randInt(1, 4));
        return chara;
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
        List<Enemy> entourage = makeBodyGuards(MyRandom.randInt(0, 3), 'B');
        for (int i = MyRandom.randInt(2, 4); i > 0; --i) {
            entourage.add(0, new CompanionEnemy(PortraitSubView.makeRandomPortrait(Classes.NOB),
                    Classes.NOB, new ShortSword()));
        }
        for (int i = MyRandom.randInt(2, 4); i > 0; --i) {
            entourage.add(new ServantEnemy(PortraitSubView.makeRandomPortrait(Classes.None)));
        }
        return entourage;
    }

    @Override
    protected ProvokedStrategy getProvokedStrategy() {
        return ProvokedStrategy.FIGHT_IF_ADVANTAGE;
    }


}
