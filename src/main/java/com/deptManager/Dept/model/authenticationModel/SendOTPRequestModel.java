package com.deptManager.Dept.model.authenticationModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendOTPRequestModel {
    private String mobile;
    private String message;
    private String senderid;
    private String RequestDate;
}
