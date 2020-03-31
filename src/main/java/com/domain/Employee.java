package com.domain;
import java.util.Date;
import lombok.Setter;
import lombok.Getter;
@Setter
@Getter
public class Employee{
 private Integer empno;
 private String ename;
 private String job;
 private String mgr;
private Date hireDate;
 private Integer sal;
 private Integer comm;
 private Integer deptNo;
 private Float price;
}
