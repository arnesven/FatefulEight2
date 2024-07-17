package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.combat.conditions.VampirismCondition;
import view.MyColors;
import view.combat.CombatTheme;
import view.sprites.AvatarSprite;
import view.sprites.Sprite;

import java.util.List;

public class CureVampirismRitualEvent extends RitualEvent {
    private final GameCharacter vampire;
    private final CombatTheme theme;
    private final CharacterAppearance mainDruid;
    private boolean wasCured = false;

    public CureVampirismRitualEvent(Model model, GameCharacter vampire, CharacterAppearance mainDruid) {
        super(model, MyColors.GREEN);
        this.vampire = vampire;
        this.theme = model.getCurrentHex().getCombatTheme();
        this.mainDruid = mainDruid;
    }

    @Override
    protected List<GameCharacter> getRitualTargetedPartyMembers() {
        return List.of(vampire);
    }

    @Override
    protected CombatTheme getTheme() {
        return theme;
    }

    @Override
    protected boolean runEventIntro(Model model, List<GameCharacter> ritualists) {
        ritualists.clear();
        for (int i = 5; i > 0; --i) {
            GameCharacter gc = makeRandomCharacter();
            gc.setClass(Classes.DRU);
            gc.addToHP(999);
            ritualists.add(gc);
        }
        ritualists.get(0).setAppearance(mainDruid);
        return true;
    }

    @Override
    protected void runEventOutro(Model model, boolean success, int power) {
        showExplicitPortrait(model, mainDruid, "Druid");
        if (success) {
            vampire.removeCondition(VampirismCondition.class);
            println(vampire.getName() + " was cured of vampirism!");
            portraitSay("There... you will feel weak for some time, but the affliction has been " +
                    "purged from your body. Welcome back to the world of the living friend.");
            partyMemberSay(vampire, "Thank you. I feel... alive. But yeah, weak.");
            vampire.addToSP(-vampire.getSP());
            if (vampire.getHP() > 4) {
                vampire.addToHP(-4);
                println(vampire.getFirstName() + " lost 4 HP and all SP.");
            }
            if (vampire != model.getParty().getLeader()) {
                leaderSay("Good to have that sorted out. I don't know what we would have done if that ritual hadn't worked.");
            }
            portraitSay("Glad we could be of service. Best of luck to you.");
            leaderSay("Goodbye.");
            wasCured = true;
        } else {
            println(vampire.getName() + " is still a vampire.");
            portraitSay("I'm sorry... the ritual has failed.");
            partyMemberSay(vampire, "Can we try it again?");
            portraitSay("Yes, but not today. We are all exhausted from the ritual.");
            partyMemberSay(vampire, "I understand.");
        }
    }

    @Override
    public Sprite getCenterSprite() {
        AvatarSprite avatar = vampire.getAvatarSprite();
        avatar.synch();
        return avatar;
    }

    public boolean wasCured() {
        return wasCured;
    }
}
