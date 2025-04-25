package com.sci;

import com.sci.config.DBConfig;
import com.sci.criteria.FilterQuery;
import com.sci.criteria.Operator;
import com.sci.dao.DBEmployee;
import com.sci.dao.DBTestTable;

import java.util.List;

import com.sci.models.Employee;
import com.sci.models.TestTable;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Test {

    public static void testCache1() {

        System.out.println("Test cache scenario 1");

        try (Session session = DBConfig.getSessionFactory().openSession()) {

            System.out.println(session.get(Employee.class, 2));

            System.out.println("--------------------------------");

            System.out.println(session.get(Employee.class, 2));

            System.out.println("--------------------------------");

            System.out.println(session.get(Employee.class, 2));

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    public static void testCache2() {

        System.out.println("Test cache scenario 2");

        try (Session session = DBConfig.getSessionFactory().openSession()) {

            System.out.println(session.get(Employee.class, 103));

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    public static void main(String[] args) {
//        DBTestTable dbTestTable = new DBTestTable();
//        List<TestTable> res = dbTestTable.getAll(2, 2);
//
//        res.forEach(System.out::println);

        DBEmployee dbEmployee = new DBEmployee();
//        Employee emp = new Employee();
//        emp.setFirst_name("mlwe");
//        emp.setEmail("dewc");
//        dbEmployee.insert(emp);
//        System.out.println(emp);

//        dbEmployee.delete("email");

//        Employee emp2 = dbEmployee.get(100);
//        Employee emp1 = dbEmployee.get(100);
//        System.out.println(emp2);
//        System.out.println(emp1);
//        int id = dbEmployee.insert(emp);
//        dbEmployee.delete(emp);



        List<Employee> employees = dbEmployee.getByFilter(
                List.of(
                        new FilterQuery("id", 200, Operator.GreaterThanOrEqual),
                        new FilterQuery("salary", 10000, Operator.EQ)
                ),
                true
        );
        for (Employee employee : employees) {
            System.out.println(employee);
        }





    }
}
