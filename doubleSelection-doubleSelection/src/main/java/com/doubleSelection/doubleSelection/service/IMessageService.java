package com.doubleSelection.doubleSelection.service;

import com.doubleSelection.doubleSelection.domain.DTO.MessageListDTO;
import com.doubleSelection.doubleSelection.domain.DTO.SendMessageDTO;
import com.doubleSelection.doubleSelection.domain.Message;
import com.doubleSelection.doubleSelection.domain.PageResult;
import com.doubleSelection.doubleSelection.domain.VO.MessageListVO;

public interface IMessageService {
    void sendMessage(SendMessageDTO sendMessageDTO);

    PageResult<MessageListVO> messageList(MessageListDTO dto);

    Message messageDetail(Long messageId);

    void deleteMessage();
}
