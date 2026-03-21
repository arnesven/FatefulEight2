package model.achievements;

import model.Model;
import model.QuestDeck;
import model.items.spells.Spell;
import model.items.spells.SummonShipSpell;
import model.mainstory.MainStory;
import model.map.*;
import model.quests.Quest;
import model.states.dailyaction.shops.ShopCustomer;
import model.states.dailyaction.shops.ShopSupplier;
import model.states.events.*;
import util.MyLists;
import view.MyColors;

import java.io.Serializable;
import java.util.*;

public class GameAchievements implements Serializable {
    private final Map<String, Achievement> partyAchievements;

    public GameAchievements() {
        partyAchievements = new HashMap<>();
        addEventAchievements();
        addQuestAchievements();
        addSpellAchievements();
        addDungeonAchievements();
        addMiscAchievements();
    }

    private void addSpellAchievements() {
        for (MyColors color : Spell.spellColors) {
            new CollectAllSpellsOfColorAchievement(color).registerYourself(partyAchievements);
        }
        registerAchievement(SummonShipSpell.getAchievementData());
    }

    private void addMiscAchievements() {
        new LittleSpenderAchievement().registerYourself(partyAchievements);
        new BigSpenderAchievement().registerYourself(partyAchievements);
        new LittleEarnerAchievement().registerYourself(partyAchievements);
        new BigEarnerAchievement().registerYourself(partyAchievements);
        new BeginnerFishermanAchievement().registerYourself(partyAchievements);
        new ExpertFishermanAchievement().registerYourself(partyAchievements);
        new OrcSlayerAchievement().registerYourself(partyAchievements);
        new ApprenticeCrafterAchievement().registerYourself(partyAchievements);
        new JourneymanCrafterAchievement().registerYourself(partyAchievements);
        new MasterCrafterAchievement().registerYourself(partyAchievements);
        new ApprenticeBrewerAchievement().registerYourself(partyAchievements);
        new JourneymanBrewerAchievement().registerYourself(partyAchievements);
        new MasterBrewerAchievement().registerYourself(partyAchievements);
        new PettyThiefAchievement().registerYourself(partyAchievements);
        new MasterThiefAchievement().registerYourself(partyAchievements);
        new HorsemanAchievement().registerYourself(partyAchievements);
        new DuelistAchievement().registerYourself(partyAchievements);
        new DiligentStudentAchievement().registerYourself(partyAchievements);
        new CardSharkAchievement().registerYourself(partyAchievements);
        new RitualistAchievement().registerYourself(partyAchievements);
        new DrunkardAchievement().registerYourself(partyAchievements);
        new SailorAchievement().registerYourself(partyAchievements);
        new PassengerAchievement().registerYourself(partyAchievements);
        new HitchhikerAchievement().registerYourself(partyAchievements);
        new HorseTraderAchievement().registerYourself(partyAchievements);
        new SalvagerAchievement().registerYourself(partyAchievements);
        new TinkererAchievement().registerYourself(partyAchievements);
        new BloodLustAchievement().registerYourself(partyAchievements);
        partyAchievements.put(HighDamageAchievement.KEY, new HighDamageAchievement());
        partyAchievements.put(MultiSlayerAchievement.KEY, new MultiSlayerAchievement());
        partyAchievements.put(AlucardAchievement.KEY, new AlucardAchievement());
        partyAchievements.put(HomeOwnerAchievement.KEY, new HomeOwnerAchievement());
        partyAchievements.put(RenovatorAchievement.KEY, new RenovatorAchievement());
        registerAchievement(ShopCustomer.getAchievementData());
        registerAchievement(ShopSupplier.getAchievementData());
    }

    private void addDungeonAchievements() {
        WorldHex[][] worldContent = WorldBuilder.buildWorld(WorldType.original);
        List<UrbanLocation> lordLocations = new ArrayList<>();
        List<RuinsLocation> ruins = new ArrayList<>();
        List<TombLocation> tombs = new ArrayList<>();

        for (WorldHex[] arr1 : worldContent) {
            for (WorldHex wh : arr1) {
                if (wh.hasLord()) {
                    lordLocations.add((UrbanLocation) wh.getLocation());
                } else if (wh.getLocation() instanceof RuinsLocation) {
                    ruins.add((RuinsLocation) wh.getLocation());
                } else if (wh.getLocation() instanceof TombLocation) {
                    tombs.add((TombLocation) wh.getLocation());
                }
            }
        }

        for (UrbanLocation urb : lordLocations) {
            if (urb instanceof CastleLocation) {
                registerDungeonAchievement(((CastleLocation) urb).getDungeonAchievement());
            }
        }

        for (RuinsLocation loc : ruins) {
            registerDungeonAchievement(loc.getAchievementData());
        }

        for (TombLocation loc : tombs) {
            registerDungeonAchievement(loc.getAchievementData());
        }
    }

    private void addQuestAchievements() {
        List<Quest> allQuests = new ArrayList<>(QuestDeck.getAllQuests());
        allQuests.addAll(MainStory.getQuests());
        for (Quest q : allQuests) {
            if (q.givesAchievement()) {
                registerQuestAchievement(q.getAchievementData());
            }
        }
    }

    private void addEventAchievements() {
        registerAchievement(BurningBuildingEvent.getAchievementData());
        registerAchievement(DiggingGameEvent.getAchievementData());
        registerAchievement(DwarvenCityEvent.getAchievementData());
        registerAchievement(EnchantressEvent.getAchievementData());
        registerAchievement(GardenMazeEvent.getAchievementData());
        registerAchievement(SmugglersEvent.getAchievementData());
        registerAchievement(TallSpireEvent.getAchievementData());
        registerAchievement(VisitMonasteryEvent.getAchievmentData());
        registerAchievement(DoppelgangerEvent.getAchievementData());
        registerAchievement(DigForTreasureEvent.getAchievementData());
        for (Achievement.Data data : CommandOutpostDailyEventState.getAchievementDatas()) {
            registerAchievement(data);
        }
        for (Achievement.Data data : BrigandInBurgundyEvent.getAchievementDatas()) {
            registerAchievement(data);
        }
    }

    private void registerAchievement(Achievement.Data achievementData) {
        partyAchievements.put(achievementData.getKey(), new Achievement(achievementData));
    }

    private void registerQuestAchievement(Achievement.Data achievementData) {
        partyAchievements.put(achievementData.getKey(), new QuestAchievement(achievementData));
    }

    private void registerDungeonAchievement(Achievement.Data achievementData) {
        partyAchievements.put(achievementData.getKey(), new DungeonAchievement(achievementData));
    }

    public int numberOfCompleted(Model model) {
        return MyLists.intAccumulate(getAsList(), a -> a.isCompleted(model) ? 1 : 0);
    }

    public int getTotal() {
        return partyAchievements.size();
    }

    public List<Achievement> getAsList() {
        List<Achievement> result = new ArrayList<>(partyAchievements.values());
        return result;
    }

    public void setCompleted(String key) {
        if (!partyAchievements.containsKey(key)) {
            for (Achievement a : partyAchievements.values()) {
                System.err.println(a.getKey() + ":" + a.getName());
            }

            throw new IllegalArgumentException("No achievement for '" + key + "'");
        }
        partyAchievements.get(key).setCompleted(true);
    }

    public boolean isCompleted(String key, Model model) {
        return partyAchievements.get(key).isCompleted(model);
    }

    public Achievement getAchievement(String key) {
        return partyAchievements.get(key);
    }
}
