package com.doubleSelection.doubleSelection.domain.VO;

import com.doubleSelection.common.annotation.Excel;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntroductionVO {
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
    /**
     * 剩余名额
     */
    private Long remainingNumber;
    /**
     * 相似度
     */
    private Double similarity;
}
