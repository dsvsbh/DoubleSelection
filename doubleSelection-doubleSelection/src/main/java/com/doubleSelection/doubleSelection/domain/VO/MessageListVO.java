package com.doubleSelection.doubleSelection.domain.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageListVO {
    private Long messageId;
    private Long senderId;
    private Long receiverId;
    private String messageContent;
    private Date sentTime;
    private Boolean isRead;
}
