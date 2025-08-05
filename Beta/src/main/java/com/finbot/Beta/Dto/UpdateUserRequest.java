package com.finbot.Beta.Dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest { // Created a Wrapper class to hold both login and updated user details
    private LoginDto loginDto;
    private UpdatedUserDto updatedUserDto;
}
