package com.dao;
import java.util.List;
import com.domain.Employee;
import org.apache.ibatis.annotations.*;
@Mapper
public interface EmployeeDao {
@Insert("insert into employee(ename,job,mgr,hire_date,sal,comm,dept_no,price )values(#{ename},#{job},#{mgr},#{hireDate},#{sal},#{comm},#{deptNo},#{price})")
@Options(useGeneratedKeys=true, keyProperty = "empno",keyColumn="empno")
 int create(Employee employee);
  @Select("select empno,ename,job,mgr,hire_date,sal,comm,dept_no,price from employee")
List<Employee> selectList();
@Select("select empno,ename,job,mgr,hire_date,sal,comm,dept_no,price from employee where empno=#{empno}")
Employee selectOne(int id);
@Update("update employee set ename=#{ename},job=#{job},mgr=#{mgr},hire_date=#{hireDate},sal=#{sal},comm=#{comm},dept_no=#{deptNo},price=#{price} where empno=#{empno}")
 int update(Employee employee);
@Delete("delete from  employee where empno=#{empno}")
int delete(int empno);
}
