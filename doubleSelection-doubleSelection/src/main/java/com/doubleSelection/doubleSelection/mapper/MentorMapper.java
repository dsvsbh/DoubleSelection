package com.doubleSelection.doubleSelection.mapper;

import java.util.List;


import com.doubleSelection.doubleSelection.domain.Mentor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * 导师Mapper接口
 * 
 * @author ruoyi
 * @date 2024-09-23
 */
@Mapper
public interface MentorMapper
{
    /**
     * 查询导师
     * 
     * @param mentorId 导师主键
     * @return 导师
     */
    public Mentor selectMentorByMentorId(Long mentorId);

    /**
     * 查询导师列表
     * 
     * @param mentor 导师
     * @return 导师集合
     */
    public List<Mentor> selectMentorList(Mentor mentor);

    /**
     * 新增导师
     * 
     * @param mentor 导师
     * @return 结果
     */
    public int insertMentor(Mentor mentor);

    /**
     * 修改导师
     * 
     * @param mentor 导师
     * @return 结果
     */
    public int updateMentor(Mentor mentor);

    /**
     * 删除导师
     * 
     * @param mentorId 导师主键
     * @return 结果
     */
    public int deleteMentorByMentorId(Long mentorId);

    /**
     * 批量删除导师
     * 
     * @param mentorIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMentorByMentorIds(Long[] mentorIds);
}
