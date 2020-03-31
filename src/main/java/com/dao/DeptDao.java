package com.dao;
import java.util.List;
import com.domain.Dept;
import org.apache.ibatis.annotations.*;
@Mapper
public interface DeptDao {
@Insert("insert into dept(deptname )values(#{deptname})")
@Options(useGeneratedKeys=true, keyProperty = "deptno",keyColumn="deptno")
 int create(Dept dept);
  @Select("select deptno,deptname from dept")
List<Dept> selectList();
@Select("select deptno,deptname from dept where deptno=#{deptno}")
Dept selectOne(int id);
@Update("update dept set deptname=#{deptname} where deptno=#{deptno}")
 int update(Dept dept);
@Delete("delete from  dept where deptno=#{deptno}")
int delete(int deptno);
}
