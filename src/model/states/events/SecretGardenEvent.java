package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.CharacterClass;
import model.enemies.ElfEnemy;
import model.races.Race;
import model.ruins.DungeonMaker;
import model.ruins.DungeonRoom;
import model.ruins.FinalDungeonLevel;
import model.ruins.RuinsDungeon;
import model.ruins.objects.DungeonChest;
import model.ruins.objects.DungeonMonster;
import model.states.DailyEventState;
import model.states.ExploreRuinsState;
import model.states.GameState;
import model.states.RecruitState;
import util.MyRandom;
import view.subviews.*;

import java.util.List;
import java.util.Random;

public class SecretGardenEvent extends DailyEventState {
    public SecretGardenEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("The party comes to a neat hedge.");
        leaderSay("Hey, these bushes look like they've been planted...");
        leaderSay("And here's a gate.");
        print("Do you enter the secret garden? (Y/N) ");
        if (yesNoInput()) {
            RuinsDungeon dungeon =  new RuinsDungeon(DungeonMaker.makeGardenDungeon(3)); // TODO: Size 8 (or more?)
            FinalGardenRoom finalRoom = new FinalGardenRoom();
            FinalDungeonLevel finalLevel = (FinalDungeonLevel) dungeon.getLevel(dungeon.getNumberOfLevels()-1);
            finalLevel.setFinalRoom(finalRoom);

            ExploreRuinsState explore = new ExploreSecretGardenState(model, dungeon, "Secret Garden");
            explore.run(model);
            println("You left the secret garden.");
            //setCurrentTerrainSubview(model);
        } else {
            leaderSay("Perhaps it's better not to trespass here.");
        }
    }

    private static class ExploreSecretGardenState extends ExploreRuinsState {
        public ExploreSecretGardenState(Model model, RuinsDungeon dungeon, String ruinsType) {
            super(model, dungeon, ruinsType);
        }

        @Override
        public CombatTheme getCombatTheme() {
            if (getCurrentLevel() == 1) {
                return new DungeonTheme();
            }
            return new GrassCombatTheme(); // TODO: Dark Woods combat theme.
        }
    }

    private static class FinalGardenRoom extends DungeonRoom {
        private final int roll;

        public FinalGardenRoom() {
            super(3, 3);
            this.roll = 4; //MyRandom.rollD6(); // TODO: Roll die
            if (roll < 3) {
                addObject(new DungeonChest(new Random())); // TODO: Make real dungeon object which overrides entry trigger
            } else if (roll < 5) {
                addObject(new DungeonMonster(List.of(new ElfEnemy('A')))); // TODO: Make real dungeon object which overrides entry trigger
            }  // TODO: an enchanted spring that heals entire party and blesses each party member for 10 days.
            // TODO: Or a grand fairy who improves one item for three characters.
        }

        @Override
        public void entryTrigger(Model model, ExploreRuinsState exploreRuinsState) {
            exploreRuinsState.leaderSay("What's this, a secret room beneath the garden?");
            if (this.roll < 3) {
                exploreRuinsState.leaderSay("A chest!");
                exploreRuinsState.println("The party gains 50 ingredients!");
                model.getParty().getInventory().addToIngredients(50);
            } else {
                exploreRuinsState.leaderSay("An elf? Who might you be?");
                exploreRuinsState.printQuote("Elf", "Thank goodness. You have no idea how long I've been trapped here. " +
                        "I've been forced to live off roots and insects!");
                exploreRuinsState.leaderSay("How did you even get down here in the first place?");
                exploreRuinsState.printQuote("Elf", "My ancestors planted this garden many centuries ago. I come " +
                        "here every so often to check on things. I never imagined I would lose my way in here however. " +
                        "I really would like to get out of here now. You don't suppose I could tag along with you for a while?");
                List<GameCharacter> list = model.getAvailableCharactersOfRace(Race.WOOD_ELF);
                if (list.isEmpty()) {
                    list.add(GameState.makeRandomCharacter(4, Race.WOOD_ELF));
                }
                GameCharacter chara = MyRandom.sample(list);
                chara.setLevel(4);
                chara.setRandomStartingClass();
                RecruitState recruitState = new RecruitState(model, List.of(chara));
                recruitState.setStartingGoldEnabled(true);
                recruitState.run(model);
            }
            exploreRuinsState.print("Press enter to continue.");
            exploreRuinsState.waitForReturn();
            exploreRuinsState.setDungeonExited(true);
        }
    }
}
