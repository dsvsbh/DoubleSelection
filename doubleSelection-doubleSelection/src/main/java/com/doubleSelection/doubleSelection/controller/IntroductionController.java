package com.doubleSelection.doubleSelection.controller;

import com.doubleSelection.doubleSelection.domain.Mentor;
import com.doubleSelection.doubleSelection.domain.VO.IntroductionVO;
import com.doubleSelection.doubleSelection.service.IIntroductionService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class IntroductionController {
    private final IIntroductionService introductionService;
    @GetMapping("/introduction")
    @ApiOperation("根据学生感兴趣研究方向推荐导师")
    public List<IntroductionVO> list()
    {
        return introductionService.list();
    }
}
