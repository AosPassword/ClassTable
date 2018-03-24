package org.redrock.web.java;

import java.util.Comparator;

public class ComparatorClassTable implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        ClassTable classTable1= (ClassTable) o1;
        ClassTable classTable2= (ClassTable) o2;
        if (classTable1.getSort_day()!=classTable2.getSort_day()){
            return classTable1.getSort_day()-classTable2.getSort_day();
        }else {
            return classTable1.getSort_lesson()-classTable2.getSort_lesson();
        }
    }
}
