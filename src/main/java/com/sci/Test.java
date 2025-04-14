package com.sci;

import com.sci.dao.DBTestTable;

import java.util.List;

import com.sci.models.TestTable;

public class Test {

    public static void main(String[] args) {
        DBTestTable dbTestTable = new DBTestTable();
        List<TestTable> res = dbTestTable.getAll(2, 2);

        res.forEach(System.out::println);
    }
}
