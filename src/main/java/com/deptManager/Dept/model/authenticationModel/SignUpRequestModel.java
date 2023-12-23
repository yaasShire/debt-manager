package com.deptManager.Dept.model.authenticationModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SignUpRequestModel {
    private String shopName;
    private String phoneNumber;
    private String password;
}
