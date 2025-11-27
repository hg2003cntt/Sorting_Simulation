/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sorting_simulation;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.plaf.RootPaneUI;

/**
 *
 * @author admin
 */
public class SimulationController {

    private static final int PADDING = 10;
    private static final int MAX_BAR_HEIGHT = 300, MIN_BAR_HEIGHT = 30;
    private Integer[] Array, tmpArray;
    private int arraySize, speed;
    private Bar[] bars;
    private boolean hasArray;

    private boolean running = false;
    // statistic
    private int comp, swapping;
    private int max_size = 30;
    private Color swappingColor, comparingColor;

    private BufferStrategy bs;
    private Graphics g;
    private SortedListener listener;
    private MainFrame frame;

    public SimulationController(int capacity, int fps, SortedListener listener, MainFrame frame, String canvasType) {
        this.arraySize = capacity;
        this.speed = (int) (1000.0 / fps);
        this.listener = listener;
        this.frame = frame;
        comp = swapping = 0;

        comparingColor = new Color(0, 153, 153);
        swappingColor = ColorManager.BAR_RED;

        bs = listener.getBufferStrategy(canvasType);

        hasArray = false;
    }

    public void Initialize(int canvasWidth, int canvasHeight) {
        System.out.println("Initialize");
        bars = new Bar[Array.length];
        hasArray = true;

        // initial position
        double x = PADDING;
        int y = canvasHeight - PADDING;

        // width of all bars
        double width = (double) (canvasWidth - PADDING * 2) / Array.length;

        // get graphics
        g = bs.getDrawGraphics();
        g.setColor(ColorManager.CANVAS_BACKGROUND);
        g.fillRect(0, 0, canvasWidth, canvasHeight);

        Bar bar;
        for (int i = 0; i < Array.length; i++) {
            Array[i] = tmpArray[i];
            bar = new Bar((int) x, y, (int) width, tmpArray[i], ColorManager.BAR_BUBBLE);
            if (Array.length > max_size) {
                bar.drawNoText(g);
            } else {
                bar.drawWithText(g);
            }

            bars[i] = bar;

            // move to the next bar
            x += width;
        }
        //listener.clearLable();
        bs.show();
        g.dispose();
    }

    public void createRandomArray(int xStart, int canvasWidth, int canvasHeight) {
        tmpArray = new Integer[arraySize];
        Array = new Integer[arraySize];
        bars = new Bar[arraySize];
        hasArray = true;
        System.out.println("goi ham create ramdom");
        // initial position
        double x = xStart + PADDING;
        int y = canvasHeight - PADDING;
        // width of all bars
        double width = (double) (canvasWidth - PADDING * 2) / arraySize;

        // get graphics
        g = bs.getDrawGraphics();
        g.setColor(ColorManager.CANVAS_BACKGROUND);
        g.fillRect(0, 0, canvasWidth, canvasHeight);

        Random rand = new Random();
        int value;
        Bar bar;
        for (int i = 0; i < Array.length; i++) {
            // Ensure a minimum height for the bar
            int minHeight = MIN_BAR_HEIGHT;
            value = rand.nextInt(MAX_BAR_HEIGHT - minHeight) + minHeight;
            tmpArray[i] = value;
            Array[i] = value;
            bar = new Bar((int) x, y, (int) width, value, ColorManager.BAR_BUBBLE);
            if (Array.length <= max_size) {
                bar.drawWithText(g);
            } else {
                bar.drawNoText(g);
            }

            bars[i] = bar;
            System.out.print(Array[i] + ";");
            // move to the next bar
            x += width;
        }
        System.out.println("");
        // listener.clearLable();
        bs.show();
        g.dispose();
    }

