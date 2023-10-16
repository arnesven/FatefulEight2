package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.StandardCombatLoot;
import model.items.Lockpick;
import model.states.DailyEventState;
import sound.SoundEffects;

import java.util.List;

public class ChestEvent extends DailyEventState {
    public ChestEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        SkillCheckResult result = null;
        GameCharacter performer = null;
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            result = gc.testSkill(Skill.Perception, 7);
            if (result.isSuccessful()) {
                performer = gc;
                break;
            }
        }
        if (!result.isSuccessful()) {
            new NoEventState(model).doEvent(model);
            return;
        }

        println(performer.getName() + " spots something! (Perception " + result.asString() + ")");
        if (model.getParty().size() > 1) {
            model.getParty().partyMemberSay(model, performer, "It's a chest. Help me dig it up!");
        } else {
            model.getParty().randomPartyMemberSay(model, List.of("It's a chest!"));
        }
        findAChest(model);
    }

    public void findAChest(Model model) {
        println("The chest appears to be locked.");
        boolean success = model.getParty().doSoloSkillCheck(model, this, Skill.Security,
                Lockpick.askToUseLockpick(model, this, 7));
        if (success) {
            model.getParty().randomPartyMemberSay(model, List.of("It's opening!"));
            SoundEffects.playUnlock();
            int gold = 0;
            for (int i = 0; i < 5; ++i) {
                StandardCombatLoot loot = new StandardCombatLoot(model);
                if (!loot.getText().equals("")) {
                    println("The party finds " + loot.getText());
                }
                gold += loot.getGold();
                loot.giveYourself(model.getParty());
            }
            println("The party finds " + gold + " gold!");
            model.getParty().randomPartyMemberSay(model, List.of("Lucky!",
                    "One man's treasure is another man's... well treasure.",
                    "Who left this here?", "Free stuff!"));
        } else {
            model.getParty().randomPartyMemberSay(model, List.of("Well, we're not getting into that thing. We had better just forget about it."));
            model.getParty().randomPartyMemberSay(model, List.of("What a shame!", "But I really wanted to know what was inside!"));
        }
    }
}
