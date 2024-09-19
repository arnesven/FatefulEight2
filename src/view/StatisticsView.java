package view;

import model.GameStatistics;
import model.Model;
import model.Summon;
import model.map.TempleLocation;
import model.map.UrbanLocation;
import model.states.GameState;
import model.states.events.GentlepersonsClubEvent;
import model.states.events.LeagueOfMagesEvent;
import util.MyPair;
import view.party.DrawableObject;
import view.party.SelectableListMenu;

import java.awt.event.KeyEvent;
import java.util.*;

public class StatisticsView extends SelectableListMenu {
    private List<MyPair<String, String>> factionStatus;

    public StatisticsView(GameView previous) {
        super(previous, 60, 40);
    }

    @Override
    public void transitionedTo(Model model) {
        super.transitionedTo(model);
        this.factionStatus = findFactionStatuses(model);
    }

    private List<MyPair<String, String>> findFactionStatuses(Model model) {
        List<MyPair<String, String>> result = new ArrayList<>();
        for (Map.Entry<String, Summon> summon : model.getParty().getSummons().entrySet()) {
            if (summon.getValue().getStep() > 0) {
                String description = "Acquainted";
                if (summon.getValue().getStep() == 2) {
                    description = "Friend";
                }
                UrbanLocation urb = model.getWorld().getUrbanLocationByPlaceName(summon.getKey());
                if (model.getParty().hasHeadquartersIn(urb)) {
                    description = "Resident (HQ)";
                }
                result.add(new MyPair<>(summon.getKey(), description));
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
        result.add(new ListContent(leftColumn, row++, "PARTY"));
        String partySize = model.getParty().size() + "/" + model.getParty().getInventory().getTentSize();
        result.add(makeStringLine(leftColumn, row++, 46, 10, "Current party size", partySize));
        String averagePartyLevel = String.format("%2.1f", GameState.calculateAverageLevel(model));
        result.add(makeStringLine(leftColumn, row++, 46, 10, "Average party level", averagePartyLevel));
        result.add(makeIntLine(leftColumn, row++, "Total experience gained", GameStatistics.getTotalXpGained()));
        result.add(makeIntLine(leftColumn, row++, "Distance travelled", GameStatistics.getDistanceTraveled()));
        result.add(makeIntLine(leftColumn, row++, "Rations consumed", GameStatistics.getRationsConsumed()));
        result.add(makeIntLine(leftColumn, row++, "Gold earned", GameStatistics.getGoldEarned()));
        result.add(makeIntLine(leftColumn, row++, "Gold lost", GameStatistics.getGoldLost()));

        row++;
        result.add(new ListContent(leftColumn, row++, "GEAR"));
        result.add(makeIntLine(leftColumn, row++, "Items purchased", GameStatistics.getItemsBought()));
        result.add(makeIntLine(leftColumn, row++, "Items sold", GameStatistics.getItemsSold()));
        result.add(makeIntLine(leftColumn, row++, "Items crafted", GameStatistics.getItemsCrafted()));
        result.add(makeIntLine(leftColumn, row++, "Items upgraded", GameStatistics.getItemsUpgraded()));

        row++;
        result.add(new ListContent(leftColumn, row++, "COMBAT"));
        result.add(makeIntLine(leftColumn, row++, "Combat events", GameStatistics.getCombatEvents()));
        result.add(makeIntLine(leftColumn, row++, "Surprise combats", GameStatistics.getSurpriseCombats()));
        result.add(makeIntLine(leftColumn, row++, "Ambush combats", GameStatistics.getAmbushCombats()));
        result.add(makeIntLine(leftColumn, row++, "Total damage dealt", GameStatistics.getTotalDamage()));
        result.add(makeIntLine(leftColumn, row++, "Enemies killed", GameStatistics.getEnemiesKilled()));
        result.add(makeIntLine(leftColumn, row++, "Maximum damage dealt", GameStatistics.getMaximumDamage()));

        row++;
        result.add(new ListContent(leftColumn, row++, "SKILLS"));
        result.add(makeIntLine(leftColumn, row++, "Solo Skill Checks", GameStatistics.getSoloSkillChecks()));
        result.add(makeIntLine(leftColumn, row++, "Collaborative Skill Checks", GameStatistics.getCollaborativeSkillChecks()));
        result.add(makeIntLine(leftColumn, row++, "Collective Skill Checks", GameStatistics.getCollectiveSkillChecks()));
        result.add(makeIntLine(leftColumn, row++, "Total Skill Checks", GameStatistics.getTotalSkillChecks()));
        result.add(makeIntLine(leftColumn, row++, "Stamina Re-rolls used", GameStatistics.getRerollsUsed()));
        result.add(makeIntLine(leftColumn, row++, "Training sessions", GameStatistics.getTrainingSessions()));
        result.add(makeIntLine(leftColumn, row++, "Class changes", GameStatistics.getClassChanges()));

        row++;
        result.add(new ListContent(leftColumn, row++, "MAGIC"));
        result.add(makeIntLine(leftColumn, row++, "Spell casts attempted", GameStatistics.getSpellCastsAttempted()));
        result.add(makeIntLine(leftColumn, row++, "Spell casts successes", GameStatistics.getSpellSuccesses()));

        row++;
        result.add(new ListContent(leftColumn, row++, "CRIME"));
        result.add(makeIntLine(leftColumn, row++, "Gold pick pocketed", GameStatistics.getPickpocketGold()));
        result.add(makeIntLine(leftColumn, row++, "Items stolen", GameStatistics.getItemsStolen()));
        result.add(makeIntLine(leftColumn, row++, "Maximum notoriety", GameStatistics.getMaximumNotoriety()));
        result.add(makeIntLine(leftColumn, row++, "Current Brotherhood loan", getLoanAmount(model)));
        result.add(makeIntLine(leftColumn, row++, "Murders", GameStatistics.getMurders()));

        row++;
        result.add(new ListContent(leftColumn, row++, "FACTIONS"));
        for (MyPair<String, String> p : factionStatus) {
            result.add(makeStringLine(leftColumn, row++, 40, 16, p.first, p.second));
        }

        row++;
        result.add(new ListContent(leftColumn, row++, "MISCELLANEOUS"));
        result.add(makeIntLine(leftColumn, row++, "Card games played", GameStatistics.getCardGamesPlayed()));
        result.add(makeIntLine(leftColumn, row++, "Rituals performed", GameStatistics.getRituals()));
        result.add(makeIntLine(leftColumn, row++, "Battles fought", GameStatistics.getBattlesFought()));
        result.add(makeIntLine(leftColumn, row++, "Largest fish caught", GameStatistics.getLargestFishCaught()));
        result.add(makeIntLine(leftColumn, row++, "Horse races participated in", GameStatistics.getHorseRaces()));

        return result;
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
        return new ListContent(column, row, String.format("%-" + leftWidth + "s %" + rightWidth + "s", leftText, rightText));
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

    }
}
