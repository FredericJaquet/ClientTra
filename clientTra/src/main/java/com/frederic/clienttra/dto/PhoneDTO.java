package com.frederic.clienttra.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhoneDTO {
    private Integer idPhone;
    private String phoneNumber;
    private String kind;
}
