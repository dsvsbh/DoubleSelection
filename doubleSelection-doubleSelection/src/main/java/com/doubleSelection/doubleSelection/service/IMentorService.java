package com.doubleSelection.doubleSelection.service;

import java.util.List;


import com.doubleSelection.doubleSelection.domain.Mentor;

/**
 * 导师Service接口
 * 
 * @author ruoyi
 * @date 2024-09-23
 */
public interface IMentorService
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
     * 批量删除导师
     * 
     * @param mentorIds 需要删除的导师主键集合
     * @return 结果
     */
    public int deleteMentorByMentorIds(Long[] mentorIds);

    /**
     * 删除导师信息
     * 
     * @param mentorId 导师主键
     * @return 结果
     */
    public int deleteMentorByMentorId(Long mentorId);
}
