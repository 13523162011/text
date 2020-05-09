package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 课程计划mapper
 * Created by Administrator.
 */
@Mapper
public interface TeachplanMapper extends JpaRepository<CourseBase,String> {
    //课程计划查询
    public TeachplanNode selectList(String courseId);
}
