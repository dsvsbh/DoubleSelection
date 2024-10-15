package com.doubleSelection.doubleSelection.service.impl;

import com.doubleSelection.common.constant.Constants;
import com.doubleSelection.common.core.domain.entity.SysRole;
import com.doubleSelection.common.utils.SecurityUtils;
import com.doubleSelection.doubleSelection.constants.CacheKeyConstant;
import com.doubleSelection.doubleSelection.constants.SelectionStatusConstants;
import com.doubleSelection.doubleSelection.domain.*;
import com.doubleSelection.doubleSelection.domain.DTO.PageDTO;
import com.doubleSelection.doubleSelection.domain.VO.SelectionListVO;
import com.doubleSelection.doubleSelection.domain.VO.SelectionResultVO;
import com.doubleSelection.doubleSelection.mapper.MentorMapper;
import com.doubleSelection.doubleSelection.mapper.StudentMapper;
import com.doubleSelection.doubleSelection.mapper.StudentMentorSelectionMapper;
import com.doubleSelection.doubleSelection.service.IActivityService;
import com.doubleSelection.doubleSelection.service.ISelectionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SelectionServiceImpl implements ISelectionService {
    private final StudentMentorSelectionMapper studentMentorSelectionMapper;
    private final IActivityService activityService;
    private final MentorMapper mentorMapper;
    private final StudentMapper studentMapper;
    private final RedisTemplate redisTemplate;

    @Override
    public void studentSelectMentor(Long mentorId) {
        if(mentorId==null)
        {
            return ;
        }
        // 判断当前是否有活动
        Activity current = activityService.current();
        if(current==null)
        {
            throw new RuntimeException("当前没有活动,不可选择导师");
        }
        //判断学生是否已经双选
        StudentMentorSelection studentMentorSelection = new StudentMentorSelection();
        studentMentorSelection.setStatus(SelectionStatusConstants.DOUBLE_SELECTED);
        studentMentorSelection.setStudentId(SecurityUtils.getLoginUser().getUserId());
        List<StudentMentorSelection> list=studentMentorSelectionMapper.list(studentMentorSelection);
        if(list!=null&&list.size()!=0)
        {
            throw new RuntimeException("您已经双选,不可再次选择导师");
        }
        //判断导师的双选数是否到达限制
        studentMentorSelection = new StudentMentorSelection();
        studentMentorSelection.setMentorId(mentorId);
        studentMentorSelection.setStatus(SelectionStatusConstants.DOUBLE_SELECTED);
        List<StudentMentorSelection> list1 = studentMentorSelectionMapper.list(studentMentorSelection);
        Mentor mentor = mentorMapper.selectMentorByMentorId(mentorId);
        if(list1!=null&&list1.size()==mentor.getStudentLimit())
        {
            throw new RuntimeException("该导师已满额,不可选择导师");
        }
        //判断学生是否已经选过该导师
        studentMentorSelection = new StudentMentorSelection();
        studentMentorSelection.setMentorId(mentorId);
        studentMentorSelection.setStudentId(SecurityUtils.getLoginUser().getUserId());
        List<StudentMentorSelection> list2 = studentMentorSelectionMapper.list(studentMentorSelection);
        if(list2!=null&&list2.size()!=0)
        {
            throw new RuntimeException("您已经选过该导师，不可重复选");
        }
        //新增选择记录
        StudentMentorSelection selection = new StudentMentorSelection();
        selection.setStudentId(SecurityUtils.getLoginUser().getUserId());
        selection.setMentorId(mentorId);
        selection.setStatus(SelectionStatusConstants.SELECTED);
        selection.setUpdateTime(LocalDateTime.now());
        selection.setCreateTime(LocalDateTime.now());
        studentMentorSelectionMapper.insert(selection);
    }

    @Override
    public void studentCancelSelectMentor(Long mentorId) {
        StudentMentorSelection selection = new StudentMentorSelection();
        selection.setStudentId(SecurityUtils.getLoginUser().getUserId());
        selection.setMentorId(mentorId);
        List<StudentMentorSelection> list = studentMentorSelectionMapper.list(selection);
        if(list==null||list.size()==0)
        {
            throw new RuntimeException("您没有选择该导师");
        }
        if(Objects.equals(list.get(0).getStatus(), SelectionStatusConstants.DOUBLE_SELECTED))
        {
            throw new RuntimeException("您已经双选,不可取消选择导师");
        }
        if(list.get(0).getStatus().equals(SelectionStatusConstants.REJECTED))
        {
            throw new RuntimeException("导师已拒绝您的申请,不可取消选择导师");
        }
        studentMentorSelectionMapper.delete(list.get(0));
    }

    @Override
    public void doubleSelection(Long studentId) {
         if(studentId==null)
         {
             throw new RuntimeException("学生id为空");
         }
         StudentMentorSelection selection = new StudentMentorSelection();
         selection.setStudentId(studentId);
         selection.setMentorId(SecurityUtils.getLoginUser().getUserId());
         List<StudentMentorSelection> list = studentMentorSelectionMapper.list(selection);
         if(list==null|| list.isEmpty())
         {
             throw new RuntimeException("学生没有选择您");
         }
         if(Objects.equals(list.get(0).getStatus(), SelectionStatusConstants.DOUBLE_SELECTED))
         {
             throw new RuntimeException("您已经双选该学生,不可再次双选");
         }
         if(list.get(0).getStatus().equals(SelectionStatusConstants.REJECTED))
         {
             throw new RuntimeException("您已经拒绝该学生，不可再选择");
         }
         //查询该导师的双选记录
        StudentMentorSelection selection1 = new StudentMentorSelection();
         selection1.setMentorId(SecurityUtils.getLoginUser().getUserId());
         selection1.setStatus(SelectionStatusConstants.DOUBLE_SELECTED);
         List<StudentMentorSelection> list1 = studentMentorSelectionMapper.list(selection1);
         //查询导师
         Mentor mentor = mentorMapper.selectMentorByMentorId(SecurityUtils.getLoginUser().getUserId());
         //双选记录已经到达限额
         if(list1!=null&&list1.size()>=mentor.getStudentLimit())
         {
             throw new RuntimeException("您的学生已满额,不可选择学生");
         }
        selection.setStatus(SelectionStatusConstants.DOUBLE_SELECTED);
         selection.setUpdateTime(LocalDateTime.now());
         selection.setSelectionId(list.get(0).getSelectionId());
         studentMentorSelectionMapper.update(selection);
         //导师双选后名额减少，影响导师推荐榜，因此需要删除推荐的缓存
        Pattern compile = Pattern.compile(CacheKeyConstant.MESSAGE_LIST_KEY+"*");
        Set keys = redisTemplate.keys(compile);
        redisTemplate.delete(keys);
    }

    @Override
    public PageResult<SelectionListVO> list(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        //拿到当前导师，并查询与当前导师有关的选择记录
        Long mentorId = SecurityUtils.getLoginUser().getUserId();
        StudentMentorSelection selection = new StudentMentorSelection();
        selection.setMentorId(mentorId);
        List<StudentMentorSelection> list = studentMentorSelectionMapper.list(selection);
        //封装成分页信息返回
        PageInfo<StudentMentorSelection> studentMentorSelectionPageInfo = new PageInfo<>(list);
        List<SelectionListVO> records = studentMentorSelectionPageInfo.getList().stream().map(item ->
        {
            SelectionListVO vo = new SelectionListVO();
            Long studentId = item.getStudentId();
            Student student = studentMapper.selectStudentByStudentId(studentId);
            vo.setStudentId(studentId);
            vo.setStatus(item.getStatus());
            vo.setName(student.getName());
            vo.setEmail(student.getEmail());
            vo.setMajor(student.getMajor());
            vo.setInterests(student.getInterests());
            return vo;
        }).collect(Collectors.toList());
        PageResult<SelectionListVO> result = new PageResult<>();
        result.setTotal(studentMentorSelectionPageInfo.getTotal());
        result.setPageNum(studentMentorSelectionPageInfo.getPageNum());
        result.setPageSize(studentMentorSelectionPageInfo.getPageSize());
        result.setRecords(records);
        return result;
    }

    @Override
    public void reject(Long studentId) {
        if(studentId==null)
        {
            throw new RuntimeException("学生id为空");
        }
        StudentMentorSelection selection = new StudentMentorSelection();
        selection.setStudentId(studentId);
        selection.setMentorId(SecurityUtils.getLoginUser().getUserId());
        List<StudentMentorSelection> list = studentMentorSelectionMapper.list(selection);
        if(list==null|| list.isEmpty())
        {
            throw new RuntimeException("学生没有选择您");
        }
        if(Objects.equals(list.get(0).getStatus(), SelectionStatusConstants.DOUBLE_SELECTED))
        {
            throw new RuntimeException("您已经双选该学生,不可再拒绝");
        }
        if(list.get(0).getStatus().equals(SelectionStatusConstants.REJECTED))
        {
            throw new RuntimeException("您已经拒绝该学生，不可再拒绝");
        }
        selection.setStatus(SelectionStatusConstants.REJECTED);
        selection.setUpdateTime(LocalDateTime.now());
        selection.setSelectionId(list.get(0).getSelectionId());
        studentMentorSelectionMapper.update(selection);
    }

    @Override
    public PageResult<SelectionResultVO> result(PageDTO pageDTO) {
        Long userId = SecurityUtils.getLoginUser().getUserId();
        List<SysRole> roles = SecurityUtils.getLoginUser().getUser().getRoles();
        if(roles==null|| roles.isEmpty())
        {
            throw new RuntimeException("您暂未绑定角色");
        }
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());//开分页
        Long roleId = roles.get(0).getRoleId();
        List<StudentMentorSelection> list=null;
        if(roleId.equals(Constants.MENTOR_ROLE_ID))
        {
            StudentMentorSelection selection = new StudentMentorSelection();
            selection.setMentorId(userId);
            list = studentMentorSelectionMapper.list(selection);
        } else if(roleId.equals(Constants.STUDENT_ROLE_ID))
        {
            StudentMentorSelection selection = new StudentMentorSelection();
            selection.setStudentId(userId);
            list = studentMentorSelectionMapper.list(selection);
        } else {
            throw new RuntimeException("您既不是学生也不是导师，无法查询双选结果");
        }
        List<SelectionResultVO> collect = list.stream().map(item -> {
            Student student = studentMapper.selectStudentByStudentId(item.getStudentId());
            Mentor mentor = mentorMapper.selectMentorByMentorId(item.getMentorId());
            SelectionResultVO selectionResultVO = new SelectionResultVO();
            selectionResultVO.setMentorName(mentor.getName());
            selectionResultVO.setStudentName(student.getName());
            selectionResultVO.setStatus(item.getStatus());
            selectionResultVO.setMentorEmail(mentor.getEmail());
            selectionResultVO.setStudentEmail(student.getEmail());
            return selectionResultVO;
        }).collect(Collectors.toList());
        PageInfo<SelectionResultVO> pageInfo = new PageInfo<>(collect);
        PageResult<SelectionResultVO> result = new PageResult<>();
        result.setTotal(pageInfo.getTotal());
        result.setPages(pageInfo.getPages());
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setRecords(collect);
        return result;
    }
}
