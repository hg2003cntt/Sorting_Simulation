/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sorting_simulation;

import java.awt.Canvas;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Huynh Giao
 */
public class CompareFrame extends JFrame implements ChangeListener,
        MyCanvas.SimulationProvider, ButtonPanel.SortButtonListener,
        SimulationController.SortedListener {

    private static final int WIDTH = 1440, HEIGHT = 788, FPS = 100;
    private int ARRAY_SIZE;
    private MyCanvas canvas, canvasleft, canvasright;
    private CardLayout cardLayout, startCard;
    private JSlider slSpeed;
    private MainFrame mainFrame;
    private Panel mainPanel;
    private JPanel topPanel, startPanel;
    private int cWidth, cHeight;
    private String sortName1, sortName2;
    private Integer[] array;
    private SimulationController controllerLeft, controllerRight;
    private BufferStrategy leftBuffer, rightBuffer;

    public CompareFrame(MainFrame mainFrame, Integer[] array, String sortName1, String sortName2) {
        this.mainFrame = mainFrame;
        this.array = array;
        this.sortName1 = sortName1;
        this.sortName2 = sortName2;
        ARRAY_SIZE = array.length;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMaximumSize(new Dimension(WIDTH + 20, HEIGHT));
        setMinimumSize(new Dimension(WIDTH + 20, HEIGHT));
        setPreferredSize(new Dimension(WIDTH + 20, HEIGHT));
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Sorting Simulation");
        setVisible(true);
        initialize();
    }

    public void initialize() {

        //Tạo Panel chính
        mainPanel = new Panel("background_null");
        mainPanel.setBounds(0, 0, WIDTH, HEIGHT);
        add(mainPanel);
        mainPanel.setLayout(null);

        JLabel frameName = new JLabel("Algorithm Comparison");
        frameName.setForeground(ColorManager.TEXT_BUBBLE);
        frameName.setBounds(WIDTH / 2 - 150, 15, 400, 50);
        frameName.setFont(new Font("Aileron", Font.BOLD, 30));

        mainPanel.add(frameName);
        createTopPanel();
        createCanvas();
    }

    private void createTopPanel() {
        topPanel = new Panel("TopPanel");
        topPanel.setBounds(50, 70, WIDTH - 100, 100);
        topPanel.setLayout(null);
        topPanel.setOpaque(false);
        //Thêm nút 

        ButtonPanel backButton = new ButtonPanel(this, "back", 15);
        backButton.setBounds(100, 40, 175, 40);
        topPanel.add(backButton);

        JLabel lbSize = new JLabel("Array Size: " + ARRAY_SIZE);
        lbSize.setFont(new Font("Aileron", Font.BOLD, 19));
        lbSize.setBounds(420, 42, 175, 30);
        topPanel.add(lbSize);

        Panel sizeButton = new Panel("size_button");
        sizeButton.setBounds(400, 40, 175, 40);
        topPanel.add(sizeButton);

        //Thêm cardlayout cho nút Start
        startCard = new CardLayout();
        startPanel = new JPanel(startCard);
        ButtonPanel startButton = new ButtonPanel(this, "start", 6);
        ButtonPanel stopButton = new ButtonPanel(this, "stop", 7);
        startPanel.add(startButton, "StartButton");
        startPanel.add(stopButton, "StopButton");
        startPanel.setOpaque(false);
        startPanel.setBounds(700, 40, 175, 40);
        topPanel.add(startPanel);

        //Tạo Slider
        JLabel lbSpeed = new JLabel("Speed");
        lbSpeed.setFont(new Font("Aileron", Font.BOLD, 19));
        lbSpeed.setHorizontalAlignment(SwingConstants.CENTER);
        lbSpeed.setVerticalAlignment(SwingConstants.CENTER);
        lbSpeed.setBounds(1000, 15, 175, 60);
        topPanel.add(lbSpeed);

        slSpeed = new JSlider(10, 500, FPS);
        slSpeed.setBounds(1000, 40, 175, 60);
        slSpeed.setOpaque(false);
        slSpeed.setMajorTickSpacing(100);
        slSpeed.setMinorTickSpacing(20);
        slSpeed.setPaintTrack(true);
        slSpeed.addChangeListener(this);
        topPanel.add(slSpeed);

        mainPanel.add(topPanel);
    }

    private void createCanvas() {
        // add canvas

        canvasleft = new MyCanvas(this);

        cWidth = (WIDTH - 100) / 2 - 5; //1070
        cHeight = HEIGHT - 267; //518

        canvasleft.setFocusable(false);
        canvasleft.setMaximumSize(new Dimension(cWidth, cHeight));
        canvasleft.setMinimumSize(new Dimension(cWidth, cHeight));
        canvasleft.setPreferredSize(new Dimension(cWidth, cHeight));
        canvasleft.setBounds(50, 190, cWidth, cHeight);
        canvasleft.repaint();
        canvasleft.setVisible(true);

        mainPanel.add(canvasleft);
        canvasleft.createBufferStrategy(2);
        leftBuffer = canvasleft.getBufferStrategy();
        
        // sorting simulation
        controllerLeft = new SimulationController(ARRAY_SIZE, FPS, this, this.mainFrame, "left");
        controllerLeft.setMaxSize(20);
        controllerLeft.createArray(cWidth, cHeight, array);
        
        showName(sortName1, "left");
        
        canvasright = new MyCanvas(this);
        canvasright.setFocusable(false);
        canvasright.setMaximumSize(new Dimension(cWidth, cHeight));
        canvasright.setMinimumSize(new Dimension(cWidth, cHeight));
        canvasright.setPreferredSize(new Dimension(cWidth, cHeight));
        canvasright.setBounds(50 + cWidth + 10, 190, cWidth, cHeight);
        canvasright.repaint();
        canvasright.setVisible(true);
        
        mainPanel.add(canvasright);
        canvasright.createBufferStrategy(2);
        rightBuffer = canvasright.getBufferStrategy();
       
        controllerRight = new SimulationController(ARRAY_SIZE, FPS, this, this.mainFrame, "right");
        controllerRight.setMaxSize(20);
        controllerRight.createArray(cWidth, cHeight, array);
        
        showName(sortName2, "right");
        pack();
    }

    private void stopThread(SimulationController controller, Thread sortingThread) {
        controller.stopSorting();
        if (sortingThread != null) {
            System.out.println("Goi ham stop");
            sortingThread.interrupt(); // Gửi tín hiệu ngắt tới luồng
            try {
                sortingThread.join(); // Đợi luồng dừng lại hoàn toàn
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Khôi phục trạng thái ngắt
                System.out.println("Bat trang thai stop!");
            } finally {
                sortingThread = null; // Đặt lại thành null để xác nhận luồng đã ngắt hoàn toàn
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (!slSpeed.getValueIsAdjusting()) {
            int value = (int) slSpeed.getValue();
            controllerLeft.setFPS(value);
            controllerRight.setFPS(value);
            System.out.println("Changed Speed!");
        }
    }

    @Override
    public void onDrawArray() {
        if (controllerLeft != null) {
            controllerLeft.drawArray();
            System.out.println("left not null");
        }
        if (controllerRight != null) {
            controllerRight.drawArray();
            System.out.println("right not null");
        }
    }
    private Thread sortingThread1, sortingThread2;

    @Override
    public void sortButtonClicked(int id) {
        switch (id) {
            case 15:
                System.out.println("Goi lai mainFrame");
                mainFrame.setVisible(true);
                this.dispose();
                stopThread(controllerLeft, sortingThread1);
                stopThread(controllerRight, sortingThread2);
                break;
            case 6: // Nút Start
                Runnable sortingTask1 = () -> {
                    callSort(controllerLeft, sortName1, "left");
                };
                sortingThread1 = new Thread(sortingTask1);
                sortingThread1.start();
                Runnable sortingTask2 = () -> {
                    callSort(controllerRight, sortName2, "right");
                };
                sortingThread2 = new Thread(sortingTask2);
                sortingThread2.start();
                break;
            case 7:
                stopThread(controllerLeft, sortingThread1);
                stopThread(controllerRight, sortingThread2);
                break;
        }
    }

    public void showInfo(String swap, String comp, String type) {
        BufferStrategy bs = getBufferStrategy(type);
        Graphics g = bs.getDrawGraphics();
        g.setColor(ColorManager.CANVAS_BACKGROUND);
        g.fillRect(30, 60, 200, 60);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Aileron", Font.BOLD, 15));
        // g.drawString("Algorithm: " + sortedName, 30, 50);
        g.drawString("Swap:         " + swap, 30, 80);
        g.drawString("Compare:  " + comp, 30, 110);
        bs.show();
        g.dispose();
    }
    
    public void showName(String sortName, String type){
            BufferStrategy bs = getBufferStrategy(type);
            Graphics g = bs.getDrawGraphics();
            System.out.println(type + sortName);
            g.setFont(new Font("Aileron", Font.BOLD, 15));
            g.drawString("Algorithm: " + sortName, 30, 50);
            bs.show();
    }

    public void callSort(SimulationController controller, String sortName, String type) {
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
            String swap = String.valueOf(controller.getSwap());
            String comp = String.valueOf(controller.getCompare());
            showInfo(swap, comp, type);
        } catch (InterruptedException error) {
            System.out.println("Luong sap xep da bi ngat!");
            Thread.currentThread().interrupt(); // Khôi phục trạng thái ngắt
        } finally {
            startCard.show(startPanel, "StartButton");
        }
    }

    @Override
    public BufferStrategy getBufferStrategy(String canvasType) {
        if (canvasType.equals("left")) {
            return leftBuffer;
        } else if (canvasType.equals("right")) {
            return rightBuffer;
        }
        return null;
    }
}
