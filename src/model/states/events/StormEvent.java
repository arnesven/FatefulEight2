package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.Skill;
import model.states.DailyEventState;
import view.sprites.MiniPictureSprite;
import view.subviews.MiniPictureSubView;

import java.util.ArrayList;
import java.util.List;

public class StormEvent extends DailyEventState {
    private static final MiniPictureSprite SPRITE = new MiniPictureSprite(0x31);

    public StormEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        model.setSubView(new MiniPictureSubView(model.getSubView(), SPRITE, "Storm"));
        println("Dark clouds are looming and drops start to fall on your " +
                "heads.");
        model.getParty().randomPartyMemberSay(model, List.of("Looks like bad weather...", "Here it comes...",
                "Get ready to get wet.", "Well it could be worse."));
        println("Soon a strong torrent of rain is coming. The party " +
                "is now severely impeded by the downpour and the strong wind.");
        randomSayIfPersonality(PersonalityTrait.narcissistic, new ArrayList<>(),
                "Why does this always happen to me?");
        model.getParty().randomPartyMemberSay(model, List.of("It's worse.",
                "Even my underwear is wet!", "Can we get a fire going?"));
        print("Do you seek shelter (Y) or trudge on through the storm (N)? ");
        if (yesNoInput()) {
            boolean success = model.getParty().doSoloSkillCheck(model, this, Skill.Survival, 7);
            if (!success) {
                println("Your attempts at seeking shelter have failed and you have lost your way." +
                        " You will have to spend the entire day tomorrow to try to find your way back" +
                        " to the path.");
                model.mustStayInHex(true);
            } else {
                println("You soon find a little cave and set up camp. Out of the wind and with a warm fire going " +
                        "the storm is only heard, not felt.");
                leaderSay("Ah... shelter. Everybody hang your wet clothes up around the fire.");
                randomSayIfPersonality(PersonalityTrait.prude, List.of(model.getParty().getLeader()),
                        "I'm keeping my clothes on, thank you.");
                randomSayIfPersonality(PersonalityTrait.romantic, new ArrayList<>(), "This is actually kind of cozy.");
                randomSayIfPersonality(PersonalityTrait.playful, new ArrayList<>(), "Let's tell ghost stories!");
            }
        } else {
            List<GameCharacter> toRemove = new ArrayList<>();
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                if (gc.getSP() == 0) {
                    if (gc.getHP() > 0) {
                        println(gc.getName() + " suffers 1 damage from the storm.");
                        gc.addToHP(-1);
                        if (gc.isDead()) {
                            toRemove.add(gc);
                        }
                    }
                } else {
                    println(gc.getName() + " loses 1 stamina from the storm.");
                    gc.addToSP(-1);
                }
                model.getParty().getHorseHandler().someHorsesRunAway(model);
            }
            for (GameCharacter gc : toRemove) {
                printAlert(gc.getName() + " has died from the exertion. Press enter to continue.");
                waitForReturn();
                model.getParty().remove(gc, true, false, 0);
            }

        }
    }
}
