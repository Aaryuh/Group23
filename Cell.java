import java.util.ArrayList;
import java.util.Scanner;

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
        this.neighbour_cells = new ArrayList<>();
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
    public edgeDirection exitEdge; //exit edge of the ray that entered this cell


    private int deflectIndex(int curr_cell, int degree) {

        int indexOut;

        if( curr_cell < 0 || curr_cell > 5 || degree < -360 || degree > 360) {
            throw new IllegalArgumentException("Index must be a value between [0, 5], Degree must be a value between [-360, 360]");
        }

        switch(degree) {
            case 0:
            case 360:
            case -360:
                indexOut = curr_cell;
                break;
            case 60:
            case -300:
                indexOut = (curr_cell+ 1) % 6;
                break;
            case 120:
            case -240:
                indexOut = (curr_cell + 2) % 6;
                break;
            case 180:
            case -180:
                indexOut = (curr_cell + 3) % 6;
                break;
            case -120:
            case 240:
                indexOut = (curr_cell + 4) % 6;
                break;
            case -60:
            case 300:
                indexOut = (curr_cell + 5) % 6;
                break;
            default:
                throw new IllegalArgumentException("Input index not valid");
        }

        return indexOut;
    }

    public Cell getNextCell(Cell cIn) {

        while (true) {

            System.out.println("Cell: " + cIn.getCellNumber() +  "; Cell Situation: " + cIn.getCell_situation());

            Cell nextCell = null;
        int nbIndex = neighbour_cells.indexOf(cIn);
        int deflectionsCnt = 0;

        if(neighbour_cells.get(deflectIndex(nbIndex, 60)).cell_situation.equals(situation.ATOM)||neighbour_cells.get(deflectIndex(nbIndex, -60)).cell_situation.equals(situation.ATOM)|| (neighbour_cells.get(deflectIndex(nbIndex, 120)).cell_situation.equals(situation.ATOM) && neighbour_cells.get(deflectIndex(nbIndex, -120)).cell_situation.equals(situation.ATOM))) {
            nextCell = cIn;
        }
        else if(neighbour_cells.get(deflectIndex(nbIndex, 120)).cell_situation.equals(situation.ATOM)) {
            deflectionsCnt++;

            if(neighbour_cells.get(deflectIndex(nbIndex, 180)).cell_situation.equals(situation.ATOM)) {
                deflectionsCnt++;
            }

            int exitIndex = deflectIndex(nbIndex, 180+60*deflectionsCnt);
            nextCell = neighbour_cells.get(exitIndex);
        }
        else if(neighbour_cells.get(deflectIndex(nbIndex, -120)).cell_situation.equals(situation.ATOM)) {
            deflectionsCnt++;

            if(neighbour_cells.get(deflectIndex(nbIndex, 180)).cell_situation.equals(situation.ATOM)) {
                deflectionsCnt++;
            }

            int exitIndex = deflectIndex(nbIndex, 180-60*deflectionsCnt);
            nextCell = neighbour_cells.get(exitIndex);
        }
        else if(neighbour_cells.get(deflectIndex(nbIndex, 180)).cell_situation.equals(situation.ATOM)) {
            nextCell = null;
        }
        else {
            nextCell = neighbour_cells.get(deflectIndex(nbIndex, 180));
        }

        return nextCell;
    }

}





































    //Method to determine the next cell based on the input edge
   public Cell newEntryAndExit(edgeDirection inputEdge){

        // Special case - If the first Cell has an atom, return the cell itself
        if(this.getCell_situation() == situation.ATOM){
           return this;
        }

        //entry edge of the ray at the beginning
        edgeDirection currentEdge = inputEdge;
        Cell currentCell = this;
        int index = 0;
        int deflectionCount = 0;

        while(true){
            System.out.println("Cell: " + currentCell.getCellNumber() +  "; Cell Situation: " + currentCell.getCell_situation() + " index: " + index);

            //if the current cell has an atom, just return the exit edge of the ray
            if(currentCell.getCell_situation() == situation.ATOM){
                break;
            }
            else if (currentCell.neighbour_cells.get(deflectIndex(index,120)).cell_situation.equals(situation.ATOM)) {
                deflectionCount ++;

                int exitIndex = deflectIndex(index, 180 + 60  * deflectionCount);
                currentCell = currentCell.getNeighbour_cells().get(exitIndex);
            }
            else if(currentCell.getCell_situation() == situation.EMPTY){

                exitEdge = getExitEdge(currentEdge); //gives the exit edge type

                int neighbourIndex = neighbourNumber(currentEdge); //gets neighbour NUMBER based on the input edge type

                if(currentCell.getNeighbour_cells().get(neighbourIndex) == null){
                    break;
                }

                currentCell = currentCell.getNeighbour_cells().get(neighbourIndex); //gets the neighbour CELL from which the ray will go next.
                currentEdge = getExitEdge(exitEdge);
            }


        } // End of while

        //System.out.println("After Exit: "+currentCell.getCellNumber());

        currentCell.exitEdge = this.exitEdge;

        return currentCell;
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


    public edgeDirection NumberToEdge (int inputEdge){
        switch(inputEdge){
            case 3:
                return edgeDirection.TR;
            case 4:
                return edgeDirection.R;
            case 5:
                return edgeDirection.BR;
            case 0:
                return edgeDirection.BL;
            case 1:
                return edgeDirection.L;
            case 2:
                return edgeDirection.TL;
            default:
                throw new IllegalArgumentException("invalid edge");
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