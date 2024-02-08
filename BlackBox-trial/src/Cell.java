import java.util.ArrayList;

public class Cell {

    private int cellNumber;

    private enum situation {EMPTY, ATOM, INFLUENCE, DOUBLE_INFLUENCE, TRIPLE_INFLUENCE};

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
}