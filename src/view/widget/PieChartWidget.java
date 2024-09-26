package view.widget;

import model.Model;
import model.characters.GameCharacter;
import util.MyLists;
import util.MyTriplet;
import view.BorderFrame;
import view.MyColors;
import view.PartyCompositionView;
import view.sprites.PieChartSprite;

import java.awt.*;
import java.util.*;
import java.util.List;

public class PieChartWidget<E> {
    private final int size;
    private PieChartSprite sprite;
    private List<MyTriplet<Double, String, MyColors>> slices;

    public PieChartWidget(Model model, PieChartStrategy<E> strategy, int size) {
        this.size = size;
        this.slices = calcSlices(countFrequencies(model.getParty().getPartyMembers(), strategy),
                strategy.getColorTable(), model.getParty().size());
        this.sprite = new PieChartSprite(size*8, size*8,
                MyLists.transform(slices, trip -> trip.first),
                MyLists.transform(slices, trip -> trip.third));
    }

    private static List<MyTriplet<Double, String, MyColors>> calcSlices(Map<String, Integer> freqTable, Map<String, MyColors> colorTable, int total) {
        List<Map.Entry<String, Integer>> mapAsList = new ArrayList<>(freqTable.entrySet());
        mapAsList.sort(Comparator.comparingInt(Map.Entry::getValue));
        List<MyTriplet<Double, String, MyColors>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> e : mapAsList) {
            if (e.getValue() > 0) {
                result.add(new MyTriplet<>(e.getValue() / (double) total, e.getKey(), colorTable.get(e.getKey())));
            }
        }
        return result;
    }

    private <E> Map<String, Integer> countFrequencies(List<GameCharacter> partyMembers, PieChartStrategy<E> strategy) {
        Map<String, Integer> counts = new HashMap<>();
        for (Map.Entry<String, MyColors> entry : strategy.getColorTable().entrySet()) {
            counts.put(entry.getKey(), 0);
        }
        for (GameCharacter gc : partyMembers) {
            String raceName = strategy.getNameGetter().getString(strategy.getInner(gc));
            if (counts.containsKey(raceName)) {
                counts.put(raceName, counts.get(raceName) + 1);
            } else {
                counts.put(strategy.getOtherString(), counts.get(strategy.getOtherString()) + 1);
            }
        }
        return counts;
    }

    public void drawYourself(Model model, int x, int y) {
        Point chartPoint = new Point(x, y);
        model.getScreenHandler().register(sprite.getName(), chartPoint, sprite);

        int radius = size / 2 + 1;
        double aStart = Math.PI / 2;
        Point origin = new Point(chartPoint.x + size / 2, chartPoint.y + size / 2);
        for (MyTriplet<Double, String, MyColors> slice : slices) {
            double currSlice = Math.PI * 2 * slice.first;
            int dx = (int)Math.round(radius * Math.cos(aStart + currSlice/2.0));
            int dy = (int)Math.round(radius * Math.sin(aStart + currSlice/2.0));
            int finalX = origin.x - dx;
            int finalY = origin.y - dy;
            if (dx > 0) {
                finalX -= slice.second.length();
            } else if (dx == 0) {
                finalX -= slice.second.length() / 2;
            }
            BorderFrame.drawString(model.getScreenHandler(), slice.second, finalX, finalY, slice.third, MyColors.BLUE);
            aStart += currSlice;
        }
    }
}
