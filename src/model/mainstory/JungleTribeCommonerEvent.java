package model.mainstory;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.map.locations.PyramidLocation;
import model.races.Race;
import util.MyPair;
import util.MyRandom;
import util.MyStrings;
import util.MyTriplet;
import view.subviews.PortraitSubView;

import java.util.List;
import java.util.Map;

public class JungleTribeCommonerEvent extends JungleTribeGeneralInteractionEvent {
    private final MyTriplet<String, CharacterClass, String> job;
    private final GainSupportOfJungleTribeTask task;
    private final PyramidLocation pyramid;
    private final AdvancedAppearance portrait;

    public JungleTribeCommonerEvent(Model model, GainSupportOfJungleTribeTask task) {
        super(model, MyRandom.randInt(5, 10));
        this.job = MyRandom.sample(List.of(
                new MyTriplet<>("commoner", Classes.None, "I do a little of this and a little of that."),
                new MyTriplet<>("fisherman", Classes.None, "I fish in the river."),
                new MyTriplet<>("farmer", Classes.FARMER, "I grow crops on the outskirts of the village."),
                new MyTriplet<>("warrior", Classes.AMZ, "I defend the village."),
                new MyTriplet<>("hunter", Classes.AMZ, "I hunt game in the forests around here.")));
        this.task = task;
        this.portrait = PortraitSubView.makeRandomPortrait(job.second, Race.SOUTHERN_HUMAN, MyRandom.flipCoin());
        this.pyramid = task.getPyramidClue(model, 4);
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        println("The party encounters one of the people who inhabit this village.");
        showExplicitPortrait(model, portrait, MyStrings.capitalize(job.first));
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        if (MyRandom.flipCoin()) {
            println("The " + job.first + " ignores you.");
        } else {
            portraitSay(MyRandom.sample(List.of("What do you want?", "Can I help you?",
                    "Did you want something?")));
        }
        return true;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm a " + job.first + ". " + job.third;
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        return new GameCharacter(job.first, "", Race.SOUTHERN_HUMAN, job.second,
                portrait,
                Classes.NO_OTHER_CLASSES);
    }

    @Override
    protected Map<String, MyPair<String, String>> makeSpecificTopics(Model model) {
        return Map.of("King Jaq", new MyPair<>("Who was King Jaq, and what happened to him?",
                        "I think he was the last king of the kingdom. He died a long time ago."),
                "Jequen", new MyPair<>("What do you know about Jequen?",
                        "He's a good man. He seems completely content living out his life as a commoner in this village. " +
                                "I think a king needs that kind of humility to truly understand his subjects."),
                "Prince Jaquar", new MyPair<>("Who was Prince Jaquar, and what happened to him?",
                        "Jaquar? That's Jequen's father. He went off in search of the Jade Crown about twenty years ago. " +
                                "Haven't seen him since."),
                "Jade Crown", new MyPair<>("Do you know about the Jade Crown?",
                        "The legend says King Jaq hid it in the " + pyramid.getName() + ", to prevent his " +
                                "useless son from becoming king. Too bad for Jequen though, he would be a good king."));
    }

    @Override
    protected void specificTopicHook(Model model, MyPair<String, String> queryAndResponse) {
        super.specificTopicHook(model, queryAndResponse);
        if (queryAndResponse.first.contains("Jade Crown")) {
            task.giveClueAbout(pyramid);
        }
    }
}
