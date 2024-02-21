import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Main extends Application {

    private static final int HEX_RADIUS = 20;
    private static final Layout layout = new Layout(Layout.pointy, new Point(2 * HEX_RADIUS, 2 * HEX_RADIUS), new Point(400, 300));

    public static void main(String[] args) {
        launch(args);
    }

    private Random random;
    private ArrayList<Integer> atoms;
    private int showAtom;
    private Pane hexGridPane; // Store the hexagonal grid pane
    private Board gameBoard;

    @Override
    public void start(Stage primaryStage) {
        random = new Random();
        atoms = new ArrayList<>();
        gameBoard = new Board();
        setAtoms();
        Text gameTitle = new Text("Black Box+");
        gameTitle.setFont(Font.font(50));
        gameTitle.setFill(Color.RED);
        gameTitle.setLayoutX(400);
        gameTitle.setLayoutY(50);
        gameTitle.setFont(Font.font(gameTitle.getFont().getFamily(), FontWeight.BOLD, gameTitle.getFont().getSize()));
        gameTitle.setUnderline(true);

        primaryStage.setTitle("Hexagonal Grid");
        Button button = new Button("Show Atoms");
        button.setMinSize(40, 80);
        button.setLayoutY(100);
        button.setOnAction(event -> handleButtonClick());

        Button stopShowing = new Button("Stop Showing");
        stopShowing.setMinSize(40, 80);
        stopShowing.setOnAction(event -> handleShowClick());
        stopShowing.setLayoutX(100);
        stopShowing.setLayoutY(100);

        Button setRandom = new Button("Set Atoms");
        setRandom.setMinSize(40, 80);
        setRandom.setOnAction(event -> handleSetClick());
        setRandom.setLayoutX(210);
        setRandom.setLayoutY(100);

        hexGridPane = createHexGrid(); // Initialize hexagonal grid
        Pane rootPane = new Pane(hexGridPane); // Create a root pane and add the hex grid to it
        rootPane.getChildren().addAll(button, stopShowing, setRandom, gameTitle); // Add the button to the root pane

        Scene scene = new Scene(rootPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void handleSetClick(){
        setAtoms();
        updateHexGrid();
    }

    public void handleShowClick(){
        this.showAtom = 0;
        updateHexGrid();
    }

    private Pane createHexGrid() {
        Pane pane = new Pane();

        int[] hexCounts = {5, 6, 7, 8, 9, 8, 7, 6, 5};

        int totalRows = hexCounts.length;
        int maxWidth = hexCounts[totalRows - 1];
        double gridWidth = maxWidth * 3 * HEX_RADIUS;
        double gridHeight = totalRows * 1.5 * HEX_RADIUS;

        int count = 0;

        for (int row = 0; row < totalRows; row++) {
            int hexCount = hexCounts[row];

            for (int col = 0; col < hexCount; col++) {
                int q = 0;
                if (row == 2 || row == 3) {
                    q = col - hexCount / 2 - 1;
                } else if (row == 4 || row == 5) {
                    q = col - hexCount / 2 - 2;
                } else if (row == 6 || row == 7) {
                    q = col - hexCount / 2 - 3;
                } else if (row == 8) {
                    q = col - hexCount / 2 - 4;
                } else {
                    q = col - hexCount / 2;
                }

                int r = row - (totalRows - 1) / 2;

                Hex hex = new Hex(q, r, -q - r);
                Point hexCenter = layout.hexToPixel(hex);
                double startX = (pane.getWidth() - gridWidth) / 2;
                double startY = (pane.getHeight() - gridHeight) / 2;
                drawHexagon(pane, new Point(hexCenter.x + startX, hexCenter.y + startY), count);
                count++;
            }
        }

        return pane;
    }

    public void handleButtonClick() {
        this.showAtom = 100;
        updateHexGrid(); // Update the hex grid when the button is clicked
    }

    private void updateHexGrid() {

        // Iterate over existing hexagons and update their colors
        for (Node node : hexGridPane.getChildren()) {
            if (node instanceof Polygon) {
                Polygon hexagon = (Polygon) node;
                int count = hexGridPane.getChildren().indexOf(hexagon) - 1; // Subtract 1 for the button
                if (atoms.contains(count) && showAtom == 100) {
                    hexagon.setFill(Color.RED); // Set color to red for atoms
                } else {
                    hexagon.setFill(Color.BLACK); // Set color to black for non-atoms
                }
            }
        }

    } // End of UpdateHexGrid


    private void setAtoms() {
        // Check if atoms have been previously allocated, and if so, deallocate them
        if(!atoms.isEmpty()){
            for(int dummy : atoms){
                gameBoard.cells[dummy].setCell_situation(Cell.situation.EMPTY);
            }
        }

        // Clear the atoms List
        atoms.clear();

        // Create a list of unique numbers from 0 to 60 (Because cells array has cells numbered from 0-60)
        List<Integer> uniqueNumbers = new ArrayList<>();
        for (int i = 0; i < 61; i++) {
            uniqueNumbers.add(i);
        }

        // Shuffle the list to randomize the order
        Collections.shuffle(uniqueNumbers);

        // Select the first six numbers from the shuffled list
        for (int i = 0; i < 6; i++) {
            atoms.add(uniqueNumbers.get(i));
        }

        for(int dummy : atoms){
            gameBoard.cells[dummy].setCell_situation(Cell.situation.ATOM);
        }
    }

    private void drawHexagon(Pane pane, Point center, int count) {
        ArrayList<Point> corners = layout.polygonCorners(new Hex(0, 0, 0));
        Polygon hexagon = new Polygon();
        for (Point corner : corners) {
            hexagon.getPoints().addAll(center.x + corner.x, center.y + corner.y);
        }
        if (atoms.contains(count) && showAtom == 100) hexagon.setFill(Color.RED);
        else hexagon.setFill(Color.BLACK);
        hexagon.setStroke(Color.WHITE);
        pane.getChildren().add(hexagon);
    }

    public static class Point {
        public final double x;
        public final double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class Hex {
        public final int q;
        public final int r;
        public final int s;

        public Hex(int q, int r, int s) {
            this.q = q;
            this.r = r;
            this.s = s;
            if (q + r + s != 0) throw new IllegalArgumentException("q + r + s must be 0");
        }
    }

    public static class Layout {
        public final Orientation orientation;
        public final Point size;
        public final Point origin;

        static public Orientation pointy = new Orientation(
                Math.sqrt(3.0), Math.sqrt(3.0) / 2.0, 0.0, 3.0 / 2.0,
                Math.sqrt(3.0) / 3.0, -1.0 / 3.0, 0.0, 2.0 / 3.0, 0.5);

        static public Orientation flat = new Orientation(
                3.0 / 2.0, 0.0, Math.sqrt(3.0) / 2.0, Math.sqrt(3.0),
                2.0 / 3.0, 0.0, -1.0 / 3.0, Math.sqrt(3.0) / 3.0, 0.0);


        public Layout(Orientation orientation, Point size, Point origin) {
            this.orientation = orientation;
            this.size = size;
            this.origin = origin;
        }

        public Point hexToPixel(Hex h) {
            double x = (orientation.f0 * h.q + orientation.f1 * h.r) * size.x;
            double y = (orientation.f2 * h.q + orientation.f3 * h.r) * size.y;
            return new Point(x + origin.x, y + origin.y);
        }

        public ArrayList<Point> polygonCorners(Hex h) {
            ArrayList<Point> corners = new ArrayList<>();
            Point center = hexToPixel(h);
            for (int i = 0; i < 6; i++) {
                Point offset = hexCornerOffset(i);
                corners.add(new Point(center.x + offset.x, center.y + offset.y));
            }
            return corners;
        }

        public Point hexCornerOffset(int corner) {
            double angle = 2.0 * Math.PI * (orientation.start_angle - corner) / 6.0;
            return new Point(size.x * Math.cos(angle), size.y * Math.sin(angle));
        }
    }

    public static class Orientation {
        public final double f0;
        public final double f1;
        public final double f2;
        public final double f3;
        public final double b0;
        public final double b1;
        public final double b2;
        public final double b3;
        public final double start_angle;

        public Orientation(double f0, double f1, double f2, double f3, double b0, double b1, double b2, double b3, double start_angle) {
            this.f0 = f0;
            this.f1 = f1;
            this.f2 = f2;
            this.f3 = f3;
            this.b0 = b0;
            this.b1 = b1;
            this.b2 = b2;
            this.b3 = b3;
            this.start_angle = start_angle;
        }
    }
}
