package com.example.meal_tracker.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("access_token")
    private String token;

    //thời gian time-out token
    @JsonProperty("expiration_time")
    private String expirationTime;

    private String status;
    private String message;


}
