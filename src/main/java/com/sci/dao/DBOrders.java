package com.sci.dao;

import com.sci.config.DBConfig;
import com.sci.criteria.FilterQuery;
import com.sci.models.Orders;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class DBOrders {

    public List<Orders> getAll(int offset, int limit) {
        try (Session session = DBConfig.getSessionFactory().openSession()) {
            return session.createQuery("from Orders", Orders.class)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .getResultList();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return new ArrayList<>();
        }
    }

    //* Insert the object to table and return its id
    public Integer insert(Orders Orders) {
        Transaction transaction = null;
        Integer id = null;

        try (Session session = DBConfig.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.persist(Orders);
            id = Orders.getOrder_id();

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
    public void update(Orders Orders) {
        Transaction transaction = null;

        try (Session session = DBConfig.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.merge(Orders);

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

            Orders Orders = get(id);
            if (Orders != null) {
                transaction = session.beginTransaction();
                session.remove(Orders);
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
    public Orders get(Integer id) {
        try (Session session = DBConfig.getSessionFactory().openSession()) {
            return session.find(Orders.class, id);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return null;
        }
    }

    public List<Orders> getByFilter(List<FilterQuery> filterQueries) {

        try (Session session = DBConfig.getSessionFactory().openSession()) {
            // To be edited in other relations CRUD OPs:
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Orders> cr = cb.createQuery(Orders.class);
            Root<Orders> root = cr.from(Orders.class);

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

            Query<Orders> query = session.createQuery(cr);
            return query.getResultList();

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        return new ArrayList<>();
    }
}
