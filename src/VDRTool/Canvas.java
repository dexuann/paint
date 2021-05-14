package VDRTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents a canvas for vector designing
 *
 * @author Ashley De Xuan Poon n9629238
 * @author Khanh Duy Nguyen n9784616
 */
public class Canvas extends JComponent {
    //declare arraylists to store respective elements
    ArrayList<Shape> shapes = new ArrayList<>();
    ArrayList<Color> shapeLine = new ArrayList<>();
    ArrayList<Color> shapeFill = new ArrayList<>();
    ArrayList<Polygon> polygons = new ArrayList<>();
    ArrayList<Color> polygonLine = new ArrayList<>();
    ArrayList<Color> polygonFill = new ArrayList<>();
    ArrayList<Integer> polygonX = new ArrayList<>();
    ArrayList<Integer> polygonY = new ArrayList<>();

    //initialise variables
    int numberOfVertices;
    Shape shape;
    Point pointStart, pointEnd;
    private static int currentTool;


    /**
     * a method to set currenttool value with
     * parameter 'tool'.
     *
     * @param tool current tool value
     */
    public static void setCurrentTool(int tool){
        currentTool = tool;
    }

    /**
     * a method to get currenttool value.
     *
     * @return current tool value
     */
    public static int getCurrentTool() {
        return currentTool;
    }

    /**
     * a method to get the coordinate of a point
     * on the canvas.
     *
     * @param s coordinate in string form
     * @return coordinate scaled to canvas size
     */
    public static int getCoordinate(String s) {
        //convert string to double
        double d = Double.valueOf(s);
        return (int) Math.round(d * VDRGui.getCanvasSize());
    }

    /**
     * a method to convert the coordinate from
     * canvas for the command line string.
     *
     * @param x canvas coordinate value
     * @return converted coordinate value
     */
    public static String convertCoordinate(int x) {
        double d = (double) x;
        //convert coordinate value
        double value = d / VDRGui.getCanvasSize();
        //format and return converted value
        NumberFormat formatter = new DecimalFormat("#0.000000");
        return formatter.format(value);
    }

    /**
     * a method to get the hex value.
     *
     * @param c colour
     * @return hex value of input colour
     */
    public static String getHex(Color c) {
        return "#"+Integer.toHexString(c.getRGB()).substring(2);
    }

