package com.doubleSelection.doubleSelection.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageDTO {
    @NotEmpty
    private Long senderId;
    @NotEmpty
    private Long receiverId;
    @NotEmpty
    private String messageContent;
}
