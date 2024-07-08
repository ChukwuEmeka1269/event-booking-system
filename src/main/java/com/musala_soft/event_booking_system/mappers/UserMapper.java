package com.musala_soft.event_booking_system.mappers;

import com.musala_soft.event_booking_system.models.AppUser;
import com.musala_soft.event_booking_system.openapi.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper implements Mapper<AppUser, User> {

    private final ModelMapper modelMapper;

    @Override
    public User mapTo(AppUser appUser) {
        return (appUser == null) ? null : modelMapper.map(appUser, User.class);
    }

    @Override
    public AppUser mapFrom(User user) {
        return (user == null) ? null : modelMapper.map(user, AppUser.class);
    }


}
