package view;

import model.GameStatistics;
import model.Model;
import model.Summon;
import model.characters.GameCharacter;
import model.headquarters.Headquarters;
import model.items.Item;
import model.map.TempleLocation;
import model.map.UrbanLocation;
import model.races.AllRaces;
import model.states.GameState;
import model.states.events.GentlepersonsClubEvent;
import model.states.events.LeagueOfMagesEvent;
import model.states.events.OrcsEvent;
import util.MyLists;
import util.MyPair;
import util.MyStrings;
import view.party.DrawableObject;
import view.party.SelectableListMenu;

import java.awt.event.KeyEvent;
import java.util.*;

public class StatisticsView extends SelectableListMenu {
    public static final int WIDTH = 60;
    public static final int HEIGHT = 40;
    private List<MyPair<String, String>> factionStatus;

    public StatisticsView(GameView previous) {
        super(previous, WIDTH, HEIGHT);
    }

    @Override
    public void transitionedTo(Model model) {
        super.transitionedTo(model);
        this.factionStatus = findFactionStatuses(model);
    }

    private List<MyPair<String, String>> findFactionStatuses(Model model) {
        List<MyPair<String, String>> result = new ArrayList<>();
        List<MyPair<String, String>> mainStoryFactions = model.getMainStory().getFactionStrings();
        result.addAll(mainStoryFactions);
        for (Map.Entry<String, Summon> summon : model.getParty().getSummons().entrySet()) {
            if (!MyLists.any(result, p -> p.first.equals(summon.getKey()))) {
                if (summon.getValue().getStep() > 0) {
                    String description = "Acquainted";
                    if (summon.getValue().getStep() == 2) {
                        description = "Friend";
                    }
                    UrbanLocation urb = model.getWorld().getUrbanLocationByPlaceName(summon.getKey());
                    if (model.getParty().hasHeadquartersIn(urb)) {
                        description = "Resident (HQ)";
                    }
                    result.add(new MyPair<>(MyStrings.capitalize(summon.getKey()), description));
                }
            }
        }
        for (TempleLocation temple : model.getWorld().getTempleLocations()) {
            if (model.getParty().isBannedFromTemple(temple.getName())) {
                result.add(new MyPair<>(temple.getName(),"Banned"));
            }
        }
        if (GentlepersonsClubEvent.isMember(model)) {
            result.add(new MyPair<>("Gentleperson's Club", "Member"));
        }
        if (LeagueOfMagesEvent.isMember(model)) {
            result.add(new MyPair<>("League of Mages", "Member"));
        }
        String orcsFaction = OrcsEvent.getOrcsFactionString(model);
        if (!orcsFaction.equals("")) {
            result.add(new MyPair<>("Orcish Tribes", orcsFaction));
        }

        return result;
    }

