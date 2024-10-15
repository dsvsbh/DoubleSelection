package com.doubleSelection.doubleSelection.domain.VO;

import com.doubleSelection.common.core.domain.entity.SysUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVO extends SysUser {
    private String name;
    private String major;
    private String interests;
    private String researchArea;
    private Long studentLimit;
}
