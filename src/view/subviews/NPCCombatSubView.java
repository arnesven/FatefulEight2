package view.subviews;

import model.Model;
import model.characters.GameCharacter;
import model.items.accessories.ShieldItem;
import model.states.CombatEvent;
import model.states.CombatMatrix;
import view.MyColors;
import view.sprites.Animation;

import java.awt.event.KeyEvent;

public class NPCCombatSubView extends CombatSubView {
    private final GameCharacter topFighter;
    private final GameCharacter bottomFighter;

    public NPCCombatSubView(CombatEvent event, GameCharacter topFighter, GameCharacter bottomFighter) {
        super(event, makeMatrix(topFighter, bottomFighter), new DungeonTheme());
        this.topFighter = topFighter;
        this.bottomFighter = bottomFighter;
        synchAnimations(topFighter);
        synchAnimations(bottomFighter);
    }

    private void synchAnimations(GameCharacter chara) {
        chara.getEquipment().getWeapon().getOnAvatarSprite(chara).synch();
        if (chara.getEquipment().getAccessory() instanceof ShieldItem) {
            ((Animation)((ShieldItem) chara.getEquipment().getAccessory()).getOnAvatarSprite(chara)).synch();
        }
        chara.getAvatarSprite().synch();
    }

    private static CombatMatrix makeMatrix(GameCharacter topFighter, GameCharacter bottomFighter) {
        CombatMatrix matrix = new CombatMatrix();
        matrix.addElement(4, 2, topFighter);
        matrix.addElement(3, 3, bottomFighter);
        return matrix;
    }

    @Override
    public void drawArea(Model model) {
        model.getScreenHandler().clearSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX);
        topFighter.drawYourself(model.getScreenHandler(), X_OFFSET+10, Y_OFFSET, MyColors.LIGHT_GRAY);
        bottomFighter.drawYourself(model.getScreenHandler(), X_OFFSET, Y_MAX-10, MyColors.LIGHT_GRAY);
        drawCombatants(model);
    }

    @Override
    protected String getUnderText(Model model) {
        return "You are watching two combatants fighting at the tournament.";
    }

    @Override
    protected String getTitleText(Model model) {
        return "COMBAT - TOURNAMENT";
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        return false;
    }
}
