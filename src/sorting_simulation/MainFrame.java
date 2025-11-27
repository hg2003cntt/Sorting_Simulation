package sorting_simulation;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Huynh Giao
 */
public class MainFrame extends JFrame implements PropertyChangeListener, ChangeListener,
        MyCanvas.SimulationProvider, ButtonPanel.SortButtonListener,
        SimulationController.SortedListener {

    private static final int ARRAY_SIZE = 20, WIDTH = 1440, HEIGHT = 788, FPS = 100;
    private JPanel mainPanel, rightPanel, topPanel;
    private MyCanvas canvas, canvasleft, canvasright;
    private Panel cbPanel;
    private JPanel startPanel;
    private ButtonPanel cbButton, compButton;
    private ComboBoxPanel cbAlgo;
    private CardLayout cardLayout, startCard;
    private JSlider slSpeed;
    private SimulationController controller, controllerRight;
    private JFormattedTextField txtSize;
    private String sortName;
    private int cWidth, cHeight;

    public MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMaximumSize(new Dimension(WIDTH + 20, HEIGHT));
        setMinimumSize(new Dimension(WIDTH + 20, HEIGHT));
        setPreferredSize(new Dimension(WIDTH + 20, HEIGHT));
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Sorting Simulation");
        setVisible(true);
        sortName = "";
        initialize();
    }

    private void initialize() {
        //Tạo Panel chính
        mainPanel = new Panel("mainBackground");
        mainPanel.setBounds(0, 0, WIDTH, HEIGHT);
        add(mainPanel);
        mainPanel.setLayout(null);

        JLabel appName = new JLabel("Sorting Simulation");
        appName.setForeground(ColorManager.TEXT_BUBBLE);
        appName.setBounds(WIDTH / 2 - 100, 15, 400, 50);
        appName.setFont(new Font("Aileron", Font.BOLD, 30));

        mainPanel.add(appName);
        //Thêm các panel con
        createTopPanel();
        mainPanel.add(topPanel);

        //Thêm các button
        // add canvas
        canvas = new MyCanvas(this);

        cWidth = WIDTH - 370; //1070
        cHeight = HEIGHT - 267; //518

        canvas.setFocusable(false);
        canvas.setMaximumSize(new Dimension(cWidth, cHeight));
        canvas.setMinimumSize(new Dimension(cWidth, cHeight));
        canvas.setPreferredSize(new Dimension(cWidth, cHeight));
        canvas.setBounds(50, 192, cWidth, cHeight);
        // canvas.repaint();
        canvas.setVisible(true);

        mainPanel.add(canvas);
        //canvas.createBufferStrategy();

        // sorting simulation
        controller = new SimulationController(20, FPS, this, this, "main");
        controller.createRandomArray(0, cWidth, cHeight);
        pack();

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });

    }

    private void createTopPanel() {
        topPanel = new JPanel();
        topPanel.setBounds(50, 70, WIDTH - 370, 100);
        topPanel.setLayout(null);
        topPanel.setOpaque(false);

        //Thêm combo box
        cardLayout = new CardLayout();
        cbPanel = new Panel(null);
        cbPanel.setLayout(cardLayout);
        cbAlgo = new ComboBoxPanel(this);
        cbButton = new ButtonPanel(this, "cbbox", 0);
        cbAlgo.setOpaque(false);
        cbButton.setOpaque(false);
        cbPanel.add(cbButton, "ButtonPanel");
        cbPanel.add(cbAlgo, "ComboBoxPanel");

        cbPanel.setOpaque(false);
        cbPanel.setBounds(149, 110, 174, 232);
        //cbPanel.setBounds(149, 110, 174, 40);
        mainPanel.add(cbPanel);

        createRightPanel();
        mainPanel.add(rightPanel);

        //Thêm label
        JLabel lbAlgo = new JLabel("Algorithm");
        lbAlgo.setFont(new Font("Aileron", Font.BOLD, 20));
        lbAlgo.setBounds(105, 10, 175, 30);
        topPanel.add(lbAlgo);

        JLabel lbSize = new JLabel("Array Size");
        lbSize.setFont(new Font("Aileron", Font.BOLD, 20));
        lbSize.setBounds(340, 10, 175, 30);
        topPanel.add(lbSize);

        // Thêm các nút
        Panel sizeButton = new Panel("size_button");
        sizeButton.setBounds(100 + 230, 40, 175, 40);

        NumberFormat format = NumberFormat.getNumberInstance();
        MyFormatter formatter = new MyFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(200);
        formatter.setAllowsInvalid(false); // không cho phép nhật giá trị không hợp lệ
        formatter.setCommitsOnValidEdit(true); // Tự động lưu

        txtSize = new JFormattedTextField(formatter);
        txtSize.setBounds(100 + 230, 40, 175, 40);
        txtSize.setOpaque(false);
        txtSize.setFont(new Font("Aileron", Font.BOLD, 20));
        txtSize.setHorizontalAlignment(SwingConstants.CENTER);
        txtSize.setBorder(null);
        txtSize.setValue(ARRAY_SIZE);
        txtSize.addPropertyChangeListener("value", this);
        topPanel.add(txtSize);
        topPanel.add(sizeButton);

        //Thêm cardlayout cho nút Start
        startCard = new CardLayout();
        startPanel = new JPanel(startCard);
        ButtonPanel startButton = new ButtonPanel(this, "start", 6);
        ButtonPanel stopButton = new ButtonPanel(this, "stop", 7);
        startPanel.add(startButton, "StartButton");
        startPanel.add(stopButton, "StopButton");
        startPanel.setOpaque(false);
        startPanel.setBounds(100 + 2 * 230, 40, 175, 40);
        topPanel.add(startPanel);

        ButtonPanel introButton = new ButtonPanel(this, "intro", 8);
        introButton.setBounds(100 + 3 * 230, 40, 175, 40);
        topPanel.add(introButton);
    }

    private void createRightPanel() {

        rightPanel = new JPanel();
        rightPanel.setBounds(WIDTH - 300, 70, 250, HEIGHT - 150);
        rightPanel.setLayout(null);
        rightPanel.setOpaque(false);
        //Tạo các nút
        ButtonPanel buttons[] = new ButtonPanel[5];
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new ButtonPanel(this);
        }
        buttons[0] = new ButtonPanel(this, "random", 9);
        buttons[1] = new ButtonPanel(this, "key", 10);
        buttons[2] = new ButtonPanel(this, "file", 11);
        buttons[3] = new ButtonPanel(this, "reinit", 12);

        for (int i = 0; i <= 3; i++) {
            buttons[i].setBounds(35, 25 + (i + 1) * 70, 175, 60);
            rightPanel.add(buttons[i]);
        }

        buttons[4] = new ButtonPanel(this, "export", 13);
        buttons[4].setBounds(35, 485, 175, 60);
        compButton = new ButtonPanel(this, "compare", 14);
        compButton.setBounds(35, 555, 175, 60);

        rightPanel.add(buttons[4]);
        rightPanel.add(compButton);

        //Tạo các label
        JLabel lbCreate = new JLabel("Create Array");
        lbCreate.setFont(new Font("Aileron", Font.BOLD, 20));
        lbCreate.setHorizontalAlignment(SwingConstants.CENTER);
        lbCreate.setVerticalAlignment(SwingConstants.CENTER);
        lbCreate.setBounds(35, 40, 175, 30);
        rightPanel.add(lbCreate);

        JLabel lbSpeed = new JLabel("Speed");
        lbSpeed.setFont(new Font("Aileron", Font.BOLD, 20));
        lbSpeed.setHorizontalAlignment(SwingConstants.CENTER);
        lbSpeed.setVerticalAlignment(SwingConstants.CENTER);
        lbSpeed.setBounds(35, 370, 175, 60);
        rightPanel.add(lbSpeed);

        //Tạo Slider
        slSpeed = new JSlider(10, 500, FPS);
        slSpeed.setBounds(35, 410, 175, 60);
        slSpeed.setOpaque(false);
        slSpeed.setMajorTickSpacing(100);
        slSpeed.setMinorTickSpacing(20);