    public void createArray(int canvasWidth, int canvasHeight, Integer[] value) {
        tmpArray = new Integer[value.length];
        Array = new Integer[value.length];

        frame.changSize(value.length);
        bars = new Bar[value.length];
        hasArray = true;

        // initial position
        double x = PADDING;
        int y = canvasHeight - PADDING;

        // width of all bars
        double width = (double) (canvasWidth - PADDING * 2) / value.length;

        // get graphics
        g = bs.getDrawGraphics();
        g.setColor(ColorManager.CANVAS_BACKGROUND);
        g.fillRect(0, 0, canvasWidth, canvasHeight);

        Bar bar;
        for (int i = 0; i < Array.length; i++) {
            // Ensure a minimum height for the bar
            tmpArray[i] = value[i];
            Array[i] = value[i];

            bar = new Bar((int) x, y, (int) width, value[i], ColorManager.BAR_BUBBLE);
            if (value.length > max_size) {
                bar.drawNoText(g);
            } else {
                bar.drawWithText(g);
            }
            bars[i] = bar;
            // move to the next bar
            x += width;
        }

        bs.show();
        g.dispose();
    }

    // return a color for a bar
    private Color getBarColor(int value) {
        int interval = (int) (Array.length / 5.0);
        if (value < interval) {
            return ColorManager.BAR_ORANGE;
        } else if (value < interval * 2) {
            return ColorManager.BAR_YELLOW;
        } else if (value < interval * 3) {
            return ColorManager.BAR_GREEN;
        } else if (value < interval * 4) {
            return new Color(153, 255, 204);
        }
        return ColorManager.BAR_BLUE;

    }

    /* BUBBLE SORT */
    public void bubbleSort() throws InterruptedException {
        if (!isCreated()) {
            return;
        }
        running = true;
        // get graphics
        g = bs.getDrawGraphics();
        comp = swapping = 0;
        int count = 0;
        for (int i = 0; i <= Array.length - 2 && running; i++) {
            for (int j = Array.length - 1; j > i; j--) {
                colorPair(j, j - 1, comparingColor);
                if (Array[j] < Array[j - 1]) {
                    swap(j, j - 1);
                    count++;
                    swapping++;
                }
                comp++;
            }
            bars[i].setColor(getBarColor(i));
            if (Array.length > max_size) {
                bars[i].drawNoText(g);
            } else {
                bars[i].drawWithText(g);
            }

            bs.show();

            if (count == 0) // the array is sorted
            {
                break;
            }
        }
        if (!running) {
            StopSorting();
            return;
        }
        finishAnimation();
        running = false;

        g.dispose();
    }

    /* SELECTION SORT */
    public void selectionSort() throws InterruptedException {
        if (!isCreated()) {
            return;
        }
        running = true;
        System.out.println("Gọi hàm selection");
        // get graphics
        g = bs.getDrawGraphics();
        comp = swapping = 0;
        // for (int i = Array.length - 1; i >= 0; i--) {
        for (int i = 0; i <= Array.length - 2 && running; i++) {
            // find the max
            int max = Array[i], index = i;
            for (int j = i + 1; j <= Array.length - 1; j++) {
                colorPair(index, j, comparingColor);
                comp++;
                if (max > Array[j]) {
                    max = Array[j];
                    index = j;
                }

            }

            swap(i, index);
            swapping++;

            bars[i].setColor(getBarColor(i));
            if (Array.length > max_size) {
                bars[i].drawNoText(g);
            } else {
                bars[i].drawWithText(g);
            }

            bs.show();
        }
        System.out.println("swap: "+swapping);
        if (!running) {
            StopSorting();
            return;
        }
        finishAnimation();
        running = false;
        g.dispose();
    }

    /* INSERTION SORT */
    public void insertionSort() throws InterruptedException {
        if (!isCreated()) {
            return;
        }
        running = true;
        // gett graphics
        g = bs.getDrawGraphics();
        comp = swapping = 0;

        Bar bar;
        for (int i = 1; i < Array.length && running; i++) {

            // find the insertion location by comparing to its predecessor
            int index = i;
            while (index > 0) {
                colorPair(index, index - 1, comparingColor);
                comp++;

                if (Array[index] >= Array[index - 1]) {
                    break;
                }

                swap(index, index - 1);
                swapping++;
                index--;
            }
            bars[i].setColor(getBarColor(i));
            if (Array.length > max_size) {
                bars[i].drawNoText(g);
            } else {
                bars[i].drawWithText(g);
            }
            bs.show();
        }
        if (!running) {
            StopSorting();
            return;
        }
        finishAnimation();
        running = false;
        g.dispose();
    }

