package com.doubleSelection.doubleSelection.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDetailDTO {
    private Long userId;
    private String detail;
}
