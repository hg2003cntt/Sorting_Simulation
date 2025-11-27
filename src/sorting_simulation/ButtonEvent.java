/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sorting_simulation;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;

/**
 *
 * @author admin
 */
public class ButtonEvent extends JButton{
    private final int WIDTH = 100, HEIGHT = 30;
    private String name = null;
    public ButtonEvent(String text) {
        super(text);
        name = new String(text);
        createButton(name);
    }
    public void createButton(String text){
        setSize(100, 30);
        setFont(new Font("Aileron", Font.BOLD, 15));
        setFocusPainted(false); // bỏ đường viền focus vào chữ
        setBackground(new Color(200, 255, 200));
        setBorderPainted(false);
    }
    
}
