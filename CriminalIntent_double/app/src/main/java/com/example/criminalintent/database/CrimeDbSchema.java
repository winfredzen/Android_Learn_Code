package com.example.criminalintent.database;

public class CrimeDbSchema {

    //内部类 数据库表名称CrimeTable.NAME
    public static final class CrimeTable {
        public static final String NAME = "crimes";
    }

    //定义数据表字段
    public static final class Cols {
        public static final String UUID = "uuid";
        public static final String TITLE = "title";
        public static final String DATE = "date";
        public static final String SOLVED = "solved";
        public static final String SUSPECT = "suspect";
    }

}
