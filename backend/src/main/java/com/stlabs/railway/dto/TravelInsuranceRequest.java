package com.stlabs.railway.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class TravelInsuranceRequest {
    @NotBlank
    @Size(min = 2, max = 100)
    private String insuredName;

    @NotNull
    @Past
    private LocalDate insuredDateOfBirth;

    @NotBlank
    @Pattern(regexp = "MALE|FEMALE|OTHER")
    private String insuredGender;

    @NotBlank
    @Pattern(regexp = "[6-9][0-9]{9}")
    private String insuredPhone;

    @NotBlank
    @Email
    @Size(max = 254)
    private String insuredEmail;

    @NotBlank
    @Size(min = 8, max = 250)
    private String insuredAddress;

    @NotBlank
    @Size(min = 2, max = 100)
    private String nomineeName;

    @NotBlank
    @Pattern(regexp = "SPOUSE|PARENT|CHILD|SIBLING|OTHER")
    private String nomineeRelationship;

    @NotNull
    @Past
    private LocalDate nomineeDateOfBirth;

    @NotBlank
    @Pattern(regexp = "[6-9][0-9]{9}")
    private String nomineePhone;

    public String getInsuredName() { return insuredName; }
    public void setInsuredName(String insuredName) { this.insuredName = insuredName; }
    public LocalDate getInsuredDateOfBirth() { return insuredDateOfBirth; }
    public void setInsuredDateOfBirth(LocalDate insuredDateOfBirth) { this.insuredDateOfBirth = insuredDateOfBirth; }
    public String getInsuredGender() { return insuredGender; }
    public void setInsuredGender(String insuredGender) { this.insuredGender = insuredGender; }
    public String getInsuredPhone() { return insuredPhone; }
    public void setInsuredPhone(String insuredPhone) { this.insuredPhone = insuredPhone; }
    public String getInsuredEmail() { return insuredEmail; }
    public void setInsuredEmail(String insuredEmail) { this.insuredEmail = insuredEmail; }
    public String getInsuredAddress() { return insuredAddress; }
    public void setInsuredAddress(String insuredAddress) { this.insuredAddress = insuredAddress; }
    public String getNomineeName() { return nomineeName; }
    public void setNomineeName(String nomineeName) { this.nomineeName = nomineeName; }
    public String getNomineeRelationship() { return nomineeRelationship; }
    public void setNomineeRelationship(String nomineeRelationship) { this.nomineeRelationship = nomineeRelationship; }
    public LocalDate getNomineeDateOfBirth() { return nomineeDateOfBirth; }
    public void setNomineeDateOfBirth(LocalDate nomineeDateOfBirth) { this.nomineeDateOfBirth = nomineeDateOfBirth; }
    public String getNomineePhone() { return nomineePhone; }
    public void setNomineePhone(String nomineePhone) { this.nomineePhone = nomineePhone; }
}