    /**
     * a method that draws a shape based on
     * the input string.
     *
     * @param s command string
     */
    public static void drawString(String s) {
        //split string into words
        String[] words = s.split("\\s+");

        //perform action based on input command
        switch (words[0]) {
            case "LINE": {
                Shape shape;
                /*
                draw a line according to given coordinates and add shape
                shape and colour attributes into respective arraylists
                and update canvas
                 */
                shape = VDRGui.canvas.drawLine(getCoordinate(words[1]), getCoordinate(words[2]),
                        getCoordinate(words[3]), getCoordinate(words[4]));
                VDRGui.canvas.shapes.add(shape);
                VDRGui.canvas.shapeLine.add(VDRGui.getPenColour());
                VDRGui.canvas.shapeFill.add(VDRGui.getFillColour());
                VDRGui.canvas.repaint();
                break;
            }
            case "ELLIPSE": {
                Shape shape;
                /*
                draw a ellipse according to given coordinates and add shape
                shape and colour attributes into respective arraylists
                and update canvas
                 */
                shape = VDRGui.canvas.drawEllipse(getCoordinate(words[1]), getCoordinate(words[2]),
                        getCoordinate(words[3]), getCoordinate(words[4]));
                VDRGui.canvas.shapes.add(shape);
                VDRGui.canvas.shapeLine.add(VDRGui.getPenColour());
                VDRGui.canvas.shapeFill.add(VDRGui.getFillColour());
                VDRGui.canvas.repaint();
                break;
            }
            case "RECTANGLE": {
                Shape shape;
                /*
                draw a rectangle according to given coordinates and add shape
                shape and colour attributes into respective arraylists
                and update canvas
                 */
                shape = VDRGui.canvas.drawRectangle(getCoordinate(words[1]), getCoordinate(words[2]),
                        getCoordinate(words[3]), getCoordinate(words[4]));
                VDRGui.canvas.shapes.add(shape);
                VDRGui.canvas.shapeLine.add(VDRGui.getPenColour());
                VDRGui.canvas.shapeFill.add(VDRGui.getFillColour());
                VDRGui.canvas.repaint();
                break;
            }
            case "POLYGON": {
                Polygon polygon;
                /*
                for loop that gets the x and y coordinates according
                to every even and odd word after the first
                 */
                for (int i = 1; i < words.length; i++) {
                    if((i % 2) == 0) {
                        VDRGui.canvas.polygonY.add(getCoordinate(words[i]));
                    } else {
                        VDRGui.canvas.polygonX.add(getCoordinate(words[i]));
                    }
                }
                VDRGui.canvas.numberOfVertices = VDRGui.canvas.polygonX.size();
                /*
                draw a polygon according to given coordinates and add shape
                shape and colour attributes into respective arraylists
                and update canvas
                 */
                Polygon p = VDRGui.canvas.drawPolygon(VDRGui.canvas.polygonX, VDRGui.canvas.polygonY, VDRGui.canvas.numberOfVertices);
                VDRGui.canvas.polygons.add(p);
                VDRGui.canvas.polygonLine.add(VDRGui.getPenColour());
                VDRGui.canvas.polygonFill.add(VDRGui.getFillColour());
                VDRGui.canvas.repaint();

                //reset polygon elements
                VDRGui.canvas.polygonX.clear();
                VDRGui.canvas.polygonY.clear();
                VDRGui.canvas.numberOfVertices = VDRGui.canvas.polygonX.size();

                break;
            }
            case "PLOT": {
                Shape shape;
                /*
                draw a plot according to given coordinates and add shape
                shape and colour attributes into respective arraylists
                and update canvas
                 */
                shape = VDRGui.canvas.drawLine(getCoordinate(words[1]), getCoordinate(words[2]),
                        getCoordinate(words[1]), getCoordinate(words[2]));
                VDRGui.canvas.shapes.add(shape);
                VDRGui.canvas.shapeLine.add(VDRGui.getPenColour());
                VDRGui.canvas.shapeFill.add(null);
                VDRGui.canvas.repaint();
                break;
            }
            case "PEN": {
                //set new pen colour
                VDRGui.setPenColour(Color.decode(words[1]));
                break;
            }
            case "FILL": {
                if (words[1].equals("OFF")) {
                    //set empty fill colour
                    VDRGui.setFillColour(null);
                } else {
                    //set new fill colour
                    VDRGui.setFillColour(Color.decode(words[1]));
                }
                break;
            }
        }
    }

    /**
     * a method that reverts the canvas to the state
     * prior to the latest drawing.
     */
    public static void undo() {
        //split string into lines
        String[] lines = VDRGui.getFileString().split("[\r\n]+");
        StringBuilder drawingCommands = new StringBuilder();

        //for loop to iterate through each command line
        for (String line : lines) {
            //filter then add drawing command lines
            if (!line.contains("PEN") && !line.contains("FILL")) {
                drawingCommands.append(line).append("\n");
            }
        }

        //split string into lines
        String[] commandLines = drawingCommands.toString().split("[\r\n]+");
        String removeCommand;

        if (commandLines.length > 1) {
            //remove last element if there is more than one command
            removeCommand = commandLines[commandLines.length - 1];
        } else if (commandLines.length == 1) {
            //remove sole element if there is only one command
            removeCommand = commandLines[0];
        } else {
            removeCommand = null;
        }

        //set VEC file string
        VDRGui.setFileString("");

        //for loop to iterate through each command line
        for (String line : lines) {
            //filter for command that does not have to be removed
            if (!line.equals(removeCommand)) {
                //add command line to VEC file
                VDRGui.addToFileString(line + "\n");
            }
        }
        //reset drawing elements and update canvas
        VDRGui.canvas.shapes.clear();
        VDRGui.canvas.shapeFill.clear();
        VDRGui.canvas.shapeLine.clear();
        VDRGui.canvas.polygons.clear();
        VDRGui.canvas.polygonLine.clear();
        VDRGui.canvas.polygonFill.clear();
        VDRGui.canvas.polygonX.clear();
        VDRGui.canvas.polygonY.clear();
        VDRGui.canvas.numberOfVertices = VDRGui.canvas.polygonX.size();
        VDRGui.canvas.repaint();

        //redraw updated shapes
        String[] strings = VDRGui.getFileString().split("[\r\n]+");
        for (String string : strings) {
            drawString(string);
        }
    }

