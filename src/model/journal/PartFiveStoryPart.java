package model.journal;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.SilhouetteAppearance;
import model.items.Equipment;
import model.items.Item;
import model.items.PotionRecipe;
import model.items.ReadableItem;
import model.items.designs.CraftingDesign;
import model.items.spells.Spell;
import model.mainstory.MainStory;
import model.mainstory.SitInDungeonState;
import model.mainstory.VisitLordEvent;
import model.map.CastleLocation;
import model.map.UrbanLocation;
import model.map.WorldBuilder;
import model.map.locations.AncientStrongholdLocation;
import model.quests.*;
import model.states.DailyEventState;
import model.states.EveningState;
import model.states.GameState;
import model.states.QuestState;
import model.states.dailyaction.TownDailyActionState;
import util.MyLists;
import view.JournalView;
import view.LogView;
import view.subviews.CollapsingTransition;
import view.subviews.ImageSubView;

import java.awt.*;
import java.util.List;

public class PartFiveStoryPart extends StoryPart {
    private static final int QUEST_DONE = 1;
    private static final int LORD_RETURN_TO = 2;
    private final String castleName;
    private int internalStep = 0;

    public PartFiveStoryPart(String castleName) {
        this.castleName = castleName;
    }

    @Override
    public List<JournalEntry> getJournalEntries() {
        return List.of(new GoToAncientStrongholdEntry());
    }

    @Override
    public void handleTownSetup(TownDailyActionState townDailyActionState) {

    }

    @Override
    public void progress() {
        internalStep++;
    }

    @Override
    protected StoryPart getNextStoryPart(Model model, int track) {
        return new PartSixStoryPart(castleName);
    }

    @Override
    public void addQuests(Model model, List<Quest> quests) {
        if (model.getCurrentHex().getLocation() != null &&
                model.getCurrentHex().getLocation() instanceof AncientStrongholdLocation && internalStep < QUEST_DONE) {
            CastleLocation loc = model.getWorld().getCastleByName(castleName);
            quests.add(getQuestAndSetPortrait(AncientStrongholdQuest.QUEST_NAME, model.getLordPortrait(loc), loc.getLordName()));
        }
    }

    @Override
    public VisitLordEvent getVisitLordEvent(Model model, UrbanLocation location) {
        if (location instanceof CastleLocation &&
                ((CastleLocation) location).getName().equals(castleName) && internalStep == QUEST_DONE) {
            return new PartFiveEvent(model, location);
        }
        return super.getVisitLordEvent(model, location);
    }

    @Override
    protected boolean isCompleted() {
        return internalStep >= LORD_RETURN_TO;
    }

    private class GoToAncientStrongholdEntry extends MainStoryTask {
        public GoToAncientStrongholdEntry() {
            super("The Ancient Stronghold");
        }

        @Override
        public String getText() {
            if (internalStep == 0) {
                return "Travel to the location of the Ancient Stronghold.";
            }
            if (internalStep == QUEST_DONE) {
                return "Return to " + castleName + " to report your success.";
            }
            return "You defeated the spirit of the Quad, but upon your return to " +
                    castleName + ", you were thrown in the dungeon!";
        }

        @Override
        public boolean isComplete() {
            return PartFiveStoryPart.this.isCompleted();
        }

        @Override
        public Point getPosition(Model model) {
            if (internalStep == 0) {
                return WorldBuilder.getFortressPosition(model.getMainStory().getExpandDirection());
            }
            return model.getMainStory().getCastlePosition(model);
        }
    }

    private class PartFiveEvent extends VisitLordEvent {
        private final CastleLocation castle;

        public PartFiveEvent(Model model, UrbanLocation location) {
            super(model);
            this.castle = (CastleLocation)location;
        }

        @Override
        public boolean returnEveningState() {
            return true;
        }

