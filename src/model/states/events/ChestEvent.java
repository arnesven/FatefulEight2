package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.loot.StandardCombatLoot;
import model.states.DailyEventState;
import model.states.GameState;
import sound.SoundEffects;
import util.MyPair;

import java.util.List;

public class ChestEvent extends DailyEventState {
    public ChestEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        GameCharacter performer = null;
        MyPair<SkillCheckResult, GameCharacter> result = doPassiveSkillCheck(Skill.Perception, 7);
        if (result.first.isSuccessful()) {
            performer = result.second;
        } else {
            new NoEventState(model).doEvent(model);
            return;
        }

        println(performer.getName() + " spots something! (Perception " + result.first.asString() + ")");
        if (model.getParty().size() > 1) {
            model.getParty().partyMemberSay(model, performer, "It's a chest. Help me dig it up!");
        } else {
            model.getParty().randomPartyMemberSay(model, List.of("It's a chest!"));
        }
        findAChest(model);
    }

    public void findAChest(Model model) {
        println("The chest appears to be locked.");
        boolean success = model.getParty().doSoloLockpickCheck(model, this, 7);

        if (success) {
            chestOpens(model, this, 5);
        } else {
            model.getParty().randomPartyMemberSay(model, List.of("Well, we're not getting into that thing. We had better just forget about it."));
            model.getParty().randomPartyMemberSay(model, List.of("What a shame!", "But I really wanted to know what was inside!"));
        }
    }

    public static void chestOpens(Model model, GameState state, int loots) {
        model.getParty().randomPartyMemberSay(model, List.of("It's opening!"));
        SoundEffects.playUnlock();
        int gold = 0;
        for (int i = 0; i < loots; ++i) {
            StandardCombatLoot loot = new StandardCombatLoot(model);
            if (!loot.getText().equals("")) {
                state.println("The party finds " + loot.getText());
            }
            gold += loot.getGold();
            loot.giveYourself(model.getParty());
        }
        state.println("The party finds " + gold + " gold!");
        model.getParty().randomPartyMemberSay(model, List.of("Lucky!",
                "One man's treasure is another man's... well treasure.",
                "Who left this here?", "Free stuff!"));
    }
}
