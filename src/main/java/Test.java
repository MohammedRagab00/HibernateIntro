import dao.DBTestTable;
import java.util.List;
import models.TestTable;

public class Test {

  public static void main(String[] args) {
    DBTestTable dbTestTable = new DBTestTable();
    List<TestTable> res = dbTestTable.getAll();
    System.out.println(res);
  }
}
