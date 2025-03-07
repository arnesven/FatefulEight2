package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.special.GoblinAppearance;
import model.characters.special.GoblinCharacter;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.*;
import model.races.Race;
import model.states.CombatEvent;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.RecruitState;
import util.MyPair;

import java.util.ArrayList;
import java.util.List;

import static model.classes.Classes.None;

public class GoblinFugitiveEvent extends DailyEventState {
    private final GameCharacter goblinChar;
    private boolean didFlee = false;

    public GoblinFugitiveEvent(Model model) {
        super(model);
        goblinChar = new GoblinCharacter();
    }

    @Override
    public String getDistantDescription() {
        return "some goblins. Looks like one of them is being chased by the others";
    }

    @Override
    protected void doEvent(Model model) {
        if (model.getParty().isSpecialCharacterMarked(goblinChar)) {
            new NoEventState(model).doEvent(model);
            return;
        }
        print("You come around a bend in the road and suddenly you see half a dozen goblins rushing toward you. Do you turn and flee? (Y/N) ");
        if (yesNoInput()) {
            didFlee = true;
            return;
        }
        model.getParty().markSpecialCharacter(goblinChar);
        model.getParty().randomPartyMemberSay(model, List.of("It looks like their chasing the one in the front..."));
        showExplicitPortrait(model, goblinChar.getAppearance(), goblinChar.getName());
        portraitSay("Help, help, they're going to kill me!");
        println("The screaming goblin rushes past you and hides behind the ranks of the party. " +
                "The other goblins stop running and approach more carefully.");
        printQuote("Goblin Leader", "Hand him over. This is none of your business " + model.getParty().getLeader().getRace().getName().toLowerCase() + ".");
        print("Hand over the goblin fugitive? (Y/N) ");
        if (yesNoInput()) {
            printQuote("Goblin Leader", "Smart move. Now come here you!");
            portraitSay("No, no, noooooo!");
            println("The goblins drag the fugitive away.");
            model.getParty().randomPartyMemberSay(model, List.of("As he said, it was none of our business."));
        } else {
            MyPair<Boolean, GameCharacter> pair = model.getParty().doSoloSkillCheckWithPerformer(model, this, Skill.Persuade, 10);
            if (pair.first) {
                model.getParty().partyMemberSay(model, pair.second, List.of("You don't want to mess with us. We're not only bigger and stronger, " +
                        "but there's more of us than of you."));
                printQuote("Goblin Leader", "Fair point. Ah, hell with it. Let's head back to the tunnels boys!");
                recruit(model);
            } else {
                model.getParty().partyMemberSay(model, pair.second, List.of("Come on, let him off. It's a nice day after all."));
                printQuote("Goblin Leader", "No way.");
                doCombat(model);
                if (didFlee) {
                    return;
                }
                setCurrentTerrainSubview(model);
                if (goblinChar.isDead()) {
                    println("The goblin have been slaughtered. Unfortunately so has the goblin fugitive.");
                    model.getParty().randomPartyMemberSay(model,
                            List.of("Well, he's dead.", "Poor bugger.", "I wonder why the were after him...",
                            "Comon folks, let's move on.", "Don't have much sympathy for goblins."));
                } else {
                    recruit(model);
                }
            }
        }


    }

    private void recruit(Model model) {
        portraitSay("Thanks for helping me. Those guys really had it in for me.");
        leaderSay("Why were they chasing you?");
        portraitSay("Oh... I drank the last of the booze. We were saving it for the moon festival tomorrow, but uh, I got thirsty.");
        leaderSay("Fair enough. Well, so long Goblin.");
        portraitSay("Oh, uhm... You know, I could really do with some permanent protection. " +
                "In case my gang comes back. Do you mind if I team up with you?");
        waitForReturn();
        goblinChar.setLevel((int)Math.max(1, Math.floor(GameState.calculateAverageLevel(model))));
        RecruitState recruitState = new RecruitState(model, List.of(goblinChar));
        recruitState.run(model);
        if (model.getParty().getPartyMembers().contains(goblinChar)) {
            portraitSay("Wheeee! I promise I can be useful!");
        }
    }

    private void doCombat(Model model) {
        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new GoblinSwordsman('A'));
        enemies.add(new GoblinSwordsman('A'));
        enemies.add(new GoblinClubWielder('B'));
        enemies.add(new GoblinClubWielder('B'));
        enemies.add(new GoblinBowman('C'));
        enemies.add(new GoblinBowman('C'));

        CombatEvent combat = new CombatEvent(model, enemies);
        combat.addAllies(List.of(goblinChar));
        combat.run(model);
        didFlee = didFlee || combat.fled();
    }

    @Override
    public boolean haveFledCombat() {
        return super.haveFledCombat() || didFlee;
    }
}
