package com.controller;
import java.util.List;
import com.domain.Employee;
import com.service.EmployeeService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
@RestController
public class EmployeeController {
@Autowired
private EmployeeService employeeService;
@RequestMapping("/addEmployee")
public int create(@RequestBody Employee employee){
int result = employeeService.create(employee);
return result;
}
@RequestMapping("/getEmployeeList")
public List<Employee> selectList(){
List<Employee> result = employeeService.selectList();
return result;
}
@RequestMapping("/getEmployee")
public Employee selectOne(int empno){
Employee employee= employeeService.selectOne(empno);
return employee;
}
@RequestMapping("/editEmployee")
public int update(@RequestBody Employee employee){
int result = employeeService.update(employee);
return result;
}
@RequestMapping("/deleteEmployee")
public int delete(int empno){
int result = employeeService.delete(empno);
return result;
}
}
