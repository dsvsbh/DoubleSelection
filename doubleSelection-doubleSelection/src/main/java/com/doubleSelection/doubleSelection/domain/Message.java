package com.doubleSelection.doubleSelection.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private Long messageId;
    @NotNull
    private Long senderId;
    @NotNull
    private Long receiverId;
    @NotNull
    private String messageContent;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sentTime;
    private Boolean isRead;
}
