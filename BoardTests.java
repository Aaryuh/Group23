import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BoardTests {

    @Test
    void setAllCellsEmpty() {
        Board game = new Board();

        for(Cell dummy: game.cells){
            assertEquals(Cell.situation.EMPTY, dummy.getCell_situation());
        }
    }



    @Test
    void numCells() {

        Board game = new Board();
        assertEquals(61, game.cells.length);

    }

    @Test
    void checkNeighbours() {

        Board game = new Board();
        ArrayList<Cell> dummy = game.cells[6].getNeighbour_cells();
        ArrayList<Integer> neighbourNumbers = new ArrayList<>();

        for(Cell a : dummy){
            neighbourNumbers.add(a.getCellNumber());
        }
         assertEquals("[1, 7, 13, 12, 5, 0]", neighbourNumbers.toString());

    }

    @Test
    void emptyPath(){
        Board game = new Board();
        ArrayList<Integer> path = game.cells[4].rayPath(Cell.edgeDirection.R);
        assertEquals("[4, 3, 2, 1, 0]", path.toString());
    }

    @Test
    void checkHit(){
        Board game = new Board();
        game.cells[2].addAtom();
        ArrayList<Integer> path = game.cells[4].rayPath(Cell.edgeDirection.R);
        assertEquals("[4, 3, 2]", path.toString());
    }

    @Test
    void check_60_Deflection(){
        Board game = new Board();
        game.cells[15].addAtom();

        ArrayList<Integer> path = game.cells[3].rayPath(Cell.edgeDirection.TR);
        assertEquals("[3, 8, 7, 6, 5]", path.toString());
    }

    @Test
    void check_120_Deflection(){
        Board game = new Board();
        game.cells[15].addAtom();
        game.cells[14].addAtom();

        ArrayList<Integer> path = game.cells[3].rayPath(Cell.edgeDirection.TR);
        assertEquals("[3, 8, 2]", path.toString());
    }

    @Test
    void check_TripleInfluenced(){
        Board game = new Board();
        game.cells[15].addAtom();
        game.cells[14].addAtom();
        game.cells[9].addAtom();

        ArrayList<Integer> path = game.cells[2].rayPath(Cell.edgeDirection.TL);
        assertEquals("[2, 8]", path.toString());
    }

    @Test
    void check_Reflection(){
        Board game = new Board();
        game.cells[1].addAtom();

        ArrayList<Integer> path = game.cells[2].rayPath(Cell.edgeDirection.TR);
        assertEquals("[2]", path.toString());
    }

    @Test
    void equally_spaced_atoms_Reflection(){
        Board game = new Board();
        game.cells[30].addAtom();
        game.cells[14].addAtom();

        ArrayList<Integer> path = game.cells[18].rayPath(Cell.edgeDirection.L);
        assertEquals("[18, 19, 20, 21]", path.toString());
    }
}