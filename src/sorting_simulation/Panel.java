/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sorting_simulation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author admin
 */
public class Panel extends JPanel{

    private String name, text1, text2, text3;
    private int x, y, width, height;
    private boolean hasTextToDraw;

    public Panel(String namePanel) {
        name = namePanel;
//        this.x = x;
//        this.y = y;
//        this.height = height;
//        this.width = width;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (name !=null){
            ImageIcon img = new ImageIcon(getClass().getResource("/Image/" + name + ".png"));
        g.drawImage(img.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
        }
        if (hasTextToDraw != false) {
            // Vẽ chuỗi lên JPanel không trong suốt
            g.setFont(new Font("Aileron", Font.BOLD, 15));
            g.drawString(text1, 10, 132);
            g.drawString(text2, 10, 162);
            g.drawString(text3, 10, 192);
            
        }
    }

    public void drawString(String text1, String text2, String text3) {
        hasTextToDraw = true;
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        this.x=x;
        this.y=y;
        repaint(); // Gọi repaint để vẽ lại JPanel với chuỗi mới
    }
    public void clearString(){
        hasTextToDraw=false;
        repaint();
    }
}