    /* QUICK SORT */
    public void callquickSort() throws InterruptedException {
        if (!isCreated()) {
            return;
        }
        running = true;
        g = bs.getDrawGraphics();
        comp = swapping = 0;
        quickSort(0, Array.length - 1);

        finishAnimation();
        running = false;
        g.dispose();
    }

    private void quickSort(int start, int end) throws InterruptedException {
        if (!running) {
            StopSorting();
            return;
        }
        if (start < end) {
            int pivotIndex = choosePivotIndex(start, end);
            System.out.println("pIndex=" + pivotIndex);

            if (pivotIndex != -1) {
                int pivot = Array[pivotIndex];
                int partitionIndex = parttion(start, end, pivot, pivotIndex);
                quickSort(start, partitionIndex - 1);
                quickSort(partitionIndex, end);
            }

        }
    }

    private int parttion(int start, int end, int pivot, int pivotIndex) throws InterruptedException {
        Bar bar = bars[pivotIndex];
        if (Array.length > max_size) {
            bar.clearNoText(g);
        } else {
            bar.clearWithText(g);
        }
        bs.show();
        Color oldColor = bar.getColor();
        bar.setColor(ColorManager.PIVOT);

        int L, R;
        L = start;
        R = end;
        System.out.println("Chốt: " + pivot);
        while (L <= R) {
            if (!running) {
                StopSorting();
                break;
            }
            while (Array[L] < pivot) {
                colorPair1(L, comparingColor);
                L++;
                comp++;
                bs.show();
                if (!running) {
                    StopSorting();
                    break;
                }

            }

            while (Array[R] >= pivot) {
                colorPair1(R,comparingColor);
                R--;
                comp++;
                bs.show();
                if (!running) {
                    StopSorting();
                    break;
                }
            }
            if (L < R) {
                swap(L, R);
                swapping++;
            }
            
        }
        // Restore the color of the pivot bar
        bar.setColor(oldColor);
        if (Array.length > max_size) {
            bar.drawNoText(g);
        } else {
            bar.drawWithText(g);
        }
        bs.show();
        bar = bars[L];
        // Restore the color of the pivot bar
        bar.setColor(getBarColor(L));
        if (Array.length > max_size) {
            bar.drawNoText(g);
        } else {
            bar.drawWithText(g);
        }
        bs.show();
        return L;
    }

    private int choosePivotIndex(int start, int end) {
        // Chọn chốt là phần tử có khóa lớn hơn trong hai phần tử khác nhau đầu tiên
        int first = Array[start];
        int i = start + 1;
        while (i <= end && Array[i] == first) {
            i++;
        }
        try {
            TimeUnit.MILLISECONDS.sleep(speed);
        } catch (InterruptedException ex) {
        }
        if (i > end) {
            return -1;
        }
        if (Array[i] > first) {
            return i;
        }
        return start; // If all elements are the same, return the first index as pivot
    }

    // heap Sort
    public void heapSort() throws InterruptedException {
        if (!isCreated()) {
            return;
        }
        running = true;
        // Lấy đối tượng đồ họa
        g = bs.getDrawGraphics();
        comp = swapping = 0;
        // Xây dựng đống (sắp xếp lại mảng)
        for (int i = Array.length / 2 - 1; i >= 0 && running; i--) {
            heapify(Array.length, i);
        }
        // Lấy từng phần tử một từ đống
        for (int i = Array.length - 1; i > 0 && running; i--) {
            // Di chuyển gốc hiện tại đến cuối
            swap(0, i);
            swapping++;
            // Gọi heapify trên đống đã giảm kích thước
            heapify(i, 0);
            bars[i].setColor(getBarColor(i));
            if (Array.length > max_size) {
                bars[i].drawNoText(g);
            } else {
                bars[i].drawWithText(g);
            }
            bs.show();
        }
        if (!running) {
            StopSorting();
            return;
        }
        finishAnimation();
        running = false;
        g.dispose();
    }

