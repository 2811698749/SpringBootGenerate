package com.dao;
import java.util.List;
import com.domain.Person;
import org.apache.ibatis.annotations.*;
@Mapper
public interface PersonDao {
@Insert("insert into person(username,address,salary,department,age )values(#{username},#{address},#{salary},#{department},#{age})")
@Options(useGeneratedKeys=true, keyProperty = "id",keyColumn="id")
 int create(Person person);
  @Select("select username,address,salary,department,age,id from person")
List<Person> selectList();
@Select("select username,address,salary,department,age,id from person where id=#{id}")
Person selectOne(int id);
@Update("update person set username=#{username},address=#{address},salary=#{salary},department=#{department},age=#{age} where id=#{id}")
 int update(Person person);
@Delete("delete from  person where id=#{id}")
int delete(int id);
}
