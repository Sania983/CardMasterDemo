package com.CardMaster.dto.paa;

import lombok.Data;

@Data
public class CustomerDto {

        private Long customerId;
        private String name;
        private String dob;            // could be LocalDate if preferred
        private String contactInfo;
        private Double income;
        private String employmentType;
        private String status;         // Active/Inactive/Blacklisted
    }


