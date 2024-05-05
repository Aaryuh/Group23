import java.util.ArrayList;

/**
 * Represents a cell in the Black Box game grid.
 */
public class Cell {

    /**
     * The number of the cell.
     */
    private int cellNumber;

    /**
     * Enumeration representing different situations of a cell.
     */
    public enum situation {EMPTY, ATOM, INFLUENCE, DOUBLE_INFLUENCE, TRIPLE_INFLUENCE, REFLECTED};

    /**
     * Enumeration representing edge directions of a cell.
     */
    public enum edgeDirection{TR, R, BR, BL, L, TL};

    /**
     * The situation of the cell, such as whether it's empty, contains an atom, or is influenced by a ray.
     */
    private situation cell_situation;

    /**
     * List of cells that influence the current cell.
     */
    public ArrayList<Integer> influencer;

    /**
     * The list of neighbouring cells.
     */
    private ArrayList<Cell> neighbour_cells;

    /**
     * Constructs a Cell object with default properties.
     */
    public Cell(){
        this.cell_situation = situation.EMPTY;
        this.cellNumber = 0;
        neighbour_cells = new ArrayList<>();
        this.influencer = new ArrayList<>();
    }

    /**
     * Sets the situation of the cell.
     * @param cell_situation The situation of the cell.
     */
    public void setCell_situation(situation cell_situation) {
        this.cell_situation = cell_situation;
    }

    /**
     * Sets the neighbour cells of the current cell.
     * @param neighbour_cells The neighbour cells.
     */
    public void setNeighbour_cells(ArrayList<Cell> neighbour_cells) {
        this.neighbour_cells = neighbour_cells;
    }

    /**
     * Gets the cell number.
     * @return The cell number.
     */
    public int getCellNumber() {
        return cellNumber;
    }

    /**
     * Sets the cell number.
     * @param cellNumber The cell number.
     */
    public void setCellNumber(int cellNumber) {
        this.cellNumber = cellNumber;
    }

    /**
     * Gets the situation of the cell.
     * @return The situation of the cell.
     */
    public situation getCell_situation() {
        return cell_situation;
    }

    /**
     * Gets the neighbour cells.
     * @return The neighbour cells.
     */
    public ArrayList<Cell> getNeighbour_cells() {
        return neighbour_cells;
    }

    // --------- Ray Tracing Algorithm Starts Here ------------

    Cell currentNeighbour;
    public edgeDirection exitEdge; //this is the exitEdge of the ray that entered this cell

    /**
     * Checks if the cell is influenced by a ray.
     * @return True if influenced, otherwise false.
     */
    public boolean isInfluenced(){
        return cell_situation == situation.INFLUENCE || cell_situation == situation.DOUBLE_INFLUENCE || cell_situation == situation.TRIPLE_INFLUENCE;
    }

    /**
     * Adds an atom to the cell and updates neighbouring cells.
     */
    public void addAtom(){
        this.setCell_situation(situation.ATOM);
        for(Cell dummy : this.neighbour_cells){
            if(dummy != null) dummy.setInfluence(this.getCellNumber());
        }
    } // End of method - ADD ATOM

    /**
     * Sets the influence of a cell based on its number.
     * @param cellNumber The cell number.
     */
    public void setInfluence(int cellNumber){

//        the cell situation will only change if cell_situation is EMPTY, INFLUENCE or DOUBLE_INFLUENCE
//        Cell situation will remain unchanged if there is an ATOM or TRIPLE_INFLUENCE
        if(this.cell_situation == situation.EMPTY){
            this.cell_situation = situation.INFLUENCE;
            this.influencer.add(cellNumber);

        }
        else if(this.cell_situation == situation.INFLUENCE){
            this.cell_situation = situation.DOUBLE_INFLUENCE;
            this.influencer.add(cellNumber);

        }
        else if(this.cell_situation == situation.DOUBLE_INFLUENCE){
            this.cell_situation = situation.TRIPLE_INFLUENCE;
            this.influencer.add(cellNumber);

        }

    } // End of method - SET_INFLUENCED

