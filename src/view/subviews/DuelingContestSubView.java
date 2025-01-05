package view.subviews;

import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import model.states.duel.gauges.PowerGauge;
import view.BorderFrame;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.WandSprite;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DuelingContestSubView extends TournamentSubView {
    private final Map<GameCharacter, PowerGauge> gaugeMap;
    private final Map<GameCharacter, Sprite> wandSprites;

    public DuelingContestSubView(List<GameCharacter> duelists,
                                 Map<GameCharacter, MyColors> colorsMap, Map<GameCharacter, PowerGauge> gaugeMap) {
        super(duelists);
        this.gaugeMap = gaugeMap;
        wandSprites = new HashMap<>();
        for (GameCharacter gc : colorsMap.keySet()) {
            wandSprites.put(gc, new WandSprite(gc.getRace().getColor(), RitualSubView.convertColor(colorsMap.get(gc))));
        }
    }


    @Override
    protected String getTitleText(Model model) {
        return "EVENT - DUELING CONTEST";
    }

    @Override
    protected String getTitle(int i) {
        if (i == 0) {
            return "Wait 5 Min";
        }
        return "Next Duel";
    }

    protected void drawCharacterCard(Model model, GameCharacter duelist) {
        Point pos = new Point(X_OFFSET + 4, Y_MAX-10);
        model.getScreenHandler().clearSpace(X_OFFSET, X_MAX, pos.y, Y_MAX);
        BorderFrame.drawString(model.getScreenHandler(), duelist.getName(), pos.x, pos.y, MyColors.LIGHT_GRAY);
        String raceAndClassString = duelist.getRace().getName() + " " + duelist.getCharClass().getShortName() + " Lvl " + duelist.getLevel();
        BorderFrame.drawString(model.getScreenHandler(), raceAndClassString, pos.x, pos.y+1, MyColors.LIGHT_GRAY);
        if (model.getParty().getPartyMembers().contains(duelist)) {
            duelist.getAppearance().drawYourself(model.getScreenHandler(), pos.x, pos.y + 3);
        } else {
            boolean showFace = false;
            if (wandSprites.containsKey(duelist)) {
                Sprite spr = wandSprites.get(duelist);
                model.getScreenHandler().put(pos.x + 8, pos.y + 3, spr);
                showFace = true;
            }
            if (gaugeMap.containsKey(duelist)) {
                gaugeMap.get(duelist).drawGaugeLogo(model.getScreenHandler(), pos.x + 19, pos.y + 8);
                showFace = true;
            }

            if (showFace) {
                duelist.getAppearance().drawYourself(model.getScreenHandler(), pos.x, pos.y + 3);
            } else {
                SIL_APPEARANCE.drawYourself(model.getScreenHandler(), pos.x, pos.y + 3);
            }
        }
    }
}
