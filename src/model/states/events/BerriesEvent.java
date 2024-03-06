package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.conditions.PoisonCondition;
import model.states.DailyEventState;
import util.MyRandom;
import view.sprites.MiniPictureSprite;
import view.subviews.MiniPictureSubView;

import java.util.List;

public class BerriesEvent extends DailyEventState {
    private static final MiniPictureSprite SPRITE = new MiniPictureSprite(0x11);

    public BerriesEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        model.setSubView(new MiniPictureSubView(model.getSubView(), SPRITE, "Berries"));
        println("The party encounters a large thicket, rich with bright red berries.");
        model.getParty().randomPartyMemberSay(model, List.of("Those look yummy!"));
        randomSayIfPersonality(PersonalityTrait.naive, List.of(model.getParty().getLeader()),
                "I'm sure their not poisonous. They have a beautiful color. " +
                        "How could something beautiful be poisonous?");
        print("Do you wish to pick the berries? (Y/N) ");
        if (!yesNoInput()) {
            return;
        }

        boolean poisonous = MyRandom.randInt(3) == 0;
        if (!poisonous) {
            println("The party happily eats and picks as many berries as they can carry.");
            model.getParty().addToFood(2*model.getParty().size());
        } else {
            GameCharacter survivalist = null;
            int best = Integer.MIN_VALUE;
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                if (gc.getRankForSkill(Skill.Survival) > best) {
                    survivalist = gc;
                }
            }
            SkillCheckResult result = survivalist.testSkill(Skill.Survival, 6);
            if (result.isSuccessful()) {
                println(survivalist.getName() + "'s survival instinct kicks in. (Survival " + result.asString() + ")");
                model.getParty().partyMemberSay(model, survivalist, List.of("Wait! Those are poisonous!"));
                println("With great disappointment, the party turns away from the bushes and continues on their journey.");
            } else {
                println("The party happily eats the berries but quickly realizes their error.");
                model.getParty().randomPartyMemberSay(model, List.of("Uh... I don't feel so good."));
                println("Each party takes damage from eating poisonous berries.");
                for (GameCharacter gc : model.getParty().getPartyMembers()) {
                    if (MyRandom.rollD10() < 3) {
                        gc.addCondition(new PoisonCondition());
                        println(gc.getName() + " has been poisoned by the bad berries.");
                    } else {
                        gc.addToHP(-2);
                    }
                }
                removeKilledPartyMembers(model, false);
            }
        }
    }
}
