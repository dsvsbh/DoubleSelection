package com.doubleSelection.doubleSelection.controller;

import com.doubleSelection.doubleSelection.domain.DTO.MessageListDTO;
import com.doubleSelection.doubleSelection.domain.DTO.SendMessageDTO;
import com.doubleSelection.doubleSelection.domain.Mentor;
import com.doubleSelection.doubleSelection.domain.Message;
import com.doubleSelection.doubleSelection.domain.PageResult;
import com.doubleSelection.doubleSelection.domain.Student;
import com.doubleSelection.doubleSelection.domain.VO.MessageListVO;
import com.doubleSelection.doubleSelection.mapper.MentorMapper;
import com.doubleSelection.doubleSelection.mapper.StudentMapper;
import com.doubleSelection.doubleSelection.service.IMessageService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 学生导师留言controller
 */
@RestController
@Slf4j
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {
    private final IMessageService messageService;
    private final StudentMapper studentMapper;
    private final MentorMapper mentorMapper;
    /**
     * 发送留言
     * @param sendMessageDTO
     * @return
     */
   @PostMapping("/sendMessage")
   @ApiOperation(value = "发送留言")
    public String sendMessage(@RequestBody SendMessageDTO sendMessageDTO)
   {
       return messageService.sendMessage(sendMessageDTO);
   }

    /**
     * 查询当前用户的消息列表
     * @param dto
     * @return
     */
   @GetMapping("/list")
   @ApiOperation(value = "查询当前用户的消息列表")
    public PageResult<MessageListVO> messageList(MessageListDTO dto)
   {
      return messageService.messageList(dto);
   }

    /**
     * 查看留言详情
     * @param messageId
     * @return
     */
    @PostMapping("/{messageId}")
    @ApiOperation(value = "查看留言详情")
    public MessageListVO messageDetail(@PathVariable Long messageId)
    {
        Message message = messageService.messageDetail(messageId);
        MessageListVO result = new MessageListVO();
        BeanUtils.copyProperties(message, result);
        Long senderId = message.getSenderId();
        Student student = studentMapper.selectStudentByStudentId(senderId);
        Mentor mentor = mentorMapper.selectMentorByMentorId(senderId);
        if(student!=null)
        {
            result.setSenderName(student.getName());
        }
        if(mentor!=null)
        {
            result.setSenderName(mentor.getName());
        }
        return result;
    }
}
