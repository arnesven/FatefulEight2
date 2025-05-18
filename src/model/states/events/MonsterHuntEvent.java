package model.states.events;

import model.Model;
import model.characters.PersonalityTrait;
import model.enemies.*;
import model.map.*;
import model.states.DailyEventState;
import model.tasks.Destination;
import model.tasks.MonsterHunt;
import model.tasks.MonsterHuntDestinationTask;
import util.MyLists;
import util.MyRandom;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class MonsterHuntEvent extends DailyEventState {
    private static final int DISTANCE_THRESHOLD = 5;
    private final TownLocation turnInTown;
    private final boolean withIntro;

    public MonsterHuntEvent(Model model, TownLocation turnInTown, boolean withIntro) {
        super(model);
        this.turnInTown = turnInTown;
        this.withIntro = withIntro;
    }

    public MonsterHuntEvent(Model model, TownLocation turnInTown) {
        this(model, turnInTown, true);
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Go to notice board",
                "There's been trouble lately from a savage monster who dwells nearby. " +
                        "I think there's a note on the town notice board about it");
    }

    @Override
    protected void doEvent(Model model) {
        if (withIntro) {
            println("You pass by a noticeboard, a note catches your eye.");
        }
        List<MonsterHuntData> monsterHuntData = findMonsterHuntData(model, turnInTown);
        if (monsterHuntData.isEmpty()) {
            new NoEventState(model).run(model);
            return;
        }
        MonsterHuntData huntData = MyRandom.sample(monsterHuntData);
        MonsterHunt monsterHunt = new MonsterHunt(turnInTown.getPlaceName(),
                Destination.makeDestination(model, huntData.getPosition(), huntData.getDwelling(), "a"),
                huntData.getEnemy(), huntData.getReward());
        println("The note says: 'Monster Hunters Needed! " +
                "The " + turnInTown.getLordTitle() + " of the " + turnInTown.getName() + " is offering a reward of " +
                monsterHunt.getReward() + " gold pieces to the one who can slay the " +
                monsterHunt.getMonster().getName() + " who resides in " + monsterHunt.getDestination() + "'");
        model.getTutorial().monsterHunts(model);
        leaderSay("A " + monsterHunt.getMonster().getName() + " huh? We could take care of it.");
        print("Do you take the note? (Y/N) ");
        if (yesNoInput()) {
            if (withIntro) {
                println("You rip the note off the noticeboard.");
            }
            leaderSay(iOrWeCap() + "'ll deal with the monster terrorizing the people of " + turnInTown.getTownName() + ".");
            randomSayIfPersonality(PersonalityTrait.cowardly, new ArrayList<>(), "Are you sure this is a good idea?");
            randomSayIfPersonality(PersonalityTrait.brave, new ArrayList<>(), "Finally, some real adventuring work!");
            model.getParty().addDestinationTask(new MonsterHuntDestinationTask(monsterHunt));
        } else {
            leaderSay("But, perhaps there's lower hanging fruit elsewhere. " +
                     iOrWeCap() + "'ll let somebody else deal with the " + monsterHunt.getMonster().getName() + ".");
            randomSayIfPersonality(PersonalityTrait.cowardly, new ArrayList<>(), "Exactly. Why risk our necks?");
        }
    }

    private List<MonsterHuntData> findMonsterHuntData(Model model, TownLocation aroundWhere) {
        model.getWorld().dijkstrasByLand(model.getWorld().getPositionForLocation(aroundWhere));
        System.out.println("Finding monster hunt data:");

        List<MonsterHuntData> monsterHunts = new ArrayList<>();

        List<Point> path = model.getWorld().generalShortestPath(0, wh -> wh instanceof SwampHex);
        System.out.println("Nearest swamp is " + path.size() + " hexes away.");
        System.out.println(MyLists.commaAndJoin(path, p -> "(" + p.x + "," + p.y + ") "));
        if (path.size() > DISTANCE_THRESHOLD) {
            System.out.println("  Discarding");
        } else {
            monsterHunts.add(new MonsterHuntData(path.getLast(), "burrow", new GiantSpider('A'), path.size()-1));
        }

        path = model.getWorld().generalShortestPath(0, wh -> wh instanceof DesertHex);
        System.out.println("Nearest desert is " + path.size() + " hexes away.");
        System.out.println(MyLists.commaAndJoin(path, p -> "(" + p.x + "," + p.y + ") "));
        if (path.size() > DISTANCE_THRESHOLD) {
            System.out.println("  Discarding");
        } else {
            monsterHunts.add(new MonsterHuntData(path.getLast(), "hole", new GiantScorpion('A'), path.size()-1));
        }

        path = model.getWorld().generalShortestPath(0, wh -> wh instanceof WoodsHex);
        System.out.println("Nearest woods is " + path.size() + " hexes away.");
        System.out.println(MyLists.commaAndJoin(path, p -> "(" + p.x + "," + p.y + ") "));
        if (path.size() > DISTANCE_THRESHOLD) {
            System.out.println("  Discarding");
        } else {
            monsterHunts.add(new MonsterHuntData(path.getLast(), "cave", new WerewolfEnemy('A'), path.size()-1));
        }

        path = model.getWorld().generalShortestPath(1, wh -> wh instanceof PlainsHex);
        System.out.println("Nearest plains is " + path.size() + " hexes away.");
        System.out.println(MyLists.commaAndJoin(path, p -> "(" + p.x + "," + p.y + ") "));
        if (path.size() > DISTANCE_THRESHOLD) {
            System.out.println("  Discarding");
        } else {
            monsterHunts.add(new MonsterHuntData(path.getLast(), "crypt", new VampireEnemy('A'), path.size()-1));
        }

        path = model.getWorld().generalShortestPath(0, wh -> wh instanceof MountainHex);
        System.out.println("Nearest mountains is " + path.size() + " hexes away.");
        System.out.println(MyLists.commaAndJoin(path, p -> "(" + p.x + "," + p.y + ") "));
        if (path.size() > DISTANCE_THRESHOLD) {
            System.out.println("  Discarding");
        } else {
            monsterHunts.add(new MonsterHuntData(path.getLast(), "nest", DragonEnemy.generateDragon('A'), path.size()-1));
        }

        path = model.getWorld().generalShortestPath(0, wh -> wh instanceof HillsHex);
        System.out.println("Nearest hills is " + path.size() + " hexes away.");
        System.out.println(MyLists.commaAndJoin(path, p -> "(" + p.x + "," + p.y + ") "));
        if (path.size() > DISTANCE_THRESHOLD) {
            System.out.println("  Discarding");
        } else {
            monsterHunts.add(new MonsterHuntData(path.getLast(), "cave", new WerebearEnemy('A'), path.size()-1));
        }
        return monsterHunts;
    }
}
