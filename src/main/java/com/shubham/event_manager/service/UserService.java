package com.shubham.event_manager.service;


import com.shubham.event_manager.dto.ChangePasswordRequest;
import com.shubham.event_manager.dto.UserProfileDTO;

public interface UserService {

    UserProfileDTO getMyProfile(String email);

    UserProfileDTO updateMyProfile(String email, UserProfileDTO dto);

    void changePassword(
            String email, ChangePasswordRequest request);

    void deleteMyAccount(String email);



}
