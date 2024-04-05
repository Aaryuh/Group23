import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Main extends Application {
    private ArrayList<Integer> guessedAtoms; ///---------
    private Text userGuess; /////------
    private Timeline timeline; // -----
    private Timeline newTimeline;
    private int timeFlag;

    private ArrayList<Main.Point> cellLocation;
    private ArrayList<Main.Point> buttonLocation;
    private Text raysUsed;
    public ArrayList<ArrayList<Integer>> listOfRayPaths;

    private static final int HEX_RADIUS = 20;
    private static final Layout layout = new Layout(Layout.pointy, new Point(2 * HEX_RADIUS, 2 * HEX_RADIUS), new Point(400, 300));

    public static void main(String[] args) {
        launch(args);
    }

    private Random random;
    private ArrayList<Integer> atoms;
    private int showAtom; // flag -- if enabled(show atoms) or disabled (hide atoms)
    private Pane hexGridPane; // Store the hexagonal grid pane
    private Board gameBoard;
    private ArrayList<Button> buttons; // List of buttons for the edges 0 -> 53
    private static int rayCount; // Count of the number of rays sent
    private Pane rootPane;

    @Override
    public void start(Stage primaryStage) {
        timeFlag = 0;
        guessedAtoms = new ArrayList<>(); ///---------

        listOfRayPaths = new ArrayList<>();

        cellLocation = new ArrayList<>();
        setCelllocations(cellLocation);
        buttonLocation = new ArrayList<>();
        setButtonLocations(buttonLocation);

        rayCount = 0;
        buttons = new ArrayList<>();
        random = new Random();
        atoms = new ArrayList<>();
        gameBoard = new Board();
        // setAtoms();
        //System.out.println(atoms);
        Text gameTitle = new Text("Black Box+");
        gameTitle.setFont(Font.font(50));
        gameTitle.setFill(Color.RED);
        gameTitle.setLayoutX(400);
        gameTitle.setLayoutY(50);
        gameTitle.setFont(Font.font(gameTitle.getFont().getFamily(), FontWeight.BOLD, gameTitle.getFont().getSize()));
        gameTitle.setUnderline(true);

        primaryStage.setTitle("Black Box+ By Group 23");
        Button button = new Button("Show Atoms");
        button.setMinSize(40, 80);
        button.setLayoutY(100);
        button.setOnAction(event -> handleButtonClick());

        Button stopShowing = new Button("Stop Showing");
        stopShowing.setMinSize(40, 80);
        stopShowing.setOnAction(event -> handleShowClick());
        stopShowing.setLayoutX(100);
        stopShowing.setLayoutY(100);

        /*Button setRandom = new Button("Set Atoms");
        setRandom.setMinSize(40, 80);
        setRandom.setOnAction(event -> handleSetClick());
        setRandom.setLayoutX(210);
        setRandom.setLayoutY(100);*/

        hexGridPane = createHexGrid(); // Initialize hexagonal grid
        rootPane = new Pane(hexGridPane); // Create a root pane and add the hex grid to it
        rootPane.getChildren().addAll(button, stopShowing, gameTitle); // Add the button to the root pane
        setRayButtons(rootPane); // Makes and sets the coordinates for the edge buttons
        setButtonOnAction(); // Set action for each edge button

        // Setting Cell numbers
        setCellNumbers();
        setAtoms();

        // ------
//        timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> updateHexGrid()));
//        timeline.setCycleCount(Animation.INDEFINITE);
//        timeline.play();
        setHexClicks();
        // updateHexGrid();

        Scene scene = new Scene(rootPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    } // End of Start

    public void setHexClicks(){
        for (Node node : hexGridPane.getChildren()) {
            if (node instanceof Polygon) {
                Polygon hexagon = (Polygon) node;
                int count = hexGridPane.getChildren().indexOf(hexagon);
                hexagon.setOnMouseClicked(event -> {
                    double clickX = event.getX();
                    double clickY = event.getY();
                    double centerX = cellLocation.get(count).x;
                    double centerY = cellLocation.get(count).y;
                    double distance = Math.sqrt(Math.pow(clickX - centerX, 2) + Math.pow(clickY - centerY, 2));

                    if (distance <= 30) { // Check if click is within 30 radius
                        guessAtom(count);
                        updateGuess();
                        updateHexGrid();
                    }
                });
            }
        }
    }

    public void addCircleOfInfluence(){
        for(int i = 0; i < atoms.size(); i++){
            Circle dummy = new Circle();
            dummy.setCenterX(cellLocation.get(atoms.get(i)).x);
            dummy.setCenterY(cellLocation.get(atoms.get(i)).y);
            dummy.setRadius(60);
            dummy.setFill(null);
            dummy.setStroke(Color.PURPLE);
            dummy.setStrokeType(StrokeType.OUTSIDE);
            dummy.getStrokeDashArray().addAll(5d, 5d);
            rootPane.getChildren().add(dummy);
        }
    } // End Of CIRCLE Of Influence

    public void setCellNumbers(){

        //
        for(int i = 0; i < 61; i++){
            Text dummy = new Text(""+i);
            dummy.setFont(Font.font(15));
            dummy.setFill(Color.CYAN);
            dummy.setLayoutX(cellLocation.get(i).x);
            dummy.setLayoutY(cellLocation.get(i).y);
            rootPane.getChildren().add(dummy);
        }

    }

    private void setButtonLocations(ArrayList<Main.Point> list) {

        list.add(new Point(354.4, 197.6)); // 0
        list.add(new Point(384, 194.4)); // 1
        list.add(new Point(424.8, 197.6)); // 2
        list.add(new Point(453.6,193.6)); // 3
        list.add(new Point(493.6,197.6)); // 4
        list.add(new Point(524.8,195.2)); // 5
        list.add(new Point(560.8,199.2)); // 6
        list.add(new Point(592.8,193.6)); // 7
        list.add(new Point(631.2,197.6)); // 8
        list.add(new Point(661.6,192.8)); // 9
        list.add(new Point(682.4,222.4)); // 10
        list.add(new Point(698.4,255.2)); // 11
        list.add(new Point(716.8,283.2)); // 12
        list.add(new Point(733.6,314.4)); // 13
        list.add(new Point(752,342.4)); // 14
        list.add(new Point(768,373.6)); // 15
        list.add(new Point(785.8,399.2)); // 16
        list.add(new Point(803.2,436)); // 17
        list.add(new Point(820,464)); // 18
        list.add(new Point(802.4,493.6)); // 19
        list.add(new Point(784.8,525.6)); // 20
        list.add(new Point(768.8,552)); // 21
        list.add(new Point(751.2,585.6)); // 22
        list.add(new Point(738.4,610.4)); // 23
        list.add(new Point(716,644.8)); // 24
        list.add(new Point(702.4,670.4)); // 25
        list.add(new Point(683.2,707.2)); // 26
        list.add(new Point(669.6,730.4)); // 27
        list.add(new Point(634.4,732)); // 28
        list.add(new Point(598.4,731.2)); // 29
        list.add(new Point(560.8,730.4)); // 30
        list.add(new Point(528,732.8)); // 31
        list.add(new Point(495.2,732)); // 32
        list.add(new Point(459.2,732.8)); // 33
        list.add(new Point(426.4,731.2)); // 34
        list.add(new Point(390.4,732.8)); // 35
        list.add(new Point(353.6,730.4)); // 36
        list.add(new Point(339.2,704.8)); // 37
        list.add(new Point(318.4,669.6)); // 38
        list.add(new Point(304.8,643.2)); // 39
        list.add(new Point(280,608.8)); // 40
        list.add(new Point(270.4,584)); // 41
        list.add(new Point(252.8,552)); // 42
        list.add(new Point(235.2,521.6)); // 43
        list.add(new Point(216.8,491.2)); // 44
        list.add(new Point(200.8,466.4)); // 45
        list.add(new Point(217.6,436)); // 46
        list.add(new Point(236,404)); // 47
        list.add(new Point(249.6,376)); // 48
        list.add(new Point(270.4,343.2)); // 49
        list.add(new Point(288.8,315.2)); // 50
        list.add(new Point(305.6,284.8)); // 51
        list.add(new Point(321.6,256)); // 52
        list.add(new Point(339.2,224)); // 53

    } // End of button Locations

    private void setCelllocations(ArrayList<Main.Point> list) {

        list.add(new Point(372,225.6)); // 0
        list.add(new Point(441.6,225.6)); // 1
        list.add(new Point(512,225.6)); // 2
        list.add(new Point(580,225.6)); // 3
        list.add(new Point(649.6,225.6)); // 4
        list.add(new Point(336,284)); // 5
        list.add(new Point(406.4,284)); // 6
        list.add(new Point(476,284)); // 7
        list.add(new Point(545.6,284)); // 8
        list.add(new Point(615.2,284)); // 9
        list.add(new Point(685.6,284)); // 10
        list.add(new Point(301.6,344)); // 11
        list.add(new Point(371.2,344)); // 12
        list.add(new Point(441.6,344)); // 13
        list.add(new Point(512,344)); // 14
        list.add(new Point(580,344)); // 15
        list.add(new Point(649.6,344)); // 16
        list.add(new Point(718.4,344)); // 17
        list.add(new Point(268,405.6)); // 18
        list.add(new Point(335.2,405.6)); // 19
        list.add(new Point(407.2,405.6)); // 20
        list.add(new Point(476,405.6)); // 21
        list.add(new Point(544,405.6)); // 22
        list.add(new Point(614.4,405.6)); // 23
        list.add(new Point(684,405.6)); // 24
        list.add(new Point(755.2,405.6)); // 25
        list.add(new Point(232,463.2)); // 26 ------------- 5th row
        list.add(new Point(304,463.2)); // 27
        list.add(new Point(374.4,463.2)); // 28
        list.add(new Point(440,463.2)); // 29
        list.add(new Point(511.2,463.2)); // 30
        list.add(new Point(581.6,463.2)); // 31
        list.add(new Point(649.6,463.2)); // 32
        list.add(new Point(720.8,463.2)); // 33
        list.add(new Point(790,463.2)); // 34
        list.add(new Point(268.8,524)); // 35 ------------- 6th row
        list.add(new Point(338.4,524)); // 36
        list.add(new Point(405.6,524)); // 37
        list.add(new Point(476,524)); // 38
        list.add(new Point(546.4,524)); // 39
        list.add(new Point(615,524)); // 40
        list.add(new Point(682.4,524)); // 41
        list.add(new Point(754.4,524)); // 42
        list.add(new Point(303.2,582.4)); // 43 ----------- 7th row
        list.add(new Point(371,582.4)); // 44
        list.add(new Point(440,582.4)); // 45
        list.add(new Point(508.8,582.4)); // 46
        list.add(new Point(582.4,582.4)); // 47
        list.add(new Point(651,582.4)); // 48
        list.add(new Point(721.6,582.4)); // 49
        list.add(new Point(337.6,643.2)); // 50 ----------- 8th row
        list.add(new Point(408,643.2)); // 51
        list.add(new Point(473.6,643.2)); // 52
        list.add(new Point(546.4,643.2)); // 53
        list.add(new Point(616,643.2)); // 54
        list.add(new Point(686.4,643.2)); // 55
        list.add(new Point(372.8,702.4)); // 56 ----------- 9th row
        list.add(new Point(443.2,702.4)); // 57
        list.add(new Point(512,702.4)); // 58
        list.add(new Point(582.4,702.4)); // 59
        list.add(new Point(652,702.4)); // 60


    } // End of setting cell locations

    public void setButtonOnAction() { // Assigns the action for each button
        for(int i = 0; i < buttons.size(); i++){
            int finalI = i;
            buttons.get(i).setOnAction(event -> handleEdgeButtonClick(finalI));
        }
    }

    public void handleEdgeButtonClick(int num){ // Performs the assigned action when an edge button is clicked
        rayCount++; // Increase Count

        rootPane.getChildren().remove(raysUsed);

        raysUsed = new Text("Rays Used: "+rayCount);
        raysUsed.setFont(Font.font(30));
        raysUsed.setFill(Color.CYAN);
        raysUsed.setLayoutX(0);
        raysUsed.setLayoutY(75);
        raysUsed.setFont(Font.font(raysUsed.getFont().getFamily(), FontWeight.BOLD, raysUsed.getFont().getSize()));
        rootPane.getChildren().add(raysUsed);

        buttons.get(num).setDisable(true); // Disable Button

        Cell exitCell = gameBoard.cells[buttonNumberToCellNumber(num)].newEntryAndExit(buttonNumberToEdge(num));
        ArrayList<Integer> path = gameBoard.cells[buttonNumberToCellNumber(num)].rayPath(buttonNumberToEdge(num));
        // listOfRayPaths.add(path);
        System.out.println(path); // path

        ArrayList<Integer> dummy = new ArrayList<>();
        dummy.add(num);
        dummy.addAll(path);

        Result result = new Result(exitCell.getCellNumber(), exitCell.exitEdge);
        result.setSituation(exitCell.getCell_situation());

        int exitButtonNumber = findExitButtonNumber(result);
        if(exitButtonNumber != -1 && result.situation != Cell.situation.ATOM){
            dummy.add(-100);
            dummy.add(exitButtonNumber);
        }
        else{
            dummy.add(-1);
        }
        listOfRayPaths.add(dummy);
//        System.out.println(result.situation);
        if(result.situation == Cell.situation.ATOM){
            buttons.get(num).setStyle("-fx-background-color: red");
        }
        else{
            buttons.get(num).setText(""+rayCount);
            if(exitButtonNumber != -1){
                buttons.get(exitButtonNumber).setText(""+rayCount);
                buttons.get(exitButtonNumber).setDisable(true);
            }
        }

        updateHexGrid();

    }

    public void handleSetClick(){
        setAtoms();
        updateHexGrid();
    }

    public void handleShowClick(){
        this.showAtom = 0;
        rootPane.getChildren().removeIf(node -> node instanceof Circle);
        rootPane.getChildren().removeIf(node -> node instanceof Line);
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
        updateGuess();
        updateHexGrid(); // Update the hex grid when the button is clicked
    }

    private void updateHexGrid() {

        // Iterate over existing hexagons and update their colors
        for (Node node : hexGridPane.getChildren()) {
            if (node instanceof Polygon) {
                Polygon hexagon = (Polygon) node;
                int count = hexGridPane.getChildren().indexOf(hexagon);
                if(atoms.contains(count) && guessedAtoms.contains(count) && showAtom == 100){
                    hexagon.setFill(Color.GREEN);
                }
                else if (atoms.contains(count) && showAtom == 100) {
                    addCircleOfInfluence(); // Make COI ------------
                    addRayLines(); // Add Ray Lines
                    hexagon.setFill(Color.RED); // Set color to red for atoms
                } /*else if(influenced.contains(count) && showAtom == 100) {
                    hexagon.setFill(Color.GRAY);
                } */
                else if(guessedAtoms.contains(count)){
                    hexagon.setFill(Color.YELLOW);
                }
                else {
                    hexagon.setFill(Color.BLACK); // Set color to black for non-atoms
                }

            }
        }

    } // End of UpdateHexGrid

    public void updateGuess(){
        rootPane.getChildren().remove(userGuess);

        if(!guessedAtoms.isEmpty()){
            userGuess = new Text("Guessed Atoms: "+guessedAtoms);
            userGuess.setFont(Font.font(30));
            userGuess.setFill(Color.CYAN);
            userGuess.setLayoutX(850);
            userGuess.setLayoutY(163);
            userGuess.setFont(Font.font(userGuess.getFont().getFamily(), FontWeight.BOLD, userGuess.getFont().getSize()));
            rootPane.getChildren().add(userGuess);
        }
    }

    public void guessAtom(int num){
        //if(guessedAtoms.size() < 6 && !guessedAtoms.contains(num)) guessedAtoms.add(num+1);
        if(guessedAtoms.contains(num)){
            guessedAtoms.remove(guessedAtoms.indexOf(num));
            return;
        }

        if(guessedAtoms.size() < 6){
            guessedAtoms.add(num);
        }
    }


    public void addRayLines(){

        //System.out.println();

        for(ArrayList<Integer> path : listOfRayPaths){
            //System.out.println(path);

            int i = 0;
            //System.out.println(path.get(0)+" ;"+path.get(1));
            Line dash1 = new Line(buttonLocation.get(path.get(0)).x, buttonLocation.get(path.get(0)).y,cellLocation.get(path.get(1)).x,cellLocation.get(path.get(1)).y);
            dash1.setStroke(Color.YELLOW);
            rootPane.getChildren().add(dash1);

            for(i = 1; i < path.size() - 1; i++){
                if(path.get(i) == -1 || path.get(i) == -100 || (path.get(i+1) == -1) || (path.get(i+1) == -100)){
                    break;
                }

                //System.out.println(path.get(i)+" ;"+path.get(i+1));
                Line dash = new Line(cellLocation.get(path.get(i)).x,cellLocation.get(path.get(i)).y,cellLocation.get(path.get(i+1)).x,cellLocation.get(path.get(i+1)).y);
                dash.setStroke(Color.YELLOW);
                rootPane.getChildren().add(dash);
            } // End of inner-for

            if(path.getLast() != -1){

                //System.out.print(path.get(i)+" ;"+path.getLast());
                Line dash2 = new Line(cellLocation.get(path.get(i)).x,cellLocation.get(path.get(i)).y,buttonLocation.get(path.getLast()).x,buttonLocation.get(path.getLast()).y);
                dash2.setStroke(Color.YELLOW);
                rootPane.getChildren().add(dash2);
            }
        }

    } // End of addRayLines


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
            gameBoard.cells[dummy].addAtom();
            //gameBoard.cells[dummy].setCell_situation(Cell.situation.ATOM);
        }
        updateHexGrid();
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

//        -------------


        pane.getChildren().add(hexagon);
    }

    public int buttonNumberToCellNumber(int num){ // Takes in the number of an edge button and returns the corresponding cell number (refer to board drawing to verify)
        switch(num){
            case 0:
            case 1:
            case 53:
                return 0;
            case 2:
            case 3:
                return 1;
            case 4:
            case 5:
                return 2;
            case 6:
            case 7:
                return 3;
            case 8:
            case 9:
            case 10:
                return 4;
            case 11:
            case 12:
                return 10;
            case 13:
            case 14:
                return 17;
            case 15:
            case 16:
                return 25;
            case 17:
            case 18:
            case 19:
                return 34;
            case 20:
            case 21:
                return 42;
            case 22:
            case 23:
                return 49;
            case 24:
            case 25:
                return 55;
            case 26:
            case 27:
            case 28:
                return 60;
            case 29:
            case 30:
                return 59;
            case 31:
            case 32:
                return 58;
            case 33:
            case 34:
                return 57;
            case 35:
            case 36:
            case 37:
                return 56;
            case 38:
            case 39:
                return 50;
            case 40:
            case 41:
                return 43;
            case 42:
            case 43:
                return 35;
            case 44:
            case 45:
            case 46:
                return 26;
            case 47:
            case 48:
                return 18;
            case 49:
            case 50:
                return 11;
            case 51:
            case 52:
                return 5;
            default: return -1;
        }
    }

    public int findExitButtonNumber(Result dummy){ // Finds the number of the button where a ray will exit
        System.out.println("Exit Button: "+ dummy.CellNumber+" ; "+dummy.lastExitEdge+"; Exit button sitution: "+dummy.situation);
        if(dummy.CellNumber == 0){
            if(dummy.lastExitEdge == Cell.edgeDirection.TL) return 0;
            else if(dummy.lastExitEdge == Cell.edgeDirection.TR) return 1;
            else if(dummy.lastExitEdge == Cell.edgeDirection.L) return 53;
        }
        else if(dummy.CellNumber == 1){
            if(dummy.lastExitEdge == Cell.edgeDirection.TL) return 2;
            else if(dummy.lastExitEdge == Cell.edgeDirection.TR) return 3;
        }
        else if(dummy.CellNumber == 2){
            if(dummy.lastExitEdge == Cell.edgeDirection.TL) return 4;
            else if(dummy.lastExitEdge == Cell.edgeDirection.TR) return 5;
        }
        else if(dummy.CellNumber == 3){
            if(dummy.lastExitEdge == Cell.edgeDirection.TL) return 6;
            else if(dummy.lastExitEdge == Cell.edgeDirection.TR) return 7;
        }
        else if(dummy.CellNumber == 4){
            if(dummy.lastExitEdge == Cell.edgeDirection.TL) return 8;
            else if(dummy.lastExitEdge == Cell.edgeDirection.TR) return 9;
            else if(dummy.lastExitEdge == Cell.edgeDirection.R) return 10;
        }
        else if(dummy.CellNumber == 10){
            if(dummy.lastExitEdge == Cell.edgeDirection.TR) return 11;
            else if(dummy.lastExitEdge == Cell.edgeDirection.R) return 12;
        }
        else if(dummy.CellNumber == 17){
            if(dummy.lastExitEdge == Cell.edgeDirection.TR) return 13;
            else if(dummy.lastExitEdge == Cell.edgeDirection.R) return 14;
        }
        else if(dummy.CellNumber == 25){
            if(dummy.lastExitEdge == Cell.edgeDirection.TR) return 15;
            else if(dummy.lastExitEdge == Cell.edgeDirection.R) return 16;
        }
        else if(dummy.CellNumber == 34){ // --------------
            if(dummy.lastExitEdge == Cell.edgeDirection.TR) return 17;
            else if(dummy.lastExitEdge == Cell.edgeDirection.R) return 18;
            else if(dummy.lastExitEdge == Cell.edgeDirection.BR) return 19;
        }
        else if(dummy.CellNumber == 42){
            if(dummy.lastExitEdge == Cell.edgeDirection.R) return 20;
            else if(dummy.lastExitEdge == Cell.edgeDirection.BR) return 21;
        }
        else if(dummy.CellNumber == 49){
            if(dummy.lastExitEdge == Cell.edgeDirection.R) return 22;
            else if(dummy.lastExitEdge == Cell.edgeDirection.BR) return 23;
        }
        else if(dummy.CellNumber == 55){
            if(dummy.lastExitEdge == Cell.edgeDirection.R) return 24;
            else if(dummy.lastExitEdge == Cell.edgeDirection.BR) return 25;
        }
        else if(dummy.CellNumber == 60){
            if(dummy.lastExitEdge == Cell.edgeDirection.R) return 26;
            else if(dummy.lastExitEdge == Cell.edgeDirection.BR) return 27;
            else if(dummy.lastExitEdge == Cell.edgeDirection.BL) return 28;
        }
        else if(dummy.CellNumber == 59){
            if(dummy.lastExitEdge == Cell.edgeDirection.BR) return 29;
            else if(dummy.lastExitEdge == Cell.edgeDirection.BL) return 30;
        }
        else if(dummy.CellNumber == 58){
            if(dummy.lastExitEdge == Cell.edgeDirection.BR) return 31;
            else if(dummy.lastExitEdge == Cell.edgeDirection.BL) return 32;
        }
        else if(dummy.CellNumber == 57){
            if(dummy.lastExitEdge == Cell.edgeDirection.BR) return 33;
            else if(dummy.lastExitEdge == Cell.edgeDirection.BL) return 34;
        }
        else if(dummy.CellNumber == 56){
            if(dummy.lastExitEdge == Cell.edgeDirection.BR) return 35;
            else if(dummy.lastExitEdge == Cell.edgeDirection.BL) return 36;
            else if(dummy.lastExitEdge == Cell.edgeDirection.L) return 37;
        }
        else if(dummy.CellNumber == 50){
            if(dummy.lastExitEdge == Cell.edgeDirection.BL) return 38;
            else if(dummy.lastExitEdge == Cell.edgeDirection.L) return 39;
        }
        else if(dummy.CellNumber == 43){
            if(dummy.lastExitEdge == Cell.edgeDirection.BL) return 40;
            else if(dummy.lastExitEdge == Cell.edgeDirection.L) return 41;
        }
        else if(dummy.CellNumber == 35){
            if(dummy.lastExitEdge == Cell.edgeDirection.BL) return 42;
            else if(dummy.lastExitEdge == Cell.edgeDirection.L) return 43;
        }
        else if(dummy.CellNumber == 26){ // -----------
            if(dummy.lastExitEdge == Cell.edgeDirection.BL) return 44;
            else if(dummy.lastExitEdge == Cell.edgeDirection.L) return 45;
            else if(dummy.lastExitEdge == Cell.edgeDirection.TL) return 46;
        }
        else if(dummy.CellNumber == 18){
            if(dummy.lastExitEdge == Cell.edgeDirection.L) return 47;
            else if(dummy.lastExitEdge == Cell.edgeDirection.TL) return 48;
        }
        else if(dummy.CellNumber == 11){
            if(dummy.lastExitEdge == Cell.edgeDirection.L) return 49;
            else if(dummy.lastExitEdge == Cell.edgeDirection.TL) return 50;
        }
        else if(dummy.CellNumber == 5){
            if(dummy.lastExitEdge == Cell.edgeDirection.L) return 51;
            else if(dummy.lastExitEdge == Cell.edgeDirection.TL) return 52;
        }
        return -1;
    }

    public Cell.edgeDirection buttonNumberToEdge(int num){ // Takes in a button number and gives the corresponding edge direction to send the ray from
        switch(num){
            case 0:
                return Cell.edgeDirection.TL;
            case 1:
                return Cell.edgeDirection.TR;
            case 2:
                return Cell.edgeDirection.TL;
            case 3:
                return Cell.edgeDirection.TR;
            case 4:
                return Cell.edgeDirection.TL;
            case 5:
                return Cell.edgeDirection.TR;
            case 6:
                return Cell.edgeDirection.TL;
            case 7:
                return Cell.edgeDirection.TR;
            case 8:
                return Cell.edgeDirection.TL;
            case 9:
                return Cell.edgeDirection.TR;
            case 10:
                return Cell.edgeDirection.R;
            case 11:
                return Cell.edgeDirection.TR;
            case 12:
                return Cell.edgeDirection.R;
            case 13:
                return Cell.edgeDirection.TR;
            case 14:
                return Cell.edgeDirection.R;
            case 15:
                return Cell.edgeDirection.TR;
            case 16:
                return Cell.edgeDirection.R;
            case 17:
                return Cell.edgeDirection.TR;
            case 18:
                return Cell.edgeDirection.R;
            case 19:
                return Cell.edgeDirection.BR;
            case 20:
                return Cell.edgeDirection.R;
            case 21:
                return Cell.edgeDirection.BR;
            case 22:
                return Cell.edgeDirection.R;
            case 23:
                return Cell.edgeDirection.BR;
            case 24:
                return Cell.edgeDirection.R;
            case 25:
                return Cell.edgeDirection.BR;
            case 26:
                return Cell.edgeDirection.R;
            case 27:
                return Cell.edgeDirection.BR;
            case 28:
                return Cell.edgeDirection.BL;
            case 29:
                return Cell.edgeDirection.BR;
            case 30:
                return Cell.edgeDirection.BL;
            case 31:
                return Cell.edgeDirection.BR;
            case 32:
                return Cell.edgeDirection.BL;
            case 33:
                return Cell.edgeDirection.BR;
            case 34:
                return Cell.edgeDirection.BL;
            case 35:
                return Cell.edgeDirection.BR;
            case 36:
                return Cell.edgeDirection.BL;
            case 37:
                return Cell.edgeDirection.L;
            case 38:
                return Cell.edgeDirection.BL;
            case 39:
                return Cell.edgeDirection.L;
            case 40:
                return Cell.edgeDirection.BL;
            case 41:
                return Cell.edgeDirection.L;
            case 42:
                return Cell.edgeDirection.BL;
            case 43:
                return Cell.edgeDirection.L;
            case 44:
                return Cell.edgeDirection.BL;
            case 45:
                return Cell.edgeDirection.L;
            case 46:
                return Cell.edgeDirection.TL;
            case 47:
                return Cell.edgeDirection.L;
            case 48:
                return Cell.edgeDirection.TL;
            case 49:
                return Cell.edgeDirection.L;
            case 50:
                return Cell.edgeDirection.TL;
            case 51:
                return Cell.edgeDirection.L;
            case 52:
                return Cell.edgeDirection.TL;
            case 53:
                return Cell.edgeDirection.L;
            default:
                return null;
        }
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

    public void setRayButtons(Pane rootPane){
        //        0
        Button button0 = new Button();
        button0.setMinSize(15, 20);
        button0.setLayoutX(344);
        button0.setLayoutY(179);

        // Create a Rotate transformation
        Rotate rotation = new Rotate();
        rotation.setAngle(60); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button0.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button0.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button0.getTransforms().add(rotation);
        buttons.add(button0);
        rootPane.getChildren().add(button0);

        //        1
        Button button1 = new Button();
        button1.setMinSize(15, 20);
        button1.setLayoutX(385);
        button1.setLayoutY(178);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(120); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button1.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button1.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button1.getTransforms().add(rotation);
        buttons.add(button1);
        rootPane.getChildren().add(button1);

        //        2
        Button button2 = new Button();
        button2.setMinSize(15, 20);
        button2.setLayoutX(415);
        button2.setLayoutY(178);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(60); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button2.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button2.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button2.getTransforms().add(rotation);
        buttons.add(button2);
        rootPane.getChildren().add(button2);

        //        3
        Button button3 = new Button();
        button3.setMinSize(15, 20);
        button3.setLayoutX(456);
        button3.setLayoutY(178);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(120); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button3.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button3.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button3.getTransforms().add(rotation);
        buttons.add(button3);
        rootPane.getChildren().add(button3);

        //        4
        Button button4 = new Button();
        button4.setMinSize(15, 20);
        button4.setLayoutX(482);
        button4.setLayoutY(178);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(60); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button4.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button4.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button4.getTransforms().add(rotation);
        buttons.add(button4);
        rootPane.getChildren().add(button4);

        //        5
        Button button5 = new Button();
        button5.setMinSize(15, 20);
        button5.setLayoutX(524);
        button5.setLayoutY(178);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(120); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button5.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button5.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button5.getTransforms().add(rotation);
        buttons.add(button5);
        rootPane.getChildren().add(button5);

        //        6
        Button button6 = new Button();
        button6.setMinSize(15, 20);
        button6.setLayoutX(550);
        button6.setLayoutY(178);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(60); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button6.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button6.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button6.getTransforms().add(rotation);
        buttons.add(button6);
        rootPane.getChildren().add(button6);

        //                7
        Button button7 = new Button();
        button7.setMinSize(15, 20);
        button7.setLayoutX(593);
        button7.setLayoutY(176);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(120); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button7.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button7.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button7.getTransforms().add(rotation);
        buttons.add(button7);
        rootPane.getChildren().add(button7);

        //        8
        Button button8 = new Button();
        button8.setMinSize(15, 20);
        button8.setLayoutX(618);
        button8.setLayoutY(178);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(60); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button8.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button8.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button8.getTransforms().add(rotation);
        buttons.add(button8);
        rootPane.getChildren().add(button8);

        //        9 -------------
        Button button9 = new Button();
        button9.setMinSize(15, 20);
        button9.setLayoutX(663);
        button9.setLayoutY(176);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(120); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button9.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button9.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button9.getTransforms().add(rotation);
        buttons.add(button9);
        rootPane.getChildren().add(button9);

        //        10
        Button button10 = new Button();
        button10.setMinSize(15, 20);
        button10.setLayoutX(685);
        button10.setLayoutY(213);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(0); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button10.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button10.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button10.getTransforms().add(rotation);
        buttons.add(button10);
        rootPane.getChildren().add(button10);

        //        11
        Button button11 = new Button();
        button11.setMinSize(15, 20);
        button11.setLayoutX(700);
        button11.setLayoutY(240);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(120); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button11.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button11.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button11.getTransforms().add(rotation);
        buttons.add(button11);
        rootPane.getChildren().add(button11);

        //        12
        Button button12 = new Button();
        button12.setMinSize(15, 20);
        button12.setLayoutX(719);
        button12.setLayoutY(274);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(0); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button12.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button12.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button12.getTransforms().add(rotation);
        buttons.add(button12);
        rootPane.getChildren().add(button12);

        //        13
        Button button13 = new Button();
        button13.setMinSize(15, 20);
        button13.setLayoutX(736);
        button13.setLayoutY(298);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(120); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button13.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button13.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button13.getTransforms().add(rotation);
        buttons.add(button13);
        rootPane.getChildren().add(button13);

        //        14
        Button button14 = new Button();
        button14.setMinSize(15, 20);
        button14.setLayoutX(754);
        button14.setLayoutY(332);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(0); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button14.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button14.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button14.getTransforms().add(rotation);
        buttons.add(button14);
        rootPane.getChildren().add(button14);

        //        15
        Button button15 = new Button();
        button15.setMinSize(15, 20);
        button15.setLayoutX(769);
        button15.setLayoutY(358);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(120); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button15.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button15.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button15.getTransforms().add(rotation);
        buttons.add(button15);
        rootPane.getChildren().add(button15);

        //        16
        Button button16 = new Button();
        button16.setMinSize(15, 20);
        button16.setLayoutX(788);
        button16.setLayoutY(391);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(0); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button16.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button16.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button16.getTransforms().add(rotation);
        buttons.add(button16);
        rootPane.getChildren().add(button16);

        //        17
        Button button17 = new Button();
        button17.setMinSize(15, 20);
        button17.setLayoutX(803);
        button17.setLayoutY(420);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(120); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button17.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button17.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button17.getTransforms().add(rotation);
        buttons.add(button17);
        rootPane.getChildren().add(button17);

        //        18
        Button button18 = new Button();
        button18.setMinSize(15, 20);
        button18.setLayoutX(822);
        button18.setLayoutY(454);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(0); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button18.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button18.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button18.getTransforms().add(rotation);
        buttons.add(button18);
        rootPane.getChildren().add(button18);

        //        19
        Button button19 = new Button();
        button19.setMinSize(15, 20);
        button19.setLayoutX(803);
        button19.setLayoutY(491);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(60); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button19.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button19.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button19.getTransforms().add(rotation);
        buttons.add(button19);
        rootPane.getChildren().add(button19);

        //        20 ------------
        Button button20 = new Button();
        button20.setMinSize(15, 20);
        button20.setLayoutX(787);
        button20.setLayoutY(516);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(0); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button20.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button20.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button20.getTransforms().add(rotation);
        buttons.add(button20);
        rootPane.getChildren().add(button20);

        //        21
        Button button21 = new Button();
        button21.setMinSize(15, 20);
        button21.setLayoutX(770);
        button21.setLayoutY(549);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(60); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button21.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button21.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button21.getTransforms().add(rotation);
        buttons.add(button21);
        rootPane.getChildren().add(button21);

        //        22
        Button button22 = new Button();
        button22.setMinSize(15, 20);
        button22.setLayoutX(753);
        button22.setLayoutY(577);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(0); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button22.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button22.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button22.getTransforms().add(rotation);
        buttons.add(button22);
        rootPane.getChildren().add(button22);

        //        23
        Button button23 = new Button();
        button23.setMinSize(15, 20);
        button23.setLayoutX(738);
        button23.setLayoutY(608);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(60); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button23.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button23.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button23.getTransforms().add(rotation);
        buttons.add(button23);
        rootPane.getChildren().add(button23);

        //        24
        Button button24 = new Button();
        button24.setMinSize(15, 20);
        button24.setLayoutX(718);
        button24.setLayoutY(636);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(0); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button24.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button24.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button24.getTransforms().add(rotation);
        buttons.add(button24);
        rootPane.getChildren().add(button24);

        //        25
        Button button25 = new Button();
        button25.setMinSize(15, 20);
        button25.setLayoutX(703);
        button25.setLayoutY(668);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(60); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button25.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button25.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button25.getTransforms().add(rotation);
        buttons.add(button25);
        rootPane.getChildren().add(button25);

        //        26
        Button button26 = new Button();
        button26.setMinSize(15, 20);
        button26.setLayoutX(684);
        button26.setLayoutY(696);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(0); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button26.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button26.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button26.getTransforms().add(rotation);
        buttons.add(button26);
        rootPane.getChildren().add(button26);

        //        27
        Button button27 = new Button();
        button27.setMinSize(15, 20);
        button27.setLayoutX(668);
        button27.setLayoutY(727);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(60); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button27.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button27.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button27.getTransforms().add(rotation);
        buttons.add(button27);
        rootPane.getChildren().add(button27);

        //        28
        Button button28 = new Button();
        button28.setMinSize(15, 20);
        button28.setLayoutX(625);
        button28.setLayoutY(734);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(120); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button28.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button28.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button28.getTransforms().add(rotation);
        buttons.add(button28);
        rootPane.getChildren().add(button28);

        //        29
        Button button29 = new Button();
        button29.setMinSize(15, 20);
        button29.setLayoutX(598);
        button29.setLayoutY(729);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(60); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button29.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button29.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button29.getTransforms().add(rotation);
        buttons.add(button29);
        rootPane.getChildren().add(button29);

        //        30
        Button button30 = new Button();
        button30.setMinSize(15, 20);
        button30.setLayoutX(553);
        button30.setLayoutY(731);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(120); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button30.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button30.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button30.getTransforms().add(rotation);
        buttons.add(button30);
        rootPane.getChildren().add(button30);

        //        31
        Button button31 = new Button();
        button31.setMinSize(15, 20);
        button31.setLayoutX(528);
        button31.setLayoutY(730);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(60); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button31.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button31.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button31.getTransforms().add(rotation);
        buttons.add(button31);
        rootPane.getChildren().add(button31);

        //        32
        Button button32 = new Button();
        button32.setMinSize(15, 20);
        button32.setLayoutX(487);
        button32.setLayoutY(733);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(120); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button32.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button32.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button32.getTransforms().add(rotation);
        buttons.add(button32);
        rootPane.getChildren().add(button32);

        //        33
        Button button33 = new Button();
        button33.setMinSize(15, 20);
        button33.setLayoutX(458);
        button33.setLayoutY(730);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(60); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button33.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button33.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button33.getTransforms().add(rotation);
        buttons.add(button33);
        rootPane.getChildren().add(button33);

        //        34
        Button button34 = new Button();
        button34.setMinSize(15, 20);
        button34.setLayoutX(418);
        button34.setLayoutY(732);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(120); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button34.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button34.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button34.getTransforms().add(rotation);
        buttons.add(button34);
        rootPane.getChildren().add(button34);

        //        35
        Button button35 = new Button();
        button35.setMinSize(15, 20);
        button35.setLayoutX(390);
        button35.setLayoutY(730);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(60); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button35.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button35.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button35.getTransforms().add(rotation);
        buttons.add(button35);
        rootPane.getChildren().add(button35);

        //        36
        Button button36 = new Button();
        button36.setMinSize(15, 20);
        button36.setLayoutX(345);
        button36.setLayoutY(731);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(120); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button36.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button36.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button36.getTransforms().add(rotation);
        buttons.add(button36);
        rootPane.getChildren().add(button36);

        //        37
        Button button37 = new Button();
        button37.setMinSize(15, 20);
        button37.setLayoutX(322);
        button37.setLayoutY(694);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(0); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button37.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button37.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button37.getTransforms().add(rotation);
        buttons.add(button37);
        rootPane.getChildren().add(button37);

        //        38
        Button button38 = new Button();
        button38.setMinSize(15, 20);
        button38.setLayoutX(308);
        button38.setLayoutY(670);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(120); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button38.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button38.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button38.getTransforms().add(rotation);
        buttons.add(button38);
        rootPane.getChildren().add(button38);

        //        39
        Button button39 = new Button();
        button39.setMinSize(15, 20);
        button39.setLayoutX(288);
        button39.setLayoutY(633);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(0); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button39.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button39.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button39.getTransforms().add(rotation);
        buttons.add(button39);
        rootPane.getChildren().add(button39);

        //        40
        Button button40 = new Button();
        button40.setMinSize(15, 20);
        button40.setLayoutX(272);
        button40.setLayoutY(610);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(120); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button40.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button40.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button40.getTransforms().add(rotation);
        buttons.add(button40);
        rootPane.getChildren().add(button40);

        //        41
        Button button41 = new Button();
        button41.setMinSize(15, 20);
        button41.setLayoutX(252);
        button41.setLayoutY(574);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(0); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button41.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button41.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button41.getTransforms().add(rotation);
        buttons.add(button41);
        rootPane.getChildren().add(button41);

        //        42
        Button button42 = new Button();
        button42.setMinSize(15, 20);
        button42.setLayoutX(244);
        button42.setLayoutY(552);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(120); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button42.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button42.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button42.getTransforms().add(rotation);
        buttons.add(button42);
        rootPane.getChildren().add(button42);

        //        43
        Button button43 = new Button();
        button43.setMinSize(15, 20);
        button43.setLayoutX(219);
        button43.setLayoutY(512);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(0); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button43.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button43.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button43.getTransforms().add(rotation);
        buttons.add(button43);
        rootPane.getChildren().add(button43);

        //        44
        Button button44 = new Button();
        button44.setMinSize(15, 20);
        button44.setLayoutX(208);
        button44.setLayoutY(492);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(120); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button44.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button44.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button44.getTransforms().add(rotation);
        buttons.add(button44);
        rootPane.getChildren().add(button44);

        //        45
        Button button45 = new Button();
        button45.setMinSize(15, 20);
        button45.setLayoutX(184);
        button45.setLayoutY(456);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(0); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button45.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button45.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button45.getTransforms().add(rotation);
        buttons.add(button45);
        rootPane.getChildren().add(button45);

        //        46
        Button button46 = new Button();
        button46.setMinSize(15, 20);
        button46.setLayoutX(208);
        button46.setLayoutY(416);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(60); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button46.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button46.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button46.getTransforms().add(rotation);
        buttons.add(button46);
        rootPane.getChildren().add(button46);

        //        47
        Button button47 = new Button();
        button47.setMinSize(15, 20);
        button47.setLayoutX(218);
        button47.setLayoutY(393);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(0); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button47.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button47.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button47.getTransforms().add(rotation);
        buttons.add(button47);
        rootPane.getChildren().add(button47);

        //        48
        Button button48 = new Button();
        button48.setMinSize(15, 20);
        button48.setLayoutX(240);
        button48.setLayoutY(357);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(60); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button48.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button48.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button48.getTransforms().add(rotation);
        buttons.add(button48);
        rootPane.getChildren().add(button48);

        //        49
        Button button49 = new Button();
        button49.setMinSize(15, 20);
        button49.setLayoutX(253);
        button49.setLayoutY(333);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(0); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button49.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button49.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button49.getTransforms().add(rotation);
        buttons.add(button49);
        rootPane.getChildren().add(button49);

        //        50
        Button button50 = new Button();
        button50.setMinSize(15, 20);
        button50.setLayoutX(278);
        button50.setLayoutY(297);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(60); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button50.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button50.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button50.getTransforms().add(rotation);
        buttons.add(button50);
        rootPane.getChildren().add(button50);

        //        51
        Button button51 = new Button();
        button51.setMinSize(15, 20);
        button51.setLayoutX(289);
        button51.setLayoutY(274);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(0); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button51.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button51.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button51.getTransforms().add(rotation);
        buttons.add(button51);
        rootPane.getChildren().add(button51);

        //        52 -----------
        Button button52 = new Button();
        button52.setMinSize(15, 20);
        button52.setLayoutX(312);
        button52.setLayoutY(237);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(60); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button52.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button52.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button52.getTransforms().add(rotation);
        buttons.add(button52);
        rootPane.getChildren().add(button52);

        //        53
        Button button53 = new Button();
        button53.setMinSize(15, 20);
        button53.setLayoutX(322);
        button53.setLayoutY(215);

        // Create a Rotate transformation
        rotation = new Rotate();
        rotation.setAngle(0); // Set the angle of rotation (in degrees)
        rotation.setPivotX(button53.getMinWidth() / 2); // Set the pivot point X (center of the button)
        rotation.setPivotY(button53.getMinHeight() / 2); // Set the pivot point Y (center of the button)

        // Apply the rotation transformation to the button
        button53.getTransforms().add(rotation);
        buttons.add(button53);
        rootPane.getChildren().add(button53);


    } // End of method Set-Ray buttons

}
