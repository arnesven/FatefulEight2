package model.mainstory;

import model.Model;
import model.map.CastleLocation;
import model.map.UrbanLocation;

public abstract class GetSupportFromLordEvent extends VisitLordEvent {
    private final UrbanLocation castle;
    private final GainSupportOfNeighborKingdomTask task;
    private final UrbanLocation otherNeighbor;

    public GetSupportFromLordEvent(Model model, UrbanLocation location, GainSupportOfNeighborKingdomTask task,
                                   UrbanLocation otherNeighbor) {
        super(model);
        this.castle = location;
        this.task = task;
        this.otherNeighbor = otherNeighbor;
    }

    protected abstract String getShortThreatDescription(CastleLocation kingdom);

    protected abstract String getLongThreatDescription(CastleLocation kingdom);

    @Override
    protected void doEvent(Model model) {
        setCurrentTerrainSubview(model);
        CastleLocation kingdom = model.getWorld().getCastleByName(model.getMainStory().getCastleName());
        showExplicitPortrait(model, model.getLordPortrait(castle), castle.getLordName());
        if (task.getStage() > 0) {
            portraitSay("Once the threat of the " + getShortThreatDescription(kingdom) +
                    " has been dealt with, I'll send my elite troops to the rendezvous spot.");
            leaderSay("Understood.");
            portraitSay("Good bye, and good luck!");
            return;
        }

        String agent;
        if (model.getParty().size() > 1) {
            portraitSay("Oh, it's you. I've heard of you, '" + model.getParty().getLeader().getFullName() + "'s Company', right?");
            leaderSay("Yes, that's us.");
            agent = "an agent";
        } else {
            portraitSay("Oh, it's you. I've heard of you, " + model.getParty().getLeader().getFullName() + ", right?");
            leaderSay("Yes, that's me.");
            agent = "agents";
        }
        portraitSay("I'm " + castle.getLordName() + ".");
        portraitSay("It's well known you are " + agent + " of the now infamous " +
                        kingdom.getLordName() + " of " + CastleLocation.placeNameToKingdom(kingdom.getPlaceName()) +
                ". State your business quickly before you are thrown out!");
        leaderSay("Actually, " + imOrWere() + " persona non grata in " + CastleLocation.placeNameShort(kingdom.getPlaceName()) +
                " at the moment.");
        portraitSay("How interesting. What happened?");
        leaderSay("It seems the good " + kingdom.getLordTitle() + " has been seduced, persuaded, coerced or possessed by the very " +
                "threat we were hired to eliminate.");
        portraitSay("So you've come to seek asylum in this kingdom?");
        leaderSay("More than that, " + imOrWere() + " going to see this through to the end. " +
                "We need to get back into the keep at " + kingdom.getPlaceName() + ".");
        portraitSay("Well good luck with that. The " + CastleLocation.placeNameToKingdom(kingdom.getPlaceName()) +
                " seems to be a very dark place these days. There are tons of troops along their borders " +
                "and royal agents are harassing the population, trying to root out traitors and informants.");
        leaderSay("Yes, " + iOrWe() + " know. But with your help, it may be possible for " + meOrUs() +
                " to get back to " + kingdom.getPlaceName() + ".");
        portraitSay("My help? What do you expect me to do?");
        leaderSay("Give me your support. First, put diplomatic pressure on " + CastleLocation.placeNameShort(kingdom.getPlaceName()) +
                ". Secondly, amass your forces along the border, that should get " + kingdom.getLordName() + "'s attention.");
        leaderSay("And finally, lend me a company of your best soldiers. At the opportune moment, we'll storm the castle.");
        portraitSay("That's a ludicrous plan, it'll never work.");
        leaderSay("Don't be so sure. I'm making the same proposition to the " + otherNeighbor.getLordTitle() +
                " of " + CastleLocation.placeNameShort(otherNeighbor.getPlaceName()) +
                ". And I'm getting the support of the " + model.getMainStory().getRemotePeopleName() + " of the " +
                model.getMainStory().getExpandDirectionName().toLowerCase() + ". With combined forces, I think it's doable.");
        portraitSay("You think you can ally with the " + model.getMainStory().getRemotePeopleName() + "? That would be impressive...");
        println("The " + castle.getLordTitle() + " rubs " + hisOrHer(castle.getLordGender()) + " chin for a bit while thinking.");
        portraitSay("Hmmm... Okay, you've convinced me. If you can convince the others to give you their support, " +
                "I'll do the same. But I'll need your help first.");
        leaderSay("Of course. What is it?");
        portraitSay(getLongThreatDescription(kingdom) + ". I just can't divert any troops from " +
                "that upcoming battle, or I fear my whole kingdom will be overrun. If you could travel to the front line and " +
                "ensure the positive outcome of the battle, then I'll be happy to back you in your quest to bring down " + kingdom.getLordName() + ".");
        leaderSay("I understand.");
        portraitSay("Once the threat of " + getShortThreatDescription(kingdom) +
                " has been dealt with, I'll send my elite troops to rendezvous with you near " + kingdom.getPlaceName() + ".");
        leaderSay("Thank you. You won't regret this.");
        portraitSay("Good bye, and good luck!");
        task.progress();
    }
}
