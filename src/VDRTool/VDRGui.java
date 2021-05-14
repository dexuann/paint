package VDRTool;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * Represents a GUI for vector designing
 *
 * @author Ashley De Xuan Poon n9629238
 * @author Khanh Duy Nguyen n9784616
 */
public class VDRGui extends JFrame implements ActionListener, Runnable {
    //declare frame dimensions
    private static final int WIDTH = 815;
    private static final int HEIGHT = 738;

    //declare canvas size
    private static int canvasSize = 700;

    // set default stroke and fill colors
    private static Color penColor = Color.BLACK;
    private static Color fillColor = null;
    private boolean line = true;
    private static boolean polyDone = false;

    //create toolbar and canvas panels
    private JPanel panelToolbar;
    public static VDRTool.Canvas canvas;

    //declare colour buttons
    private JButton buttonBlack, buttonWhite, buttonDarkGray, buttonLightGray,
            buttonBlue, buttonCyan, buttonGreen, buttonYellow, buttonRed,
            buttonOrange, buttonMagenta, buttonPink;
    //declare operation buttons
    private JButton buttonLine, buttonPlot, buttonRect, buttonEllipse, buttonPoly,
            buttonEndPoly, buttonPen, buttonFill, buttonLoad, buttonSave, buttonUndo,
            buttonColourPicker, buttonFillOff, buttonZoomIn, buttonZoomOut;

    //declare string for vec file storage
    private static String fileString = "";

    /**
     * a method to set the current pen colour
     * with parameter 'c'.
     *
     * @param c new pen colour
     */
    public static void setPenColour(Color c) {
        penColor = c;
    }

    /**
     * a method to get the current pen colour.
     *
     * @return current pen colour
     */
    public static Color getPenColour() {
        return penColor;
    }

    /**
     * a method to set the current fill colour
     * with parameter 'c'.
     *
     * @param c new fill colour
     */
    public static void setFillColour(Color c) {
        fillColor = c;
    }

    /**
     * a method to get current fill colour.
     *
     * @return current fill colour
     */
    public static Color getFillColour() {
        return fillColor;
    }

    /**
     * a method to set the VEC file string
     * with parameter 'string'.
     *
     * @param string new VEC file string
     */
    public static void setFileString(String string) {
        fileString = string;
    }

    /**
     * a method to add parameter 'string' to
     * current VEC file string.
     *
     * @param string new VEC file string addition
     */
    public static void addToFileString(String string) {
        fileString += string;
    }

    /**
     * a method to get current VEC file string.
     *
     * @return current VEC file string
     */
    public static String getFileString() {
        return fileString;
    }

    /**
     * a method that returns true if polygon is fully drawn
     * or false if not fully drawn.
     *
     * @return polygon drawn boolean status
     */
    public static boolean getPolyDone() {
        return polyDone;
    }

    /**
     * a method to get the canvas size.
     *
     * @return canvas size
     */
    public static int getCanvasSize() {
        return canvasSize;
    }

    /**
     * a method to initialise a frame.
     *
     * @param title the title of the frame
     * @throws HeadlessException
     */
    public VDRGui(String title) throws HeadlessException {
        super(title);
    }

    /**
     * a method to create a new canvas with
     * given width and height supplied by
     * parameters 'w' and 'h'.
     *
     * @param w canvas width
     * @param h canvas height
     * @return canvas object
     */
    private VDRTool.Canvas createCanvas(int w, int h) {
        VDRTool.Canvas cv = new VDRTool.Canvas();
        cv.setPreferredSize(new Dimension(w, h));
        return cv;
    }

    /**
     * a method that creates a tool button for
     * the GUI.
     *
     * @param str button label
     * @param toolNum tool number
     * @return tool button object
     */
    private JButton createToolButton(String str, final int toolNum) {
        //Create a JButton object and store it in a local var
        JButton jb = new JButton(str);

        //add an action listener to the JButton object
        jb.addActionListener(this);

        //set JButton attributes
        jb.setPreferredSize(new Dimension(100, 30));
        jb.setBackground(null);
        jb.setFont(new Font("Calibri", Font.BOLD, 14));
        jb.setFocusPainted(false);

        //add an action listener to the JButton
        jb.addActionListener(e -> {
            VDRTool.Canvas.setCurrentTool(toolNum);
            if (VDRTool.Canvas.getCurrentTool() == 11) {

                //create input colour variable
                Color chosenColour;

                //if pen colour tool chosen
                if (line) {
                    //open colour picker window
                    chosenColour = JColorChooser.showDialog(null, "Choose a pen colour", penColor);

                    /*
                    if a colour is chosen, store change in VEC file
                    and assign current pen colour with chosen colour
                     */
                    if (chosenColour != null) {
                        penColor = chosenColour;
                        fileString += "PEN " + VDRTool.Canvas.getHex(penColor) + "\n";
                        line = false;
                    }
                } else {
                    /*
                    if a colour is chosen, store change in VEC file
                    and assign current fill colour with chosen colour
                     */
                    chosenColour = JColorChooser.showDialog(null, "Choose fill colour", fillColor);
                    if (chosenColour != null) {
                        fillColor = chosenColour;
                        fileString += "FILL " + VDRTool.Canvas.getHex(fillColor) + "\n";
                        line = true;
                    }
                }
            } else if (VDRTool.Canvas.getCurrentTool() == 12) {
                //empty fill colour if no fill tool chosen
                fillColor = null;
            }
        });

        //Return the JButton object
        return jb;
    }

