package dao;

import config.Config;
import java.util.ArrayList;
import java.util.List;
import models.TestTable;
import org.hibernate.Session;

public class DBTestTable {

  public List<TestTable> getAll() {
    try (Session session = Config.SESSION_FACTORY.openSession()) {
      return session.createQuery("from TestTable", TestTable.class).getResultList();
    } catch (Exception ex) {
      System.err.println(ex.getMessage());
      return new ArrayList<>();
    }
  }

  // insert the object to table and return its id
  // public Integer insert(TestTable testTable)

  // update the object
  // public void update(TestTable testTable)

  // delete the object with the given id
  // public void delete(Integer id)

  // retrieve the object with the given id
  // public TestTable get(Integer id)

  // retrieve all object that have the given name
  // public List<TestTable> get(String name)
}
