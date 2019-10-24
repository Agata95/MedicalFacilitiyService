package com.j25.finalproject.final_project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRoleChangeRequest {
    private Long accountId;
    private Map<String, String> roles;
}
