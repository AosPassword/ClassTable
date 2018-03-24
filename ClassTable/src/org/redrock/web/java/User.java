package org.redrock.web.java;

public class User {
    private String term;
    private String stuNum;
    private ClassTable info;

    public String getTerm() {
        return term;
    }
    public void setTerm(String term) {
        this.term = term;
    }
    public String getStuNum() {
        return stuNum;
    }

    public void setStuNum(String stuNum) {
        this.stuNum = stuNum;
    }

    public ClassTable getInfo() {
        return info;
    }

    public void setInfo(ClassTable info) {
        this.info = info;
    }
}
