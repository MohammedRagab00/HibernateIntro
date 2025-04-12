package dao;

import config.DBConfig;

import java.util.ArrayList;
import java.util.List;

import models.TestTable;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class DBTestTable {

    public List<TestTable> getAll(int offset, int limit) {
        try (Session session = DBConfig.getSessionFactory().openSession()) {
            return session.createQuery("from TestTable", TestTable.class)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .getResultList();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return new ArrayList<>();
        }
    }

    //* Insert the object to table and return its id
    public Integer insert(TestTable testTable) {
        Transaction transaction = null;
        Integer id = null;

        try (Session session = DBConfig.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.persist(testTable);
            id = testTable.getId();

            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println(ex.getMessage());
        }

        return id;
    }

    //* Update the object
    public void update(TestTable testTable) {
        Transaction transaction = null;

        try (Session session = DBConfig.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.merge(testTable);

            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println(ex.getMessage());
        }
    }

    //* Delete the object with the given id
    public void delete(Integer id) {
        Transaction transaction = null;

        try (Session session = DBConfig.getSessionFactory().openSession()) {

            TestTable testTable = get(id);
            if (testTable != null) {
                transaction = session.beginTransaction();
                session.remove(testTable);
                transaction.commit();
            }

        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println(ex.getMessage());
        }
    }

    //* Retrieve the object with the given id
    public TestTable get(Integer id) {
        try (Session session = DBConfig.getSessionFactory().openSession()) {
            return session.find(TestTable.class, id);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return null;
        }
    }

    //* Retrieve all object that have the given name
    public List<TestTable> get(String name) {
        try (Session session = DBConfig.getSessionFactory().openSession()) {
            Query<TestTable> query = session.createQuery(
                    "from TestTable where name = :name",
                    TestTable.class
            );
            query.setParameter("name", name);
            return query.getResultList();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return new ArrayList<>();
        }
    }
}