    /**
     * Determines the entry and exit edges of a ray based on its input edge.
     * @param inputEdge The input edge of the ray.
     * @return The next cell where the ray enters.
     */
    public Cell newEntryAndExit(edgeDirection inputEdge){

        if(this.getCell_situation() == situation.ATOM){ // Special case - If the first Cell has an atom
            Cell dummy = new Cell();
            dummy.setCellNumber(this.cellNumber);
            dummy.exitEdge = this.exitEdge;
            dummy.setCell_situation(this.getCell_situation());
            return dummy;
        }

        if(this.isInfluenced() || this.cell_situation == situation.REFLECTED){
            if(this.cell_situation == situation.DOUBLE_INFLUENCE || this.cell_situation == situation.REFLECTED){
                Cell dummy = new Cell();
                dummy.setCellNumber(this.cellNumber);
                dummy.exitEdge = this.exitEdge;
                dummy.setCell_situation(situation.REFLECTED);
                return dummy;
            }
            if(this.getNeighbour_cells().get(neighbourNumber(inputEdge)).getCell_situation() != situation.ATOM){
                Cell dummy = new Cell();
                dummy.setCellNumber(this.cellNumber);
                dummy.exitEdge = this.exitEdge;
                dummy.setCell_situation(situation.REFLECTED);
                return dummy;
            }
        }

        edgeDirection currentEdge = inputEdge;
        Cell currentCell = this;

        while(true){

            if(currentCell == null) break;


            if(currentCell.getCell_situation() == situation.ATOM){
                break;
            }
            else if(currentCell.getCell_situation() == situation.TRIPLE_INFLUENCE){
                break;
            }
            else if(currentCell.getCell_situation() == situation.INFLUENCE){
                exitEdge = getExitEdge(currentEdge); //gives the exit edge type

                int neighbourIndex = neighbourNumber(currentEdge); //gets neighbour NUMBER based on the input edge type
                int leftNeighbourIndex = ( (neighbourIndex - 1) + 6 ) % 6;
                int rightNeighbourIndex = ( (neighbourIndex + 1) + 6 ) % 6;

                Cell leftNeighbourCell = currentCell.getNeighbour_cells().get(leftNeighbourIndex);
                Cell rightNeighbourCell = currentCell.getNeighbour_cells().get(rightNeighbourIndex);

                if(currentCell.getNeighbour_cells().get(neighbourIndex) != null && currentCell.getNeighbour_cells().get(neighbourIndex).getCell_situation() == situation.ATOM){
                    currentCell = currentCell.getNeighbour_cells().get(neighbourIndex); //gets the neighbour CELL from which the ray will go next.
                    currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(exitEdge);
                    continue;
                }

                // At this point, next cell is not null and next cell does not have an atom (hence ray is not on a hit path)
                if(leftNeighbourCell != null && rightNeighbourCell != null ){
                    if(leftNeighbourCell.getCell_situation() == situation.ATOM){
                        currentCell = rightNeighbourCell;
                        exitEdge = getRightNeighbour(currentEdge);
                        currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(exitEdge);
                        currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(currentEdge);
                    } else if( rightNeighbourCell.getCell_situation() == situation.ATOM ){
                        currentCell = leftNeighbourCell;
                        exitEdge = getLeftNeighbour(currentEdge);
                        currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(exitEdge);
                        currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(currentEdge);
                    }
                } else if(leftNeighbourCell == null){
                    exitEdge = getLeftNeighbour(currentEdge);
                    currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(exitEdge);
                    currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(currentEdge);
                    exitEdge = nextInputEdge_BasedOn_PreviousExitEdge(currentEdge);
                    break;
                } else if(rightNeighbourCell == null){
                    exitEdge = getRightNeighbour(currentEdge);
                    currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(exitEdge);
                    currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(currentEdge);
                    exitEdge = nextInputEdge_BasedOn_PreviousExitEdge(currentEdge);
                    break;
                }

            }
            else if(currentCell.getCell_situation() == situation.DOUBLE_INFLUENCE){
                exitEdge = getExitEdge(currentEdge); //gives the exit edge type

                int neighbourIndex = neighbourNumber(currentEdge); //gets neighbour NUMBER based on the input edge type
                int leftNeighbourIndex = ( (neighbourIndex - 2) + 6 ) % 6;
                int rightNeighbourIndex = ( (neighbourIndex + 2) + 6 ) % 6;

                Cell leftNeighbourCell = currentCell.getNeighbour_cells().get(leftNeighbourIndex);
                Cell rightNeighbourCell = currentCell.getNeighbour_cells().get(rightNeighbourIndex);

                // -----------

                if(leftNeighbourCell != null && rightNeighbourCell != null ){
                    if(leftNeighbourCell.isInfluenced() && rightNeighbourCell.isInfluenced()){

                        ArrayList<Integer> leftNeighbourInfluencer = leftNeighbourCell.influencer;
                        ArrayList<Integer> rightNeighbourInfluencer = rightNeighbourCell.influencer;

                        if(!(leftNeighbourInfluencer.contains(currentCell.influencer.get(0)) || leftNeighbourInfluencer.contains(currentCell.influencer.get(1))) ) {

                            currentCell = leftNeighbourCell; //we can exit from the left cell as it is not influenced by the cells influencing the current cell
                            exitEdge = getLeftNeighbour(getLeftNeighbour(currentEdge));
                            currentEdge = exitEdge;
//                            System.out.print(" ;; Next Cell: "+currentCell.getCellNumber()+" ; Entry Edge: "+currentEdge+"\n");

                        }else if(!(rightNeighbourInfluencer.contains(currentCell.influencer.get(0)) || rightNeighbourInfluencer.contains(currentCell.influencer.get(1)))){

                            currentCell = rightNeighbourCell;
                            exitEdge = getRightNeighbour(getRightNeighbour(currentEdge));
                            currentEdge = exitEdge;
//                            System.out.print(" ;; Next Cell: "+currentCell.getCellNumber()+" ; Entry Edge: "+currentEdge+"\n");

                        }else{
                            currentCell.setCell_situation(situation.REFLECTED); //if the atoms are EQUIDISTANT
                            break;
                        }
                    }
                    else if(leftNeighbourCell.isInfluenced()){
                        currentCell = rightNeighbourCell;
                        exitEdge = getRightNeighbour(getRightNeighbour(currentEdge));
                        currentEdge = exitEdge;
//                        System.out.print(" ;; Next Cell: "+currentCell.getCellNumber()+" ; Entry Edge: "+currentEdge+"\n");

                    } else if( rightNeighbourCell.isInfluenced()){
                        currentCell = leftNeighbourCell;
                        exitEdge = getLeftNeighbour(getLeftNeighbour(currentEdge));
                        currentEdge = exitEdge;
//                        System.out.print(" ;; Next Cell: "+currentCell.getCellNumber()+" ; Entry Edge: "+currentEdge+"\n");
                    }
                }

                // -----------
            }
            else if(currentCell.getCell_situation() == situation.EMPTY){ // temporary as ray wouldn't be printed without this
//                System.out.println();
                exitEdge = getExitEdge(currentEdge); //gives the exit edge type

                int neighbourIndex = neighbourNumber(currentEdge); //gets neighbour NUMBER based on the input edge type

                if( currentCell.getNeighbour_cells().get(neighbourIndex) == null){
                    break;
                }

                currentCell = currentCell.getNeighbour_cells().get(neighbourIndex); //gets the neighbour CELL from which the ray will go next.
                currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(exitEdge);
            }

        } // End of while


        currentCell.exitEdge = this.exitEdge;

        return currentCell;
    }

