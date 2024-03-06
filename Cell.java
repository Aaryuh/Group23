import java.util.ArrayList;

public class Cell {

    private int cellNumber;  //Unique identifier for the cell

    //Enum defining possible situation for a cell
    public enum situation {EMPTY, ATOM, INFLUENCE, DOUBLE_INFLUENCE, TRIPLE_INFLUENCE}

    //Enum defining possible edge direction
    public enum edgeDirection{TR, R, BR, BL, L, TL}

    //current situation of the cell
    private situation cell_situation;

    //List of neighboring cells
    private ArrayList<Cell> neighbour_cells;

    //Constructor to initialize the cell with default values
    public Cell(){
        this.cell_situation = situation.EMPTY;
        this.cellNumber = 0;
        neighbour_cells = new ArrayList<>();
    }

    //Setter for the cell situation
    public void setCell_situation(situation cell_situation) {
        this.cell_situation = cell_situation;
    }

    //Setter for the list of neighboring cells
    public void setNeighbour_cells(ArrayList<Cell> neighbour_cells) {
        this.neighbour_cells = neighbour_cells;
    }

    //getter for the cell number
    public int getCellNumber() {
        return cellNumber;
    }

    //setter for the cell number
    public void setCellNumber(int cellNumber) {
        this.cellNumber = cellNumber;
    }

    //getter for the cell situation
    public situation getCell_situation() {
        return cell_situation;
    }

    //getter for the list of neighboring cells
    public ArrayList<Cell> getNeighbour_cells() {
        return neighbour_cells;
    }

    //Proprieties related to ray tracing
    Cell currentNeighbour;    //Current neighbour during ray tracing
    public edgeDirection exitEdge; //exit edge of the ray that entered this cell

    //Method to determine the next cell based on the input edge
    public Cell newEntryAndExit(edgeDirection inputEdge){

        // Special case - If the first Cell has an atom, return the cell itself
        if(this.getCell_situation() == situation.ATOM){
           return this;
        }

        //entry edge of the ray at the beginning
        edgeDirection currentEdge = inputEdge;
        Cell currentCell = this;

        while(true){
            System.out.println("Cell: " + currentCell.getCellNumber()+"; Cell Situation: " + currentCell.getCell_situation());

            if(currentCell == null) {
                break;
            }

            //if the current cell has an atom, just return the exit edge of the ray
            if(currentCell.getCell_situation() == situation.ATOM){
                break;
            }
            else if(currentCell.getCell_situation() == situation.EMPTY){

                exitEdge = getExitEdge(currentEdge); //gives the exit edge type

                int neighbourIndex = neighbourNumber(currentEdge); //gets neighbour NUMBER based on the input edge type

                if(currentCell.getNeighbour_cells().get(neighbourIndex) == null){
                    break;
                }

                currentCell = currentCell.getNeighbour_cells().get(neighbourIndex); //gets the neighbour CELL from which the ray will go next.
                currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(exitEdge);
            }


        } // End of while

        //System.out.println("After Exit: "+currentCell.getCellNumber());

        currentCell.exitEdge = this.exitEdge;

        return currentCell;
    }

    public ArrayList<Integer> rayPath(edgeDirection inputEdge){ //returns the path of the ray

        ArrayList<Integer> path = new ArrayList<>();
        path.add(this.getCellNumber());

        if(this.getCell_situation() == situation.ATOM) return path;

        edgeDirection currentEdge = inputEdge;
        Cell currentCell = this;

        while(true){
            if(!path.contains(currentCell.getCellNumber())) path.add(currentCell.cellNumber);

            if(currentCell == null) break;

            if(currentCell.getCell_situation() == situation.ATOM){
                break;
            }
            else if(currentCell.getCell_situation() == situation.EMPTY){

                exitEdge = getExitEdge(currentEdge); //gives the exit edge type

                int neighbourIndex = neighbourNumber(currentEdge); //gets neighbour NUMBER based on the input edge type

                if( currentCell.getNeighbour_cells().get(neighbourIndex) == null){
                    break;
                }

                currentCell = currentCell.getNeighbour_cells().get(neighbourIndex); //gets the neighbour CELL from which the ray will go next.
                currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(exitEdge);
            }

        } // End of while

        return path;

    } // End of rayPath

    //method to calculate the next input edge based on the previous exit edge
    public edgeDirection nextInputEdge_BasedOn_PreviousExitEdge(edgeDirection previousExit){

        switch(previousExit){
            case TR:
                return edgeDirection.BL;
            case R:
                return edgeDirection.L;
            case BR:
                return edgeDirection.TL;
            case BL:
                return edgeDirection.TR;
            case L:
                return edgeDirection.R;
            case TL:
                return edgeDirection.BR;
            default:
                return null;

        }
    }

    //Method to determine the neighbor number based on the input edge
    public int neighbourNumber(edgeDirection inputEdge){
        switch(inputEdge){
            case TR:
                return 3;
            case R:
                return 4;
            case BR:
                return 5;
            case BL:
                return 0;
            case L:
                return 1;
            case TL:
                return 2;
            default:
                return -1;

        }
    }

    //method to determine the exit edge based on the input edge(entry edge)
    public edgeDirection getExitEdge(edgeDirection inputEdge) {

        switch(inputEdge){
            case TR:
                return edgeDirection.BL;
            case R:
                return edgeDirection.L;
            case BR:
                return edgeDirection.TL;
            case BL:
                return edgeDirection.TR;
            case L:
                return edgeDirection.R;
            case TL:
                return edgeDirection.BR;
            default:
                return null;

        }

    }

}