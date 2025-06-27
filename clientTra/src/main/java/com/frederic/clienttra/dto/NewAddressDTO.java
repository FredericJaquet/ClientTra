package com.frederic.clienttra.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewAddressDTO {

    @NonNull
    private String street;

    @NonNull
    private String stNumber;

    private String apt;

    @NonNull
    private String cp;

    @NonNull
    private String city;

    private String state;

    @NonNull
    private String country;
}

