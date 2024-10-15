package com.doubleSelection.framework.web.service;



import com.doubleSelection.doubleSelection.constants.CacheKeyConstant;
import com.doubleSelection.doubleSelection.domain.Mentor;
import com.doubleSelection.doubleSelection.domain.Student;
import com.doubleSelection.doubleSelection.mapper.MentorMapper;
import com.doubleSelection.doubleSelection.mapper.StudentMapper;
import com.doubleSelection.system.domain.SysUserRole;
import com.doubleSelection.system.mapper.SysUserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import com.doubleSelection.common.constant.CacheConstants;
import com.doubleSelection.common.constant.Constants;
import com.doubleSelection.common.constant.UserConstants;
import com.doubleSelection.common.core.domain.entity.SysUser;
import com.doubleSelection.common.core.domain.model.RegisterBody;
import com.doubleSelection.common.core.redis.RedisCache;
import com.doubleSelection.common.exception.user.CaptchaException;
import com.doubleSelection.common.exception.user.CaptchaExpireException;
import com.doubleSelection.common.utils.MessageUtils;
import com.doubleSelection.common.utils.SecurityUtils;
import com.doubleSelection.common.utils.StringUtils;
import com.doubleSelection.framework.manager.AsyncManager;
import com.doubleSelection.framework.manager.factory.AsyncFactory;
import com.doubleSelection.system.service.ISysConfigService;
import com.doubleSelection.system.service.ISysUserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 注册校验方法
 * 
 * @author ruoyi
 */
@Component
public class SysRegisterService
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private RedisCache redisCache;
    @Resource
    private SysUserRoleMapper userRoleMapper;
    @Autowired
    private MentorMapper mentorMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 注册
     */
    @Transactional
    public String register(RegisterBody registerBody)
    {
        String msg = "", username = registerBody.getUsername(), password = registerBody.getPassword();
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);

        // 验证码开关
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled)
        {
            validateCaptcha(username, registerBody.getCode(), registerBody.getUuid());
        }

        if (StringUtils.isEmpty(username))
        {
            msg = "用户名不能为空";
        }
        else if (StringUtils.isEmpty(password))
        {
            msg = "用户密码不能为空";
        }
        else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            msg = "账户长度必须在2到20个字符之间";
        }
        else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            msg = "密码长度必须在5到20个字符之间";
        }
        else if (!userService.checkUserNameUnique(sysUser))
        {
            msg = "保存用户'" + username + "'失败，注册账号已存在";
        } else if(registerBody.getRoleId()==null)
        {
            msg="请选择用户角色";
        }
        else
        {
            sysUser.setNickName(username);
            sysUser.setPassword(SecurityUtils.encryptPassword(password));
            sysUser.setEmail(registerBody.getEmail());
            boolean regFlag = userService.registerUser(sysUser);
            if (!regFlag)
            {
                msg = "注册失败,请联系系统管理人员";
            }
            else
            {
                //注册用户为导师
                if(registerBody.getRoleId().equals(Constants.MENTOR_ROLE_ID))
                {
                    //关联导师角色
                    SysUserRole sysUserRole = new SysUserRole();
                    sysUserRole.setUserId(sysUser.getUserId());
                    sysUserRole.setRoleId(registerBody.getRoleId());
                    userRoleMapper.insert(sysUserRole);
                    //新增导师表
                    Mentor mentor = new Mentor();
                    mentor.setEmail(registerBody.getEmail());
                    mentor.setMentorId(sysUser.getUserId());
                    mentor.setName(registerBody.getName());
                    mentor.setPassword(sysUser.getPassword());
                    mentor.setResearchArea(registerBody.getResearchArea());
                    mentor.setStudentLimit(registerBody.getStudentLimit());
                    mentorMapper.insertMentor(mentor);
                    //新增导师影响推荐榜，删推荐缓存
                    Pattern compile = Pattern.compile(CacheKeyConstant.MESSAGE_LIST_KEY+"*");
                    Set keys = redisTemplate.keys(compile);
                    redisTemplate.delete(keys);
                }
                //注册用户为学生
                if(registerBody.getRoleId().equals(Constants.STUDENT_ROLE_ID))
                {
                    //关联学生角色
                    SysUserRole sysUserRole = new SysUserRole();
                    sysUserRole.setUserId(sysUser.getUserId());
                    sysUserRole.setRoleId(registerBody.getRoleId());
                    userRoleMapper.insert(sysUserRole);
                    //新增学生表
                    Student student = new Student();
                    student.setStudentId(sysUser.getUserId());
                    student.setName(registerBody.getName());
                    student.setEmail(registerBody.getEmail());
                    student.setPassword(sysUser.getPassword());
                    student.setMajor(registerBody.getMajor());
                    student.setInterests(registerBody.getInterests());
                    studentMapper.insertStudent(student);
                }
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.register.success")));
            }
        }
        return msg;
    }

    /**
     * 校验验证码
     * 
     * @param username 用户名
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid)
    {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null)
        {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha))
        {
            throw new CaptchaException();
        }
    }
}
