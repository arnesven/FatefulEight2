package model.mainstory.honorable;

import model.Model;
import model.characters.appearance.AdvancedAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.classes.normal.ArtisanClass;
import model.races.Race;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.PickSamuraiSwordState;
import model.states.events.ArtisanEvent;
import view.subviews.PortraitSubView;
import view.subviews.SubView;

public class EasternSmithEvent extends DailyEventState {
    public EasternSmithEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("You approach the little hut in the mountains. It appears to be a smithy. " +
                "Outside is a muscular woman hard at work smithing a sword.");
        CharacterClass charClass = new ArtisanEvent.Smith().makeArtisanSubClass();
        AdvancedAppearance app = PortraitSubView.makeRandomPortrait(charClass, Race.EASTERN_HUMAN, true);
        showExplicitPortrait(model, app, "Smith");
        portraitSay("A westerner? You're a long way from home.");
        leaderSay("And you're a long way from your customers? Why is your smithy all the way out here?");
        portraitSay("I need coal from the hills to stoke my furnace. I use pristine water from the spring " +
                "to cool the steel. The mountain air, the peace and solitude lets me focus. Because of all this, " +
                "my blades are the finest in the world.");
        leaderSay("Not very modest, are you?");
        portraitSay("Why be modest, when you know you're the best?");
        leaderSay("Uh-huh. Do you have anything for sale?");
        portraitSay("Of course, but my blades are not cheap, and I'll have no haggling. If you don't like my prices " +
                "I urge you to take your business elsewhere.");
        leaderSay("I understand. Where do you keep your stock?");
        portraitSay("Come this way.");
        println("The smith leads you inside the little cottage. Many blades hang on the walls.");
        leaderSay("Impressive. That's quite a few swords.");
        portraitSay("That's just half of them, there's more in the next room.");
        model.getLog().waitForAnimationToFinish();
        SubView subView = model.getSubView();
        PickSamuraiSwordState pickSwordState = new PickSamuraiSwordState(model);
        pickSwordState.doTheEvent(model);
        model.setSubView(subView);
        portraitSay("No refunds!");
        leaderSay("Fine, " + iOrWe() + " understand.");
    }
}
