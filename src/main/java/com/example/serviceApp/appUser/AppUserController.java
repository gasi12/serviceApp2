package com.example.serviceApp.appUser;

import com.example.serviceApp.serviceRequest.ServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class AppUserController {

  private final AppUserService appUserService;

    @PostMapping("/saveappuserdumb")
    public AppUser createAppUserDumb(@RequestBody AppUser appUser) {
        //return appUser;
       return appUserService.createUserWithoutChecking(appUser);
    }

@GetMapping("/user/{id}")
    public AppUserDto findUserById(@PathVariable Long id){
       return AppUserDtoMapper.mapToAppUserDto(appUserService.findUserById(id));
}
    @GetMapping("/user/{id}/all")
    public AppUser getUserByIdAll(@PathVariable Long id){
        return appUserService.findUserById(id);
    }
@PutMapping("/user/{id}")
    public AppUser editUserById(@PathVariable Long id,@RequestBody AppUserDto user){
        return appUserService.editUserById(id,user);
}
@GetMapping("/user/phonenumber/{number}")
public AppUserDto findUserByPhoneNumber(@PathVariable Long number){
        return AppUserDtoMapper.mapToAppUserDto(appUserService.findUserByPhoneNumber(number));
}
@GetMapping("/user/getall")
public List<AppUserDto> findAllUsers(){
        return AppUserDtoMapper.mapToAppUsersDtos(appUserService.findAllUsers());
}
    }