    /**
     * a method to create a colour button
     * for the colour palette.
     *
     * @param color colour of the button/ in palette
     * @return colour button object
     */
    private JButton createColourButton(Color color) {
        //Create a JButton object and store it in a local var
        JButton jb = new JButton("");

        //set JButton attributes
        jb.setPreferredSize(new Dimension(50, 30));
        jb.setBackground(color);
        jb.setFont(new Font("Calibri", Font.BOLD, 14));
        jb.setFocusPainted(false);

        //add an action listener to the JButton
        jb.addActionListener(e -> {
            //if pen colour tool chosen
            if (line) {
                /*
                store change in VEC file and assign current pen
                colour with chosen colour
                 */
                penColor = color;
                fileString += "PEN " + VDRTool.Canvas.getHex(color) + "\n";
                line = false;

            } else {
                /*
                store change in VEC file and assign current fill
                colour with chosen colour
                 */
                fillColor = color;
                fileString += "FILL " + VDRTool.Canvas.getHex(color) + "\n";
                line = true;

            }
        });

        //Return the JButton object
        return jb;
    }

    /**
     * a method that creates the GUI.
     */
    private void createGUI() {
        //set frame attributes
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //create new panel object for tools
        panelToolbar = new JPanel();

        //set background colour
        panelToolbar.setBackground(Color.GRAY);

        //create canvas
        canvas = createCanvas(canvasSize, canvasSize);

        //set background colour
        canvas.setBackground(Color.WHITE);

        //create tool and colour buttons
        buttonLoad = createToolButton("Load", 1);
        buttonSave = createToolButton("Save", 2);
        buttonBlack = createColourButton(Color.BLACK);
        buttonWhite = createColourButton(Color.WHITE);
        buttonDarkGray = createColourButton(Color.DARK_GRAY);
        buttonLightGray = createColourButton(Color.LIGHT_GRAY);
        buttonBlue = createColourButton(Color.BLUE);
        buttonCyan = createColourButton(Color.CYAN);
        buttonGreen = createColourButton(Color.GREEN);
        buttonYellow = createColourButton(Color.YELLOW);
        buttonRed = createColourButton(Color.RED);
        buttonOrange = createColourButton(Color.ORANGE);
        buttonMagenta = createColourButton(Color.MAGENTA);
        buttonPink = createColourButton(Color.PINK);
        buttonLine = createToolButton("Line", 3);
        buttonPlot = createToolButton("Plot", 4);
        buttonRect = createToolButton("Rect", 5);
        buttonEllipse = createToolButton("Elli", 6);
        buttonPoly = createToolButton("Poly", 7);
        buttonEndPoly = createToolButton("EndPoly", 7);
        buttonPen = createToolButton("Pen", 8);
        buttonFill = createToolButton("Fill", 9);
        buttonUndo = createToolButton("Undo", 10);
        buttonColourPicker = createToolButton("Colour", 11);
        buttonFillOff = createToolButton("No Fill", 12);
        buttonZoomIn = createToolButton("ZoomIn", 13);
        buttonZoomOut = createToolButton("ZoomOut", 14);

        //add button components to the tool bar panel
        layoutToolbarPanel();

        //add tool bar and canvas to the frame
        this.getContentPane().add(panelToolbar, BorderLayout.WEST);
        this.getContentPane().add(canvas, BorderLayout.CENTER);
        repaint();
        this.setResizable(false);
        this.setVisible(true);
    }

    /**
     * A convenience method to add a component to given grid bag
     * layout locations
     *  @param c           the component to add
     * @param constraints the grid bag constraints to use
     * @param x           the x grid position
     * @param y           the y grid position
     * @param w           the grid width
     */
    private void addToPanel(JPanel jp, Component c, GridBagConstraints constraints, int x, int y, int w) {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = w;
        constraints.gridheight = 1;
        jp.add(c, constraints);
    }

