package com.service;
import java.util.List;
import com.domain.Dept;
import com.dao.DeptDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class DeptService {
@Autowired
private DeptDao deptDao;
public int create(Dept dept){
int result = deptDao.create(dept);
return result;
}
public List<Dept> selectList(){
List<Dept> result = deptDao.selectList();
return result;
}
public Dept selectOne(int deptno){
Dept dept= deptDao.selectOne(deptno);
return dept;
}
public int update(Dept dept){
int result = deptDao.update(dept);
return result;
}
public int delete(int deptno){
int result = deptDao.delete(deptno);
return result;
}
}