    /**
     * a method to create a canvas object.
     */
    Canvas() {
        //set background colour
        setBackground(Color.WHITE);
        //add a mouselistner component
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //assign a start point with each mouse press
                pointStart = new Point(e.getX(), e.getY());

                /*
                if polygon tool selected, add coordinates to polygon
                x and y arraylists, assign the number of vertices
                and update canvas
                 */
                if (currentTool == 7) {
                    if (!VDRGui.getPolyDone()) {
                        polygonX.add(e.getX());
                        polygonY.add(e.getY());
                        numberOfVertices = polygonX.size();
                    }
                }
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //perform action based on currenttool with every mouse release
                switch (currentTool) {
                    case 3: {
                        /*
                        draw a line according to the start and end point on the canvas
                        , add shape and colour attributes into respective arraylists and
                        insert command line into VEC file
                         */
                        String st = "LINE " + convertCoordinate(pointStart.x) + " " + convertCoordinate(pointStart.y)
                                + " " + convertCoordinate(e.getX()) + " " + convertCoordinate(e.getY()) + "\n";
                        VDRGui.addToFileString(st);
                        shape = drawLine(pointStart.x, pointStart.y, e.getX(), e.getY());
                        shapes.add(shape);
                        shapeLine.add(VDRGui.getPenColour());
                        shapeFill.add(VDRGui.getFillColour());

                        break;
                    }
                    case 4: {
                         /*
                        draw a plot according to the start point on the canvas
                        , add shape and colour attributes into respective arraylists and
                        insert command line into VEC file
                         */
                        String st = "PLOT " + convertCoordinate(pointStart.x) + " " + convertCoordinate(pointStart.y) + "\n";
                        VDRGui.addToFileString(st);
                        shape = drawLine(pointStart.x, pointStart.y, pointStart.x, pointStart.y);
                        shapes.add(shape);
                        shapeLine.add(VDRGui.getPenColour());
                        shapeFill.add(null);

                        break;
                    }
                    case 5: {
                         /*
                        draw a rectangle according to the start and end point on the canvas
                        , add shape and colour attributes into respective arraylists and
                        insert command line into VEC file
                         */
                        String st = "RECTANGLE " + convertCoordinate(pointStart.x) + " " + convertCoordinate(pointStart.y)
                                + " " + convertCoordinate(e.getX()) + " " + convertCoordinate(e.getY()) + "\n";
                        VDRGui.addToFileString(st);
                        shape = drawRectangle(pointStart.x, pointStart.y, e.getX(), e.getY());
                        shapes.add(shape);
                        shapeLine.add(VDRGui.getPenColour());
                        shapeFill.add(VDRGui.getFillColour());

                        break;
                    }
                    case 6: {
                         /*
                        draw a ellipse according to the start and end point on the canvas
                        , add shape and colour attributes into respective arraylists and
                        insert command line into VEC file
                         */
                        String st = "ELLIPSE " + convertCoordinate(pointStart.x) + " " + convertCoordinate(pointStart.y)
                                + " " + convertCoordinate(e.getX()) + " " + convertCoordinate(e.getY()) + "\n";
                        VDRGui.addToFileString(st);
                        shape = drawEllipse(pointStart.x, pointStart.y, e.getX(), e.getY());
                        shapes.add(shape);
                        shapeLine.add(VDRGui.getPenColour());
                        shapeFill.add(VDRGui.getFillColour());

                        break;
                    }
                }
                //reset start and end point and update canvas
                pointStart = null;
                pointEnd = null;
                repaint();
            }
        });
        //add a motionlistner component
        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                //get the final x and y pos as the mouse drags and update canvas
                pointEnd = new Point(e.getX(), e.getY());
                repaint();
            }
        });
    }

    /**
     * a method that constructs a rectangle for the canvas.
     *
     * @param x1 start x
     * @param y1 start y
     * @param x2 end x
     * @param y2 end y
     * @return rectangle shape
     */
    public Rectangle2D.Float drawRectangle(
            int x1, int y1, int x2, int y2){

        // set x and y as the smaller value between x1, x2 and y1, y2
        int x = Math.min(x1, x2);
        int y = Math.min(y1, y2);

        // get distance between start and end coordinates
        int width = Math.abs(x1 - x2);
        int height = Math.abs(y1 - y2);

        //return rectangle shape
        return new Rectangle2D.Float(x, y, width, height);
    }

    /**
     * a method that constructs an ellipse for the canvas.
     * @param x1 start x
     * @param y1 start y
     * @param x2 end x
     * @param y2 end y
     * @return ellipse shape
     */
    public Ellipse2D.Float drawEllipse(int x1, int y1, int x2, int y2){

        // set x and y as the smaller value between x1, x2 and y1, y2
        int x = Math.min(x1, x2);
        int y = Math.min(y1, y2);

        // get distance between start and end coordinates
        int width = Math.abs(x1 - x2);
        int height = Math.abs(y1 - y2);

        //return rectangle shape
        return new Ellipse2D.Float(x, y, width, height);
    }

    /**
     * a method that constructs a line for the canvas.
     *
     * @param x1 start x
     * @param y1 start y
     * @param x2 end x
     * @param y2 end y
     * @return line shape
     */
    public Line2D.Float drawLine(int x1, int y1, int x2, int y2){
        return new Line2D.Float(x1, y1, x2, y2);
    }

    /**
     * a method that constructs a polygon for the canvas.
     *
     * @param x arraylist of x coordinates
     * @param y arraylist of y coordinates
     * @param numberOfPoints number of points in the polygon
     * @return polygon shape
     */
    public Polygon drawPolygon(ArrayList<Integer> x, ArrayList<Integer> y, int numberOfPoints) {

        //convert x and y arraylists into arrays
        int[] xx = new int[x.size()];
        for (int i=0; i < xx.length; i++) {
            xx[i] = x.get(i);
        }

        int[] yy = new int[y.size()];
        for (int i=0; i < yy.length; i++) {
            yy[i] = y.get(i);
        }

        //return polygon shape
        return new Polygon(xx, yy, numberOfPoints);
    }

    /**
     * a method that would draw the graphics on the canvas
     *
     * @param g graphics object
     */
    public void paint(Graphics g){
        //cast into a 2d graphics object
        Graphics2D drawings = (Graphics2D) g;

        //make a white background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, VDRGui.getCanvasSize(), VDRGui.getCanvasSize());

        //set line width
        drawings.setStroke(new BasicStroke(2));

        //create iterators to go through shape, pen and fill colours
        Iterator<Color> lineCount = shapeLine.iterator();
        Iterator<Color> fillCount = shapeFill.iterator();
        Iterator<Color> poFillCount = polygonFill.iterator();
        Iterator<Color> poLineCount = polygonLine.iterator();

        //for loop to draw each polygon with its respective attributes
        for (Polygon polygon : polygons) {
            g.setColor(poLineCount.next());
            g.drawPolygon(polygon);
            Color fill = poFillCount.next();
            g.setColor(fill);
            if(fill != null) {
                g.fillPolygon(polygon);
            }
        }

        //for loop to draw the other shape(s) with its respective attributes
        for (Shape shape : shapes){
            drawings.setPaint(lineCount.next());
            drawings.draw(shape);
            Color fill = fillCount.next();
            drawings.setPaint(fill);
            if(fill != null) {
                drawings.fill(shape);
            }
        }
    }
}