package com.doubleSelection.doubleSelection.mapper;

import java.util.List;


import com.doubleSelection.doubleSelection.domain.Student;
import org.apache.ibatis.annotations.Mapper;

/**
 * 学生Mapper接口
 * 
 * @author liuzhicheng
 * @date 2024-09-23
 */
@Mapper
public interface StudentMapper
{
    /**
     * 查询学生
     * 
     * @param studentId 学生主键
     * @return 学生
     */
    public Student selectStudentByStudentId(Long studentId);

    /**
     * 查询学生列表
     * 
     * @param student 学生
     * @return 学生集合
     */
    public List<Student> selectStudentList(Student student);

    /**
     * 新增学生
     * 
     * @param student 学生
     * @return 结果
     */
    public int insertStudent(Student student);

    /**
     * 修改学生
     * 
     * @param student 学生
     * @return 结果
     */
    public int updateStudent(Student student);

    /**
     * 删除学生
     * 
     * @param studentId 学生主键
     * @return 结果
     */
    public int deleteStudentByStudentId(Long studentId);

    /**
     * 批量删除学生
     * 
     * @param studentIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStudentByStudentIds(Long[] studentIds);

    void deleteAllStudent();
}
