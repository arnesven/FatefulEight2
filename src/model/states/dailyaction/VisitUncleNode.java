package model.states.dailyaction;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.map.TownLocation;
import model.races.Race;
import model.states.DailyEventState;
import model.states.GameState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.PortraitSubView;
import view.subviews.TownSubView;

import java.awt.*;

public class VisitUncleNode extends DailyActionNode {
    private static final Sprite SPRITE = new Sprite32x32("uncleshouse", "world_foreground.png", 0x22,
            MyColors.YELLOW, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.BEIGE);
    private final GameCharacter whos;
    private final TownLocation town;
    private final AdvancedAppearance unclePortrait;

    public VisitUncleNode(TownLocation town, GameCharacter whos, AdvancedAppearance unclePortrait) {
        super("Visit " + whos.getFirstName() + "'s Uncle");
        this.town = town;
        this.whos = whos;
        this.unclePortrait = unclePortrait;
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new VisitUncleEvent(model, town, whos, unclePortrait);
    }

    public void drawYourself(Model model, Point p) {
        model.getScreenHandler().register(getBackgroundSprite().getName(), new Point(p), getBackgroundSprite());
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {

    }

    private static class VisitUncleEvent extends DailyEventState {
        private final GameCharacter whos;
        private final TownLocation town;
        private final AdvancedAppearance unclePortrait;
        private final AdvancedAppearance everix;

        public VisitUncleEvent(Model model, TownLocation town, GameCharacter whos, AdvancedAppearance unclePortrait) {
            super(model);
            this.town = town;
            this.whos = whos;
            this.unclePortrait = unclePortrait;
            everix = PortraitSubView.makeRandomPortrait(Classes.DRU, Race.ALL);
        }

        @Override
        protected void doEvent(Model model) {
            setCurrentTerrainSubview(model);
            if (model.getMainStory().getStep() == 0) {
                model.getMainStory().increaseStep();
                println("You enter the hut where " + whos.getFirstName() + "'s uncle lives. The man greets you cheerfully.");
                showUnclePortrait(model);
                if (model.getParty().getPartyMembers().contains(whos)) {
                    portraitSay(whos.getFirstName() + "! It's good to see you!");
                    partyMemberSay(whos, "Uncle, it's been too long. How have you been?");
                    portraitSay("Oh, fairly well, I suppose. I see you've brought some friends.");
                    leaderSay(model.getParty().getLeader().getName() + ", and company, at your service.");
                    partyMemberSay(whos, "You had some trouble that needed to be sorted?");
                    portraitSay("Indeed. These damnable frogmen. They've always been a bit of a nuisance, raiding " +
                            "our stores, clogging our canals with their fishing nets.");
                    partyMemberSay(whos, "Yes I remember them from when I grew up here...");
                } else {
                    portraitSay("Hello there. Can I help you?");
                    leaderSay(model.getParty().getLeader().getName() + ", and company, at your service. We're friends of " + whos.getName() + ".");
                    leaderSay("You had some trouble that needed to be sorted?");
                    portraitSay("Indeed. These damnable frogmen. They've always been a bit of a nuisance, raiding " +
                            "our stores, clogging our canals with their fishing nets.");
                }
                portraitSay("But now they're worse than ever. It's like they've gone mad or something.");
                leaderSay("Seems odd. Frogmen are usually quite peaceful. Tell me more.");
                portraitSay("Well it started a few months ago. We were a few townspeople up at the mill. " +
                        "Suddenly they just attacked out of nowhere! We fought them off best we could, " +
                        "Jimmy got wounded, and Emma's mom still hasn't recovered.");
                leaderSay("Why did they attack you?");
                portraitSay("I don't know. They didn't even respond when we tried to communicate with them. " +
                        "Normally you can make out a few words between the croaks. They just seemed filled with rage.");
                portraitSay("Since then, attacks have become more common. And there have been more thefts than usual.");
                leaderSay("Something must have disturbed them.");
                portraitSay("Well, the " + town.getLordTitle() + " has decided that the disturbances must end. " +
                        heOrSheCap(town.getLordGender()) + " has put me in charge of resolving the issue. I say we need to " +
                        "wipe out the settlement of Frogmen.");
                println("Suddenly, a druid enters the hut.");
                showEverixPortrait(model);
                portraitSay("Don't even think about it!");
                portraitSay("The frogman population is an integral part of the ecosystem. You can't just wipe them out!");
                showUnclePortrait(model);
                portraitSay("Hello Everix, don't you knock before entering someone's home? This is Everix, the town's druid. " +
                        "She's been trying to convince me that there's a 'peaceful solution' to our problem.");
                leaderSay("Maybe there is.");
                portraitSay("I doubt it. These creatures are dimwitted and a travesty to nature.");
                showEverixPortrait(model);
                portraitSay("And you are an old fool. Ignorant and naive.");
                showUnclePortrait(model);
                portraitSay("Nevertheless, they need to be dealt with. If you lot could look into this we would sure " +
                        "greatly appreciate it. Even you Everix, have been attacked by them.");
                showEverixPortrait(model);
                portraitSay("Yes, but... they're just not acting normally...");
                portraitSay("I implore you. Try to find out more about this. We've managed to coexist in peace for centuries. " +
                        "Sure there's been the occasional spat. But normally the townsfolk and the frogmen just ignore each other and go on " +
                        "with their lives. What's changed?");
                leaderSay("We'll look into it.");
                showUnclePortrait(model);
                portraitSay("We'll compensate you of course, the " + town.getLordTitle() + " has assured me there's gold put aside " +
                        "as a reward for whoever helps us.");
            } else if (model.getMainStory().getStep() == 1) {
                showUnclePortrait(model);
                portraitSay("Please take care of the Frogmen as soon as you can!");
                leaderSay("Don't worry, we will");
                showEverixPortrait(model);
                portraitSay("And no violence!");
                showUnclePortrait(model);
                portraitSay("Everix, get out of my house.");
            } else {
                showUnclePortrait(model);
                portraitSay("Oh, there you are! Thanks again for handling the frogmen problem for us!");
                leaderSay("No trouble at all... Say, could you help us with something...");
                portraitSay("What do you need?");
            }
        }

        private void showEverixPortrait(Model model) {
            showExplicitPortrait(model, everix, "Everix");
        }

        private void showUnclePortrait(Model model) {
            showExplicitPortrait(model, unclePortrait, whos.getFirstName() + "'s Uncle");
        }
    }
}
