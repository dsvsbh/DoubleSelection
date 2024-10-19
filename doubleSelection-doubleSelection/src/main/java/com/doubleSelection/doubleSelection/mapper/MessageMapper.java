package com.doubleSelection.doubleSelection.mapper;

import com.doubleSelection.doubleSelection.domain.DTO.MessageListDTO;
import com.doubleSelection.doubleSelection.domain.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {

    void insert(Message message);

    List<Message> list(Message message);

    Message getById(Long messageId);

    void updateById(Message message);

    void deleteAllMessage();
}
