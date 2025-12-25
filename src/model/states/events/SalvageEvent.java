package model.states.events;

import model.Model;
import model.characters.PersonalityTrait;
import model.classes.Skill;
import model.states.DailyEventState;
import util.MyRandom;
import view.sprites.MiniPictureSprite;
import view.subviews.MiniPictureSubView;

import java.util.ArrayList;
import java.util.List;

public abstract class SalvageEvent extends DailyEventState {
    private final String text;
    private final int amount;
    private final String title;

    public SalvageEvent(Model model, String title, String text, int amount) {
        super(model);
        this.title = title;
        this.text = text;
        this.amount = amount;
    }

    @Override
    public String getDistantDescription() {
        return "a" + text;
    }

    protected abstract MiniPictureSprite getMinipicSprite();

    protected abstract String getMinipicSubviewText();

    @Override
    protected void doEvent(Model model) {
        model.setSubView(new MiniPictureSubView(model.getSubView(), getMinipicSprite(), getMinipicSubviewText()));
        showEventCard(title, "The party encounters a" + text + ". It seems to be hastily abandoned and there are some crates " +
                        "and package scattered about.");
        model.getParty().randomPartyMemberSay(model, getPartyMemberComments());
        model.getParty().randomPartyMemberSay(model, List.of("Nobody is around. Why don't we see if there is anything to salvage?"));
        boolean success = model.getParty().doCollaborativeSkillCheck(model, this, Skill.Search, 7);
        if (success) {
            println("You thoroughly search the wreckage and are happy to find that it isn't all trash.");
            randomSayIfPersonality(PersonalityTrait.snobby, new ArrayList<>(),
                    "Ugh... this type of work is beneath me.");
            int dieRoll = MyRandom.rollD10();
            if (dieRoll < 4) {
                int rations = MyRandom.rollObD6(3);
                model.getParty().addToFood(rations);
                println("The party finds " + rations + " rations.");
            } else if (dieRoll < 7) {
                int ingredients = MyRandom.rollObD6(2);
                model.getParty().getInventory().addToIngredients(ingredients);
                println("The party finds " + ingredients + " ingredients.");
            } else if (dieRoll < 10) {
                int materials = MyRandom.rollObD6(2);
                model.getParty().getInventory().addToMaterials(materials);
                println("The party finds " + materials + " materials.");
            } else {
                println("The party finds a chest.");
                new ChestEvent(model).findAChest(model);
            }
        }
    }

    protected abstract List<String> getPartyMemberComments();

}
