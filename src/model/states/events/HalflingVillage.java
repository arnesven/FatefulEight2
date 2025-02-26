package model.states.events;

import model.Model;
import model.Party;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.Classes;
import model.races.Race;
import model.states.DailyEventState;
import model.states.EveningState;
import util.MyLists;

import java.util.ArrayList;
import java.util.List;

public class HalflingVillage extends DailyEventState {
    private boolean free = false;

    public HalflingVillage(Model model) {
        super(model);
    }

    @Override
    protected boolean isFreeRations() {
        return free;
    }

    @Override
    public String getDistantDescription() {
        return "a little village";
    }

    @Override
    protected void doEvent(Model model) {
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc.getRace().id() == Race.HALFLING.id()) {
                foundVillage(model, gc);
                return;
            }
        }
        print("The party stumbles upon a little miniature village. ");
        println("The halflings quickly race for their dwellings and promptly shut their doors and windows.");
        showRandomPortrait(model, Classes.None, Race.HALFLING, "Halfling Woman");
        portraitSay("Go away, we don't want any big-people trouble here!");
        model.getParty().randomPartyMemberSay(model, List.of("How rude."));
        randomSayIfPersonality(PersonalityTrait.naive, new ArrayList<>(),
                "Hello! Please come out! You can trust us, we're good people!");
    }

    private void foundVillage(Model model, GameCharacter halflingCharacter) {
        free = true;
        print("The party stumbles upon a little miniature village. ");
        if (!allHalflings(model.getParty())) {
            println("The halflings quickly race for their dwellings.");
            model.getParty().partyMemberSay(model, halflingCharacter, List.of("It's alright, these people are with me!"));
        } else {
            println("The halflings welcome you as if you were related to them.");
        }
        EveningState.buyRations(model, this);
        new HalflingEvent(model).doEvent(model);
    }

    private boolean allHalflings(Party party) {
        return MyLists.all(party.getPartyMembers(),
                (GameCharacter gc) -> gc.getRace().id() == Race.HALFLING.id());
    }
}
