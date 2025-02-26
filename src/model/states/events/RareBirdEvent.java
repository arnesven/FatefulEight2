package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.items.designs.CageCraftingDesign;
import model.items.designs.CraftingDesign;
import model.items.special.CageItem;
import model.items.special.CageWithBird;
import model.items.special.StoryItem;
import model.journal.JournalEntry;
import model.states.DailyEventState;
import model.states.ExploreRuinsState;
import model.states.GameState;
import util.MyLists;
import util.MyPair;
import util.MyRandom;
import view.sprites.MiniPictureSprite;
import view.subviews.MiniPictureSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RareBirdEvent extends DailyEventState {

    private static final MiniPictureSprite SPRITE = new MiniPictureSprite(0x41);
    private static final String HAS_STARTED_KEY = "RareBirdEventStarted";
    private static final String BIRD_GOTTEN_KEY = "RareBirdEventGotten";

    public RareBirdEvent(Model model) {
        super(model);
    }

    @Override
    public String getDistantDescription() {
        return "a small creature, looks like a bird with a colorful plumage";
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Find rare bird", "I know a spot where I've seen rare birds make their nests");
    }

    public static boolean hasStarted(Model model) {
        return model.getSettings().getMiscFlags().containsKey(HAS_STARTED_KEY);
    }

    public static boolean birdGotten(Model model) {
        return model.getSettings().getMiscFlags().containsKey(BIRD_GOTTEN_KEY);
    }

    public static boolean hasBird(Model model) {
        return MyLists.any(model.getParty().getInventory().getStoryItems(), (StoryItem si) -> si instanceof CageWithBird);
    }

    public static boolean checkForSquawk(Model model, GameState state) {
        if (hasBird(model) && MyRandom.flipCoin()) {
            birdSquawk(state);
            List<GameCharacter> candidates = new ArrayList<>(model.getParty().getPartyMembers());
            candidates.removeAll(model.getParty().getBench());
            state.partyMemberSay(candidates.get(0), MyRandom.sample(List.of("Damn bird!", "Who said that?", "Huh!?", "Be quiet bird!")));
            return true;
        }
        return false;
    }

    @Override
    protected void doEvent(Model model) {
        if (birdGotten(model)) {
            new NoEventState(model).doEvent(model);
            return;
        }
        println("As you push a branch out of your way something colorful flutters by.");
        leaderSay("What was that?");
        model.getLog().waitForAnimationToFinish();
        model.setSubView(new MiniPictureSubView(model.getSubView(), SPRITE, "Rare Bird"));
        GameCharacter rando = model.getParty().getLeader();
        if (model.getParty().size() > 1) {
            rando = model.getParty().getRandomPartyMember(model.getParty().getLeader());
        }

        if (hasStarted(model)) {
            if (hasCage(model)) {
                tryToUseCage(model, rando);
            } else if (MyLists.any(model.getParty().getInventory().getCraftingDesigns(),
                        cd -> cd instanceof CageCraftingDesign) ||
                       MyLists.any(model.getParty().getLearnedCraftingDesigns(),
                            cd -> cd instanceof CageCraftingDesign)) {
                leaderSay("Oh, it's that bird again. Darn, we forgot to make a cage!");
            } else {
                leaderSay("Oh, it's that strange bird again. It's so pretty!");
                proposeMakeCage(model, rando);
            }
        } else {
            model.getSettings().getMiscFlags().put(HAS_STARTED_KEY, true);
            partyMemberSay(rando, "What a beautiful bird! I've never seen anything like it before.");
            leaderSay("Yes beautiful indeed.");
            partyMemberSay(rando, "If we could catch it, we could probably sell it to a " +
                    "collector for a good bit of gold.");
            proposeMakeCage(model, rando);

        }
    }

    private void proposeMakeCage(Model model, GameCharacter rando) {
        boolean success = tryCatchBird(model);
        if (success) {
            leaderSay("We've got it!");
            println("The bird is indeed very beautiful but is jerking frantically in your hand.");
            leaderSay("Hmm... I guess we could tie it up. But that would likely kill it. If only we had some kind of cage.");
            println("You have nothing in your inventory which could function like a cage. " +
                    "Grudgingly, you release the bird back into the wild.");
        } else {
            leaderSay("Shoot... it flew away.");
            partyMemberSay(rando, "But even if we did catch it. What would we do with it? It's not " +
                    "like we could tie it up with rope, that would kill it.");
            leaderSay("If only we had some kind of cage.");
        }
        partyMemberSay(rando, "We could probably make a cage out of some materials if we had access to a workbench.");
        leaderSay("What would that even look like?");
        MyPair<Boolean, GameCharacter> pair = model.getParty().doSoloSkillCheckWithPerformer(model, this, Skill.Labor, 7);
        if (pair.first) {
            partyMemberSay(pair.second, "Here... something like this.");
            println(pair.second.getFirstName() + " hands you a hastily drawn sketch.");
            model.getParty().getInventory().add(new CageCraftingDesign());
            leaderSay("Interesting... maybe we can get that done the next time we see a crafting bench.");
            partyMemberSay(rando, "Then we just need to find that bird again...");
            leaderSay("And when we do, we'll be ready!");
            JournalEntry.printJournalUpdateMessage(model);
        } else {
            leaderSay("Ach... this is just some wild goose chase anyway. " +
                    "Let's focus on the task at hand instead.");
        }
    }


    private void tryToUseCage(Model model, GameCharacter rando) {
        boolean success = tryCatchBird(model);
        if (success) {
            partyMemberSay(rando, "We have you now!");
            leaderSay("Quick. Put it in the cage!");
            model.getParty().getInventory().getStoryItems().removeIf((StoryItem si) -> si instanceof CageItem);
            model.getParty().getInventory().add(new CageWithBird());
            println("The bird squawks loudly and tries to get away but you quickly shove the bird in the cage.");
            leaderSay("Presto!");
            partyMemberSay(rando, "Great!");
            println("The party members admire the bird.");
            partyMemberSay(rando, "So what do we do with it.");
            leaderSay("Now we just have to find a buyer.");
            birdSquawk(this);
            model.getSettings().getMiscFlags().put(BIRD_GOTTEN_KEY, true);
        } else {
            println("The rare bird flew away again.");
        }
    }

    public static void birdSquawk(GameState state) {
        state.printQuote("Rare Bird", MyRandom.sample(List.of("SQUAAAAWK!", "PRIII PRIII PRIIIII!",
                "KOO-RAAA, KOOOO, KOO-RAA", "BA-CAAAAWK!")));
    }

    private static boolean hasCage(Model model) {
        return MyLists.any(model.getParty().getInventory().getStoryItems(),
                (StoryItem si) -> si instanceof CageItem);
    }

    private boolean tryCatchBird(Model model) {
        print("Do you attempt to catch the bird? (Y/N) ");
        if (!yesNoInput()) {
            return false;
        }
        return model.getParty().doSoloSkillCheck(model, this, Skill.Acrobatics, 9);
    }


    public static JournalEntry makeJournalEntry(Model model) {
        return new JournalEntry() {
            private boolean hasCage = hasCage(model);
            private boolean completed = birdGotten(model);
            @Override
            public String getName() {
                return "The Rare Bird";
            }

            @Override
            public String getText() {
                String intro = "You've encountered a rare colorful bird. ";
                if (completed) {
                    return intro + " You constructed a cage and caught the rare bird in it.\n\nCompleted";
                }
                if (hasCage) {
                    return intro + "Catch the colorful bird in the cage.";
                }
                return intro + "You're determined to catch " +
                        "the bird and sell it to a collector.";
            }

            @Override
            public boolean isComplete() {
                return completed;
            }

            @Override
            public boolean isFailed() {
                return false;
            }

            @Override
            public boolean isTask() {
                return true;
            }

            @Override
            public Point getPosition(Model model) {
                return null;
            }
        };
    }
}
