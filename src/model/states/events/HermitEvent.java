package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.enemies.Enemy;
import model.enemies.HermitEnemy;
import model.races.Race;
import util.MyLists;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class HermitEvent extends GeneralInteractionEvent {
    private CharacterAppearance portrait;

    public HermitEvent(Model model) {
        super(model, "Talk to", MyRandom.randInt(3, 6));
    }

    @Override
    public String getDistantDescription() {
        return "a small hut";
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Seek out hermit", "I've heard that an old Hermit lives nearby");
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        this.portrait = PortraitSubView.makeOldPortrait(Classes.None, Race.randomRace(), false);
        showExplicitPortrait(model, portrait, "Hermit");
        println("The party encounters an old man. He has a long beard, and his clothes are nothing " +
                "but shreds. He must have lived out here in the wilderness " +
                "for a long, long time. He seems tight lipped about his life " +
                "but you just know that he must have some good stories.");
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        randomSayIfPersonality(PersonalityTrait.friendly, new ArrayList<>(), "Hello there friend!");
        leaderSay("Care to share our camp for the evening?");
        println("You sit down at your campfire and ask the hermit a few gentle questions.");
        int roll = MyRandom.rollD10();
        if (roll == 1) {
            println("The hermit is enraged and attacks you!");
            runCombat(List.of(new HermitEnemy('A')));
            return false;
        }
        if (roll <= 3 || model.getParty().getFood() == 0) {
            println("The hermit seems annoyed by your attempts to converse with him. He just wanders off.");
            model.getParty().randomPartyMemberSay(model, List.of("Goodbye...?", "...", "Guess he didn't want to."));
            return false;
        }

        model.getParty().addToFood(-1);
        if (roll <= 8) {
            println("The Hermit just nods and grunts while he consumes one of your rations.");
        } else {
            println("After consuming one of your rations, the Hermit tells you a fantastic story. Each character gains 15 experience.");
            MyLists.forEach(model.getParty().getPartyMembers(), (GameCharacter gc) ->
                model.getParty().giveXP(model, gc, 15));
        }
        return true;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "Me? I'm... just me.";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        return new GameCharacter("Hermit", "", portrait.getRace(), Classes.HERMIT, portrait,
                Classes.NO_OTHER_CLASSES);
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
        return new ArrayList<>();
    }

    @Override
    protected ProvokedStrategy getProvokedStrategy() {
        return ProvokedStrategy.FIGHT_TO_DEATH;
    }
}
