package model.states.mine;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.AdvancedAppearance;
import model.states.GameState;
import model.states.events.MinerEvent;
import util.MyPair;
import util.MyRandom;
import view.ScreenHandler;
import view.sprites.AvatarSprite;
import view.sprites.SleepAnimationSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.util.List;

public class MinerMineObject extends MineObject {
    private static final Sprite SLEEP_ANIMATION = new SleepAnimationSprite();
    private final GameCharacter character;
    private boolean interacted;
    private boolean isSleeping;

    public MinerMineObject(GameCharacter character, boolean isSleeping) {
        this.character = character;
        this.isSleeping = isSleeping;
        this.interacted = false;
    }

    @Override
    public void drawYourself(ScreenHandler screenHandler, LogicalMine mine, Point screenPosition) {
        if (isSleeping) {
            AvatarSprite sprite = character.getAvatarSprite();
            sprite.synch();
            screenHandler.register(sprite.getName(), screenPosition, sprite);
            screenHandler.register(SLEEP_ANIMATION.getName(), screenPosition, SLEEP_ANIMATION);
        } else {
            character.drawAvatar(screenHandler, screenPosition.x, screenPosition.y);
        }
    }

    @Override
    public Point gotMovedInto(Model model, AdvancedMineEvent state, Point currentLocation) {
        if (isSleeping) {
            state.println("The " + character.getRace().getName().toLowerCase() + " miner is sleeping.");
            if (MyRandom.flipCoin()) {
                List<MyPair<PersonalityTrait, String>> comments = List.of(
                        new MyPair<>(PersonalityTrait.forgiving, "Mining is hard work... Rest easy friend."),
                        new MyPair<>(PersonalityTrait.critical,"What a slacker..."),
                        new MyPair<>(PersonalityTrait.rude, "Hey! Wake up! You're in my way!"),
                        new MyPair<>(PersonalityTrait.anxious, GameState.heOrSheCap(character.getGender()) + " will probably get angry if we wake " +
                                GameState.himOrHer(character.getGender()) + " up. Let's not."),
                        new MyPair<>(PersonalityTrait.prudish, "Ugh... What a nasty place to lie down in.")
                );
                MyPair<PersonalityTrait, String> comment = MyRandom.sample(comments);
                state.randomSayIfPersonality(comment.first, List.of(model.getParty().getLeader()), comment.second);
            }
            state.leaderSay("Best not to disturb " + GameState.himOrHer(character.getGender()) + ".");
            return currentLocation;
        }
        if (interacted) {
            state.println("The miner is toiling at the rock and ignores you.");
            return currentLocation;
        }
        interacted = true;
        new MineMinerEvent(model, character.getAppearance()).run(model);
        return currentLocation;
    }
}
