import java.util.ArrayList;

public class Board {

    public Cell[] cells;

    public Board() {
        cells = new Cell[61];
        addCells();
        makeBoard();
//        ----- New Addition
        setAllCellsEmpty();
    }

    public void setAllCellsEmpty(){
        for(Cell dummy: cells){
            dummy.setCell_situation(Cell.situation.EMPTY);
        }
    }

    private void addCells() {
        for (int i = 0; i < cells.length; i++) {
            cells[i] = new Cell();
            cells[i].setCellNumber(i);
        }
    }

    private void makeBoard(){
        ArrayList<Cell> dummy = new ArrayList<>();
        //     ------------------0------------------
        dummy.add(null); // TR
        dummy.add(cells[1]); // R
        dummy.add(cells[6]); // BR
        dummy.add(cells[5]); // BL
        dummy.add(null); // L
        dummy.add(null); // TL
        cells[0].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();
        //        ---------------1-------------------
        dummy.add(null);
        dummy.add(cells[2]);
        dummy.add(cells[7]);
        dummy.add(cells[6]);
        dummy.add(cells[0]);
        dummy.add(null);
        cells[1].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------2-------------------
        dummy.add(null);
        dummy.add(cells[3]);
        dummy.add(cells[8]);
        dummy.add(cells[7]);
        dummy.add(cells[1]);
        dummy.add(null);
        cells[2].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();
        //        ---------------3-------------------
        dummy.add(null);
        dummy.add(cells[4]);
        dummy.add(cells[9]);
        dummy.add(cells[8]);
        dummy.add(cells[2]);
        dummy.add(null);
        cells[3].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();
        //        ---------------4-------------------
        dummy.add(null);
        dummy.add(null);
        dummy.add(cells[10]);
        dummy.add(cells[9]);
        dummy.add(cells[3]);
        dummy.add(null);
        cells[4].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------5-------------------
        dummy.add(cells[0]);
        dummy.add(cells[6]);
        dummy.add(cells[12]);
        dummy.add(cells[11]);
        dummy.add(null);
        dummy.add(null);
        cells[5].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------6-------------------
        dummy.add(cells[1]);
        dummy.add(cells[7]);
        dummy.add(cells[13]);
        dummy.add(cells[12]);
        dummy.add(cells[5]);
        dummy.add(cells[0]);
        cells[6].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------7-------------------
        dummy.add(cells[2]);
        dummy.add(cells[8]);
        dummy.add(cells[14]);
        dummy.add(cells[13]);
        dummy.add(cells[6]);
        dummy.add(cells[1]);
        cells[7].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        --------------8-------------------
        dummy.add(cells[3]);
        dummy.add(cells[9]);
        dummy.add(cells[15]);
        dummy.add(cells[14]);
        dummy.add(cells[7]);
        dummy.add(cells[2]);
        cells[8].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------9-------------------
        dummy.add(cells[4]);
        dummy.add(cells[10]);
        dummy.add(cells[16]);
        dummy.add(cells[15]);
        dummy.add(cells[8]);
        dummy.add(cells[3]);
        cells[9].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------10-------------------
        dummy.add(null);
        dummy.add(null);
        dummy.add(cells[17]);
        dummy.add(cells[16]);
        dummy.add(cells[9]);
        dummy.add(cells[4]);
        cells[10].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------11-------------------
        dummy.add(cells[5]);
        dummy.add(cells[12]);
        dummy.add(cells[19]);
        dummy.add(cells[18]);
        dummy.add(null);
        dummy.add(null);
        cells[11].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------12-------------------
        dummy.add(cells[6]);
        dummy.add(cells[13]);
        dummy.add(cells[20]);
        dummy.add(cells[19]);
        dummy.add(cells[11]);
        dummy.add(cells[5]);
        cells[12].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------13-------------------
        dummy.add(cells[7]);
        dummy.add(cells[14]);
        dummy.add(cells[21]);
        dummy.add(cells[20]);
        dummy.add(cells[12]);
        dummy.add(cells[6]);
        cells[13].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------14-------------------
        dummy.add(cells[8]);
        dummy.add(cells[15]);
        dummy.add(cells[22]);
        dummy.add(cells[21]);
        dummy.add(cells[13]);
        dummy.add(cells[7]);
        cells[14].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------15-------------------
        dummy.add(cells[9]);
        dummy.add(cells[16]);
        dummy.add(cells[23]);
        dummy.add(cells[22]);
        dummy.add(cells[14]);
        dummy.add(cells[8]);
        cells[15].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------16-------------------
        dummy.add(cells[10]);
        dummy.add(cells[17]);
        dummy.add(cells[24]);
        dummy.add(cells[23]);
        dummy.add(cells[15]);
        dummy.add(cells[9]);
        cells[16].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------17-------------------
        dummy.add(null);
        dummy.add(null);
        dummy.add(cells[25]);
        dummy.add(cells[24]);
        dummy.add(cells[16]);
        dummy.add(cells[10]);
        cells[17].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------18-------------------
        dummy.add(cells[11]);
        dummy.add(cells[19]);
        dummy.add(cells[27]);
        dummy.add(cells[26]);
        dummy.add(null);
        dummy.add(null);
        cells[18].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------19-------------------
        dummy.add(cells[12]);
        dummy.add(cells[20]);
        dummy.add(cells[28]);
        dummy.add(cells[27]);
        dummy.add(cells[18]);
        dummy.add(cells[11]);
        cells[19].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------20-------------------
        dummy.add(cells[13]);
        dummy.add(cells[21]);
        dummy.add(cells[29]);
        dummy.add(cells[28]);
        dummy.add(cells[19]);
        dummy.add(cells[12]);
        cells[20].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------21-------------------
        dummy.add(cells[14]);
        dummy.add(cells[22]);
        dummy.add(cells[30]);
        dummy.add(cells[29]);
        dummy.add(cells[20]);
        dummy.add(cells[13]);
        cells[21].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------22-------------------
        dummy.add(cells[15]);
        dummy.add(cells[23]);
        dummy.add(cells[31]);
        dummy.add(cells[30]);
        dummy.add(cells[21]);
        dummy.add(cells[14]);
        cells[22].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------23-------------------
        dummy.add(cells[16]);
        dummy.add(cells[24]);
        dummy.add(cells[32]);
        dummy.add(cells[31]);
        dummy.add(cells[22]);
        dummy.add(cells[15]);
        cells[23].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------24-------------------
        dummy.add(cells[17]);
        dummy.add(cells[25]);
        dummy.add(cells[33]);
        dummy.add(cells[32]);
        dummy.add(cells[23]);
        dummy.add(cells[16]);
        cells[24].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------25-------------------
        dummy.add(null);
        dummy.add(null);
        dummy.add(cells[34]);
        dummy.add(cells[33]);
        dummy.add(cells[24]);
        dummy.add(cells[17]);
        cells[25].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------26-------------------
        dummy.add(cells[18]);
        dummy.add(cells[27]);
        dummy.add(cells[35]);
        dummy.add(null);
        dummy.add(null);
        dummy.add(null);
        cells[26].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------27-------------------
        dummy.add(cells[19]);
        dummy.add(cells[28]);
        dummy.add(cells[36]);
        dummy.add(cells[35]);
        dummy.add(cells[26]);
        dummy.add(cells[18]);
        cells[27].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------28-------------------
        dummy.add(cells[20]);
        dummy.add(cells[29]);
        dummy.add(cells[37]);
        dummy.add(cells[36]);
        dummy.add(cells[27]);
        dummy.add(cells[19]);
        cells[28].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------29-------------------
        dummy.add(cells[21]);
        dummy.add(cells[30]);
        dummy.add(cells[38]);
        dummy.add(cells[37]);
        dummy.add(cells[28]);
        dummy.add(cells[20]);
        cells[29].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------30-------------------
        dummy.add(cells[22]);
        dummy.add(cells[31]);
        dummy.add(cells[39]);
        dummy.add(cells[38]);
        dummy.add(cells[29]);
        dummy.add(cells[21]);
        cells[30].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------31-------------------
        dummy.add(cells[23]);
        dummy.add(cells[32]);
        dummy.add(cells[40]);
        dummy.add(cells[39]);
        dummy.add(cells[30]);
        dummy.add(cells[22]);
        cells[31].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------32-------------------
        dummy.add(cells[24]);
        dummy.add(cells[33]);
        dummy.add(cells[41]);
        dummy.add(cells[40]);
        dummy.add(cells[31]);
        dummy.add(cells[23]);
        cells[32].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------33-------------------
        dummy.add(cells[25]);
        dummy.add(cells[34]);
        dummy.add(cells[42]);
        dummy.add(cells[41]);
        dummy.add(cells[32]);
        dummy.add(cells[24]);
        cells[33].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------34-------------------
        dummy.add(null);
        dummy.add(null);
        dummy.add(null);
        dummy.add(cells[42]);
        dummy.add(cells[33]);
        dummy.add(cells[25]);
        cells[34].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------35-------------------
        dummy.add(cells[27]);
        dummy.add(cells[36]);
        dummy.add(cells[43]);
        dummy.add(null);
        dummy.add(null);
        dummy.add(cells[26]);
        cells[35].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------36-------------------
        dummy.add(cells[28]);
        dummy.add(cells[37]);
        dummy.add(cells[44]);
        dummy.add(cells[43]);
        dummy.add(cells[35]);
        dummy.add(cells[27]);
        cells[36].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------37-------------------
        dummy.add(cells[29]);
        dummy.add(cells[38]);
        dummy.add(cells[45]);
        dummy.add(cells[44]);
        dummy.add(cells[36]);
        dummy.add(cells[28]);
        cells[37].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();


        //        ---------------38-------------------
        dummy.add(cells[30]);
        dummy.add(cells[39]);
        dummy.add(cells[46]);
        dummy.add(cells[45]);
        dummy.add(cells[37]);
        dummy.add(cells[29]);
        cells[38].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------39-------------------
        dummy.add(cells[31]);
        dummy.add(cells[40]);
        dummy.add(cells[47]);
        dummy.add(cells[46]);
        dummy.add(cells[38]);
        dummy.add(cells[30]);
        cells[39].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------40-------------------
        dummy.add(cells[32]);
        dummy.add(cells[41]);
        dummy.add(cells[48]);
        dummy.add(cells[47]);
        dummy.add(cells[39]);
        dummy.add(cells[31]);
        cells[40].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------41-------------------
        dummy.add(cells[33]);
        dummy.add(cells[42]);
        dummy.add(cells[49]);
        dummy.add(cells[48]);
        dummy.add(cells[40]);
        dummy.add(cells[32]);
        cells[41].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------42-------------------
        dummy.add(cells[34]);
        dummy.add(null);
        dummy.add(null);
        dummy.add(cells[49]);
        dummy.add(cells[41]);
        dummy.add(cells[33]);
        cells[42].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------43-------------------
        dummy.add(cells[36]);
        dummy.add(cells[44]);
        dummy.add(cells[50]);
        dummy.add(null);
        dummy.add(null);
        dummy.add(cells[35]);
        cells[43].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------44-------------------
        dummy.add(cells[37]);
        dummy.add(cells[45]);
        dummy.add(cells[51]);
        dummy.add(cells[50]);
        dummy.add(cells[43]);
        dummy.add(cells[36]);
        cells[44].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------45-------------------
        dummy.add(cells[38]);
        dummy.add(cells[46]);
        dummy.add(cells[52]);
        dummy.add(cells[51]);
        dummy.add(cells[44]);
        dummy.add(cells[37]);
        cells[45].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------46-------------------
        dummy.add(cells[39]);
        dummy.add(cells[47]);
        dummy.add(cells[53]);
        dummy.add(cells[52]);
        dummy.add(cells[45]);
        dummy.add(cells[38]);
        cells[46].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------47-------------------
        dummy.add(cells[40]);
        dummy.add(cells[48]);
        dummy.add(cells[54]);
        dummy.add(cells[53]);
        dummy.add(cells[46]);
        dummy.add(cells[39]);
        cells[47].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------48-------------------
        dummy.add(cells[41]);
        dummy.add(cells[49]);
        dummy.add(cells[55]);
        dummy.add(cells[54]);
        dummy.add(cells[47]);
        dummy.add(cells[40]);
        cells[48].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------49-------------------
        dummy.add(cells[42]);
        dummy.add(null);
        dummy.add(null);
        dummy.add(cells[55]);
        dummy.add(cells[48]);
        dummy.add(cells[41]);
        cells[49].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------50-------------------
        dummy.add(cells[44]);
        dummy.add(cells[51]);
        dummy.add(cells[56]);
        dummy.add(null);
        dummy.add(null);
        dummy.add(cells[43]);
        cells[50].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();


        //        ---------------51-------------------
        dummy.add(cells[45]);
        dummy.add(cells[52]);
        dummy.add(cells[57]);
        dummy.add(cells[56]);
        dummy.add(cells[50]);
        dummy.add(cells[44]);
        cells[51].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------52-------------------
        dummy.add(cells[46]);
        dummy.add(cells[53]);
        dummy.add(cells[58]);
        dummy.add(cells[57]);
        dummy.add(cells[51]);
        dummy.add(cells[45]);
        cells[52].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------53-------------------
        dummy.add(cells[47]);
        dummy.add(cells[54]);
        dummy.add(cells[59]);
        dummy.add(cells[58]);
        dummy.add(cells[52]);
        dummy.add(cells[46]);
        cells[53].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------54-------------------
        dummy.add(cells[48]);
        dummy.add(cells[55]);
        dummy.add(cells[60]);
        dummy.add(cells[59]);
        dummy.add(cells[53]);
        dummy.add(cells[47]);
        cells[54].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------55-------------------
        dummy.add(cells[49]);
        dummy.add(null);
        dummy.add(null);
        dummy.add(cells[60]);
        dummy.add(cells[54]);
        dummy.add(cells[48]);
        cells[55].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------56-------------------
        dummy.add(cells[51]);
        dummy.add(cells[57]);
        dummy.add(null);
        dummy.add(null);
        dummy.add(null);
        dummy.add(cells[50]);
        cells[56].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------57-------------------
        dummy.add(cells[52]);
        dummy.add(cells[58]);
        dummy.add(null);
        dummy.add(null);
        dummy.add(cells[56]);
        dummy.add(cells[51]);
        cells[57].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------58-------------------
        dummy.add(cells[53]);
        dummy.add(cells[59]);
        dummy.add(null);
        dummy.add(null);
        dummy.add(cells[57]);
        dummy.add(cells[52]);
        cells[58].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------59-------------------
        dummy.add(cells[54]);
        dummy.add(cells[60]);
        dummy.add(null);
        dummy.add(null);
        dummy.add(cells[58]);
        dummy.add(cells[53]);
        cells[59].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

        //        ---------------60-------------------
        dummy.add(cells[55]);
        dummy.add(null);
        dummy.add(null);
        dummy.add(null);
        dummy.add(cells[59]);
        dummy.add(cells[54]);
        cells[60].setNeighbour_cells(new ArrayList<>(dummy));
        dummy.clear();

    }

    public static void main(String[] args) {

    }
}