    @Override
    public void transitionedFrom(Model model) {

    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        return new ArrayList<>();
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        int leftColumn = xStart + 2;
        int row = yStart + 1;
        List<ListContent> result = new ArrayList<>();
        result.add(makeTitleLine(leftColumn, row++, "PARTY"));
        String achievements = model.getAchievements().numberOfCompleted(model)  + "/" + model.getAchievements().getTotal();
        result.add(new PermanentlyEnabledListContent(leftColumn, row++, format(46, 10, "Achievements", achievements)) {
            @Override
            public void performAction(Model model, int x, int y) {
                setInnerMenu(new AchievementsView(StatisticsView.this), model);
            }
        });

        String partySize = model.getParty().size() + "/" + model.getParty().getInventory().getTentSize();
        result.add(new PermanentlyEnabledListContent(leftColumn, row++, format(46, 10, "Current party size", partySize)) {
            @Override
            public void performAction(Model model, int x, int y) {
                setInnerMenu(new PartyCompositionView(model), model);
            }
        });
        String averagePartyLevel = String.format("%2.1f", GameState.calculateAverageLevel(model));
        result.add(makeStringLine(leftColumn, row++, 46, 10, "Average party level", averagePartyLevel));
        result.add(makeIntLine(leftColumn, row++, "Total experience gained", GameStatistics.getTotalXpGained()));
        result.add(makeIntLine(leftColumn, row++, "Distance travelled", GameStatistics.getDistanceTraveled()));
        result.add(makeIntLine(leftColumn, row++, "Rations consumed", GameStatistics.getRationsConsumed()));
        result.add(makeIntLine(leftColumn, row++, "Gold earned", GameStatistics.getGoldEarned()));
        result.add(makeIntLine(leftColumn, row++, "Gold spent", GameStatistics.getGoldSpent()));
        result.add(makeIntLine(leftColumn, row++, "Gold lost", GameStatistics.getGoldLost()));
        result.add(makeIntLine(leftColumn, row++, "Wages paid", GameStatistics.getWagesPaid()));

        if (model.getParty().getHeadquarters() != null) {
            row++;
            Headquarters hq = model.getParty().getHeadquarters();
            result.add(makeTitleLine(leftColumn, row++, "HEADQUARTERS"));
            result.add(makeStringLine(leftColumn, row++, 24, 32, "Location", hq.getLocationName().replace("the", "")));
            result.add(makeStringLine(leftColumn, row++, 44, 12, "Size", hq.getSizeName()));
            String hqChars = hq.getCharacters().size() + "/" + hq.getMaxCharacters();
            result.add(makeStringLine(leftColumn, row++, 46, 10, "Characters", hqChars));
        }

        row++;
        result.add(makeTitleLine(leftColumn, row++, "GEAR"));
        result.add(makeIntLine(leftColumn, row++, "Items purchased", GameStatistics.getItemsBought()));
        result.add(makeIntLine(leftColumn, row++, "Items sold", GameStatistics.getItemsSold()));
        result.add(makeIntLine(leftColumn, row++, "Items crafted", GameStatistics.getItemsCrafted()));
        result.add(makeIntLine(leftColumn, row++, "Items upgraded", GameStatistics.getItemsUpgraded()));
        List<String> craftingDesigns = MyLists.transform(model.getParty().getLearnedCraftingDesigns(),
                cd -> cd.getCraftable().getName());
        if (craftingDesigns.size() > 0) {
            result.add(new PermanentlyEnabledListContent(leftColumn, row++,
                    format(46, 10, "Crafting designs learned", "" + craftingDesigns.size())) {
                @Override
                public void performAction(Model model, int x, int y) {
                    setInnerMenu(new SimpleListView(model.getView(), craftingDesigns, "Learned Crafting Designs"), model);
                }
            });
        } else {
            result.add(makeIntLine(leftColumn, row++, "Crafting designs learned", 0));
        }
        String horseString = String.format("%2d/%1d",  model.getParty().getHorseHandler().getFullBloods(),
                model.getParty().getHorseHandler().getPonies());
        result.add(new PermanentlyEnabledListContent(leftColumn, row++,
                format(46, 10, "Horses currently owned", horseString)) {
            @Override
            public void performAction(Model model, int x, int y) {
                setInnerMenu(new SimpleMessageView(model.getView(), getHorsesNeededText(model)), model);
            }
        });

        row++;
        result.add(makeTitleLine(leftColumn, row++, "COMBAT"));
        result.add(makeIntLine(leftColumn, row++, "Combat events", GameStatistics.getCombatEvents()));
        result.add(makeIntLine(leftColumn, row++, "Surprise combats", GameStatistics.getSurpriseCombats()));
        result.add(makeIntLine(leftColumn, row++, "Ambush combats", GameStatistics.getAmbushCombats()));
        result.add(makeIntLine(leftColumn, row++, "Combats fled", GameStatistics.getCombatsFled()));
        result.add(makeIntLine(leftColumn, row++, "Total damage dealt", GameStatistics.getTotalDamage()));
        result.add(makeIntLine(leftColumn, row++, "Enemies killed", GameStatistics.getEnemiesKilled()));
        result.add(makeIntLine(leftColumn, row++, "Maximum damage dealt", GameStatistics.getMaximumDamage()));

        row++;
        result.add(makeTitleLine(leftColumn, row++, "SKILLS"));
        result.add(makeIntLine(leftColumn, row++, "Solo Skill Checks", GameStatistics.getSoloSkillChecks()));
        result.add(makeIntLine(leftColumn, row++, "Collaborative Skill Checks", GameStatistics.getCollaborativeSkillChecks()));
        result.add(makeIntLine(leftColumn, row++, "Collective Skill Checks", GameStatistics.getCollectiveSkillChecks()));
        result.add(makeIntLine(leftColumn, row++, "Total Skill Checks", GameStatistics.getTotalSkillChecks()));
        result.add(makeIntLine(leftColumn, row++, "Stamina Re-rolls used", GameStatistics.getRerollsUsed()));
        result.add(makeIntLine(leftColumn, row++, "Training sessions", GameStatistics.getTrainingSessions()));
        result.add(makeIntLine(leftColumn, row++, "Class changes", GameStatistics.getClassChanges()));

        row++;
        result.add(makeTitleLine(leftColumn, row++, "MAGIC"));
        result.add(makeIntLine(leftColumn, row++, "Spell casts attempted", GameStatistics.getSpellCastsAttempted()));
        result.add(makeIntLine(leftColumn, row++, "Spell casts successes", GameStatistics.getSpellSuccesses()));
        List<String> spellsLearned = MyLists.transform(model.getParty().getLearnedSpells(),
                Item::getName);
        if (spellsLearned.size() > 0) {
            result.add(new PermanentlyEnabledListContent(leftColumn, row++,
                    format(46, 10, "Spells learned", "" + spellsLearned.size())) {
                @Override
                public void performAction(Model model, int x, int y) {
                    setInnerMenu(new SimpleListView(model.getView(), spellsLearned, "Learned Spells"), model);
                }
            });
        } else {
            result.add(makeIntLine(leftColumn, row++, "Spells learned", 0));
        }

        result.add(makeIntLine(leftColumn, row++, "Potions brewed", GameStatistics.getPotionsBrewed()));
        result.add(makeIntLine(leftColumn, row++, "Potions distilled", GameStatistics.getPotionsDistilled()));
        List<String> recipes = MyLists.transform(model.getParty().getLearnedPotionRecipes(),
                pr -> pr.getBrewable().getName());
        if (recipes.size() > 0) {
            result.add(new PermanentlyEnabledListContent(leftColumn, row++,
                    format(46, 10, "Potion recipes learned", "" + recipes.size())) {
                @Override
                public void performAction(Model model, int x, int y) {
                    setInnerMenu(new SimpleListView(model.getView(), recipes, "Learned Potion Recipes"), model);
                }
            });
        } else {
            result.add(makeIntLine(leftColumn, row++, "Potion recipes learned", 0));
        }

        row++;
        result.add(makeTitleLine(leftColumn, row++, "CRIME"));
        result.add(makeIntLine(leftColumn, row++, "Gold stolen", GameStatistics.getGoldStolen()));
        result.add(makeIntLine(leftColumn, row++, "Items stolen", GameStatistics.getItemsStolen()));
        result.add(makeIntLine(leftColumn, row++, "Maximum notoriety", GameStatistics.getMaximumNotoriety()));
        result.add(makeIntLine(leftColumn, row++, "Current Brotherhood loan", getLoanAmount(model)));
        result.add(makeIntLine(leftColumn, row++, "Murders", GameStatistics.getMurders()));

        row++;
        result.add(makeTitleLine(leftColumn, row++, "FACTIONS"));
        for (MyPair<String, String> p : factionStatus) {
            result.add(makeStringLine(leftColumn, row++, 39, 17, p.first, p.second));
        }

        row++;
        result.add(makeTitleLine(leftColumn, row++, "ENEMIES"));
        result.add(makeIntLine(leftColumn, row++, "Bandits killed", GameStatistics.getBanditsKilled()));
        result.add(makeIntLine(leftColumn, row++, "Beasts killed", GameStatistics.getBeastsKilled()));
        result.add(makeIntLine(leftColumn, row++, "Orcs killed", GameStatistics.getOrcsKilled()));
        result.add(makeIntLine(leftColumn, row++, "Goblins killed", GameStatistics.getGoblinsKilled()));
        result.add(makeIntLine(leftColumn, row++, "Demons killed", GameStatistics.getDemonsKilled()));
        result.add(makeIntLine(leftColumn, row++, "Dragons killed", GameStatistics.getDragonsKilled()));
        result.add(makeIntLine(leftColumn, row++, "Vampires killed", GameStatistics.getVampiresKilled()));

        row++;
        result.add(makeTitleLine(leftColumn, row++, "MISCELLANEOUS"));
        result.add(makeIntLine(leftColumn, row++, "Card games played", GameStatistics.getCardGamesPlayed()));
        result.add(makeIntLine(leftColumn, row++, "Rituals performed", GameStatistics.getRituals()));
        result.add(makeIntLine(leftColumn, row++, "Magic Duels", GameStatistics.getMagicDuels()));
        result.add(makeIntLine(leftColumn, row++, "Battles fought", GameStatistics.getBattlesFought()));
        result.add(makeIntLine(leftColumn, row++, "Number of fish caught", GameStatistics.getFishCaught()));
        result.add(makeIntLine(leftColumn, row++, "Largest fish caught", GameStatistics.getLargestFishCaught()));
        result.add(makeIntLine(leftColumn, row++, "Horse races participated in", GameStatistics.getHorseRaces()));

        return result;
    }

