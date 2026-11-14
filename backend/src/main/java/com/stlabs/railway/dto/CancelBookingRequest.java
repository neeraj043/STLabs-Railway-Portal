package com.stlabs.railway.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class CancelBookingRequest {
    @NotBlank
    @Pattern(regexp = "DEBIT_CARD|CREDIT_CARD|ORIGINAL_ACCOUNT")
    private String refundMethod;

    public String getRefundMethod() { return refundMethod; }
    public void setRefundMethod(String refundMethod) { this.refundMethod = refundMethod; }
}
