package com.doubleSelection.doubleSelection.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageDTO {
    private Long senderId;
    private Long receiverId;
    private String messageContent;
}
