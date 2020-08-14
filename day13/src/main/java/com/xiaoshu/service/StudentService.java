package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaoshu.dao.CourseMapper;
import com.xiaoshu.dao.StudentMapper;
import com.xiaoshu.entity.Course;
import com.xiaoshu.entity.Student;
import com.xiaoshu.entity.StudentVo;

import redis.clients.jedis.Jedis;

@Service
public class StudentService {

	@Autowired
	private StudentMapper sm;
	
	
	@Autowired
	private CourseMapper cm;
	
	public PageInfo<StudentVo> findPage(StudentVo studentVo,Integer pageNum,Integer pageSize,String ordername,String order){
		PageHelper.startPage(pageNum, pageSize);
		List<StudentVo> list = sm.findAll(studentVo);
		return new PageInfo<>(list);
	}

	public List<Course> findCourse() {
		List<Course> list = cm.selectAll();
		return list;
	}
	public void addStu(Student student){
		sm.insert(student);
	}
	public void updateStu(Student student){
		sm.updateByPrimaryKeySelective(student);
	}
	public Student findByName(String name){
		Student param = new Student();
		param.setName(name);
		return sm.selectOne(param );
	}
	public void deleteStu(Integer id){
		sm.deleteByPrimaryKey(id);
	}
	public List<StudentVo> countStu(){
		return sm.countStu();
	}

	public List<Course> findList(Course course) {
		return cm.findAll(course);
		
	}

	public Course findBycode(String code) {
		Course param = new Course();
		param.setCode(code);
		return cm.selectOne(param );
	}
	public void addCou(Course course) {
		cm.insert(course);
		Jedis jedis = new Jedis("127.0.0.1",6379);
		Course param = new Course();
		param.setName(course.getName());
		Course course2 = cm.selectOne(param );
		jedis.hset("课程", course2.getId()+"", course2.getName());
	}
}
