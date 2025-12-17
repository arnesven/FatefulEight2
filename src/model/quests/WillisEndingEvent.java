package model.quests;

import model.Model;
import model.RecruitInfo;
import model.RecruitableCharacter;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.RecruitState;

import java.util.List;

public class WillisEndingEvent extends DailyEventState {
    private static final String RECRUIT_FLAG = "WILLIS_RECRUITED";
    private final CharacterAppearance portrait;

    public WillisEndingEvent(Model model, CharacterAppearance willisPortrait) {
        super(model);
        this.portrait = willisPortrait;
    }

    public static boolean canWillisBeRecruited(Model model) {
        return !model.getSettings().getMiscFlags().containsKey(RECRUIT_FLAG);
    }

    @Override
    protected void doEvent(Model model) {
        println("You return to the library. Willis is ecstatic.");
        showExplicitPortrait(model, portrait, "Willis");
        portraitSay("I've never had this much help. Just think what this place will become now, " +
                "a real fulcrum of the community!");
        leaderSay("That's great Willis. Glad we could help. Now we really should be leaving.");
        portraitSay("Wait a minute...");
        println("Willis seems to be thinking about something as she watches the new librarians bustling " +
                "about the library, moving books and happily discussing authors and editions.");
        leaderSay("What's the matter Willis?");
        portraitSay("It's just. I've worked in this library for 20 years now. I've always felt like " +
                "it was my duty to keep it in shape. But now, it seems its future is secure. Maybe there's no need for me here?");
        leaderSay("Thinking about a career change?");
        portraitSay("Part of me wants to explore arcanism more. I won't be able to develop my knowledge and skills here, " +
                "I'll have to venture out in the world.");
        leaderSay("Wanna come with us?");
        println("Press enter to continue.");
        waitForReturn();
        GameCharacter willis = model.getMainStory().getWillisCharacter();
        willis.setLevel((int) Math.ceil(GameState.calculateAverageLevel(model)));
        RecruitState recruit = new RecruitState(model, RecruitableCharacter.makeOneRecruitable(willis, RecruitInfo.profession));
        recruit.run(model);
        removePortraitSubView(model);
        setCurrentTerrainSubview(model);
        if (model.getParty().getPartyMembers().contains(willis)) {
            model.getSettings().getMiscFlags().put(RECRUIT_FLAG, true);
            leaderSay("Good to have you with us Willis.");
            partyMemberSay(willis, "Hope I can be of help.");
        } else {
            leaderSay("On the other hand. Maybe we should go our separate ways.");
            showExplicitPortrait(model, portrait, "Willis");
            portraitSay("Perhaps it's for the best.");
        }
    }
}
