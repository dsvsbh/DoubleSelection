package com.doubleSelection.web.controller.system;


import com.doubleSelection.common.constant.Constants;
import com.doubleSelection.doubleSelection.domain.Mentor;
import com.doubleSelection.doubleSelection.domain.Student;
import com.doubleSelection.doubleSelection.domain.VO.UserInfoVO;
import com.doubleSelection.doubleSelection.mapper.MentorMapper;
import com.doubleSelection.doubleSelection.mapper.StudentMapper;
import com.doubleSelection.system.mapper.SysUserRoleMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.doubleSelection.common.annotation.Log;
import com.doubleSelection.common.config.RuoYiConfig;
import com.doubleSelection.common.core.controller.BaseController;
import com.doubleSelection.common.core.domain.AjaxResult;
import com.doubleSelection.common.core.domain.entity.SysUser;
import com.doubleSelection.common.core.domain.model.LoginUser;
import com.doubleSelection.common.enums.BusinessType;
import com.doubleSelection.common.utils.SecurityUtils;
import com.doubleSelection.common.utils.StringUtils;
import com.doubleSelection.common.utils.file.FileUploadUtils;
import com.doubleSelection.common.utils.file.MimeTypeUtils;
import com.doubleSelection.framework.web.service.TokenService;
import com.doubleSelection.system.service.ISysUserService;

/**
 * 个人信息 业务处理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;
    @Autowired
    private MentorMapper mentorMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    /**
     * 个人信息
     */
    @GetMapping
    public AjaxResult profile()
    {
        LoginUser loginUser = getLoginUser();
        SysUser user = loginUser.getUser();
        //扩展学生或导师信息
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfoVO);
        //获取角色
        Long roleId = user.getRoles().get(0).getRoleId();
        if(roleId.equals(Constants.MENTOR_ROLE_ID))
       {
           Mentor mentor = mentorMapper.selectMentorByMentorId(user.getUserId());
           userInfoVO.setName(mentor.getName());
           userInfoVO.setResearchArea(mentor.getResearchArea());
           userInfoVO.setStudentLimit(mentor.getStudentLimit());
       }
       if(roleId.equals(Constants.STUDENT_ROLE_ID))
       {
           Student student = studentMapper.selectStudentByStudentId(user.getUserId());
           userInfoVO.setName(student.getName());
           userInfoVO.setInterests(student.getInterests());
           userInfoVO.setMajor(student.getMajor());
       }
       userInfoVO.setRoleId(roleId);
        AjaxResult ajax = AjaxResult.success(userInfoVO);
        ajax.put("roleGroup", userService.selectUserRoleGroup(loginUser.getUsername()));
        ajax.put("postGroup", userService.selectUserPostGroup(loginUser.getUsername()));
        return ajax;
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateProfile(@RequestBody SysUser user)
    {
        LoginUser loginUser = getLoginUser();
        SysUser currentUser = loginUser.getUser();
        currentUser.setNickName(user.getNickName());
        currentUser.setEmail(user.getEmail());
        currentUser.setPhonenumber(user.getPhonenumber());
        currentUser.setSex(user.getSex());
        if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(currentUser))
        {
            return error("修改用户'" + loginUser.getUsername() + "'失败，手机号码已存在");
        }
        if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(currentUser))
        {
            return error("修改用户'" + loginUser.getUsername() + "'失败，邮箱账号已存在");
        }
        if (userService.updateUserProfile(currentUser) > 0)
        {
            // 更新缓存用户信息
            tokenService.setLoginUser(loginUser);
            return success();
        }
        return error("修改个人信息异常，请联系管理员");
    }

    /**
     * 重置密码
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public AjaxResult updatePwd(String oldPassword, String newPassword)
    {
        LoginUser loginUser = getLoginUser();
        String userName = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password))
        {
            return error("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPassword, password))
        {
            return error("新密码不能与旧密码相同");
        }
        newPassword = SecurityUtils.encryptPassword(newPassword);
        if (userService.resetUserPwd(userName, newPassword) > 0)
        {
            // 更新缓存用户密码
            loginUser.getUser().setPassword(newPassword);
            tokenService.setLoginUser(loginUser);
            return success();
        }
        return error("修改密码异常，请联系管理员");
    }

    /**
     * 头像上传
     */
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file) throws Exception
    {
        if (!file.isEmpty())
        {
            LoginUser loginUser = getLoginUser();
            String avatar = FileUploadUtils.upload(RuoYiConfig.getAvatarPath(), file, MimeTypeUtils.IMAGE_EXTENSION);
            if (userService.updateUserAvatar(loginUser.getUsername(), avatar))
            {
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", avatar);
                // 更新缓存用户头像
                loginUser.getUser().setAvatar(avatar);
                tokenService.setLoginUser(loginUser);
                return ajax;
            }
        }
        return error("上传图片异常，请联系管理员");
    }
}
