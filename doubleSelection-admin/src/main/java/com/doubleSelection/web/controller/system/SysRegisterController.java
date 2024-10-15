package com.doubleSelection.web.controller.system;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.doubleSelection.common.core.controller.BaseController;
import com.doubleSelection.common.core.domain.AjaxResult;
import com.doubleSelection.common.core.domain.model.RegisterBody;
import com.doubleSelection.common.utils.StringUtils;
import com.doubleSelection.framework.web.service.SysRegisterService;
import com.doubleSelection.system.service.ISysConfigService;

/**
 * 注册验证
 * 
 * @author ruoyi
 */
@RestController
public class SysRegisterController extends BaseController
{
    @Autowired
    private SysRegisterService registerService;

    @Autowired
    private ISysConfigService configService;

    /**
     * 用户登录接口
     * @param user
     * @return
     */
    @PostMapping("/register")
    @ApiOperation("用户注册接口")//swagger扫描有注解的接口
    public AjaxResult register(@RequestBody RegisterBody user)
    {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser"))))
        {
            return error("当前系统没有开启注册功能！");
        }
        String msg = registerService.register(user);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }
}
