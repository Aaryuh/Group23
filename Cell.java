import java.util.ArrayList;

public class Cell {

    private int cellNumber;

    private enum situation {EMPTY, ATOM, INFLUENCE, DOUBLE_INFLUENCE, TRIPLE_INFLUENCE};

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
    Cell trueNeighbour;
    edgeDirection exitPoint;//this is the exitEdge of the ray entered
    public Cell rayEntryAndExit(edgeDirection inputEdge) {

        exitPoint= exitEdge(inputEdge);//gives the exit edge type

        int edgeNeighbourNumber= neighbourNumber(inputEdge);//gets neighbour NUMBER based on the input edge -- sahi hai

        if( getNeighbour_cells().get(edgeNeighbourNumber)== null){
            return trueNeighbour;
        }
        trueNeighbour= getNeighbour_cells().get(edgeNeighbourNumber);//gets the neighbour CELL from which the ray will go next.

        edgeDirection nextInputEdge = inputEdge_BasedOn_PreviousExitEdge(exitPoint);

        if(trueNeighbour.rayEntryAndExit(nextInputEdge)== null){
            return trueNeighbour;
        }
        return trueNeighbour.rayEntryAndExit(nextInputEdge);


    }

    public edgeDirection inputEdge_BasedOn_PreviousExitEdge(edgeDirection previousExit){
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

    public edgeDirection exitEdge(edgeDirection inputEdge) {

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