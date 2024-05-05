package model.states;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.states.events.NoEventState;
import model.states.events.PersonalityTraitEvent;
import util.MyLists;
import util.MyRandom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class PersonalityEvent extends DailyEventState {
    private static final String KEY_PREFIX = "PersonalityEvent-";
    private PersonalityTraitEvent innerEvent;

    public PersonalityEvent(Model model) {
        super(model);
        System.out.println("Generating Personality Event.");
        List<GameCharacter> candidates = MyLists.filter(model.getParty().getPartyMembers(),
                gc -> !gc.isLeader() && !alreadyDonePersonalityEventFor(model, gc));
        Collections.shuffle(candidates);
        System.out.println("Candidates: " + candidates.size());
        while (!candidates.isEmpty()) {
            GameCharacter gc = candidates.remove(0);
            for (PersonalityTrait pt : PersonalityTrait.values()) {
                if (gc.hasPersonality(pt) && !alreadyUsedTrait(model, pt)) {
                    PersonalityTraitEvent event = pt.makeEvent(model, gc);
                    if (event != null && event.isApplicable(model)) {
                        innerEvent = event;
                        break;
                    }
                }
            }
        }
        if (innerEvent == null) {
            System.out.println("No event generated.");
        }
    }

    private boolean alreadyUsedTrait(Model model, PersonalityTrait pt) {
        return model.getSettings().getMiscFlags().containsKey(KEY_PREFIX + pt.toString());
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
