package com.example.webclienttutorial.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class CommonResponse {
    public String requestId;
    public String returnCode;
    public String returnMessage;

}