    /**
     * Determines the path of a ray based on its input edge.
     * @param inputEdge The input edge of the ray.
     * @return An ArrayList containing the numbers of the cells the ray traverses through.
     */
    public ArrayList<Integer> rayPath(edgeDirection inputEdge){

        ArrayList<Integer> path = new ArrayList<>();
        path.add(this.getCellNumber());

        if(this.getCell_situation() == situation.ATOM) return path;
        if(this.getCell_situation() == situation.INFLUENCE){
            if(this.getNeighbour_cells().get(neighbourNumber(inputEdge)).getCell_situation() != situation.ATOM) return path;
        }

        if(this.isInfluenced() || this.cell_situation == situation.REFLECTED){
            if(this.cell_situation == situation.DOUBLE_INFLUENCE || this.cell_situation == situation.REFLECTED){
                return path;
            }
            if(this.getNeighbour_cells().get(neighbourNumber(inputEdge)).getCell_situation() != situation.ATOM){
                return path;
            }
        }

        edgeDirection currentEdge = inputEdge;
        Cell currentCell = this;

        while(true){

            if(!path.contains(currentCell.getCellNumber())) path.add(currentCell.cellNumber);

            if(currentCell == null) break;

//            System.out.println(currentCell.getCell_situation());

            if(currentCell.getCell_situation() == situation.ATOM){
                break;
            }
            else if(currentCell.getCell_situation() == situation.TRIPLE_INFLUENCE){
                break;
            }
            else if(currentCell.getCell_situation() == situation.INFLUENCE){
                exitEdge = getExitEdge(currentEdge); //gives the exit edge type

                int neighbourIndex = neighbourNumber(currentEdge); //gets neighbour NUMBER based on the input edge type
                int leftNeighbourIndex = ( (neighbourIndex - 1) + 6 ) % 6;
                int rightNeighbourIndex = ( (neighbourIndex + 1) + 6 ) % 6;

                Cell leftNeighbourCell = currentCell.getNeighbour_cells().get(leftNeighbourIndex);
                Cell rightNeighbourCell = currentCell.getNeighbour_cells().get(rightNeighbourIndex);

                /*if( currentCell.getNeighbour_cells().get(neighbourIndex) == null){
                    break;
                }*/
                if(currentCell.getNeighbour_cells().get(neighbourIndex) != null && currentCell.getNeighbour_cells().get(neighbourIndex).getCell_situation() == situation.ATOM){
                    currentCell = currentCell.getNeighbour_cells().get(neighbourIndex); //gets the neighbour CELL from which the ray will go next.
                    currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(exitEdge);
                    continue;
                }

                // At this point, next cell is not null and next cell does not have an atom (hence ray is not on a hit path)
                if(leftNeighbourCell != null && rightNeighbourCell != null ){
                    if(leftNeighbourCell.getCell_situation() == situation.ATOM){
                        currentCell = rightNeighbourCell;
                        exitEdge = getRightNeighbour(currentEdge);
                        currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(exitEdge);
                        currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(currentEdge);
                    } else if( rightNeighbourCell.getCell_situation() == situation.ATOM ){
                        currentCell = leftNeighbourCell;
                        exitEdge = getLeftNeighbour(currentEdge);
                        currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(exitEdge);
                        currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(currentEdge);
                    }
                } else if(leftNeighbourCell == null){
                    exitEdge = getLeftNeighbour(currentEdge);
                    currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(exitEdge);
                    currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(currentEdge);
                    exitEdge = nextInputEdge_BasedOn_PreviousExitEdge(currentEdge);
                    break;
                } else if(rightNeighbourCell == null){
                    exitEdge = getRightNeighbour(currentEdge);
                    currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(exitEdge);
                    currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(currentEdge);
                    exitEdge = nextInputEdge_BasedOn_PreviousExitEdge(currentEdge);
                    break;
                }

            }
            else if(currentCell.getCell_situation() == situation.DOUBLE_INFLUENCE || currentCell.getCell_situation() == situation.REFLECTED){
                exitEdge = getExitEdge(currentEdge); //gives the exit edge type

                int neighbourIndex = neighbourNumber(currentEdge); //gets neighbour NUMBER based on the input edge type
                int leftNeighbourIndex = ( (neighbourIndex - 2) + 6 ) % 6;
                int rightNeighbourIndex = ( (neighbourIndex + 2) + 6 ) % 6;

                Cell leftNeighbourCell = currentCell.getNeighbour_cells().get(leftNeighbourIndex);
                Cell rightNeighbourCell = currentCell.getNeighbour_cells().get(rightNeighbourIndex);

                // -----------

                if(leftNeighbourCell != null && rightNeighbourCell != null ){
                    if(leftNeighbourCell.isInfluenced() && rightNeighbourCell.isInfluenced()){
                        ArrayList<Integer> leftNeighbourInfluencer = leftNeighbourCell.influencer;
                        ArrayList<Integer> rightNeighbourInfluencer = rightNeighbourCell.influencer;

                        if(!(leftNeighbourInfluencer.contains(currentCell.influencer.get(0)) || leftNeighbourInfluencer.contains(currentCell.influencer.get(1))) ) {

                            currentCell = leftNeighbourCell; //we can exit from the left cell as it is not influenced by the cells influencing the current cell
                            exitEdge = getLeftNeighbour(getLeftNeighbour(currentEdge));
                            currentEdge = exitEdge;

                        }else if(!(rightNeighbourInfluencer.contains(currentCell.influencer.get(0)) || rightNeighbourInfluencer.contains(currentCell.influencer.get(1)))){

                            currentCell = rightNeighbourCell;
                            exitEdge = getRightNeighbour(getRightNeighbour(currentEdge));
                            currentEdge = exitEdge;

                        }else{
                            break;
                        }
                    }
                    else if(leftNeighbourCell.isInfluenced()){

                        currentCell = rightNeighbourCell;
                        exitEdge = getRightNeighbour(getRightNeighbour(currentEdge));
                        currentEdge = exitEdge;

                    } else if( rightNeighbourCell.isInfluenced()){

                        currentCell = leftNeighbourCell;
                        exitEdge = getLeftNeighbour(getLeftNeighbour(currentEdge));
                        currentEdge = exitEdge;

                    }
                }

                // -----------
            }
            else if(currentCell.getCell_situation() == situation.EMPTY){ // temporary as ray wouldn't be printed without this

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

    /**
     * Converts an index to an edge direction.
     * @param num The index.
     * @return The corresponding edge direction.
     */
    public edgeDirection indexToEdgeDirection(int num){
        switch (num){
            case 0:
                return edgeDirection.TR;
            case 1:
                return edgeDirection.R;
            case 2:
                return edgeDirection.BR;
            case 3:
                return edgeDirection.BL;
            case 4:
                return edgeDirection.L;
            case 5:
                return edgeDirection.TL;
            default:
                return null;
        }
    }

    /**
     * Determines the left neighbour of the current edge.
     * @param exit The exit edge.
     * @return The left neighbour edge.
     */
    public edgeDirection getLeftNeighbour(edgeDirection exit){
        switch (exit){
            case TR:
                return edgeDirection.TL;
            case TL:
                return edgeDirection.L;
            case L:
                return edgeDirection.BL;
            case BL:
                return edgeDirection.BR;
            case BR:
                return edgeDirection.R;
            case R:
                return edgeDirection.TR;
            default:
                return null;
        }
    }

    /**
     * Determines the right neighbour of the current edge.
     * @param exit The exit edge.
     * @return The right neighbour edge.
     */
    public edgeDirection getRightNeighbour(edgeDirection exit){
        switch (exit){
            case TR:
                return edgeDirection.R;
            case R:
                return edgeDirection.BR;
            case BR:
                return edgeDirection.BL;
            case BL:
                return edgeDirection.L;
            case L:
                return edgeDirection.TL;
            case TL:
                return edgeDirection.TR;
            default:
                return null;
        }
    }

    /**
     * Determines the next input edge based on the previous exit edge.
     * @param previousExit The previous exit edge.
     * @return The next input edge.
     */
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

    /**
     * Determines the number of the neighbour based on the input edge.
     * @param inputEdge The input edge.
     * @return The number of the neighbour.
     */
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

    /**
     * Determines the exit edge based on the input edge.
     * @param inputEdge The input edge.
     * @return The exit edge.
     */
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