//        slSpeed.setPaintTicks(true);
//        slSpeed.setPaintLabels(true);
        slSpeed.setPaintTrack(true);
        slSpeed.addChangeListener(this);
        rightPanel.add(slSpeed);

    }

    public void changSize(int value) {
        txtSize.setValue(value);
    }

    public void showInfo(String sortedName, String swap, String comp) {
        BufferStrategy bs = this.getBufferStrategy("");
        Graphics g = bs.getDrawGraphics();
        //g.fillRect(100, 200, 100, 200);
        g.setFont(new Font("Aileron", Font.BOLD, 15));
        g.drawString("Algorithm: " + sortedName, 20, 50);
        g.drawString("Swap:         " + swap, 20, 80);
        g.drawString("Compare:  " + comp, 20, 110);
        bs.show();
        cbPanel.drawString(sortedName, swap, comp);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        int value = ((Number) txtSize.getValue()).intValue();
        controller.setCapacity(value);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (!slSpeed.getValueIsAdjusting()) {
            int value = (int) slSpeed.getValue();
            controller.setFPS(value);
            System.out.println("Changed Speed!");
        }
    }

    @Override
    public void onDrawArray() {
        if (controller != null) {
            controller.drawArray();
            System.out.println("not null");
        }
    }

    private volatile Thread sortingThread;
    private volatile boolean running = false;
    private String swap, comp;
    private boolean isSorted = false;

    @Override
    public void sortButtonClicked(int id) {
        if (id != 6 && sortingThread != null && sortingThread.isAlive()) {
            stopThread();
        }

        if (!controller.isRunning() && id == 6) {
            running = true;
            Runnable sortingTask = () -> {

                System.out.println("sortName:" + sortName);
                if ("".equals(sortName)) {
                    JOptionPane.showMessageDialog(this, "Bạn chưa chọn thuật toán!");
                    startCard.show(startPanel, "StartButton");
                } else {
                    try {
                        if (sortName.equals("Selection Sort")) {
                            controller.selectionSort();
                        } else if (sortName.equals("Insertion Sort")) {
                            controller.insertionSort();
                        } else if (sortName.equals("Bubble Sort")) {
                            controller.bubbleSort();
                        } else if (sortName.equals("Quick Sort")) {
                            controller.callquickSort();
                        } else {
                            controller.heapSort();
                        }
                        swap = String.valueOf(controller.getSwap());
                        comp = String.valueOf(controller.getCompare());
                        showInfo(sortName, swap, comp);
                        isSorted = true;
                    } catch (InterruptedException error) {
                        System.out.println("Luong sap xep da bi ngat!");
                        Thread.currentThread().interrupt(); // Khôi phục trạng thái ngắt
                    } finally {
                        startCard.show(startPanel, "StartButton");
                    }
                }
            };
            sortingThread = new Thread(sortingTask);
            sortingThread.start();
        } else {
            // Xử lý các nút khác ngoài nút Start (id=6)
            switch (id) {
                case 1 -> {
                    cbButton.setName("cbSelection");
                    sortName = "Selection Sort";
                }
                case 2 -> {
                    cbButton.setName("cbInsertion");
                    sortName = "Insertion Sort";
                }
                case 3 -> {
                    cbButton.setName("cbBubble");
                    sortName = "Bubble Sort";
                }
                case 4 -> {
                    cbButton.setName("cbQuick");
                    sortName = "Quick Sort";
                }
                case 5 -> {
                    cbButton.setName("cbHeap");
                    sortName = "Heap Sort";
                }
                case 7 ->
                    System.out.println("Stop!!!");
                case 8 ->
                    controller.introduction();
                case 9 -> {
                    int value = Integer.parseInt(txtSize.getText());
                    if (value < 2 || value > 200) {
                        JOptionPane.showMessageDialog(this, "Vui lòng nhập số từ 2-200!");
                        break;
                    }
                    controller.createRandomArray(0, cWidth, cHeight);
                    cbPanel.clearString();
                }
                case 10 -> {
                    controller.typeFromKeyboad();
                    cbPanel.clearString();
                    System.out.println("Size=" + controller.getSize());
                }
                case 11 -> {
                    controller.selecfile();
                    cbPanel.clearString();
                    System.out.println("Size=" + controller.getSize());
                    txtSize.setValue(controller.getSize());
                }
                case 12 -> {
                    controller.Initialize(cWidth, cHeight);
                    cbPanel.clearString();
                }
                case 13 -> {
                    if (isSorted) {
                        controller.exportFile(sortName);
                    }else JOptionPane.showMessageDialog(this, "Vui lòng sử dụng chức năng này sau khi đã sắp xếp mảng!");
                }
                case 14 ->
                    controller.compare2Sort();
                default ->
                    System.out.println("ID lỗi");
            }
        }
    }

    private void stopThread() {
        controller.stopSorting();
        if (sortingThread != null) {
            System.out.println("Goi ham stop");
            sortingThread.interrupt(); // Gửi tín hiệu ngắt tới luồng
            try {
                sortingThread.join(); // Đợi luồng dừng lại hoàn toàn
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Khôi phục trạng thái ngắt
                System.out.println("Bat trang thai stop!s");
            } finally {
                sortingThread = null; // Đặt lại thành null để xác nhận luồng đã ngắt hoàn toàn
            }
        }
    }

// return the graphics for drawing
    @Override
    public BufferStrategy getBufferStrategy(String main) {

        BufferStrategy bs = canvas.getBufferStrategy();
        if (bs == null) {
            canvas.createBufferStrategy(2);
            bs = canvas.getBufferStrategy();
        }

        return bs;
    }
}
