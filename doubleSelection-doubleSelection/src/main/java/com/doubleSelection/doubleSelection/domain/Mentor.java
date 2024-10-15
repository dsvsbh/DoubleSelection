package com.doubleSelection.doubleSelection.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.doubleSelection.common.annotation.Excel;
import com.doubleSelection.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 导师对象 mentor
 * 
 * @author ruoyi
 * @date 2024-09-23
 */
@Data
public class Mentor
{
    private static final long serialVersionUID = 1L;

    private Date createTime;
    private Date updateTime;
    private Data data;
    /** 导师ID */
    private Long mentorId;

    /** 导师姓名 */
    @Excel(name = "导师姓名")
    private String name;

    /** 导师邮箱 */
    @Excel(name = "导师邮箱")
    private String email;

    /** 导师密码 */
    private String password;

    /** 研究领域 */
    @Excel(name = "研究领域")
    private String researchArea;

    /** 带学生人数上限 */
    @Excel(name = "带学生人数上限")
    private Long studentLimit;

    public void setMentorId(Long mentorId) 
    {
        this.mentorId = mentorId;
    }

    public Long getMentorId() 
    {
        return mentorId;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setEmail(String email) 
    {
        this.email = email;
    }

    public String getEmail() 
    {
        return email;
    }
    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPassword() 
    {
        return password;
    }
    public void setResearchArea(String researchArea) 
    {
        this.researchArea = researchArea;
    }

    public String getResearchArea() 
    {
        return researchArea;
    }
    public void setStudentLimit(Long studentLimit) 
    {
        this.studentLimit = studentLimit;
    }

    public Long getStudentLimit() 
    {
        return studentLimit;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("mentorId", getMentorId())
            .append("name", getName())
            .append("email", getEmail())
            .append("password", getPassword())
            .append("researchArea", getResearchArea())
            .append("studentLimit", getStudentLimit())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
