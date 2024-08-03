package model.states.events;

import model.Model;
import model.characters.PersonalityTrait;
import model.classes.Skill;
import model.enemies.Enemy;
import model.enemies.OrcBoarRiderEnemy;
import model.enemies.OrcArcherEnemy;
import model.enemies.OrcWarrior;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class OrcBandEvent extends DailyEventState {
    public OrcBandEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("The party is taking a little rest by the side of the road " +
                "when they hear footsteps approaching, many footsteps. " +
                "It's a whole band of orcs! It may be possible for the party " +
                "to hide behind some bushes and remain undetected.");
        randomSayIfPersonality(PersonalityTrait.cowardly, List.of(model.getParty().getLeader()), "We're doomed!");
        randomSayIfPersonality(PersonalityTrait.aggressive, List.of(model.getParty().getLeader()), "Come on. We can take them!");
        boolean result = model.getParty().doCollectiveSkillCheck(model, this, Skill.Sneak, 5);
        if (result) {
            randomSayIfPersonality(PersonalityTrait.brave, List.of(model.getParty().getLeader()),
                    "Maybe if we follow them, we can find a good spot to ambush them?");
            print("You stay hidden as the throng passes by. Do you wish to follow them? (Y/N) ");
            if (yesNoInput()) {
                println("You follow the orcs for a bit.");
                randomSayIfPersonality(PersonalityTrait.anxious, List.of(model.getParty().getLeader()),
                        "This is not a good idea.");
                int dieRoll = MyRandom.rollD10();
                if (dieRoll < 3) {
                    new OrcishStrongholdEvent(model).doEvent(model);
                } else if (dieRoll < 5) {
                    interact(model);
                } else {
                    println("After a while the band approaches a gorge.");
                    model.getParty().randomPartyMemberSay(model, List.of("If we hurry a little we could ambush the orcs in the gorge."));
                    print("Do you wish to ambush the orcs (surprise attack)? (Y/N) ");
                    if (yesNoInput()) {
                        doCombat(model, true);
                    } else {
                        new OrcishStrongholdEvent(model).doEvent(model);
                    }
                }
            }
        } else {
            interact(model);
        }
    }

    private void interact(Model model) {
        println("The orcs discover you and are very annoyed by your presence.");
        if (new OrcsEvent(model).interactWithOrcs(model)) {
            println("The orcs let you continue on your journey.");
        } else {
            doCombat(model, false);
        }
    }

    private void doCombat(Model model, boolean surprise) {
        List<Enemy> enemies = new ArrayList<>();
        for (int i = 0; i < 6; ++i) {
            enemies.add(makeRandomOrcEnemy());
        }
        if (surprise) {
            runSurpriseCombat(enemies, model.getCurrentHex().getCombatTheme(), true);
        } else {
            runCombat(enemies, model.getCurrentHex().getCombatTheme(), true);
        }
        possiblyGetHorsesAfterCombat("orcs", 4);
    }

    private Enemy makeRandomOrcEnemy() {
        return MyRandom.sample(List.of(new OrcWarrior('C'),
                new OrcArcherEnemy('A'),
                new OrcBoarRiderEnemy('B')));
    }
}
