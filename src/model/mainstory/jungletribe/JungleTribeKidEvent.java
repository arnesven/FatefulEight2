package model.mainstory.jungletribe;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.ChildAppearance;
import model.classes.Classes;
import model.races.Race;
import util.MyPair;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.List;
import java.util.Map;

public class JungleTribeKidEvent extends JungleTribeGeneralInteractionEvent {
    private static final List<String> GIRLS_NAMES = List.of("Emma", "Tina", "Jessica", "Roana", "Libby");
    private static final List<String> BOYS_NAMES = List.of("Johnny", "Derek", "Max", "Manny", "Viggo", "Josh");
    private final GainSupportOfJungleTribeTask task;
    private final ChildAppearance portrait;
    private final String name;
    private final String crownReply;
    private final String jaqarReply;
    private final String jequenReply;

    public JungleTribeKidEvent(Model model, GainSupportOfJungleTribeTask gainSupportOfJungleTribeTask) {
        super(model, 0);
        this.task = gainSupportOfJungleTribeTask;
        this.portrait = PortraitSubView.makeChildAppearance(Race.SOUTHERN_HUMAN, MyRandom.flipCoin());
        this.name = portrait.getGender() ? MyRandom.sample(GIRLS_NAMES)
                : MyRandom.sample(BOYS_NAMES);
        crownReply = MyRandom.sample(List.of("I've heard it's really really old.",
                "It's like a magic hat that makes you a king when you put it on.",
                "Bobby says it's super pretty. But he's lying, nobody's seen it in ages."));
        jaqarReply = MyRandom.sample(List.of("You mean Jequen?", "A jaguar? Don't see them too often in the village.",
                "Jequen told me that was his dad's name. Do you know him?"));
        jequenReply = MyRandom.sample(List.of("He's a nice man who lives in our village.", "Yeah. He's the best.",
                "He's super nice. He helps my mom sometimes.", "I've been in his hut. It's nothing special."));
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        println("The party encounters some of the children who lives in the village.");
        showExplicitPortrait(model, portrait, "Kid");
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        portraitSay(MyRandom.sample(List.of("Whaddaya want mister?", "Will you play with us?",
                "Hey, why are your clothes so weird?", "Cool weapons! Can I touch them?")));
        leaderSay("Actually, I wanted to ask a question.");
        portraitSay("Oh. Okay. What?");
        return true;
    }

    @Override
    protected Map<String, MyPair<String, String>> makeSpecificTopics(Model model) {
        return Map.of("King Jaq", new MyPair<>("Who was King Jaq, and what happened to him?",
                        "Wasn't he the guy who hid the crown in the pyramid?"),
                "Jequen", new MyPair<>("What do you know about Jequen?", jequenReply),
                "Prince Jaquar", new MyPair<>("Who was Prince Jaquar, and what happened to him?",
                        jaqarReply),
                "Jade Crown", new MyPair<>("Do you know about the Jade Crown?",
                        "Everybody knows about that! " + crownReply));
    }

    @Override
    protected String getVictimSelfTalk() {
        return "Uhm... I'm " + name + ".";
    }

    @Override
    protected String getOutsideOfKingdomNews() {
        return "News? What's that?";
    }

    @Override
    protected String getRegionReply() {
        return "This is our village. You like it?";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        return new GameCharacter("Kid", "", portrait.getRace(), Classes.None, portrait);
    }
}
