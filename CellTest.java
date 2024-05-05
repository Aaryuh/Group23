import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CellTest {

    @Test
    void testNewCell(){

        Cell dummy = new Cell();
        assertEquals(0, dummy.getCellNumber()); // Beginning Cell Number
        assertEquals(Cell.situation.EMPTY, dummy.getCell_situation()); // Beginning Cell Situation

    }

    @Test
    void addAtom(){
        Cell dummy = new Cell();
        dummy.addAtom();
        assertEquals(Cell.situation.ATOM, dummy.getCell_situation());
    }

    @Test
    void singleInfluence(){
        Cell dummy = new Cell();
        dummy.setInfluence(5);
        assertEquals(Cell.situation.INFLUENCE, dummy.getCell_situation());
        assertEquals("[5]", dummy.influencer.toString());
    }

    @Test
    void doubleInfluence(){
        Cell dummy = new Cell();
        dummy.setInfluence(5);
        dummy.setInfluence(8);
        assertEquals(Cell.situation.DOUBLE_INFLUENCE, dummy.getCell_situation());
        assertEquals("[5, 8]", dummy.influencer.toString());
    }

    @Test
    void tripleInfluence(){
        Cell dummy = new Cell();
        dummy.setInfluence(5);
        dummy.setInfluence(8);
        dummy.setInfluence(9);
        assertEquals(Cell.situation.TRIPLE_INFLUENCE, dummy.getCell_situation());
        assertEquals("[5, 8, 9]", dummy.influencer.toString());
    }

    @Test
    void reflected(){
        Cell dummy = new Cell();
        dummy.setCell_situation(Cell.situation.REFLECTED);
        assertEquals(Cell.situation.REFLECTED, dummy.getCell_situation());
    }

}
