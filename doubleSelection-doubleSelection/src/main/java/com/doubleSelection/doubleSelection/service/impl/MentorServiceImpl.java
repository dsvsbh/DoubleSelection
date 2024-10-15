package com.doubleSelection.doubleSelection.service.impl;

import java.util.List;


import com.doubleSelection.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.doubleSelection.doubleSelection.mapper.MentorMapper;
import com.doubleSelection.doubleSelection.domain.Mentor;
import com.doubleSelection.doubleSelection.service.IMentorService;

/**
 * 导师Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-09-23
 */
@Service
public class MentorServiceImpl  implements IMentorService
{
    @Autowired
    private MentorMapper mentorMapper;

    /**
     * 查询导师
     * 
     * @param mentorId 导师主键
     * @return 导师
     */
    @Override
    public Mentor selectMentorByMentorId(Long mentorId)
    {
        return mentorMapper.selectMentorByMentorId(mentorId);
    }

    /**
     * 查询导师列表
     * 
     * @param mentor 导师
     * @return 导师
     */
    @Override
    public List<Mentor> selectMentorList(Mentor mentor)
    {
        return mentorMapper.selectMentorList(mentor);
    }

    /**
     * 新增导师
     * 
     * @param mentor 导师
     * @return 结果
     */
    @Override
    public int insertMentor(Mentor mentor)
    {
        mentor.setCreateTime(DateUtils.getNowDate());
        return mentorMapper.insertMentor(mentor);
    }

    /**
     * 修改导师
     * 
     * @param mentor 导师
     * @return 结果
     */
    @Override
    public int updateMentor(Mentor mentor)
    {
        mentor.setUpdateTime(DateUtils.getNowDate());
        return mentorMapper.updateMentor(mentor);
    }

    /**
     * 批量删除导师
     * 
     * @param mentorIds 需要删除的导师主键
     * @return 结果
     */
    @Override
    public int deleteMentorByMentorIds(Long[] mentorIds)
    {
        return mentorMapper.deleteMentorByMentorIds(mentorIds);
    }

    /**
     * 删除导师信息
     * 
     * @param mentorId 导师主键
     * @return 结果
     */
    @Override
    public int deleteMentorByMentorId(Long mentorId)
    {
        return mentorMapper.deleteMentorByMentorId(mentorId);
    }
}
