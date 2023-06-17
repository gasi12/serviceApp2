package com.example.serviceApp.appUser;


import java.util.List;
import java.util.stream.Collectors;

public class AppUserDtoMapper {
    public static List<AppUserDto> mapToAppUsersDtos(List<AppUser> users) {
        return users.stream().map(user -> mapToAppUserDto(user)).
                collect(Collectors.toList());
    }

    public static AppUserDto mapToAppUserDto(AppUser user) {
        return AppUserDto.builder()
                .id(user.getId())
                .name(user.getUserName())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

}
