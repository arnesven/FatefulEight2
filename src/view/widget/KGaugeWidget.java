package view.widget;

import model.states.duel.gauges.KTypePowerGauge;
import model.states.duel.gauges.PowerGaugeSegment;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.sprites.Sprite16x16;

public class KGaugeWidget extends PowerGaugeWidget {

    private static final Sprite TOP    = PowerGaugeSegment.makeGaugeSprite(5, 10, MyColors.CYAN, MyColors.BLUE);
    private static final Sprite BOTTOM = PowerGaugeSegment.makeGaugeSprite(4, 0,MyColors.BEIGE, MyColors.BLUE);
    private static final Sprite GAUGE_TYPE = new Sprite16x16("gaugetype", "gauge.png", 0x46,
            MyColors.WHITE, MyColors.BLUE, MyColors.GRAY, MyColors.GRAY_RED);
    private final KTypePowerGauge gauge;

    public KGaugeWidget(KTypePowerGauge kTypePowerGauge) {
        super(kTypePowerGauge);
        this.gauge = kTypePowerGauge;
    }

    @Override
    protected void drawGaugeLogo(ScreenHandler screenHandler, int xStart, int yStart) {
        screenHandler.put(xStart-3, yStart-3, GAUGE_TYPE);
        screenHandler.put(xStart-1, yStart-3, GAUGE_LEFT);
        screenHandler.put(xStart+1, yStart-3, GAUGE_RIGHT);
    }

    @Override
    protected void drawBottom(ScreenHandler screenHandler, int xStart, int yStart) {
        screenHandler.put(xStart, yStart + gauge.getNoOfSegments(), BOTTOM);
    }

    @Override
    protected void drawTop(ScreenHandler screenHandler, int xStart, int yStart) {
        screenHandler.put(xStart, yStart - 1, TOP);
    }
}
