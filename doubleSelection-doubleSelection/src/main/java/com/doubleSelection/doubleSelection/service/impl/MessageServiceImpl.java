package com.doubleSelection.doubleSelection.service.impl;

import com.doubleSelection.common.utils.SecurityUtils;
import com.doubleSelection.common.utils.StringUtils;
import com.doubleSelection.doubleSelection.domain.DTO.MessageListDTO;
import com.doubleSelection.doubleSelection.domain.DTO.SendMessageDTO;
import com.doubleSelection.doubleSelection.domain.Message;
import com.doubleSelection.doubleSelection.domain.PageResult;
import com.doubleSelection.doubleSelection.domain.VO.MessageListVO;
import com.doubleSelection.doubleSelection.mapper.MessageMapper;
import com.doubleSelection.doubleSelection.service.IMessageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements IMessageService {
    private final MessageMapper messageMapper;
    @Override
    public String sendMessage(SendMessageDTO sendMessageDTO) {
        if(sendMessageDTO.getSenderId()==null)
        {
            throw new RuntimeException("发送者id不能为空");
        }
        if(sendMessageDTO.getReceiveId()==null)
        {
            throw new RuntimeException("接受者id不能为空");
        }
        if(sendMessageDTO.getMessageContent()==null)
        {
            throw new RuntimeException("留言内容不能为空");
        }
        if(sendMessageDTO.getMessageContent().length()>100)
        {
            throw new RuntimeException("留言内容不能超过100个字,请发送邮箱");
        }
        Message message = new Message();
        message.setSenderId(sendMessageDTO.getSenderId());
        message.setReceiverId(sendMessageDTO.getReceiveId());
        message.setMessageContent(sendMessageDTO.getMessageContent());
        message.setSentTime(new Date());
        message.setIsRead(false);
        messageMapper.insert(message);
        return message.getMessageId().toString();
    }

    @Override
    public PageResult<MessageListVO> messageList(MessageListDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        Message message1 = new Message();
        message1.setSenderId(dto.getSenderId());
        message1.setIsRead(dto.getIsRead());
        message1.setReceiverId(SecurityUtils.getLoginUser().getUserId());
        List<Message> list=messageMapper.list(message1);
        list.forEach(message -> message.setMessageContent(StringUtils.substring(message.getMessageContent(), 0, 20)+"..."));
        PageInfo<Message> messagePageInfo = new PageInfo<>(list);
        return PageResult.<MessageListVO>builder()
                .total(messagePageInfo.getTotal())
                .pageNum(messagePageInfo.getPageNum())
                .pageSize(messagePageInfo.getPageSize())
                .pages(messagePageInfo.getPages())
                .records(list.stream().map(message -> {
                    MessageListVO messageListVO = new MessageListVO();
                    BeanUtils.copyProperties(message, messageListVO);
                    return messageListVO;
                }).collect(Collectors.toList()))
                .build();
    }

    @Override
    public Message messageDetail(Long messageId) {
        if(messageId==null)
        {
            throw new RuntimeException("消息id不能为空");
        }
        Message message=messageMapper.getById(messageId);
        if(message.getIsRead()==false)
        {
            message.setIsRead(true);
            messageMapper.updateById(message);
        }
        return message;
    }
}
