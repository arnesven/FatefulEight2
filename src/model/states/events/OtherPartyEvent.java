package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.states.DailyEventState;
import util.MyRandom;
import view.subviews.OtherPartySubView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OtherPartyEvent extends DailyEventState {
    private ArrayList<GameCharacter> otherPartyMembers;
    private GameCharacter leader;
    private HashMap<GameCharacter, Integer> attitudeMap;

    public OtherPartyEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("You come upon a small campsite, this one seems to be in use by a group of people.");
        model.getParty().randomPartyMemberSay(model, List.of("Looks like a company of adventurers."));
        model.getParty().randomPartyMemberSay(model, List.of("The competition?"));
        leaderSay("Maybe. Or maybe not. Let's proceed with care.");

        OtherPartySubView subView = makeOtherPartySubView(model);
        model.setSubView(subView);
        print("You can trade with the other party, offer them something, attack them, talk to them, or leave.");
        do {
            waitForReturn();
            if (subView.getTopIndex() == 3) {
                break;
            }
            if (subView.getTopIndex() == 2) {
                println("Offer");
            } else if (subView.getTopIndex() == 1) {
                println("Trade");
            } else if (subView.getTopIndex() == 0) {
                println("Attack");
            } else {
                println("Talk to " + subView.getSelectedCharacter().getName());
            }
        } while (true);
        println("You leve the camp site.");
        //int chosen = multipleOptionArrowMenu(model, 30, 30, List.of("Approach leader"));
    }

    private OtherPartySubView makeOtherPartySubView(Model model) {
        this.otherPartyMembers = new ArrayList<>();
        int level = MyRandom.randInt(2, 5);
        int baseAttitude = MyRandom.randInt(-35, Math.min(20, model.getParty().getReputation()*5));
        int maxLeadership = 0;
        this.leader = null;
        this.attitudeMap = new HashMap<>();
        for (int i = MyRandom.randInt(3, 8); i > 0; --i) {
            GameCharacter rando;
            while (true) {
                rando = MyRandom.sample(model.getAllCharacters());
                if (!otherPartyMembers.contains(rando)) {
                    otherPartyMembers.add(rando);
                    break;
                }
            }
            rando.setRandomStartingClass();
            rando.setLevel(MyRandom.randInt(level-1, level+2));
            attitudeMap.put(rando, MyRandom.randInt(baseAttitude-5, baseAttitude+5));
            if (rando.getRankForSkill(Skill.Leadership) > maxLeadership) {
                maxLeadership = rando.getRankForSkill(Skill.Leadership);
                leader = rando;
            }
        }
        return new OtherPartySubView(otherPartyMembers, leader, attitudeMap);
    }
}