    // Heapify 
    private void heapify(int n, int i) {
        int largest = i;
        int leftChild = 2 * i + 1;
        int rightChild = 2 * i + 2;
        // Lấy đối tượng đồ họa
        g = bs.getDrawGraphics();
        comp++;
        // Nếu con trái lớn hơn gốc
        if (leftChild < n && Array[leftChild] > Array[largest]) {
            colorPair(leftChild, largest, comparingColor);
            largest = leftChild;
        }

        comp++;
        // Nếu con phải lớn hơn largest đến nay
        if (rightChild < n && Array[rightChild] > Array[largest]) {
            colorPair(rightChild, largest, comparingColor);
            largest = rightChild;

        }

        // Nếu largest không phải là gốc
        if (largest != i) {
            swap(i, largest);
            swapping++;
            // Đệ quy heapify trên cây con bị ảnh hưởng
            heapify(n, largest);
        }
    }

    // swap 2 elements given 2 indexes
    private void swap(int i, int j) {
        // swap the elements
        int temp = Array[j];
        Array[j] = Array[i];
        Array[i] = temp;

        // clear the bar
        if (Array.length > max_size) {
            bars[j].clearNoText(g);
            bars[i].clearNoText(g);
        } else {
            bars[j].clearWithText(g);
            bars[i].clearWithText(g);
        }
         try {
            TimeUnit.MILLISECONDS.sleep(speed);
        } catch (InterruptedException ex) {
        }
        // swap the drawings
        bars[j].setValue(bars[i].getValue());
        bars[i].setValue(temp);

        colorPair(i, j, swappingColor);
    }

    private void colorPair(int i, int j, Color color) {
        Color color1 = bars[i].getColor(), color2 = bars[j].getColor();
        // drawing
        if (Array.length > max_size) {
            bars[j].clearNoText(g);
            bars[i].clearNoText(g);
        } else {
            bars[j].clearWithText(g);
            bars[i].clearWithText(g);
        }
        if (Array.length > max_size) {
            bars[i].setColor(color);
            bars[i].drawNoText(g);

            bars[j].setColor(color);
            bars[j].drawNoText(g);
        } else {

            bars[i].setColor(color);
            bars[i].drawWithText(g);

            bars[j].setColor(color);
            bars[j].drawWithText(g);
        }

        bs.show();

        // delay
        try {
            TimeUnit.MILLISECONDS.sleep(speed);
        } catch (InterruptedException ex) {
        }

        // put back to original color
        if (Array.length > max_size) {
            bars[i].setColor(color1);
            bars[i].drawNoText(g);

            bars[j].setColor(color2);
            bars[j].drawNoText(g);
        } else {

            bars[i].setColor(color1);
            bars[i].drawWithText(g);

            bars[j].setColor(color2);
            bars[j].drawWithText(g);
        }
        bs.show();
    }
    
     private void colorPair1(int i, Color color) {
        Color color1 = bars[i].getColor();
        // drawing
        if (Array.length > max_size) {
            bars[i].clearNoText(g);
        } else {
            bars[i].clearWithText(g);
        }
        if (Array.length > max_size) {
            bars[i].setColor(color);
            bars[i].drawNoText(g);
        } else {
            bars[i].setColor(color);
            bars[i].drawWithText(g);
        }

        bs.show();

        // delay
//        try {
//            TimeUnit.MILLISECONDS.sleep(speed);
//        } catch (InterruptedException ex) {
//        }

        // put back to original color
        if (Array.length > max_size) {
            bars[i].setColor(color1);
            bars[i].drawNoText(g);
        } else {

            bars[i].setColor(color1);
            bars[i].drawWithText(g);

        }
        bs.show();
    }
    private void colorBar(int index, Color color) {
        Bar bar = bars[index];
        Color oldColor = bar.getColor();

        bar.setColor(color);
        if (Array.length > max_size) {
            bar.drawNoText(g);
        } else {
            bar.drawWithText(g);
        }
        bs.show();

        try {
            TimeUnit.MILLISECONDS.sleep(speed);
        } catch (InterruptedException ex) {
        }

        bar.setColor(oldColor);

        if (Array.length > max_size) {
            bar.drawNoText(g);
        } else {
            bar.drawWithText(g);
        }
        bs.show();
    }

