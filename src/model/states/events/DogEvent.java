package model.states.events;

import model.Model;
import model.characters.appearance.DogAppearance;
import model.characters.appearance.FacialExpression;
import model.combat.conditions.BleedingCondition;
import model.horses.DogHorse;
import model.items.Item;
import model.items.ItemDeck;
import model.items.Prevalence;
import model.items.weapons.*;
import model.states.DailyEventState;
import model.states.GameState;
import util.MyRandom;

import java.util.List;

public class DogEvent extends DailyEventState {
    private final DogAppearance dogPortrait;
    private DailyEventState innerEvent;

    public DogEvent(Model model) {
        super(model);
        this.dogPortrait = new DogAppearance();
    }

    @Override
    public String getDistantDescription() {
        return "a small animal, it's a dog";
    }

    @Override
    protected void doEvent(Model model) {
        if (model.getParty().hasDog()) {
            showExplicitPortrait(model, dogPortrait, "Dog");
            portraitSay("Ruff ruff!");
            leaderSay("What is it " + boyOrGirl(model.getParty().getDog().getGender()) + ", smell something?");
            portraitSay("Ruff ruff!");
            leaderSay("You want " + meOrUs() + " to come with you?");
            portraitSay("Ruff ruff ruff!");
            print("Follow the dog? (Y/N) ");
            if (yesNoInput()) {
                innerEvent = model.getCurrentHex().generateDogEvent(model);
                if (innerEvent != null) {
                    innerEvent.run(model);
                } else {
                    println("You follow the dog for a while but cannot find anything of importance.");
                }
            } else {
                leaderSay("You silly doggy! Come on, it's this way!");
                println("The dog whines but reluctantly follows you.");
            }
        } else {
            findADog(model);
        }
    }

    @Override
    public boolean haveFledCombat() {
        if (innerEvent == null) {
            return super.haveFledCombat();
        }
        return innerEvent.haveFledCombat();
    }

    private void findADog(Model model) {
        showEventCard("Dog", "Up ahead in the road is a dog.");
        showExplicitPortrait(model, dogPortrait, "Dog");
        portraitSay("Ruff ruff!");
        print("Do you try to befriend it (Y) or do you want to chase the dog away (N)? ");
        if (yesNoInput()) {
            int roll = MyRandom.rollD6();
            if (roll == 1) {
                println("As you extend your hand to pet the dog, it bites your hand.");
                leaderSay("Ouch!");
                portraitSay("Grrrr....");
                println("The dog finally release your hand, and scampers away.");
                println(model.getParty().getLeader().getName() + "'s hand is bleeding ");
                model.getParty().getLeader().addCondition(new BleedingCondition());
                leaderSay("Darn mutt!", FacialExpression.angry);
            } else if (model.getParty().getFood() > 4) {
                println("You approach the dog carefully. You bring out some meat from one of your bags and offer it to the dog.");
                if (MyRandom.rollD10() > 3) {
                    println("The dog quickly gobbles up the meat. Then playfully licks your hand.");
                    leaderSay("Hungry eh? Well I'm afraid that's all I can spare for now. We better get going.");
                    println("You start to leave, but the dog will not leave your side.");
                    portraitSay("Ruff ruff!");
                    leaderSay("You wanna come with?", FacialExpression.questioning);
                    portraitSay("Ruff ruff ruff!");
                    DogHorse dog = new DogHorse();
                    leaderSay("Alright " + boyOrGirl(dog.getGender()) + ", you can come along.");
                    println("The dog jumps and scampers about playfully. It seems happy to have found new master.");
                    model.getParty().setDog(dog);
                    model.getTutorial().dog(model);
                } else {
                    println("The dog quickly snatches the bag and runs off! You have lost 5 rations.");
                    model.getParty().addToFood(-5);
                }
            } else {
                println("The dog just runs off.");
            }
        } else {
            leaderSay("Just another stray. Let's keep moving.");
        }
    }

    public static void dogInTheEvening(Model model) {
        new DogEvent(model).doEvening(model);
    }

    private void doEvening(Model model) {
        boolean dogGender = model.getParty().getDog().getGender();
        if (MyRandom.flipCoin()) {
            showExplicitPortrait(model, dogPortrait, "Dog");
            showEventCard("Your dog comes to your side and whimpers a little.");
            leaderSay("Hello there " + boyOrGirl(dogGender) + ".");
            if (CheckForVampireEvent.isVampire(model.getParty().getLeader())) {
                println("The dog snarls and backs away.");
                leaderSay("...");
                return;
            }
            int dieRoll = MyRandom.rollD6();
            if (dieRoll < 3) {
                println("You pet the dog.");
            } else if (dieRoll < 4) {
                println("You ruffle the dog's fur.");
                leaderSay("You're a good dog.");
            } else if (dieRoll < 6) {
                println("You bring out some food for the dog.");
                leaderSay("There you go.");
            } else {
                println("The dog brings you a stick.");
                leaderSay("You want to play catch?");
                portraitSay("Ruff ruff!");
                leaderSay("He he, okay, okay " + boyOrGirl(dogGender) + ". Catch!");
                println("The dog runs off in pursuit of the stick, but when " + heOrShe(dogGender) +
                        " returns, she has something else in " + hisOrHer(dogGender) + " mouth.");
                leaderSay("What do you have there " + boyOrGirl(dogGender) + "?");
                println("The dog drops the wet item at your feet.");
                int dieRoll2 = MyRandom.rollD6();
                if (dieRoll2 < 3) {
                    println("It's an old boot.");
                    leaderSay("Thanks...");
                } else if (dieRoll2 < 4) {
                    println("It's a rather find piece of wood, suitable for crafting.");
                    leaderSay("This actually looks useful. Thanks dog.");
                    println("You have gained 1 material.");
                    model.getParty().getInventory().addToMaterials(1);
                } else if (dieRoll2 < 5) {
                    println("It's a rather odd herb, suitable for alchemy.");
                    leaderSay("This actually looks useful. Thanks dog.");
                    println("You have gained 1 ingredient.");
                    model.getParty().getInventory().addToIngredients(1);
                } else if (dieRoll2 < 6) {
                    println("It's a lockpick!");
                    leaderSay("This actually looks useful. Thanks dog.");
                    println("You have gained 1 lockpick.");
                    model.getParty().getInventory().addToLockpicks(1);
                } else {
                    List<Item> its = model.getItemDeck().draw(List.of(new Club(), new Truncheon(), new LongStaff(), new MagesStaff(),
                            new OrbWand(), new PineWand()), 1, Prevalence.unspecified, 0.01);
                    Item it = its.getFirst();
                    leaderSay("This isn't a stick, it's a " + it.getName().toLowerCase() + "! Where did you find this?");
                    portraitSay("Woof?");
                    println("You gained a " + it.getName());
                    it.addYourself(model.getParty().getInventory());
                }
            }
            portraitSay("Ruff ruff!");
        }
    }
}