    /**
     * a method that adds all objects to the toolbar panel.
     */
    private void layoutToolbarPanel() {
        //set layout
        GridBagLayout layout = new GridBagLayout();
        panelToolbar.setLayout(layout);

        //set constraints
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 100;

        //add buttons to panel
        addToPanel(panelToolbar, buttonLoad, constraints, 0, 0, 3);
        addToPanel(panelToolbar, buttonSave, constraints, 0, 1, 3);
        addToPanel(panelToolbar, buttonUndo, constraints, 0, 2, 3);
        addToPanel(panelToolbar, buttonBlack, constraints, 0, 3, 1);
        addToPanel(panelToolbar, buttonWhite, constraints, 1, 3, 1);
        addToPanel(panelToolbar, buttonDarkGray, constraints, 0, 4, 1);
        addToPanel(panelToolbar, buttonLightGray, constraints, 1, 4, 1);
        addToPanel(panelToolbar, buttonBlue, constraints, 0, 5, 1);
        addToPanel(panelToolbar, buttonCyan, constraints, 1, 5, 1);
        addToPanel(panelToolbar, buttonGreen, constraints, 0, 6, 1);
        addToPanel(panelToolbar, buttonYellow, constraints, 1, 6, 1);
        addToPanel(panelToolbar, buttonRed, constraints, 0, 7, 1);
        addToPanel(panelToolbar, buttonOrange, constraints, 1, 7, 1);
        addToPanel(panelToolbar, buttonMagenta, constraints, 0, 8, 1);
        addToPanel(panelToolbar, buttonPink, constraints, 1, 8, 1);
        addToPanel(panelToolbar, buttonColourPicker, constraints, 0, 9, 3);
        addToPanel(panelToolbar, buttonPen, constraints, 0, 10, 3);
        addToPanel(panelToolbar, buttonFill, constraints, 0, 11, 3);
        addToPanel(panelToolbar, buttonFillOff, constraints, 0, 12, 3);
        addToPanel(panelToolbar, buttonLine, constraints, 0, 13, 3);
        addToPanel(panelToolbar, buttonPlot, constraints, 0, 14, 3);
        addToPanel(panelToolbar, buttonRect, constraints, 0, 15, 3);
        addToPanel(panelToolbar, buttonEllipse, constraints, 0, 16, 3);
        addToPanel(panelToolbar, buttonPoly, constraints, 0, 17, 3);
        addToPanel(panelToolbar, buttonEndPoly, constraints, 0, 18, 3);
        addToPanel(panelToolbar, buttonZoomIn, constraints, 0, 19, 3);
        addToPanel(panelToolbar, buttonZoomOut, constraints, 0, 20, 3);
    }

    /**
     * a method that overrides the abstract run method
     * to create a thread and run implemented method(s).
     */
    @Override
    public void run() {
        createGUI();
    }

