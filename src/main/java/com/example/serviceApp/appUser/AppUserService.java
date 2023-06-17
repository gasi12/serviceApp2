package com.example.serviceApp.appUser;

import com.example.serviceApp.serviceRequest.ServiceRequest;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service

public class AppUserService {
    @Autowired
    private AppUserRepository appUserRepository;

    public AppUser findUserById(Long id){
        return appUserRepository.findById(id).orElseThrow();
    }
    public List<AppUser> findAllUsers(){
        return appUserRepository.findAll();
    }

    public AppUser findUserByPhoneNumber(Long phoneNumber){return appUserRepository.findByPhoneNumber(phoneNumber).orElseThrow(()-> new IllegalArgumentException("not found"));}
    @Transactional

    public AppUser createUserWithoutChecking(AppUser appUser) {
        Optional<AppUser> newUser = appUserRepository.getAppUserByPhoneNumber(appUser.getPhoneNumber());
        List<ServiceRequest> serviceRequestList = appUser.getServiceRequestList();
        if (!newUser.isPresent()) {
            for (ServiceRequest s : serviceRequestList) {
                s.setAppUser(appUser);
                s.setStatus(ServiceRequest.Status.PENDING);
            }
            appUser.setServiceRequestList(serviceRequestList);
            return appUserRepository.save(appUser);
        }
        AppUser newAppUser = newUser.get();
        for (ServiceRequest s : serviceRequestList) {
            s.setAppUser(newAppUser);
            s.setStatus(ServiceRequest.Status.PENDING);
            newAppUser.getServiceRequestList().add(s);
        }
        newAppUser.setServiceRequestList(serviceRequestList);
        return  appUserRepository.save(newAppUser);

    }
    @Transactional
    public AppUser editUserById(Long id,AppUserDto user){
        AppUser editedUser = appUserRepository.getAppUserById(id).orElseThrow(()-> new IllegalArgumentException("user not found"));
        if(!user.getName().isBlank())
            editedUser.setUserName(user.getName());
        if(user.getPhoneNumber()!=null)
            editedUser.setPhoneNumber(user.getPhoneNumber());
       return appUserRepository.save(editedUser);
    }
}
