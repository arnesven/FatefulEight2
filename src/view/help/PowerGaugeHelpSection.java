package view.help;

import model.Model;
import model.states.duel.gauges.PowerGauge;
import view.GameView;
import view.party.DrawableObject;

import java.util.List;

public class PowerGaugeHelpSection extends SubChapterHelpDialog {
    private final PowerGauge gauge;
    private final int textHeight;

    public PowerGaugeHelpSection(GameView view, PowerGauge pg) {
        super(view, pg.getName() + "-Gauge", pg.getHelpText());
        setLevel(2);
        this.gauge = pg;
        this.textHeight = getTextHeight(pg.getHelpText());
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> list = super.buildDecorations(model, xStart, yStart);
        list.add(new DrawableObject(xStart+18, yStart + textHeight + 5) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                gauge.drawSegments(model.getScreenHandler(), x, y);
            }
        });
        return list;
    }
}