        @Override
        protected void doEvent(Model model) {
            setCurrentTerrainSubview(model);
            println("You enter into the lord's throne room. The large room, which normally just hosts the " +
                    castle.getLordTitle() + " and " + hisOrHer(castle.getLordGender()) +
                    " advisers is now crowded by " + castle.getLordName() + "'s court, council members and " +
                    hisOrHer(castle.getLordGender()) + " royal guards.");
            model.getLog().waitForAnimationToFinish();
            showLord(model);
            portraitSay("My friends, welcome back.");
            leaderSay(castle.getLordTitle() + ", we've vanquished the spirit of the Quad!");
            portraitSay("Indeed? Then why have our borders been overrun with orcish hordes?");
            leaderSay("That... that can't be. I assure you, the enemy has been beaten.");
            portraitSay("Hmmm...");
            println("The " + castle.getLordTitle() + " turns as to address the many bystanders in the throne room.");
            portraitSay("My subjects, I fear we have been lead astray for too long. For too long we let ourselves " +
                    "become weak and soft. We have permitted the forces of evil to harass our borders and we have " +
                    "let corruption seep into the heart of our kingdom.");
            leaderSay(castle.getLordName() + ", what are you talking about?");
            println("The " + castle.getLordTitle() + " seems to ignore you.");
            portraitSay("Even I, your most devoted servant, your most resolute leader, have let myself be " +
                    "swayed by the voices of serpents!");
            leaderSay("Who are you calling serpents? I don't like the sound of this.");
            portraitSay("But no more! I have decided to turn the tables on this evil!");
            leaderSay("My lord! It is true what you say about the attacks in the border lands, we must...");
            portraitSay("We must nothing! Least not anything which you would advise. Guards, arrest these " +
                    "jackals!");
            leaderSay("This is some kind of misunderstanding! We're innocent!");
            portraitSay("Lock them in the dungeons!");
            println("Before you can reach for your weapons the " + castle.getLordTitle() + "'s guards seize you " +
                    "and carry you down the hall. You are all thoroughly searched and all of your belongings are taken from you. " +
                    "Then you are promptly thrown in a cell in the castle dungeons, and a heavy door of metal " +
                    "bars is slammed shut behind you.");
            increaseStep(model);
            transitionStep(model);
            model.getLog().waitForAnimationToFinish();
        }

        private void showLord(Model model) {
            showExplicitPortrait(model, model.getLordPortrait(castle), castle.getLordName());
        }

        @Override
        protected boolean allowPartyEvent() {
            return false;
        }

        @Override
        protected GameState getEveningState(Model model) {
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                gc.unequipAccessory();
                gc.unequipArmor();
                gc.unequipWeapon();
            }
            List<Item> belongings = MyLists.filter(model.getParty().getInventory().getAllItems(),
                    (Item it) -> !getsToKeep(it));
            model.getLog().addAnimated(LogView.GOLD_COLOR + "Your belongings have been taken from you!\n" + LogView.DEFAULT_COLOR);
            EscapeTheDungeonQuest q = (EscapeTheDungeonQuest)getQuestAndSetPortrait(EscapeTheDungeonQuest.QUEST_NAME,
                    new SilhouetteAppearance(), "Nobody");
            q.setLootRewardItems(belongings,
                    model.getParty().getGold(),
                    model.getParty().getObols(),
                    model.getParty().getInventory().getMaterials(),
                    model.getParty().getInventory().getIngredients(),
                    model.getParty().getInventory().getLockpicks());
            model.getParty().addToGold(-model.getParty().getGold());
            model.getParty().addToObols(-model.getParty().getObols());
            model.getParty().getInventory().addToMaterials(-model.getParty().getInventory().getMaterials());
            model.getParty().getInventory().addToIngredients(-model.getParty().getInventory().getIngredients());
            model.getParty().getInventory().addToLockpicks(-model.getParty().getInventory().getLockpicks());
            MyLists.forEach(belongings, it -> model.getParty().getInventory().remove(it));

            q.setStoryPart(model.getMainStory().getStoryParts().get(model.getMainStory().getStoryParts().size()-1));
            return new SitInDungeonState(model, castle, q, 3);
        }


    }


    private static boolean getsToKeep(Item it) {
        return it instanceof Spell || it instanceof ReadableItem ||
                it instanceof PotionRecipe || it instanceof CraftingDesign;
    }
}
