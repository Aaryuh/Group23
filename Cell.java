import java.util.ArrayList;

public class Cell {

    private int cellNumber;

    public enum situation {EMPTY, ATOM, INFLUENCE, DOUBLE_INFLUENCE, TRIPLE_INFLUENCE, REFLECTED};

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

        if(this.getCell_situation() == situation.INFLUENCE){
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
            System.out.print("Cell: "+currentCell.getCellNumber()+"; Cell Situation: "+currentCell.getCell_situation() +"; Entry Edge: "+currentEdge);

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

                /*if( currentCell.getNeighbour_cells().get(neighbourIndex) == null){
                    break;
                }*/
                if(currentCell.getNeighbour_cells().get(neighbourIndex) != null && currentCell.getNeighbour_cells().get(neighbourIndex).getCell_situation() == situation.ATOM){
                    currentCell = currentCell.getNeighbour_cells().get(neighbourIndex); //gets the neighbour CELL from which the ray will go next.
                    currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(exitEdge);
                    continue;
                }

                // At this point, next cell is not null and next cell does not have an atom (hence ray is not on a hit path)
                if(currentCell.getNeighbour_cells().get(leftNeighbourIndex) != null && currentCell.getNeighbour_cells().get(rightNeighbourIndex) != null ){
                    if(currentCell.getNeighbour_cells().get(leftNeighbourIndex).getCell_situation() == situation.ATOM){
                        currentCell = currentCell.getNeighbour_cells().get(rightNeighbourIndex);
                        exitEdge = getRightNeighbour(currentEdge);
                        currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(exitEdge);
                        currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(currentEdge);
                        System.out.print(" ;; Next Cell: "+currentCell.getCellNumber()+" ; Entry Edge: "+currentEdge+"\n");
                    } else if( currentCell.getNeighbour_cells().get(rightNeighbourIndex).getCell_situation() == situation.ATOM ){
                        currentCell = currentCell.getNeighbour_cells().get(leftNeighbourIndex);
                        exitEdge = getLeftNeighbour(currentEdge);
                        currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(exitEdge);
                        currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(currentEdge);
                        System.out.print(" ;; Next Cell: "+currentCell.getCellNumber()+" ; Entry Edge: "+currentEdge+"\n");
                    }
                } else if(currentCell.getNeighbour_cells().get(leftNeighbourIndex) == null){
                    exitEdge = getLeftNeighbour(currentEdge);
                    currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(exitEdge);
                    currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(currentEdge);
                    exitEdge = nextInputEdge_BasedOn_PreviousExitEdge(currentEdge);
                    System.out.println(" Exit:: "+exitEdge);
                    break;
                } else if(currentCell.getNeighbour_cells().get(rightNeighbourIndex) == null){
                    exitEdge = getRightNeighbour(currentEdge);
                    currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(exitEdge);
                    currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(currentEdge);
                    exitEdge = nextInputEdge_BasedOn_PreviousExitEdge(currentEdge);
                    System.out.println(" Exit:: "+exitEdge);
                    break;
                }

            }
            else if(currentCell.getCell_situation() == situation.DOUBLE_INFLUENCE){
                exitEdge = getExitEdge(currentEdge); //gives the exit edge type

                int neighbourIndex = neighbourNumber(currentEdge); //gets neighbour NUMBER based on the input edge type
                int leftNeighbourIndex = ( (neighbourIndex - 2) + 6 ) % 6;
                int rightNeighbourIndex = ( (neighbourIndex + 2) + 6 ) % 6;

                // -----------

                if(currentCell.getNeighbour_cells().get(leftNeighbourIndex) != null && currentCell.getNeighbour_cells().get(rightNeighbourIndex) != null ){
                    if(currentCell.getNeighbour_cells().get(leftNeighbourIndex).getCell_situation() == situation.INFLUENCE && currentCell.getNeighbour_cells().get(rightNeighbourIndex).getCell_situation() == situation.INFLUENCE){
                        currentCell.setCell_situation(situation.REFLECTED);
                        break;
                    }
                    else if(currentCell.getNeighbour_cells().get(leftNeighbourIndex).getCell_situation() == situation.INFLUENCE){
                        currentCell = currentCell.getNeighbour_cells().get(rightNeighbourIndex);
                        exitEdge = getRightNeighbour(getRightNeighbour(currentEdge));
                        //currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(exitEdge);
                        //currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(currentEdge);
                        currentEdge = exitEdge;
                        System.out.print(" ;; Next Cell: "+currentCell.getCellNumber()+" ; Entry Edge: "+currentEdge+"\n");
                    } else if( currentCell.getNeighbour_cells().get(rightNeighbourIndex).getCell_situation() == situation.INFLUENCE ){
                        currentCell = currentCell.getNeighbour_cells().get(leftNeighbourIndex);
                        exitEdge = getLeftNeighbour(getLeftNeighbour(currentEdge));
                        //currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(exitEdge);
                        //currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(currentEdge);
                        currentEdge = exitEdge;
                        System.out.print(" ;; Next Cell: "+currentCell.getCellNumber()+" ; Entry Edge: "+currentEdge+"\n");
                    }
                }
                else if(currentCell.getNeighbour_cells().get(leftNeighbourIndex) == null){
                    exitEdge = getRightNeighbour(getRightNeighbour(currentEdge));
                    currentEdge = exitEdge;
                }
                else if(currentCell.getNeighbour_cells().get(rightNeighbourIndex) == null){
                    exitEdge = getLeftNeighbour(getLeftNeighbour(currentEdge));
                    currentEdge = exitEdge;
                }

                // -----------
            }
            else if(currentCell.getCell_situation() == situation.EMPTY){ // temporary as ray wouldn't be printed without this
                System.out.println();
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
        if(this.getCell_situation() == situation.INFLUENCE){
            if(this.getNeighbour_cells().get(neighbourNumber(inputEdge)).getCell_situation() != situation.ATOM) return path;
        }

        edgeDirection currentEdge = inputEdge;
        Cell currentCell = this;

        while(true){
            if(!path.contains(currentCell.getCellNumber())) path.add(currentCell.cellNumber);

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

                /*if( currentCell.getNeighbour_cells().get(neighbourIndex) == null){
                    break;
                }*/
                if(currentCell.getNeighbour_cells().get(neighbourIndex) != null && currentCell.getNeighbour_cells().get(neighbourIndex).getCell_situation() == situation.ATOM){
                    currentCell = currentCell.getNeighbour_cells().get(neighbourIndex); //gets the neighbour CELL from which the ray will go next.
                    currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(exitEdge);
                    continue;
                }

                // At this point, next cell is not null and next cell does not have an atom (hence ray is not on a hit path)
                if(currentCell.getNeighbour_cells().get(leftNeighbourIndex) != null && currentCell.getNeighbour_cells().get(rightNeighbourIndex) != null ){
                    if(currentCell.getNeighbour_cells().get(leftNeighbourIndex).getCell_situation() == situation.ATOM){
                        currentCell = currentCell.getNeighbour_cells().get(rightNeighbourIndex);
                        exitEdge = getRightNeighbour(currentEdge);
                        currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(exitEdge);
                        currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(currentEdge);
                    } else if( currentCell.getNeighbour_cells().get(rightNeighbourIndex).getCell_situation() == situation.ATOM ){
                        currentCell = currentCell.getNeighbour_cells().get(leftNeighbourIndex);
                        exitEdge = getLeftNeighbour(currentEdge);
                        currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(exitEdge);
                        currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(currentEdge);
                    }
                } else if(currentCell.getNeighbour_cells().get(leftNeighbourIndex) == null){
                    exitEdge = getLeftNeighbour(currentEdge);
                    currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(exitEdge);
                    currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(currentEdge);
                    exitEdge = nextInputEdge_BasedOn_PreviousExitEdge(currentEdge);
                    break;
                } else if(currentCell.getNeighbour_cells().get(rightNeighbourIndex) == null){
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

                // -----------

                if(currentCell.getNeighbour_cells().get(leftNeighbourIndex) != null && currentCell.getNeighbour_cells().get(rightNeighbourIndex) != null ){
                    if(currentCell.getNeighbour_cells().get(leftNeighbourIndex).getCell_situation() == situation.INFLUENCE && currentCell.getNeighbour_cells().get(rightNeighbourIndex).getCell_situation() == situation.INFLUENCE){
                        break;
                    }
                    if(currentCell.getNeighbour_cells().get(leftNeighbourIndex).getCell_situation() == situation.INFLUENCE){
                        currentCell = currentCell.getNeighbour_cells().get(rightNeighbourIndex);
                        exitEdge = getRightNeighbour(getRightNeighbour(currentEdge));
                        //currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(exitEdge);
                        //currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(currentEdge);
                        currentEdge = exitEdge;
                        System.out.print(" ;; Next Cell: "+currentCell.getCellNumber()+" ; Entry Edge: "+currentEdge+"\n");
                    } else if( currentCell.getNeighbour_cells().get(rightNeighbourIndex).getCell_situation() == situation.INFLUENCE ){
                        currentCell = currentCell.getNeighbour_cells().get(leftNeighbourIndex);
                        exitEdge = getLeftNeighbour(getLeftNeighbour(currentEdge));
                        //currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(exitEdge);
                        //currentEdge = nextInputEdge_BasedOn_PreviousExitEdge(currentEdge);
                        currentEdge = exitEdge;
                        System.out.print(" ;; Next Cell: "+currentCell.getCellNumber()+" ; Entry Edge: "+currentEdge+"\n");
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


//import java.util.ArrayList;
//
//public class Cell {
//
//    private int cellNumber;
//
//    public enum situation {EMPTY, ATOM, INFLUENCE, DOUBLE_INFLUENCE, TRIPLE_INFLUENCE};
//
//    public enum edges {TR, R , BR, BL, L, TL};
//
//    public situation cell_situation;
//
//    public ArrayList<Cell> neighbour_cells;
//
//    public Cell(){
//        this.cell_situation = situation.EMPTY;
//        this.cellNumber = 0;
//        neighbour_cells = new ArrayList<>();
//    }
//
//    public void setCell_situation(situation cell_situation) {
//        this.cell_situation = cell_situation;
//    }
//
//    public void setNeighbour_cells(ArrayList<Cell> neighbour_cells) {
//        this.neighbour_cells = neighbour_cells;
//    }
//
//    public int getCellNumber() {
//        return cellNumber;
//    }
//
//    public void setCellNumber(int cellNumber) {
//        this.cellNumber = cellNumber;
//    }
//
//    public situation getCell_situation() {
//        return cell_situation;
//    }
//
//    public ArrayList<Cell> getNeighbour_cells() {
//        return neighbour_cells;
//    }
//
//    public Result rayEntry(edges entryEdge){
//
//        if(entryEdge == null){
//            throw new IllegalArgumentException("Invalid Entry Edge given in method \"Ray Entry\"");
//        }
//
//        edges currentEdge = exitEdgeIfEmpty(entryEdge);
//        Cell currentCell = this.neighbour_cells.get(edgeToNumber(currentEdge));
//
//        int flag = 0;
//
//        while(currentCell != null){
//            if(flag != 0){
//                currentEdge = exitEdgeIfEmpty(currentEdge);
//            }
//            else{
//                flag = 1;
//            }
//            if(currentCell.neighbour_cells.get(edgeToNumber(currentEdge)) == null){
//                break;
//            }
//            if(currentCell.getCell_situation() == situation.ATOM){
//                break;
//            }
//            currentCell = currentCell.neighbour_cells.get(edgeToNumber(currentEdge));
//        }
//
//        Result finalCell = new Result(currentCell.getCellNumber(), currentEdge);
//        finalCell.setSituation(currentCell.getCell_situation());
//        return finalCell;
//    }
//
//    public edges exitEdgeIfEmpty(edges entryEdge){
//
//        switch (entryEdge){
//            case TR:
//                return edges.BL;
//            case R:
//                return edges.L;
//            case BR:
//                return edges.TL;
//            case BL:
//                return edges.TR;
//            case L:
//                return edges.R;
//            case TL:
//                return edges.BR;
//            default:
//                return null;
//        }
//
//    } // Exit edge Method END
//
//    public int edgeToNumber(edges dummy){
//        switch (dummy){
//            case TR:
//                return 0;
//            case R:
//                return 1;
//            case BR:
//                return 2;
//            case BL:
//                return 3;
//            case L:
//                return 4;
//            case TL:
//                return 5;
//            default:
//                return -1;
//        }
//    }
//
//}