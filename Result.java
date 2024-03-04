public class Result {

  //public enum situation {EMPTY, ATOM, INFLUENCE, DOUBLE_INFLUENCE, TRIPLE_INFLUENCE};

  //public enum edgeDirection {TR, R , BR, BL, L, TL};

  public Cell.situation situation;

  public Result(int CellNumber, Cell.edgeDirection exitEdge){
      this.CellNumber = CellNumber;
      this.lastExitEdge = exitEdge;
      this.situation = Cell.situation.EMPTY;
  }
  public int CellNumber;
  public Cell.edgeDirection lastExitEdge;

  public void setSituation(Cell.situation dummy){
      this.situation = dummy;
  }

}
