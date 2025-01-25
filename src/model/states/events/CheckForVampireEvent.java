package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.conditions.VampirismCondition;
import model.enemies.Enemy;
import model.enemies.FormerPartyMemberEnemy;
import model.enemies.VampireAttackBehavior;
import model.log.GameLog;
import model.states.DailyEventState;
import util.MyLists;
import util.MyRandom;
import view.LogView;

import java.util.ArrayList;
import java.util.List;

public class CheckForVampireEvent extends DailyEventState {
    private static final int PERSUADE_BASE_DIFFICULTY = 5;
    private static final int ATTITUDE_THRESHOLD = 5;

    public CheckForVampireEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        List<GameCharacter> vampires = MyLists.filter(model.getParty().getPartyMembers(),
                CheckForVampireEvent::isVampire);
        if (vampires.isEmpty() || vampires.size() == model.getParty().size()) {
            return;
        }
        GameCharacter vampire = vampires.get(0);
        GameCharacter other = model.getParty().getRandomPartyMember(vampire);
        partyMemberSay(other, "Hey " + vampire.getFirstName() + ", can I ask you something?");
        partyMemberSay(vampire, "Sure, what is it?");
        partyMemberSay(other, "Well... It's a little hard to ask...");
        partyMemberSay(vampire, "Oh, just spit it out!");
        partyMemberSay(other, "Fair enough. Here it goes. Are you a vampire?");
        partyMemberSay(vampire, "A vampire? No, hehehe, goodness. Whatever gave you that idea?");
        VampirismCondition vampCond = (VampirismCondition) vampire.getCondition(VampirismCondition.class);
        SkillCheckResult result = vampire.testSkill(model, Skill.Persuade, PERSUADE_BASE_DIFFICULTY + vampCond.getStage() * 3);
        println(vampire.getFirstName() + " performs a Persuade " + result.asString() + ".");
        if (result.isSuccessful()) {
            persuadeSuccess(model, vampire, other);
        } else {
            persuadeFailed(model, vampire, other, vampCond);
        }
    }

    private void persuadeFailed(Model model, GameCharacter vampire, GameCharacter other, VampirismCondition vampCond) {
        model.setInCombat(true);
        partyMemberSay(other, "Well, you're skin looks paler than before, your teeth are pointier and " +
                "on some nights you just disappear for hours. Please tell me there is a good explanations for all of this!");
        partyMemberSay(vampire, "Uhm, well... Fine I'm a vampire. Are you happy?");
        println(other.getFirstName() + " is visibly shaken by the news.");
        if (askForMesmerize(model, vampire, other, vampCond)) {
            return;
        }

        if (model.getParty().size() > 2) {
            partyMemberSay(other, "Yikes... Hey guys... I think you better know that " +
                    vampire.getFirstName() + " just confessed to being a vampire.");
        } else {
            partyMemberSay(other, "Yikes... A vampire? Really. I mean, I had my suspicions, " +
                    "but deep down I really just thought I was imagining things!");
        }
        dealWithVampire(model, vampire, other);
        model.setInCombat(false);
    }

    protected boolean askForMesmerize(Model model, GameCharacter vampire, GameCharacter other, VampirismCondition vampCond) {
        if (vampCond.hasMesmerizeAbility()) {
            print("Do you want to let " + vampire.getFirstName() + " use " +
                    hisOrHer(vampire.getGender()) + " Mesmerize ability on " + other.getFirstName() + "? (Y/N) ");
            if (yesNoInput()) {
                if (!tryMesmerize(model, vampire, other, vampCond)) {
                    dealWithVampire(model, vampire, other);
                }
                model.setInCombat(false);
                return true;
            }
        }
        return false;
    }

    private boolean tryMesmerize(Model model, GameCharacter vampire, GameCharacter other, VampirismCondition vampCond) {
        partyMemberSay(other, "Yikes... Hey...");
        println(vampire.getFirstName() + " stares with an intense and captivating gaze into " +
                other.getFirstName() + "'s eyes.");
        if (MyRandom.rollD10() < vampire.getLevel() + vampCond.getStage()) {
            println(other.getFirstName() + "'s eyelids drop slightly, and " + heOrShe(other.getGender()) + " seems frozen in place.");
            partyMemberSay(other, "I... I... I forgot what I was going to say...");
            partyMemberSay(vampire, "Don't worry about it " + other.getFirstName() + ", it was probably not important.");
            partyMemberSay(other, "Uh... yeah. I feel kind of weird. I think I'll just sit down for a minute.");
            return true;
        }
        println(other.getFirstName() + " pushes " + vampire.getName() + " away!");
        other.addToAttitude(vampire, -30);
        partyMemberSay(other, "Help! " + vampire.getFirstName() + " Is a vampire and " + heOrShe(vampire.getGender()) +
                " is trying to hypnotize me!");
        return false;
    }

    protected void dealWithVampire(Model model, GameCharacter vampire, GameCharacter other) {
        if (model.getParty().getLeader() == vampire) {
            vampireIsLeader(model, vampire, other);
        } else {
            vampireNotLeader(model, vampire, other);
        }
    }

    private void vampireNotLeader(Model model, GameCharacter vampire, GameCharacter other) {
        if (vampire.getAttitude(other) >= ATTITUDE_THRESHOLD) {
            partyMemberSay(other, "I think we both know we can't continue being " +
                    "in the same party. I think you should leave.");
            partyMemberSay(vampire, "Fine. I'll find my own way I guess. Perhaps we will meet " +
                    "sometime again? Perhaps during the night?");
            partyMemberSay(other, "Don't even think about it. Be happy we don't end you right here " +
                    "and now. Now begone foul creature.");
            partyMemberSay(vampire, "I'm gone. But I shall not forget this.");
            model.getParty().remove(vampire, false, false, 0);
            println(vampire.getFirstName() + " has left the party.");
        } else {
            partyMemberSay(other, "I'm sorry " + vampire.getFirstName() +
                    ". But It's our duty to hold you accountable for your crimes against the innocent.");
            partyMemberSay(vampire, "You can try.");
            model.getParty().remove(vampire, false, false, 0);
            println(vampire.getFirstName() + " has left the party.");
            println("You attack " + vampire.getFirstName() + "!");
            Enemy enm = new FormerPartyMemberEnemy(vampire);
            enm.setAttackBehavior(new VampireAttackBehavior());
            runCombat(List.of(enm));
        }
    }

    private void vampireIsLeader(Model model, GameCharacter vampire, GameCharacter other) {
        if (other.getAttitude(vampire) >= ATTITUDE_THRESHOLD) {
            String imOrWere = (model.getParty().size() > 2 ? "We're" : "I'm");
            String iveOrWeve = (model.getParty().size() > 2 ? "We've" : "I've");
            partyMemberSay(other, "I can't believe " + iveOrWeve + " been travelling around with a blood sucking monster!" +
                    imOrWere + " leaving, right now!#");
            partyMemberSay(vampire, "Fine. Who needs you?");
            for (GameCharacter gc : new ArrayList<>(model.getParty().getPartyMembers())) {
                if (!isVampire(gc)) {
                    model.getParty().remove(gc, false, false, 0);
                    println(gc.getFirstName() + " left the party.");
                } else if (gc != vampire) {
                    partyMemberSay(gc, "Actually, I think I'll stick with you " + vampire.getFirstName() + ".");
                }
            }
        } else {
            partyMemberSay(other, "I'm sorry " + vampire.getFirstName() +
                    ". But It's our duty to hold you accountable for your crimes against the innocent.");
            partyMemberSay(vampire, "You can try.");
            List<Enemy> enemies = new ArrayList<>();
            for (GameCharacter gc : new ArrayList<>(model.getParty().getPartyMembers())) {
                if (!isVampire(gc)) {
                    model.getParty().remove(gc, false, false, 0);
                    println(gc.getFirstName() + " left the party.");
                    enemies.add(new FormerPartyMemberEnemy(gc));
                } else if (gc != vampire) {
                    partyMemberSay(gc, "Actually, I think I'll stick with you " + vampire.getFirstName() + ".");
                }
            }
            if (!enemies.isEmpty()) {
                println("Your former party members attack you!");
                runCombat(enemies);
            }
        }

    }

    public static boolean isVampire(GameCharacter gc) {
        return gc.hasCondition(VampirismCondition.class);
    }

    private void persuadeSuccess(Model model, GameCharacter vampire, GameCharacter other) {
        partyMemberSay(other, "Yeah, I guess I'm being silly. I just thought your teeth had " +
                "gotten a little pointier or something. Sorry for cornering you like that.");
        partyMemberSay(vampire, "It's okay " + other.getFirstName() + ", I didn't take any offense. " +
                "Who wouldn't want to be a vampire. They're really cool right?.");
        partyMemberSay(other, "If by 'cool' you mean super scary, gross and an abomination which " +
                "must be rooted out at all costs, then yes.");
        partyMemberSay(vampire, "That's not exactly what I meant.");
        partyMemberSay(other, "Oh... Anyway. I'm glad your not one. If you were I don't think we could " +
                "continue being in a party together anymore.");
        partyMemberSay(vampire, "Perhaps you are right.");
    }
}
