package com.service;
import java.util.List;
import com.domain.Person;
import com.dao.PersonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class PersonService {
@Autowired
private PersonDao personDao;
public int create(Person person){
int result = personDao.create(person);
return result;
}
public List<Person> selectList(){
List<Person> result = personDao.selectList();
return result;
}
public Person selectOne(int id){
Person person= personDao.selectOne(id);
return person;
}
public int update(Person person){
int result = personDao.update(person);
return result;
}
public int delete(int id){
int result = personDao.delete(id);
return result;
}
}
