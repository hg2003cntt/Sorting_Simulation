/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sorting_simulation;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

/**
 *
 * @author admin
 */
public class ComboBoxPanel extends JPanel {

    private static final int BUTTON_WIDTH = 175, BUTTON_HEIGHT = 40;
    private ButtonPanel[] buttons;
    ButtonPanel.SortButtonListener listener;

    public ComboBoxPanel(ButtonPanel.SortButtonListener listener) {
        super();
        this.listener = listener;
        buttons = new ButtonPanel[5];
//        for (int i=0; i<buttons.length; i++){
//            buttons[i] = new ButtonPanel();
//        }
        buttons[0] = new ButtonPanel(this.listener, "selection", 1);
        buttons[1] = new ButtonPanel(this.listener, "insertion", 2);
        buttons[2] = new ButtonPanel(this.listener, "bubble", 3);
        buttons[3] = new ButtonPanel(this.listener, "quick", 4);
        buttons[4] = new ButtonPanel(this.listener, "heap", 5);
        //buttons[5] = new ButtonPanel(this.listener, "merge", 6);
        setLayout(null);
        Panel background = new Panel("combo_panel");
        background.setBounds(0, 0, 175, 234);
        background.setLayout(null);
        // Đặt vị trí các option
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setBounds(0, 30 + i * 38, 175, 38);
            background.add(buttons[i]);
            background.setComponentZOrder(buttons[i], 0);
            // Thêm MouseListener cho từng nút
           
        }
        add(background);
        setOpaque(false);
        //Thêm MouseListener cho toàn bộ ComboBoxPanel
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("ComboBoxPanel clicked");
                // Chuyển đổi panel
                Container parent = ComboBoxPanel.this.getParent();
                if (parent instanceof JPanel) {
                    CardLayout layout = (CardLayout) ((JPanel) parent).getLayout();
                    layout.show(parent, "ButtonPanel");
                }
                System.out.println("");

            }
        });
    }
    

}
