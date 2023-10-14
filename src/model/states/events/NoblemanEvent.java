package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.CompanionEnemy;
import model.enemies.Enemy;
import model.enemies.ServantEnemy;
import model.items.Equipment;
import model.items.ItemDeck;
import model.items.weapons.Katana;
import model.items.weapons.Longsword;
import model.items.weapons.ShortSword;
import model.states.DailyEventState;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.List;

public class NoblemanEvent extends DarkDeedsEvent {
    public NoblemanEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("You meet with a nobleman and their " +
                "entourage.");
        CharacterAppearance app = PortraitSubView.makeRandomPortrait(Classes.NOB);
        showExplicitPortrait(model, app, "Noble");
        if (darkDeedsMenu("noble", makeNobleCharacter(app), MyRandom.randInt(10, 100),
                makeCompanions(), ProvokedStrategy.FIGHT_IF_ADVANTAGE)) {
            return;
        }
        boolean success = model.getParty().doSoloSkillCheck(model, this, Skill.Entertain, 8);
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
            println("You part ways with the nobleman.");
            model.getParty().partyMemberSay(model, MyRandom.sample(model.getParty().getPartyMembers()),
                    List.of("What a sucker...", "Such a nice person.", "We should hang out with noblemen more often."));
        } else {
            println("You try to explain why supporting the party would be a good deed, but the noble seems unimpressed " +
                    "by your exploits.");
            println("You part ways with the nobleman.");
        }
    }

    private List<Enemy> makeCompanions() {
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

    private GameCharacter makeNobleCharacter(CharacterAppearance app) {
        GameCharacter chara = new GameCharacter("Noble", "", app.getRace(), Classes.NOB, app,
                Classes.NO_OTHER_CLASSES,
                new Equipment(new Longsword()));
        chara.setLevel(MyRandom.randInt(1, 4));
        return chara;
    }
}
