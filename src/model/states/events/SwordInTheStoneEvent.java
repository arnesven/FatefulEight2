package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.items.Item;
import model.items.weapons.CalixaberSword;
import model.items.weapons.Weapon;
import model.map.PlainsHex;
import model.map.WoodsHex;
import view.sprites.MiniPictureSprite;
import view.subviews.MiniPictureSubView;

public class SwordInTheStoneEvent extends PersonalityTraitEvent {

    private static final MiniPictureSprite SPRITE = new MiniPictureSprite(0x42);

    public SwordInTheStoneEvent(Model model, PersonalityTrait personalityTrait, GameCharacter mainCharacter) {
        super(model, personalityTrait, mainCharacter);
    }

    @Override
    public boolean isApplicable(Model model) {
        return model.getCurrentHex() instanceof PlainsHex || model.getCurrentHex() instanceof WoodsHex;
    }

    @Override
    protected void doEvent(Model model) {
        model.setSubView(new MiniPictureSubView(model.getSubView(), SPRITE, "Sword in the Stone"));

        if (model.getCurrentHex() instanceof PlainsHex) {
            print("In the middle of a vast plain");
        } else {
            print("In a peaceful clearing");
        }
        println(", the party encounters a large stone slab. Its perfect geometric shape seem " +
                "to indicate that it has been carved, but who, or what, could have brought it here? " +
                "Even more quixotically, a sword is sticking out of the top of the slab, as " +
                "if some otherworldly force had driven it down into the rock.");
        leaderSay("Hey, something is written here.");
        println("There are marks along the bottom of the slab.");
        leaderSay("'HE WHO IS MOST PURE OF HEART, GRASP THE IT AND THE BLADE SHALL COME FREE.'");
        if (model.getParty().size() > 1) {
            GameCharacter rando = model.getParty().getRandomPartyMember(model.getParty().getLeader());
            partyMemberSay(rando, "I guess we could try...");
        }
        do {
            print("Who should try to pull out the sword?");
            GameCharacter candidate = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
            println(candidate.getFirstName() + " gently puts " + hisOrHer(candidate.getGender()) + " on the hilt.");
            println("Then with full force " + heOrShe(candidate.getGender()) + " pulls...");
            if (candidate == getMainCharacter()) {
                println("... and the sword slides right out.");
                partyMemberSay(candidate, "Huh, it wasn't even stuck in there very hard.");
                leaderSay("Unbelievable...");
                Weapon it = new CalixaberSword();
                println(candidate.getName() + " retrieved the " + it.getName());
                model.getParty().getInventory().add(it);
                return;
            } else {
                println("... and pulls...");
                println("... and pulls...");
                println("But the sword does not budge.");
                if (candidate.getSP() > 0) {
                    candidate.addToSP(-1);
                    println(candidate.getName() + " exhausts 1 Stamina Point.");
                } else if (candidate.getHP() > 0) {
                    candidate.addToHP(-1);
                    println(candidate.getName() + " has strained " + himOrHer(candidate.getGender()) +
                            "self and loses 1 Health Point.");
                }
                partyMemberSay(candidate, "Ugh... it's... it's like its fused with the rock.");
            }
            print("Do you want keep trying to pull out the sword? (Y/N) ");
        } while (yesNoInput());
        leaderSay("Let's just give up. It's not worth breaking our backs over. And the sword is probably not very good anyway.");
        println("The party walk away from the sword in the stone.");
    }
}
