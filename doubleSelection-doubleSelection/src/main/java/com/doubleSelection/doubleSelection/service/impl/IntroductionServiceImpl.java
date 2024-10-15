package com.doubleSelection.doubleSelection.service.impl;

import com.doubleSelection.common.core.domain.entity.SysUser;
import com.doubleSelection.common.core.redis.RedisCache;
import com.doubleSelection.common.utils.SecurityUtils;
import com.doubleSelection.doubleSelection.constants.CacheKeyConstant;
import com.doubleSelection.doubleSelection.constants.SelectionStatusConstants;
import com.doubleSelection.doubleSelection.domain.Mentor;
import com.doubleSelection.doubleSelection.domain.Student;
import com.doubleSelection.doubleSelection.domain.StudentMentorSelection;
import com.doubleSelection.doubleSelection.domain.VO.IntroductionVO;
import com.doubleSelection.doubleSelection.mapper.MentorMapper;
import com.doubleSelection.doubleSelection.mapper.StudentMapper;
import com.doubleSelection.doubleSelection.mapper.StudentMentorSelectionMapper;
import com.doubleSelection.doubleSelection.service.IIntroductionService;
import com.doubleSelection.doubleSelection.utils.JaccardSimilarityUtil;
import com.doubleSelection.doubleSelection.utils.SimHashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IntroductionServiceImpl implements IIntroductionService {
    private final MentorMapper mentorMapper;
    private final StudentMentorSelectionMapper studentMentorSelectionMapper;
    private final StudentMapper studentMapper;
    private final RedisCache redisCache;

    @Override
    public List<IntroductionVO> list() {
        //先查缓存
        Long studentId = SecurityUtils.getLoginUser().getUserId();
        List<Object> cacheList = redisCache.getCacheList(CacheKeyConstant.MESSAGE_LIST_KEY + studentId);
        List<IntroductionVO> list1 = cacheList.stream().map(o -> (IntroductionVO) o).collect(Collectors.toList());
        if(!list1.isEmpty())
        {
           return list1;
        }
        //查出所有导师
        List<Mentor> mentors = mentorMapper.selectMentorList(null);
        if(mentors==null||mentors.isEmpty())
        {
            throw new RuntimeException("暂无导师信息");
        }
        List<IntroductionVO> result = mentors.stream()
                .map(mentor -> {
                    //将mentor 转化为VO
                    IntroductionVO introductionVO = new IntroductionVO();
                    BeanUtils.copyProperties(mentor, introductionVO);
                    //查出已经双选的人数
                    StudentMentorSelection selection = new StudentMentorSelection();
                    selection.setMentorId(mentor.getMentorId());
                    selection.setStatus(SelectionStatusConstants.DOUBLE_SELECTED);
                    List<StudentMentorSelection> list = studentMentorSelectionMapper.list(selection);
                    //剩余人数
                    if (list == null || list.isEmpty()) {
                        introductionVO.setRemainingNumber(mentor.getStudentLimit());
                    } else {
                        introductionVO.setRemainingNumber(mentor.getStudentLimit() - list.size());
                    }
                    Student student = studentMapper.selectStudentByStudentId(studentId);
                    Double similarity = JaccardSimilarityUtil.jaccardSimilarity(student.getInterests(), mentor.getResearchArea());//计算学生兴趣和导师研究方向相似度
                    introductionVO.setSimilarity(similarity);
                    return introductionVO;
                })
                .filter(introductionVO -> introductionVO.getRemainingNumber() > 0)//筛选有剩余空间的导师
                .sorted((o1, o2) -> o2.getSimilarity().compareTo(o1.getSimilarity()))//根据相似度降序排序
                .skip(0)
                .limit(5)
                .collect(Collectors.toList());
        redisCache.setCacheList(CacheKeyConstant.MESSAGE_LIST_KEY + studentId, result);
        return result;
    }
}
