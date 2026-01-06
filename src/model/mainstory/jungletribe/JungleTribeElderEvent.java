package model.mainstory.jungletribe;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.map.locations.PyramidLocation;
import model.races.Race;
import util.MyPair;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.List;
import java.util.Map;

public class JungleTribeElderEvent extends JungleTribeGeneralInteractionEvent {
    private final GainSupportOfJungleTribeTask task;
    private final CharacterAppearance portrait;
    private final PyramidLocation pyramid;

    public JungleTribeElderEvent(Model model, GainSupportOfJungleTribeTask task) {
        super(model, MyRandom.randInt(5, 10));
        this.task = task;
        this.portrait = PortraitSubView.makeOldPortrait(Classes.ELDER, Race.SOUTHERN_HUMAN, MyRandom.flipCoin());
        this.pyramid = task.getPyramidClue(model,6);
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        println("The party encounters one of the elders of the village.");
        showExplicitPortrait(model, portrait, "Village Elder");
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        portraitSay(MyRandom.sample(List.of("The outsider" + (model.getParty().size() > 1 ? "s":"") + ". Can I help you?",
                "Did you want something?")));
        return true;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I am an elder of the village. I tend to the sick, help raise the young, and act as a guide for society.";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        return new GameCharacter("Elder", "", portrait.getRace(), Classes.ELDER, portrait, Classes.NO_OTHER_CLASSES);
    }


    @Override
    protected Map<String, MyPair<String, String>> makeSpecificTopics(Model model) {
        return Map.of("King Jaq", new MyPair<>("Who was King Jaq, and what happened to him?",
                        "King Jaq was the ruler of this Kingdom about thirty years ago. But when it was time for " +
                                "Jaq to pass the Jade Crown on to his son Jaquar, he hid the crown in the " +
                                pyramid.getName() + " instead, thus preventing Jaquar from becoming king."),
                "Jequen", new MyPair<>("What do you know about Jequen?", "Young Jequen? He's a good kid. " +
                        "He's never been mad about the Jade Crown, like his father was."),
                "Prince Jaquar", new MyPair<>("Who was Prince Jaquar, and what happened to him?",
                        "Prince Jaquar was king Jaq's only son. He was mostly interested in eating fancy foods and " +
                                "chasing girls when he was young. He left to find the Jade Crown and never came back, " +
                                "leaving his young son Jequen behind."),
                "Jade Crown", new MyPair<>("Do you know about the Jade Crown?",
                        "The Jade Crown is a symbol of the monarchy in this kingdom. It was worn by all the ancient kings " +
                            "and queens. Because of King Jaq, it may never be found again."));
    }

    @Override
    protected void specificTopicHook(Model model, MyPair<String, String> queryAndResponse) {
        super.specificTopicHook(model, queryAndResponse);
        if (queryAndResponse.first.contains("King Jaq")) {
            task.giveClueAbout(pyramid);
        }
    }
}
