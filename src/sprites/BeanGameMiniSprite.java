package sprites;

import model.states.beangame.BeanGameBoard;
import model.states.beangame.BeanGameBoardMaker;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.SpriteCache;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class BeanGameMiniSprite extends Sprite {

    private static int counter = 0;
    private final BeanGameBoard beanBoard;

    private static int getCounter() {
        return counter++;
    }

    public BeanGameMiniSprite(BeanGameBoard beanBoard) {
        super("beangamemini" + getCounter(), "splash.png", 0, 0,
                beanBoard.boardWidth() * 2 + 2, beanBoard.boardLength() * 2 + 1, new ArrayList<>());
        this.beanBoard = beanBoard;
        SpriteCache.invalidate(this);
        try {
            getImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected BufferedImage internalGetImage() throws IOException {
        BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        g.setColor(BeanGameBoardMaker.FRAME_COLOR.toAwtColor());
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(BeanGameBoardMaker.BACK_COLOR.toAwtColor());
        g.fillRect(1, 0, getWidth()-2, getHeight()-1);
        if (beanBoard != null) {
            //g.setFont(new Font("Arial", Font.PLAIN, 10));
            for (int pocket = 0; pocket < beanBoard.getNumberOfPockets(); ++pocket) {
                if (beanBoard.getPrize(pocket) > 0) {
                    int x = pocket * beanBoard.pocketLength() * 2;
                    g.setColor(MyColors.BEIGE.toAwtColor());
                    g.fillRect(x + 1, getHeight() - 9, 7, 9);
                    g.setColor(MyColors.BLACK.toAwtColor());
                    g.drawString("" + beanBoard.getPrize(pocket),
                            x + 1, getHeight());
                }
            }

            for (int row = 0; row < beanBoard.boardLength(); ++row) {
                for (int col = 0; col < beanBoard.boardWidth(); ++col) {
                    Point pos = new Point(1 + col * 2, row * 2);
                    if (beanBoard.isAPin(col, row)) {
                        g.setColor(BeanGameBoardMaker.getColor(beanBoard.getCell(col, row)).toAwtColor());
                        g.drawLine(pos.x, pos.y, pos.x, pos.y + 1);
                    }
                }
            }

        }

        return img;
    }
}
