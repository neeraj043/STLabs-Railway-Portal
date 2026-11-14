package com.stlabs.railway.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

public class BookingRequest {
    @NotNull
    private Long trainId;
    @NotBlank
    @Size(max = 100)
    private String passengerName;
    @NotBlank
    @Email
    @Size(max = 254)
    private String email;
    @Min(1)
    @Max(6)
    private int seats = 1;
    @NotNull
    private LocalDate travelDate;
    @Size(max = 6)
    private List<@NotBlank @Size(max = 100) String> passengerNames;
    private Long returnTrainId;
    private LocalDate returnDate;
    @Pattern(regexp = "VEG|NON_VEG|JAIN")
    private String mealPreference;
    @Valid
    private TravelInsuranceRequest travelInsurance;

    public Long getTrainId() { return trainId; }
    public void setTrainId(Long trainId) { this.trainId = trainId; }
    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getSeats() { return seats; }
    public void setSeats(int seats) { this.seats = seats; }
    public LocalDate getTravelDate() { return travelDate; }
    public void setTravelDate(LocalDate travelDate) { this.travelDate = travelDate; }
    public List<String> getPassengerNames() { return passengerNames; }
    public void setPassengerNames(List<String> passengerNames) { this.passengerNames = passengerNames; }
    public Long getReturnTrainId() { return returnTrainId; }
    public void setReturnTrainId(Long returnTrainId) { this.returnTrainId = returnTrainId; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public String getMealPreference() { return mealPreference; }
    public void setMealPreference(String mealPreference) { this.mealPreference = mealPreference; }
    public TravelInsuranceRequest getTravelInsurance() { return travelInsurance; }
    public void setTravelInsurance(TravelInsuranceRequest travelInsurance) { this.travelInsurance = travelInsurance; }
}
