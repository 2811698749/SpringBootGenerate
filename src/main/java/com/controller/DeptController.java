package com.controller;
import java.util.List;
import com.domain.Dept;
import com.service.DeptService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
@RestController
public class DeptController {
@Autowired
private DeptService deptService;
@RequestMapping("/addDept")
public int create(@RequestBody Dept dept){
int result = deptService.create(dept);
return result;
}
@RequestMapping("/getDeptList")
public List<Dept> selectList(){
List<Dept> result = deptService.selectList();
return result;
}
@RequestMapping("/getDept")
public Dept selectOne(int deptno){
Dept dept= deptService.selectOne(deptno);
return dept;
}
@RequestMapping("/editDept")
public int update(@RequestBody Dept dept){
int result = deptService.update(dept);
return result;
}
@RequestMapping("/deleteDept")
public int delete(int deptno){
int result = deptService.delete(deptno);
return result;
}
}
