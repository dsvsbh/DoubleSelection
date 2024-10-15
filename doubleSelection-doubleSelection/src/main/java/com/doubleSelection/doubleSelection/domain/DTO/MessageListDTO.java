package com.doubleSelection.doubleSelection.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageListDTO {
    private Long senderId;
    private Boolean isRead;
    private Integer pageNum;
    private Integer pageSize;
}
