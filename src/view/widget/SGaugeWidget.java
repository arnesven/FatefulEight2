package view.widget;

import model.states.duel.gauges.PowerGaugeSegment;
import model.states.duel.gauges.STypePowerGauge;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.sprites.Sprite16x16;

public class SGaugeWidget extends PowerGaugeWidget {

    private static final Sprite BOTTOM = PowerGaugeSegment.makeGaugeSprite(4, 8, MyColors.BEIGE, MyColors.BLUE);
    private static final Sprite TOP    = PowerGaugeSegment.makeGaugeSprite(5, 10, MyColors.CYAN, MyColors.BLUE);

    private static final Sprite GAUGE_TYPE = new Sprite16x16("gaugetype", "gauge.png", 0x15,
            MyColors.WHITE, MyColors.BLUE, MyColors.GRAY, MyColors.GRAY_RED);
    private final STypePowerGauge gauge;

    public SGaugeWidget(STypePowerGauge sTypePowerGauge) {
        super(sTypePowerGauge);
        this.gauge = sTypePowerGauge;
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
