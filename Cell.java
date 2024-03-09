import java.util.ArrayList;

public class Cell {

    private int cellNumber;

    public enum situation {EMPTY, ATOM, INFLUENCE, DOUBLE_INFLUENCE, TRIPLE_INFLUENCE};

    public enum edgeDirection{TR, R, BR, BL, L, TL};

    private situation cell_situation;

    private ArrayList<Cell> neighbour_cells;

    public Cell(){
        this.cell_situation = situation.EMPTY;
        this.cellNumber = 0;
        neighbour_cells = new ArrayList<>();
    }

    public void setCell_situation(situation cell_situation) {
        this.cell_situation = cell_situation;
    }

    public void setNeighbour_cells(ArrayList<Cell> neighbour_cells) {
        this.neighbour_cells = neighbour_cells;
    }

    public int getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(int cellNumber) {
        this.cellNumber = cellNumber;
    }

    public situation getCell_situation() {
        return cell_situation;
    }

    public ArrayList<Cell> getNeighbour_cells() {
        return neighbour_cells;
    }

    // --------- Ray Tracing Algorithm Starts Here ------------

    Cell currentNeighbour;
    public edgeDirection exitEdge; //this is the exitEdge of the ray that entered this cell

    public Cell rayEntryAndExit(edgeDirection inputEdge) { //this function will return the cell where the ray will enter and exit

        if(this.getCell_situation() == situation.ATOM){ // Special case - If the first Cell has an atom
            Cell dummy = new Cell();
            dummy.setCellNumber(this.cellNumber);
            dummy.exitEdge = this.exitEdge;
            dummy.setCell_situation(this.getCell_situation());
            return dummy;
        }

        exitEdge = getExitEdge(inputEdge); //gives the exit edge type

        int neighbourIndex = neighbourNumber(inputEdge); //gets neighbour NUMBER based on the input edge type

        if( getNeighbour_cells().get(neighbourIndex) == null){
            return null;
        }

        currentNeighbour = getNeighbour_cells().get(neighbourIndex); //gets the neighbour CELL from which the ray will go next.

        if(currentNeighbour.getCell_situation() == situation.ATOM){
            return currentNeighbour;
        }

        edgeDirection nextInputEdge = nextInputEdge_BasedOn_PreviousExitEdge(exitEdge);

        if(currentNeighbour.rayEntryAndExit(nextInputEdge) == null){
            return currentNeighbour;
        }
        return currentNeighbour.rayEntryAndExit(nextInputEdge);


    } //end of rayEntryAndExit

    public boolean isInfluenced(){
        return cell_situation == situation.INFLUENCE || cell_situation == situation.DOUBLE_INFLUENCE || cell_situation == situation.TRIPLE_INFLUENCE;
    }

    public void addAtom(){
        this.setCell_situation(situation.ATOM);
        for(Cell dummy : this.neighbour_cells){
            if(dummy != null) dummy.setInfluence();
        }
    } // End of method - ADD ATOM

    public void setInfluence(){

//        the cell situation will only change if cell_situation is EMPTY, INFLUENCE or DOUBLE_INFLUENCE
//        Cell situation will remain unchanged if there is an ATOM or TRIPLE_INFLUENCE

        if(this.cell_situation == situation.EMPTY){
            this.cell_situation = situation.INFLUENCE;
        }
        else if(this.cell_situation == situation.INFLUENCE){
            this.cell_situation = situation.DOUBLE_INFLUENCE;
        }
        else if(this.cell_situation == situation.DOUBLE_INFLUENCE){
            this.cell_situation = situation.TRIPLE_INFLUENCE;
        }

    } // End of method - SET_INFLUENCED

    public Cell newEntryAndExit(edgeDirection inputEdge){

        if(this.getCell_situation() == situation.ATOM){ // Special case - If the first Cell has an atom
            Cell dummy = new Cell();
            dummy.setCellNumber(this.cellNumber);
            dummy.exitEdge = this.exitEdge;
            dummy.setCell_situation(this.getCell_situation());
            return dummy;
        }

        edgeDirection currentEdge = inputEdge;
        Cell currentCell = this;

        while(true){
            System.out.println("Cell: "+currentCell.getCellNumber()+"; Cell Situation: "+currentCell.getCell_situation());

            if(currentCell == null) break;


            if(currentCell.getCell_situation() == situation.ATOM){
                break;
            }
            else if(currentCell.getCell_situation() == situation.EMPTY || currentCell.getCell_situation() == situation.INFLUENCE || currentCell.getCell_situation() == situation.DOUBLE_INFLUENCE || currentCell.getCell_situation() == situation.TRIPLE_INFLUENCE){ // temporary as ray wouldn't be printed without this

                exitEdge = getExitEdge(currentEdge); //gives the exit edge type

                int neighbourIndex = neighbourNumber(currentEdge); //gets neighbour NUMBER based on the input edge type

                if( currentCell.getNeighbour_cells().get(neighbourIndex) == null){
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

    public ArrayList<Integer> rayPath(edgeDirection inputEdge){

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
            else if(currentCell.getCell_situation() == situation.EMPTY || currentCell.getCell_situation() == situation.INFLUENCE || currentCell.getCell_situation() == situation.DOUBLE_INFLUENCE || currentCell.getCell_situation() == situation.TRIPLE_INFLUENCE){ // temporary as ray wouldn't be printed without this

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

    }

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

    } //end of exitEdge

    // --------- Ray Tracing Algorithm Ends Here ------------

}
