package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.combat.TownCombatTheme;
import model.enemies.Enemy;
import model.enemies.GhostEnemy;
import model.enemies.GoblinSpearman;
import model.states.DailyEventState;
import model.states.GameState;
import util.MyRandom;
import view.sprites.MiniPictureSprite;
import view.subviews.MiniPictureSubView;

import java.util.ArrayList;
import java.util.List;

public class GhostTownEvent extends DailyEventState {
    private static final MiniPictureSprite SPRITE = new MiniPictureSprite(0x00);
    private OutpostEvent outpostEvent;
    private MiniPictureSubView miniSubView;

    public GhostTownEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        this.miniSubView = new MiniPictureSubView(model.getSubView(), SPRITE, "An abandoned town?");
        model.setSubView(miniSubView);
        println("The party comes to what seems to be an abandoned town. What would you like to do?");
        int attackChance = MyRandom.randInt(5);
        int chosen;
        do {
            chosen = multipleOptionArrowMenu(model, 26, 30,
                    List.of("Search abandoned houses",
                            "Spend the night in town",
                            "Leave town"));
            if (chosen == 0) {
                leaderSay("Come on gang, let's see what we can find in these empty houses.");
                DailyEventState event = MyRandom.sample(List.of(
                        new HermitEvent(model),
                        new AbandonedShackEvent(model),
                        new DeadBodyEvent(model)
                        ));
                attackChance++;
                if (MyRandom.rollD10() < attackChance) {
                    encounterGhosts(model);
                } else {
                    event.doTheEvent(model);
                }
            } else if (chosen == 1) {
                if (MyRandom.flipCoin()) {
                    leaderSay("Perhaps this town isn't completely abandoned?");
                    outpostEvent = new OutpostEvent(model);
                    outpostEvent.doEvent(model);
                } else {
                    println("The town seems completely abandoned. " +
                            "The party finds a house which is mostly intact and sets up camp in the largest room.");
                    model.getParty().randomPartyMemberSay(model, List.of("This is kind of spooky."));
                    model.getParty().randomPartyMemberSay(model, List.of("I'm sure there's nothing to be afraid of."));
                    model.getParty().randomPartyMemberSay(model, List.of("Wait... I'm getting an odd feeling. Like a shiver down my spine..."));
                    encounterGhosts(model);
                }
            } else if (chosen == 2) {
                leaderSay("Let's get out of here. This place gives me the creeps.");
                GameCharacter gc = model.getParty().getRandomPartyMember();
                if (gc != model.getParty().getLeader()) {
                    partyMemberSay(gc, "Can't argue with that...");
                }
            }
        } while (chosen == 0);
    }

    private void encounterGhosts(Model model) {
        println("The party encounters some ghosts!");
        List<Enemy> enemy = new ArrayList<>();
        for (int i = getSuggestedNumberOfEnemies(model, new GhostEnemy('A')); i > 0; --i) {
            enemy.add(new GhostEnemy('A'));
        }
        runCombat(enemy, new TownCombatTheme(), true);
        model.getParty().randomPartyMemberSay(model, List.of("That was kind of freaky...",
                "That was scary.", "G-g-g-g ghosts!", "Yikes! This is literally a ghost town.",
                "I'm not scared...", "We must have disturbed those spirits.",
                "Begone you foul spectres!"));
        model.setSubView(miniSubView);
    }

    @Override
    protected GameState getEveningState(Model model) {
        if (outpostEvent != null) {
            return outpostEvent.getEveningState(model);
        }
        return super.getEveningState(model);
    }
}
