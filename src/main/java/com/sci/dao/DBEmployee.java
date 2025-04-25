package com.sci.dao;

import com.sci.config.DBConfig;
import com.sci.criteria.FilterQuery;
import com.sci.models.Employee;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.processing.HQL;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class DBEmployee {

    public List<Employee> getAll() {
        try (Session session = DBConfig.getSessionFactory().openSession()) {
            return session.createQuery("from Employee", Employee.class)
                    .getResultList();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return new ArrayList<>();
        }
    }


    //* Retrieve the object with the given id
    public Employee get(Integer id) {
        try (Session session = DBConfig.getSessionFactory().openSession()) {
            return session.get(Employee.class, id);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return null;
        }
    }

    //* Retrieve the object with the given id
    public Employee getByEmail(String email) {
        try (Session session = DBConfig.getSessionFactory().openSession()) {

            return session.createQuery("from Employee WHERE email =: x", Employee.class)
                    // x = email
                    .setParameter("x", email)
                    .getSingleResult();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return null;
        }
    }

    //* Insert the object to table and return its id
    public Integer insert(Employee Employee) {
        Transaction transaction = null;
        Integer id = null;
        try (Session session = DBConfig.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.persist(Employee);
            id = Employee.getId();

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
    public void update(Employee Employee) {
        Transaction transaction = null;

        try (Session session = DBConfig.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.merge(Employee);

            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println(ex.getMessage());
        }
    }

    //* Delete the object with the given email
    public void deleteByEmail(String email) {
        Transaction transaction = null;
        try (Session session = DBConfig.getSessionFactory().openSession()) {

            Employee emp = getByEmail(email);
            if (emp != null) {
                transaction = session.beginTransaction();
                session.remove(emp);
                transaction.commit();
            }

        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println(ex.getMessage());
        }
    }

    //* Delete the object with the given id
    public void deleteById(Integer id) {
        Transaction transaction = null;
        try (Session session = DBConfig.getSessionFactory().openSession()) {

            Employee emp = get(id);
            if (emp != null) {
                transaction = session.beginTransaction();
                session.remove(emp);
                transaction.commit();
            }

        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println(ex.getMessage());
        }
    }

    //* Retrieve all object that have the given name
    public List<Employee> get(String name) {
        try (Session session = DBConfig.getSessionFactory().openSession()) {
            return session.createQuery(
                            "from Employee where first_name = :name_in_query",
                            Employee.class
                    )
                    .setParameter("name_in_query", name)
                    .getResultList();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Employee> getByFilter(List<FilterQuery> filterQueries, boolean isAnd) {

        try (Session session = DBConfig.getSessionFactory().openSession()) {
            // To be edited in other relations CRUD OPs:
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
            Root<Employee> root = cr.from(Employee.class);

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


            Predicate predicatesss = isAnd ? cb.and(predicates) : cb.or(predicates);

            cr.select(root).where(predicatesss);

            Query<Employee> query = session.createQuery(cr);
            return query.getResultList();

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        return new ArrayList<>();
    }
}
