package model.states;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.states.events.NoEventState;
import model.states.events.PersonalityTraitEvent;
import util.MyRandom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PersonalityEvent extends DailyEventState {
    private static final String KEY_PREFIX = "PersonalityEvent-";
    private PersonalityTraitEvent innerEvent;

    public PersonalityEvent(Model model) {
        super(model);
        PersonalityTrait trait = null;
        System.out.println("Generating Personality Event.");
        for (int i = 0; i < 1000; ++i) {
            PersonalityTrait tmp = MyRandom.sample(List.of(PersonalityTrait.values()));
            System.out.println("Trait " + tmp.toString());
            if (!model.getSettings().getMiscFlags().containsKey(KEY_PREFIX + tmp.toString())) {
                trait = tmp;
                break;
            } else {
                System.out.println("...already used.");
            }
        }

        if (trait != null) {
            List<GameCharacter> chars = new ArrayList<>(model.getParty().getPartyMembers());
            Collections.shuffle(chars);
            GameCharacter mainCharacter = null;
            for (GameCharacter gc : chars) {
                System.out.println("Character " + gc.getName());
                if (model.getParty().getLeader() != gc && gc.hasPersonality(trait) &&
                        !alreadyDonePersonalityEventFor(model, gc)) {
                    mainCharacter = gc;
                    break;
                } else {
                    System.out.println("Does not have trait " + trait.toString() + ", is leader, " +
                            "or have already done personality trait for that character.");
                }
            }
            if (mainCharacter != null) {
                this.innerEvent = trait.makeEvent(model, mainCharacter);
            } else {
                System.out.println("No eligible character found.");
            }
        } else {
            System.out.println("No personality trait found.");
        }
    }

    private boolean alreadyDonePersonalityEventFor(Model model, GameCharacter gc) {
        return model.getSettings().getMiscFlags().containsKey(KEY_PREFIX + gc.getName());
    }

    @Override
    protected void doEvent(Model model) {
        if (innerEvent == null || !innerEvent.isApplicable(model)) {
            System.out.println("Personality Event failed to generate or is not applicable.");
            new NoEventState(model).doTheEvent(model);
            return;
        }
        innerEvent.doTheEvent(model);
        model.getSettings().getMiscFlags().put(KEY_PREFIX + innerEvent.getMainCharacter().getName(), true);
        model.getSettings().getMiscFlags().put(KEY_PREFIX + innerEvent.getTrait().toString(), true);
    }

    @Override
    protected boolean isFreeLodging() {
        if (innerEvent == null) {
            return false;
        }
        return innerEvent.isFreeLodging();
    }

    @Override
    protected boolean isFreeRations() {
        if (innerEvent == null) {
            return false;
        }
        return innerEvent.isFreeRations();
    }

    @Override
    public boolean haveFledCombat() {
        if (innerEvent == null) {
            return false;
        }
        return innerEvent.haveFledCombat();
    }
}
