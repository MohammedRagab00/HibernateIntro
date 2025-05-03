package com.sci.dao;

import com.sci.config.DBConfig;

import java.util.ArrayList;
import java.util.List;

import com.sci.criteria.FilterQuery;
import com.sci.models.Customers;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class DBCustomers {

    public List<Customers> getAll(int offset, int limit) {
        try (Session session = DBConfig.getSessionFactory().openSession()) {
            return session.createQuery("from Customers", Customers.class)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .getResultList();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return new ArrayList<>();
        }
    }

    //* Insert the object to table and return its id
    public Integer insert(Customers Customers) {
        Transaction transaction = null;
        Integer id = null;

        try (Session session = DBConfig.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.persist(Customers);
            id = Customers.getCustomer_id();

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
    public void update(Customers Customers) {
        Transaction transaction = null;

        try (Session session = DBConfig.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.merge(Customers);

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

            Customers Customers = get(id);
            if (Customers != null) {
                transaction = session.beginTransaction();
                session.remove(Customers);
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
    public Customers get(Integer id) {
        try (Session session = DBConfig.getSessionFactory().openSession()) {
            return session.find(Customers.class, id);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return null;
        }
    }

    public List<Customers> getByFilter(List<FilterQuery> filterQueries) {

        try (Session session = DBConfig.getSessionFactory().openSession()) {
            // To be edited in other relations CRUD OPs:
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Customers> cr = cb.createQuery(Customers.class);
            Root<Customers> root = cr.from(Customers.class);

            Predicate[] predicates = new Predicate[filterQueries.size()];
            for (int i = 0; i < filterQueries.size(); i++) {
                switch (filterQueries.get(i).getOp()) {
                    case EQ:
                        predicates[i] =
                                cb.equal(root.get(filterQueries.get(i).
                                                getAttributeName()),
                                        filterQueries.get(i).getAttributeValue());
                        break;
                    case GT:
                        predicates[i] =
                                cb.greaterThan(root.get(filterQueries.
                                                get(i).getAttributeName()),
                                        (Comparable) filterQueries.get(i).
                                                getAttributeValue());
                        break;
                    case LT:
                        predicates[i] =
                                cb.lessThan(root.get(filterQueries.get(i).
                                                getAttributeName()),
                                        (Comparable) filterQueries.get(i).
                                                getAttributeValue());
                        break;
                    case NEQ:
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
                    case BT:
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

            Query<Customers> query = session.createQuery(cr);
            return query.getResultList();

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        return new ArrayList<>();
    }
}
