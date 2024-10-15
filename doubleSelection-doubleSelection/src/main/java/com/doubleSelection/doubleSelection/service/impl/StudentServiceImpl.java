package com.doubleSelection.doubleSelection.service.impl;

import java.util.List;


import com.doubleSelection.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.doubleSelection.doubleSelection.mapper.StudentMapper;
import com.doubleSelection.doubleSelection.domain.Student;
import com.doubleSelection.doubleSelection.service.IStudentService;

/**
 * 学生Service业务层处理
 * 
 * @author liuzhicheng
 * @date 2024-09-23
 */
@Service
public class StudentServiceImpl implements IStudentService
{
    @Autowired
    private StudentMapper studentMapper;

    /**
     * 查询学生
     * 
     * @param studentId 学生主键
     * @return 学生
     */
    @Override
    public Student selectStudentByStudentId(Long studentId)
    {
        return studentMapper.selectStudentByStudentId(studentId);
    }

    /**
     * 查询学生列表
     * 
     * @param student 学生
     * @return 学生
     */
    @Override
    public List<Student> selectStudentList(Student student)
    {
        return studentMapper.selectStudentList(student);
    }

    /**
     * 新增学生
     * 
     * @param student 学生
     * @return 结果
     */
    @Override
    public int insertStudent(Student student)
    {
        student.setCreateTime(DateUtils.getNowDate());
        return studentMapper.insertStudent(student);
    }

    /**
     * 修改学生
     * 
     * @param student 学生
     * @return 结果
     */
    @Override
    public int updateStudent(Student student)
    {
        student.setUpdateTime(DateUtils.getNowDate());
        return studentMapper.updateStudent(student);
    }

    /**
     * 批量删除学生
     * 
     * @param studentIds 需要删除的学生主键
     * @return 结果
     */
    @Override
    public int deleteStudentByStudentIds(Long[] studentIds)
    {
        return studentMapper.deleteStudentByStudentIds(studentIds);
    }

    /**
     * 删除学生信息
     * 
     * @param studentId 学生主键
     * @return 结果
     */
    @Override
    public int deleteStudentByStudentId(Long studentId)
    {
        return studentMapper.deleteStudentByStudentId(studentId);
    }
}