    // swiping effect when the sorting is finished
    private void finishAnimation() {
        // swiping to green

        for (int i = 0; i < bars.length; i++) {
            colorBar(i, comparingColor);
            bars[i].setColor(getBarColor(i));
            if (Array.length > max_size) {
                bars[i].clearNoText(g);
            } else {
                bars[i].clearWithText(g);
            }
            if (Array.length > max_size) {
                bars[i].drawNoText(g);
            } else {
                bars[i].drawWithText(g);
            }
            bs.show();
        }

    }

    // for restore purpose
    public void drawArray() {
        if (!hasArray) {
            return;
        }
        System.out.println("drawArray");
        g = bs.getDrawGraphics();

        for (int i = 0; i < bars.length; i++) {
            if (Array.length <= max_size) {
                bars[i].drawWithText(g);
            } else {
                bars[i].drawNoText(g);
            }
        }

        bs.show();
        g.dispose();
    }

    // check if Array is created
    private boolean isCreated() {
        if (!hasArray) {
            JOptionPane.showMessageDialog(null, "You need to create an Array!", "No Array Created Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return hasArray;
    }

    public void setCapacity(int capacity) {
        this.arraySize = capacity;
    }

    public void setFPS(int fps) {
        this.speed = (int) (1000.0 / fps);
        System.out.println("Speed = " + speed);
    }

    public void typeFromKeyboad() {
        JFrame frame = new JFrame("Input");
        JPanel keyBoardPanel = new JPanel(new GridLayout(3, 1));
        keyBoardPanel.setBackground(ColorManager.BACKROUND_PADDING);

        JLabel lbInput = new JLabel("Nhập vào mảng bạn mong muốn");
        lbInput.setHorizontalAlignment(JLabel.CENTER);
        lbInput.setFont(new Font("Aileron", Font.BOLD, 20));
        JTextField txtInput = new JTextField();
        txtInput.setFont(new Font("Aileron", Font.BOLD, 20));
        keyBoardPanel.add(lbInput);
        keyBoardPanel.add(txtInput);
        JButton okButton = new JButton("OK");
        JPanel okPanel = new JPanel(new FlowLayout());
        okPanel.setOpaque(false);
        okButton.setPreferredSize(new Dimension(70, 20));
        okPanel.add(okButton);
        keyBoardPanel.add(okPanel);
        frame.add(keyBoardPanel);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setBounds(500, 300, 400, 130);

        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Integer> numbers = new ArrayList<>();
                String text = txtInput.getText();
                System.out.print("Numbers:");
                String[] parts = text.trim().split("\\s+");
                for (String part : parts) {
                    try {
                        if (Integer.parseInt(part) < 10 || Integer.parseInt(part) > 300) {
                            continue;
                        }
                        numbers.add(Integer.parseInt(part));
                        System.out.print(part + ";");
                    } catch (NumberFormatException error) {
                        // Bỏ qua nếu không thể chuyển đổi thành số nguyên
                    }
                }
                readNumbersFromArray(numbers);
                frame.dispose();
            }
        });
    }

    public void selecfile() {
        JFrame frame = new JFrame("File Chooser Example");
        JFileChooser fileChooser = new JFileChooser(); // Tạo một JFileChooser
        int result = fileChooser.showOpenDialog(frame); // Hiển thị hộp thoại chọn file

        if (result == JFileChooser.APPROVE_OPTION) { // Nếu người dùng chọn một file
            File selectedFile = fileChooser.getSelectedFile();
            try {
                ArrayList<Integer> numbers = readNumbersFromFile(selectedFile);
                readNumbersFromArray(numbers);
            } catch (IOException ex) {
            }
        }
    }

    private ArrayList<Integer> readNumbersFromFile(File file) throws IOException {
        ArrayList<Integer> numbers = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.trim().split("\\s+");
            for (String part : parts) {
                try {
                    if (Integer.parseInt(part) < 10 || Integer.parseInt(part) > 300) {
                        continue;
                    }
                    numbers.add(Integer.parseInt(part));
                } catch (NumberFormatException e) {
                    System.out.println("Lỗi số: " + part);
                }
            }
        }
        reader.close();
        return numbers;
    }

    private void readNumbersFromArray(ArrayList<Integer> numbers) {
        // Chuyển ArrayList<Integer> thành mảng Integer[]
        Integer[] numberArray = numbers.toArray(new Integer[numbers.size()]);
        if (numberArray.length < 2) {
            JOptionPane.showMessageDialog(frame, "Vui lòng nhập mảng từ 2-300 số nguyên!");
            return;
        }
        createArray(1070, 521, numberArray);
    }

    public Integer[] Sort(Integer ar[]) {
        for (int i = ar.length - 1; i >= 0; i--) {
            // find the max
            int max = ar[i], index = i;
            for (int j = 0; j <= i; j++) {
                if (max < ar[j]) {
                    max = ar[j];
                    index = j;
                }
            }
            swap(i, index, ar);
        }
        return ar;
    }

    private void swap(int i, int j, Integer ar[]) {
        // swap the elements
        int temp = ar[j];
        ar[j] = ar[i];
        ar[i] = temp;
    }

    public void exportFile(String sortName) {
        JFileChooser fileChooser = new JFileChooser();
        int userSelection = fileChooser.showSaveDialog(null);
        //Integer[] ar = new Integer[Array.length];
        //System.arraycopy(Array, 0, ar, 0, Array.length);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = fileChooser.getSelectedFile();
                String filename = selectedFile.getAbsolutePath();
                if (!filename.contains(".")) {
                    filename += ".txt";
                }
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                    writer.write("Original Array: \n");
                    for (int i = 0; i < Array.length; i++) {
                        writer.write(tmpArray[i].toString());
                        if (i < Array.length - 1) {
                            writer.write(" ");
                        }
                    }

                    writer.newLine();
                    writer.write("Sorted Array: \n");
                    for (int i = 0; i < Array.length; i++) {
                        writer.write(Array[i].toString());
                        if (i < Array.length - 1) {
                            writer.write(" ");
                        }
                    }
                    writer.newLine();
                    writer.write("Algorithm: " + sortName + "\n");
                    writer.write("Swap: " + swapping + "\n");
                    writer.write("Compare: " + comp + "\n");
                } catch (IOException e) {
                    System.err.println("Error writing to file " + filename);
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    System.err.println("Error: Array is not initialized");
                    e.printStackTrace();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public void introduction() {
        JFrame instruction = new JFrame("Introduction");
        instruction.setSize(500, 700);
        instruction.setLocationRelativeTo(null);
        instruction.setBackground(ColorManager.BACKROUND_PADDING);
        instruction.setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());

        //lable title
        JLabel titleInstruction = new JLabel("Introduction");
        titleInstruction.setFont(new Font("Aileron", Font.BOLD, 20));
        titleInstruction.setForeground(Color.BLACK);
        titleInstruction.setHorizontalAlignment(SwingConstants.CENTER);

        // panel title 
        JPanel paneltitle = new JPanel();
        paneltitle.setBounds(0, 0, 500, 30);
        paneltitle.add(titleInstruction);
        paneltitle.setBackground(ColorManager.CANVAS_BACKGROUND);
        // instruction.add(paneltitle);

        mainPanel.add(paneltitle, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true); // Tự động xuống dòng khi văn bản quá dài
        textArea.setWrapStyleWord(true); // Xuống dòng chỉ khi gặp khoảng trắng
        textArea.setMargin(new Insets(10, 10, 10, 10));  // tạo lề 
        //JScrollPane scrollPane = new JScrollPane(textArea); // Thêm vùng văn bản vào thanh cuộn
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(1, 1));
        textPanel.add(textArea);
        // giới thiệu
        String sortingAlgorithmDescription
                = "Nhập kích thước mảng vào ô Array Size và chọn nút Randomize để tạo mảng ngẫu nhiên. Kích thước hợp lệ từ 2-200.\n\n"
                + "Chọn nút FromKeyBoard để nhập vào mảng mong muốn từ bàn phím, các giá trị cách nhau bởi khoảng trắng. Giá trị hợp lệ từ 10-300. Các giá trị ngoài phạm vi sẽ được tự động bỏ qua.\n\n"
                + "Chọn nút FromFile để tạo mảng từ các giá trị trong file có sẵn.\n\n"
                + "Chọn nút Reinitialize để khởi tạo lại mảng ban đầu.\n\n"
                + "Chọn thuật toán sắp xếp mong muốn từ ComboBox Algorithm và ấn nút Start để bắt đầu sắp xếp.\n\n"
                + "Nút Export to File dùng để xuất giá trị mảng ban đầu, sau khi sắp xếp và thông tin thuật toán ra file.\n\n"
                + "Nút Compare dùng để so sánh 2 thuật toán mong muốn.";
        // định dạng textarea
        textArea.setText(sortingAlgorithmDescription);
        textArea.setFont(new Font(null, Font.BOLD, 18));
        textArea.setBackground(ColorManager.BACKROUND_PADDING);
        textArea.setForeground(Color.BLACK);
        textArea.setFocusable(false);

        mainPanel.add(textPanel, BorderLayout.CENTER);
        instruction.add(mainPanel);
        // Hiển thị khung hình
        instruction.setVisible(true);
    }

    public void compare2Sort() {
        JFrame choose = new JFrame("Choose 2 Algorithms");
//        choose.setDefaultCloseOperation(EXIT_ON_CLOSE);
        choose.setSize(new Dimension(300, 250));
        choose.setResizable(false);
        choose.setLocationRelativeTo(null);
        choose.setVisible(true);
        JPanel choosePanel = new JPanel();
        choosePanel.setBounds(0, 0, 200, 250);
        choosePanel.setBackground(ColorManager.BACKROUND_PADDING);

        // Tạo danh sách các thuật toán
        String[] algorithms = {"Selection Sort", "Insertion Sort", "Bubble Sort", "Quick Sort", "Heap Sort",};

        // ComboBox cho thuật toán 1
        JComboBox algorithmBox1 = new JComboBox<>(algorithms);
        algorithmBox1.setSelectedIndex(0);

        // ComboBox cho thuật toán 2
        JComboBox algorithmBox2 = new JComboBox<>(algorithms);
        algorithmBox2.setSelectedIndex(1);

        // Nút OK để xác nhận
        JButton okButton = new JButton("OK");
        JPanel okPanel = new JPanel(new FlowLayout());
        okPanel.setOpaque(false);
        okButton.setPreferredSize(new Dimension(70, 20));
        okPanel.add(okButton);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sortName1 = (String) algorithmBox1.getSelectedItem();
                String sortName2 = (String) algorithmBox2.getSelectedItem();

                // Kiểm tra nếu 2 thuật toán khác nhau
                if (sortName1.equals(sortName2)) {
                    JOptionPane.showMessageDialog(null,
                            "Hãy chọn hai thuật toán khác nhau!",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Đóng cửa sổ này và chuyển lựa chọn về CompareFrame hoặc MainFrame
                    choose.dispose();
                    frame.dispose();
                    // Gọi phương thức để mở CompareFrame với hai thuật toán
                    CompareFrame compFrame = new CompareFrame(frame, tmpArray, sortName1, sortName2);
                }
            }
        });

        // Sắp xếp các thành phần trong giao diện
        choosePanel.setLayout(new GridLayout(5, 1, 10, 10));
        choosePanel.add(new JLabel("Thuật Toán 1:"));
        choosePanel.add(algorithmBox1);
        choosePanel.add(new JLabel("Thuật Toán 2:"));
        choosePanel.add(algorithmBox2);
        choosePanel.add(okPanel);
        choose.add(choosePanel);

    }

    public int getSize() {
        return arraySize;
    }

    public int getSwap() {
        return swapping;
    }

    public int getCompare() {
        return comp;
    }

    public void setMaxSize(int size) {
        max_size = size;
    }

    public synchronized void stopSorting() {
        running = false; // Đặt trạng thái dừng

    }

    public boolean isRunning() {
        return running;
    }

    public void StopSorting() throws InterruptedException {
        throw new InterruptedException(); // hoặc kết thúc phương thức

    }

    public interface SortedListener {

        BufferStrategy getBufferStrategy(String canvasType);
    }
}
