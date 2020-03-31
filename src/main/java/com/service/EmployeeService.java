package com.service;
import java.util.List;
import com.domain.Employee;
import com.dao.EmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class EmployeeService {
@Autowired
private EmployeeDao employeeDao;
public int create(Employee employee){
int result = employeeDao.create(employee);
return result;
}
public List<Employee> selectList(){
List<Employee> result = employeeDao.selectList();
return result;
}
public Employee selectOne(int empno){
Employee employee= employeeDao.selectOne(empno);
return employee;
}
public int update(Employee employee){
int result = employeeDao.update(employee);
return result;
}
public int delete(int empno){
int result = employeeDao.delete(empno);
return result;
}
}
