package com.doubleSelection.web.task;

import com.doubleSelection.doubleSelection.mapper.MentorMapper;
import com.doubleSelection.doubleSelection.mapper.MessageMapper;
import com.doubleSelection.doubleSelection.mapper.StudentMapper;
import com.doubleSelection.doubleSelection.mapper.StudentMentorSelectionMapper;
import com.doubleSelection.doubleSelection.service.IStudentService;
import com.doubleSelection.system.mapper.SysUserMapper;
import com.doubleSelection.system.mapper.SysUserRoleMapper;
import com.doubleSelection.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class CleanUserInfoTask {
    private SysUserMapper sysUserMapper;
    private SysUserRoleMapper sysUserRoleMapper;
    private StudentMapper studentMapper;
    private MentorMapper mentorMapper;
    private MessageMapper messageMapper;
    private StudentMentorSelectionMapper studentMentorSelectionMapper;
    /**
     * 每年第一天0点删除所有用户数据，第二年让用户重新注册，防止前一年的数据堆积，用户重新注册修改个人信息
     */
    @Scheduled(cron = "0 0 0 1 1 ? *")
    @Transactional
    public void deleteUserData()
    {
        sysUserMapper.deleteAllUser();
        sysUserRoleMapper.deleteAllUserRole();
        studentMapper.deleteAllStudent();
        mentorMapper.deleteAllMentor();
        messageMapper.deleteAllMessage();
        studentMentorSelectionMapper.deleteAllSelection();
    }
}
