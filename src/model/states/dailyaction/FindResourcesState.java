package model.states.dailyaction;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.states.GameState;
import util.MyRandom;

import java.util.List;

public class FindResourcesState extends GameState {
    private static final int SCORE_QUOTIENT = 12;
    private static final int MAX_COLLECT_TIMES = 3;

    public FindResourcesState(Model model) {
        super(model);
    }

    @Override
    public GameState run(Model model) {
        println("What type of resource would you like to look for?");
        model.getTutorial().collectResources(model);
        int choice = multipleOptionArrowMenu(model, 26, 12, List.of("Ingredients", "Materials", "Neither"));
        if (choice == 0) {
            model.getParty().randomPartyMemberSay(model, List.of("Perhaps there are some good ingredients growing around here."));
            return collectIngredients(model);
        }
        if (choice == 1) {
            model.getParty().randomPartyMemberSay(model, List.of("This could be a good spot to look for raw crafting materials."));
            return collectMaterials(model);
        }
        return model.getCurrentHex().getDailyActionState(model);
    }

    private GameState collectMaterials(Model model) {
        return generalCollectResources(model, "materials", Skill.Search, Skill.Labor,
                model.getCurrentHex().getResourcePrevalences().materials);
    }

    private GameState collectIngredients(Model model) {
        return generalCollectResources(model, "ingredients", Skill.Perception, Skill.Search,
                model.getCurrentHex().getResourcePrevalences().ingredients);

    }

    private GameState generalCollectResources(Model model, String resourceType, Skill skill1, Skill skill2, int prevalence) {
        println("The party spends some time searching the " + model.getCurrentHex().getTerrainName().toLowerCase() + " for " + resourceType + ".");
        for (int i = 0; i < MAX_COLLECT_TIMES; ++i) {
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                SkillCheckResult skill1Result = gc.testSkill(model, skill1);
                SkillCheckResult skill2Result = gc.testSkill(model, skill2);
                int score = skill1Result.getModifiedRoll() + skill2Result.getModifiedRoll();
                int resourcesFound = prevalence * (score / SCORE_QUOTIENT);
                if (resourcesFound == 0) {
                    println(gc.getFirstName() + " didn't find anything.");
                } else {
                    println(gc.getFirstName() + " found " + resourcesFound +
                            " " + resourceType + ". (" + skill1.getName() + " " + skill1Result.asString() + ", " +
                            skill2.getName()+ " " + skill2Result.asString() + ")");
                    model.getParty().getInventory().addToIngredients(resourcesFound);
                }
                if (gc.getSP() > 0 && MyRandom.rollD6() == 1) {
                    println(gc.getFirstName() + " lost 1 Stamina while searching for " + resourceType + ".");
                }
            }
            if (i == 0) {
                print("If you wish to travel today you must leave now. Do you want to continue looking for " + resourceType + "? (Y/N) ");
                if (!yesNoInput()) {
                    return model.getCurrentHex().getDailyActionState(model);
                }
            }
        }
        return model.getCurrentHex().getEveningState(model, false, false);
    }
}
