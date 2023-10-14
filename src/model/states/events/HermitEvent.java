package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.enemies.HermitEnemy;
import model.races.Race;
import model.states.DailyEventState;
import util.MyRandom;
import view.subviews.MountainCombatTheme;
import view.subviews.PortraitSubView;

import java.util.List;

public class HermitEvent extends DailyEventState {
    public HermitEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        CharacterAppearance portrait = PortraitSubView.makeOldPortrait(Classes.None, Race.randomRace(), false);
        showExplicitPortrait(model, portrait, "Hermit");
        println("The party encounters an old man. He has a long beard, and his clothes are nothing " +
                "but shreds. He must have lived out here in the wilderness " +
                "for a long, long time. He seems tight lipped about his life " +
                "but you just know that he must have some good stories.");

        leaderSay("Hello there friend. Care to share our camp for the evening?");
        println("You sit down at your campfire and ask the hermit a few gentle questions.");
        int roll = MyRandom.rollD10();
        if (roll == 1) {
            println("The hermit is enraged and attacks you!");
            runCombat(List.of(new HermitEnemy('A')));
        } else if (roll <= 3 || model.getParty().getFood() == 0) {
            println("The hermit seems annoyed by your attempts to converse with him. He just wanders off.");
            model.getParty().randomPartyMemberSay(model, List.of("Goodbye...?", "...", "Guess he didn't want to."));
        } else if (roll <= 9) {
            model.getParty().addToFood(-1);
            println("The Hermit just nods and grunts while he consumes one of your rations.");
        } else {
            model.getParty().addToFood(-1);
            println("After consuming one of your rations, the Hermit tells you a fantastic story. Each character gains 15 experience.");
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                model.getParty().giveXP(model, gc, 15);
            }
        }
    }
}
