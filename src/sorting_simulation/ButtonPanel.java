package sorting_simulation;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ButtonPanel extends JPanel {

    private static final int BUTTON_WIDTH = 175, BUTTON_HEIGHT = 40;
    private JLabel button;
    private int id;
    private String name; // Tên hình ảnh hiện tại
    SortButtonListener listener;

    public ButtonPanel(SortButtonListener listener) {
        super();
        this.listener = listener;

    }

    public ButtonPanel(SortButtonListener listener, String name, int id) {
        super();
        this.listener = listener;
        this.name = name;
        this.id = id;

        button = new JLabel();
        setLayout(null);

        initButton(); // Thiết lập hình ảnh dựa trên tên
        button.setBounds(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT);
        add(button);
        setOpaque(false);
    }

    private void initButton() {
        Image image = new ImageIcon(getClass().getResource("/Image/" + name + "_button.png")).getImage();
        Icon icon = new ImageIcon(image);
        button.setIcon(icon);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(name + " clicked");
                if (id == 0 || id == 6 || id == 7) {
                    String nameShow = new String();
                    switch (id) {
                        case 0 -> nameShow = "ComboBoxPanel";
                        case 6 -> nameShow = "StopButton";
                        case 7 -> nameShow = "StartButton";
                    }
                    Container parent = ButtonPanel.this.getParent();
                    if (parent instanceof JPanel) {
                        CardLayout layout = (CardLayout) ((JPanel) parent).getLayout();
                        layout.show(parent, nameShow);  // Chuyển đến ComboBoxPanel
                    }
                } else if (id >= 1 && id <= 5) {
                    //listener.sortButtonClicked(id);
                    // Khi nhấn nút trong ComboBoxPanel, chuyển về ButtonPanel
                    // Lấy cha của ButtonPanel (cha của nút này là ButtonPanel)
                    Container parent = ButtonPanel.this.getParent();
                    // Lấy cha của ButtonPanel (là ComboBoxPanel)
                    // Lấy cha của ComboBoxPanel là Cardlayout
                    Container grandparent = parent.getParent().getParent();
                    if (grandparent instanceof JPanel) {
                        CardLayout layout = (CardLayout) ((JPanel) grandparent).getLayout();
                        if (layout != null) {
                            layout.show(grandparent, "ButtonPanel");  // Chuyển đổi lại ButtonPanel
                        }
                    }
                } else if (id==14) mouseExited(e);
                listener.sortButtonClicked(id);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                Image image = new ImageIcon(getClass().getResource("/Image/" + name + "_entered_button.png")).getImage();
                Icon icon = new ImageIcon(image);
                button.setIcon(icon);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Image image = new ImageIcon(getClass().getResource("/Image/" + name + "_button.png")).getImage();
                Icon icon = new ImageIcon(image);
                button.setIcon(icon);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }
        });
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        Image image = new ImageIcon(getClass().getResource("/Image/" + name + "_button.png")).getImage();
        Icon icon = new ImageIcon(image);
        button.setIcon(icon);
    }

//    public void updateButtonIcon(String imageName) {
//        // Cập nhật hình ảnh nút
//        Image image = new ImageIcon(getClass().getResource("/Image/" + imageName + "_button.png")).getImage();
//        Icon icon = new ImageIcon(image);
//        button.setIcon(icon);
//    }
    public void setEnabledButton(boolean enabled) {
        this.setEnabled(enabled);
        button.setEnabled(enabled);
    }

    public interface SortButtonListener {

        void sortButtonClicked(int id); //Giao diện bắt sự kiện nút
    }
}