    /**
     * a main method that runs the program.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new VDRGui("Vector Design Tool"));
    }

    /**
     * a method that overrides the action performed
     * method, and executes code in accordance to
     * an unique event.
     *
     * @param e event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //store event in a component
        Component source = (Component) e.getSource();

        if (source == buttonLoad) {
            canvasSize = 700;

            //open file chooser and set attributes to VEC files only
            final JFileChooser fc = new JFileChooser();
            fc.setAcceptAllFileFilterUsed(false);
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileNameExtensionFilter ff = new FileNameExtensionFilter("VEC File", "vec");
            fc.setFileFilter(ff);

            //store chosen file result
            int returnVal = fc.showOpenDialog(this);

            //check result validity
            if (returnVal == JFileChooser.APPROVE_OPTION) {

                // Reset the entire polygon tool
                fileString = "";
                canvas.shapes.clear();
                canvas.shapeFill.clear();
                canvas.shapeLine.clear();
                canvas.polygons.clear();
                canvas.polygonLine.clear();
                canvas.polygonFill.clear();
                canvas.polygonX.clear();
                canvas.polygonY.clear();
                canvas.numberOfVertices = canvas.polygonX.size();
                canvas.repaint();
                fillColor = null;
                penColor = Color.BLACK;

                //store chosen file in file variable
                File file = fc.getSelectedFile();

                //read chosen file
                FileReader fr = null;
                try {
                    fr = new FileReader(file);
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }

                //create buffer stream
                BufferedReader br = new BufferedReader(fr);

                //read a line of the file
                String st = null;
                try {
                    st = br.readLine();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                //while the file is not empty, draw input on canvas and read another line
                while (st != null) {
                    VDRTool.Canvas.drawString(st);

                    fileString += st + "\n";
                    try {
                        st = br.readLine();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        } else if (source == buttonSave) {

            //open file chooser and set attributes to VEC files only
            final JFileChooser fc = new JFileChooser();
            fc.setAcceptAllFileFilterUsed(false);
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileNameExtensionFilter ff = new FileNameExtensionFilter("VEC File", "vec");
            fc.setFileFilter(ff);

            //declare and select default save file name
            String fileName = "NewFile.vec";
            fc.setSelectedFile(new File(fileName));

            //store save file result
            int returnVal = fc.showSaveDialog(this);

            //check validity
            if (returnVal == JFileChooser.APPROVE_OPTION) {

                //get selected save file and check if extension is evident
                String selectedFileName = fc.getSelectedFile().getName();
                if (selectedFileName.contains(".vec")) {

                    PrintWriter writer = null;
                    try {
                        //create new print writer for the selected file
                        writer = new PrintWriter(fc.getSelectedFile());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    //insert command string
                    writer.print(fileString);
                    writer.close();

                } else {

                    //get selected save file and add extension
                    String filePath;
                    File directory = new File(fc.getSelectedFile().getAbsolutePath());
                    filePath = directory.toString() + ".vec";
                    System.out.println(filePath);
                    FileOutputStream saveFile = null;
                    try {
                        //create a file output stream
                        saveFile = new FileOutputStream(filePath);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }

                    //encode string to bytes
                    byte[] strToBytes = fileString.getBytes();

                    try {
                        //write bytes to the save file
                        saveFile.write(strToBytes);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    try {
                        //close file
                        saveFile.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }


        } else if (source == buttonFill) {
            //change current pen tool status
            line = false;
        } else if (source == buttonLine) {
            //change current pen tool status
            line = true;
        } else if (source == buttonUndo) {
            VDRTool.Canvas.undo();
        } else if (source == buttonEndPoly) {
            //change polygon drawing status
            polyDone = true;

            StringBuilder polyCoord = new StringBuilder();
            for (int i = 0; i < canvas.numberOfVertices; i++) {
                //convert polygon coordinates into a string
                polyCoord.append(VDRTool.Canvas.convertCoordinate(canvas.polygonX.get(i))).append(" ");
                polyCoord.append(VDRTool.Canvas.convertCoordinate(canvas.polygonY.get(i))).append(" ");
            }

            //add full command to VEC file
            String st = "POLYGON " + polyCoord + "\n";
            fileString += st;

            /*
            draw polygon, add pen and fill colour to respective arraylists,
            and reset polygon elements
            */
            Polygon p = canvas.drawPolygon(canvas.polygonX, canvas.polygonY, canvas.numberOfVertices);
            canvas.polygons.add(p);
            canvas.polygonLine.add(penColor);
            canvas.polygonFill.add(fillColor);
            canvas.repaint();
            canvas.polygonX.clear();
            canvas.polygonY.clear();
            canvas.numberOfVertices = canvas.polygonX.size();

        } else if (source == buttonPoly) {
            //change polygon drawing status
            polyDone = false;
        } else if (source == buttonZoomIn) {
            //increase canvas size and reset drawing elements
            canvasSize += 100;
            canvas.shapes.clear();
            canvas.shapeFill.clear();
            canvas.shapeLine.clear();
            canvas.polygons.clear();
            canvas.polygonLine.clear();
            canvas.polygonFill.clear();
            canvas.polygonX.clear();
            canvas.polygonY.clear();
            canvas.numberOfVertices = canvas.polygonX.size();
            canvas.repaint();

            //redraw updated shapes
            String[] strings = fileString.split("[\r\n]+");
            for (String string : strings) {
                VDRTool.Canvas.drawString(string);
            }

        } else if (source == buttonZoomOut) {
            //reduce canvas size and reset drawing elements
            if (canvasSize <= 100) {
                canvasSize -= 10;
            } else {
                canvasSize -= 100;
            }
            canvas.shapes.clear();
            canvas.shapeFill.clear();
            canvas.shapeLine.clear();
            canvas.polygons.clear();
            canvas.polygonLine.clear();
            canvas.polygonFill.clear();
            canvas.polygonX.clear();
            canvas.polygonY.clear();
            canvas.numberOfVertices = canvas.polygonX.size();
            canvas.repaint();

            //redraw updated shapes
            String[] strings = fileString.split("[\r\n]+");
            for (String string : strings) {
                Canvas.drawString(string);
            }
        }
    }
}