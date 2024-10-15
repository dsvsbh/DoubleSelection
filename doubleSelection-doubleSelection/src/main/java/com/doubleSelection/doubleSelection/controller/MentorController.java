package com.doubleSelection.doubleSelection.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.doubleSelection.common.annotation.Log;
import com.doubleSelection.common.core.controller.BaseController;
import com.doubleSelection.common.core.domain.AjaxResult;
import com.doubleSelection.common.enums.BusinessType;
import com.doubleSelection.doubleSelection.domain.Mentor;
import com.doubleSelection.doubleSelection.service.IMentorService;
import com.doubleSelection.common.utils.poi.ExcelUtil;
import com.doubleSelection.common.core.page.TableDataInfo;

/**
 * 导师Controller
 * 
 * @author ruoyi
 * @date 2024-09-23
 */
@RestController
@RequestMapping("/doubleSelection/mentor")
public class MentorController extends BaseController
{
    @Autowired
    private IMentorService mentorService;

    /**
     * 查询导师列表
     */
    @PreAuthorize("@ss.hasPermi('doubleSelection:mentor:list')")
    @GetMapping("/list")
    public TableDataInfo list(Mentor mentor)
    {
        startPage();
        List<Mentor> list = mentorService.selectMentorList(mentor);
        return getDataTable(list);
    }

    /**
     * 导出导师列表
     */
    @PreAuthorize("@ss.hasPermi('doubleSelection:mentor:export')")
    @Log(title = "导师", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Mentor mentor)
    {
        List<Mentor> list = mentorService.selectMentorList(mentor);
        ExcelUtil<Mentor> util = new ExcelUtil<Mentor>(Mentor.class);
        util.exportExcel(response, list, "导师数据");
    }

    /**
     * 获取导师详细信息
     */
    @PreAuthorize("@ss.hasPermi('doubleSelection:mentor:query')")
    @GetMapping(value = "/{mentorId}")
    public AjaxResult getInfo(@PathVariable("mentorId") Long mentorId)
    {
        return success(mentorService.selectMentorByMentorId(mentorId));
    }

    /**
     * 新增导师
     */
    @PreAuthorize("@ss.hasPermi('doubleSelection:mentor:add')")
    @Log(title = "导师", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Mentor mentor)
    {
        return toAjax(mentorService.insertMentor(mentor));
    }

    /**
     * 修改导师
     */
    @PreAuthorize("@ss.hasPermi('doubleSelection:mentor:edit')")
    @Log(title = "导师", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Mentor mentor)
    {
        return toAjax(mentorService.updateMentor(mentor));
    }

    /**
     * 删除导师
     */
    @PreAuthorize("@ss.hasPermi('doubleSelection:mentor:remove')")
    @Log(title = "导师", businessType = BusinessType.DELETE)
	@DeleteMapping("/{mentorIds}")
    public AjaxResult remove(@PathVariable Long[] mentorIds)
    {
        return toAjax(mentorService.deleteMentorByMentorIds(mentorIds));
    }
}
