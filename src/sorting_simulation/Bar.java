/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sorting_simulation;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.ImageIcon;

/**
 *
 * @author admin
 */
public class Bar {

    private final int MARGIN = 1;
    private int x, y, width, value;
    private Color color;

    public Bar(int x, int y, int width, int value, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.value = value;
        this.color = color;
    }

    public void drawWithText(Graphics g) {
        //Vẽ thanh
        g.setColor(color);
        g.fillRect(x + MARGIN, y - value - 30, width - MARGIN * 2, value);

        // Vẽ giá trị ở giữa thanh
        g.setColor(Color.BLACK);
        String valueString = Integer.toString(value);

        Font largerFont = g.getFont().deriveFont(Font.BOLD, 13); // Điều chỉnh kích thước font
        g.setFont(largerFont);

        int textWidth = g.getFontMetrics().stringWidth(valueString);
        int textX = x + (width - textWidth) / 2;
        g.drawString(valueString, textX, 500);

    }

    public void drawNoText(Graphics g) {
        g.setColor(color);
        g.fillRect(x + MARGIN, y - value -10, width - MARGIN * 2, value);

    }

    public void clearWithText(Graphics g) {
        // clear the space
        g.setColor(ColorManager.CANVAS_BACKGROUND);
        // g.fillRect(x + MARGIN, y - value - 70, width - MARGIN * 2, value);
        g.fillRect(x + MARGIN, y - value - 30, width - MARGIN * 2, 500);

    }

    public void clearNoText(Graphics g) {
        g.setColor(ColorManager.CANVAS_BACKGROUND);
        // Vẽ thanh
        g.fillRect(x + MARGIN, y - value -10, width - MARGIN * 2, value);

    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
