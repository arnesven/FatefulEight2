package model.mainstory.honorable;

import model.Model;
import model.items.weapons.BladedWeapon;
import model.items.weapons.HigherTierWeapon;
import model.mainstory.GainSupportOfHonorableWarriorsTask;
import model.map.CastleLocation;
import model.states.DailyEventState;
import util.MyLists;

public class MeetLordShingenEvent extends DailyEventState {
    private final GainSupportOfHonorableWarriorsTask task;

    public MeetLordShingenEvent(Model model, GainSupportOfHonorableWarriorsTask gainSupportOfHonorableWarriorsTask) {
        super(model);
        this.task = gainSupportOfHonorableWarriorsTask;
    }

    @Override
    protected void doEvent(Model model) {
        model.getLog().waitForAnimationToFinish();
        setCurrentTerrainSubview(model);
        println("You finally meet with Lord Shingen");
        showExplicitPortrait(model, task.getShingenPortrait(), "Lord Shingen");
        portraitSay("Ah yes... The westerner I've heard so much about. I've heard old Miko has had you running errands for us.");
        leaderSay("Lord Shingen, it is an honor to meet you.");
        portraitSay("Now please explain the reason for your being here.");
        leaderSay("As you have surely noticed, the world beyond your borders have been more tumultuous lately.");
        CastleLocation arkvale = model.getWorld().getCastleByName(model.getMainStory().getCastleName());

        String kingdom = CastleLocation.placeNameToKingdom(arkvale.getPlaceName());
        portraitSay("Indeed. We have had a surge in orcish activity, and the forces of the " +
                kingdom +
                " have begin infringing on our territory in the west. Are you an envoy of " +
                arkvale.getLordTitle() + " " + arkvale.getLordName() + "?");
        leaderSay("No, or yes... It's complicated.");
        portraitSay("I don't understand. Are you or are you not?");
        leaderSay(iOrWeCap() + " were investigating a matter for the " + arkvale.getLordTitle() + ". The quest " +
                "finally brought " + meOrUs() + " to the ancient stronghold to the east of these lands. But when " + iOrWe() + " returned " +
                iOrWe() + " were wrongfully imprisoned. " + iOrWeCap() + " narrowly escaped " + kingdom +
                " alive. It seems the " + arkvale.getLordTitle() + " has been possessed or controlled by an evil force. " +
                "Because of this, the kingdom has been descended into disorder.");
        portraitSay("I see. And now you seek refuge here?");
        leaderSay("For a time perhaps. " + iOrWeCap() + " aim to rally the support of the kingdoms surrounding " +
                kingdom + ". We intend to return to " + kingdom + " to overthrow " +
                arkvale.getLordName() + " and root out the evil presence.");
        portraitSay("Ah, now I understand. You seek to form an alliance with me.");
        leaderSay("Yes, that's one way of putting it. If you could distract the armed forces of " + kingdom +
                " and press " + arkvale.getLordName() + " from the east, it would " +
                "greatly improve our chances of taking the castle.");
        println("Shingen strokes his beard gently as he considers your words.");
        portraitSay("This is not a decision one takes lightly. Furthermore, I cannot be sure your intentions " +
                "are completely honorable.");
        leaderSay(iOrWeCap() + "'ve already shown a gesture of good will! You said so yourself.");
        portraitSay("Yes, but to form an alliance... It requires something more, a formal gesture.");
        leaderSay(iOrWeCap() + " know nothing about your customs. Can you give " + meOrUs() + " a hint?");
        portraitSay("My grandfather once formed such an alliance with one of the kingdoms of old. If I recall " +
                "correctly, the Grand Duchess of Ardh brought my grandfather a magnificent sword. It was a masterfully " +
                "crafted item. The gesture proved that the Duchess was serious about the alliance.");
        leaderSay("You want a sword? What about all of the ones hanging on the wall?");
        portraitSay("Our customs may seem strange. But formality must be observed. Bring me a sword worthy of our " +
                "commitment to each other, and I will give your proposal sincere consideration.");
        task.setShingenMet();
        if (MyLists.find(model.getParty().getInventory().getAllItems(), it -> it instanceof BladedWeapon ||
                (it instanceof HigherTierWeapon && ((HigherTierWeapon)it).isOfType(BladedWeapon.class))) != null) {
            print("Do you want to present a sword to Lord Shingen now? (Y/N) ");
            if (yesNoInput()) {
                leaderSay("Actually, " + iOrWe() + " have a sword of good quality with us.");
                portraitSay("You do? How interesting. You are cut from a finer cloth than other westerners I've met.");
                new PresentSwordToShingenEvent(model, task, false).doEvent(model);
                return;
            }
        }
        leaderSay("Alright. " + iOrWeCap() + " will return with a sword. Goodbye for now.");
        leaderSay("Farewell.");
    }
}
