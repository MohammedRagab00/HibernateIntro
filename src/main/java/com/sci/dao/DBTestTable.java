package com.sci.dao;

import com.sci.config.DBConfig;

import java.util.ArrayList;
import java.util.List;

import com.sci.criteria.FilterQuery;
import com.sci.models.TestTable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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

    public List<TestTable> getByFilter(List<FilterQuery> filterQueries) {

        try (Session session = DBConfig.getSessionFactory().openSession()) {
            // To be edited in other relations CRUD OPs:
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<TestTable> cr = cb.createQuery(TestTable.class);
            Root<TestTable> root = cr.from(TestTable.class);

            Predicate[] predicates = new Predicate[filterQueries.size()];
            for (int i = 0; i < filterQueries.size(); i++) {
                switch (filterQueries.get(i).getOp()) {
                    case Equal:
                        predicates[i] =
                                cb.equal(root.get(filterQueries.get(i).
                                                getAttributeName()),
                                        filterQueries.get(i).getAttributeValue());
                        break;
                    case GreaterThan:
                        predicates[i] =
                                cb.greaterThan(root.get(filterQueries.
                                                get(i).getAttributeName()),
                                        (Comparable) filterQueries.get(i).
                                                getAttributeValue());
                        break;
                    case LessThan:
                        predicates[i] =
                                cb.lessThan(root.get(filterQueries.get(i).
                                                getAttributeName()),
                                        (Comparable) filterQueries.get(i).
                                                getAttributeValue());
                        break;
                    case NotEqual:
                        predicates[i] =
                                cb.notEqual(root.get(filterQueries.get(i).
                                                getAttributeName()),
                                        filterQueries.get(i).
                                                getAttributeValue());
                        break;
                    case IsNull:
                        predicates[i] =
                                cb.isNull(root.get(filterQueries.get(i).
                                        getAttributeName())
                                );
                        break;
                    case GreaterThanOrEqual:
                        predicates[i] =
                                cb.greaterThanOrEqualTo(
                                        root.get(filterQueries.get(i).
                                                getAttributeName()),
                                        (Comparable) filterQueries.get(i).
                                                getAttributeValue());
                        break;
                    case LessThanOrEqual:
                        predicates[i] =
                                cb.lessThanOrEqualTo(
                                        root.get(filterQueries.get(i)
                                                .getAttributeName()),
                                        (Comparable) filterQueries.get(i).
                                                getAttributeValue());
                        break;
                    case Like:
                        predicates[i] =
                                cb.like(root.get(filterQueries.get(i)
                                                .getAttributeName()),
                                        String.format("%%%s%%",
                                                filterQueries.get(i).getAttributeValue()));
                        break;
                    case Between:
                        List<Comparable> values =
                                (List<Comparable>) filterQueries.get(i).
                                        getAttributeValue();
                        predicates[i] =
                                cb.between(root.get(filterQueries.get(i).
                                                getAttributeName()),
                                        values.get(0), values.get(1));
                        break;
                    case In:
                        List<Object> inQuery = (List<Object>)
                                filterQueries.get(i).getAttributeValue();
                        predicates[i] =
                                root.get(filterQueries.get(i).getAttributeName()).
                                        in(inQuery);
                        break;
                }
            }

            cr.select(root).where(predicates);

            Query<TestTable> query = session.createQuery(cr);
            return query.getResultList();

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        return new ArrayList<>();
    }
}