    private String getHorsesNeededText(Model model) {
        if (model.getParty().getHorseHandler().canRide(model.getParty().getPartyMembers())) {
            return "You currently have enough horses to ride.";
        }
        return model.getParty().getHorseHandler().getSuggestedHorses(model) +
                " for your entire party to be able to ride.";
    }



    private ListContent makeTitleLine(int leftColumn, int row, String text) {
        return new ListContent(leftColumn, row, text) {
            @Override
            public MyColors getForegroundColor(Model model) {
                return MyColors.LIGHT_GRAY;
            }
        };
    }

    private int getLoanAmount(Model model) {
        if (model.getParty().getLoan() != null) {
            return model.getParty().getLoan().getAmount();
        }
        return 0;
    }

    private ListContent makeIntLine(int column, int row, String leftText, int value) {
        return new ListContent(column, row, String.format("%-48s %8d", leftText, value));
    }

    private ListContent makeStringLine(int column, int row, int leftWidth, int rightWidth, String leftText, String rightText) {
        return new ListContent(column, row, format(leftWidth, rightWidth, leftText, rightText));
    }

    private String format(int leftWidth, int rightWidth, String leftText, String rightText) {
        return String.format("%-" + leftWidth + "s %" + rightWidth + "s", leftText, rightText);
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

    }
}
