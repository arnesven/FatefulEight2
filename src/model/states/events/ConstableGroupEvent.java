package model.states.events;

import model.Model;
import model.characters.PersonalityTrait;
import model.classes.Classes;
import model.enemies.ConstableEnemy;
import model.enemies.Enemy;
import model.enemies.ToughConstableEnemy;
import model.states.DailyEventState;
import model.states.GameState;
import util.MyLists;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class ConstableGroupEvent extends DailyEventState {

    public ConstableGroupEvent(Model model) {
        super(model);
    }

    @Override
    public boolean exclusiveToOriginalWorld() {
        return true;
    }

    @Override
    protected void doEvent(Model model) {
        showEventCard("You see a group of constables approaching you on the street.");
        print("Do you run away? (Y/N) ");
        if (yesNoInput()) {
            setFledCombat(true);
            return;
        }
        showRandomPortrait(model, Classes.CONSTABLE, "Constable");
        portraitSay("Hey you lot! You're wanted criminals! You're under arrest!");
        print("Do you resist arrest? (Y/N) ");
        if (yesNoInput()) {
            resistArrest(model);
            if (model.getParty().isWipedOut()) {
                return;
            }
            setCurrentTerrainSubview(model);
            println("The commotion has attracted the attention of a large mob of constables and villagers.");
            leaderSay("We'd better get out of here if we want to keep our skins.");
            setFledCombat(true);
        } else {
            leaderSay("Fine. " + iOrWe() + " give up.");
            portraitSay("Alright. No funny business now!");
            println("The constables take your weapons and quickly bind your hands together with rope.");
            new ConstableEvent(model).goToJail(model);
        }

    }

    private void resistArrest(Model model) {
        boolean didSay = randomSayIfPersonality(PersonalityTrait.lawful, new ArrayList<>(),
                MyRandom.sample(List.of("Are we really doing this?", "This is wrong", "I don't feel good about this.")));
        didSay = randomSayIfPersonality(PersonalityTrait.mischievous, new ArrayList<>(), "Let's get them!") || didSay;
        if (!didSay) {
            model.getParty().randomPartyMemberSay(model, List.of("We're in over our heads here..."));
        }
        List<Enemy> enemies = new ArrayList<>();
        int number = Math.max(2, GameState.getSuggestedNumberOfEnemies(model, new ToughConstableEnemy('A')));
        for (int i = 0; i < number; i++) {
            enemies.add(new ToughConstableEnemy('A'));
        }
        runCombat(enemies);
        if (model.getParty().isWipedOut()) {
            return;
        }
        int kills = MyLists.intAccumulate(enemies, e -> e.isDead() ? 1 : 0);
        if (kills > 0) {
            GeneralInteractionEvent.addMurdersToNotoriety(model, this, kills);
        } else {
            GeneralInteractionEvent.addToNotoriety(model, this, 30);
        }
    }
}
