package com.example.serviceApp.serviceRequest;

import com.example.serviceApp.appUser.AppUser;
import com.example.serviceApp.appUser.AppUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@Service
@RequiredArgsConstructor

public class ServiceRequestService {

    private final ServiceRequestRepository serviceRequestRepository;
    private final AppUserRepository appUserRepository;
    private final ModelMapper modelMapper;

    public  List<ServiceRequest> getServiceRequests(){
         return serviceRequestRepository.findAll();
    }
@Transactional
    public void deleteById(Long id) {
        boolean exist = serviceRequestRepository.existsById(id);
        if(exist)
        serviceRequestRepository.deleteById(id);
        else throw new IllegalArgumentException("service doenst exist");

    }@Transactional
    public ServiceRequest addServiceToUser(Long id,ServiceRequestDto requestDto){
        ServiceRequest newService = new ServiceRequest();
        newService.setDescription(requestDto.getDescription());
        AppUser newServiceUser = appUserRepository.getAppUserById(id).orElseThrow(()->new IllegalArgumentException("user doenst exist"));
        newService.setAppUser(newServiceUser);
        return serviceRequestRepository.save(newService);
    }
    public ServiceRequest findById(Long id){
        return  serviceRequestRepository.findById(id).orElseThrow(()->new IllegalArgumentException("not found"));
    }
    public List<ServiceRequestWithUserNameDto> findAllServiceRequestsWithUserName() {

        List<ServiceRequestWithUserNameDto> serviceRequestDtos = new ArrayList<>();
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<ServiceRequest> serviceRequests = serviceRequestRepository.findAll(sort);
        for (ServiceRequest s : serviceRequests){
            serviceRequestDtos.add( modelMapper.map(s, ServiceRequestWithUserNameDto.class));
        }
        return serviceRequestDtos;
    }
    public List<ServiceRequest> findAllByStatus(ServiceRequest.Status status){
        for(ServiceRequest.Status s : ServiceRequest.Status.values()){
            if(s.name().equals(status.name())){
                return serviceRequestRepository.findAllByStatus(status);
            }
        }throw new IllegalArgumentException("Status not valid");


    }
    @Transactional
    public ServiceRequest updateServiceRequest(Long id, ServiceRequestDto serviceRequestDto) {//todo zmapowac tego potwora ifowego

        ServiceRequest serviceRequest = serviceRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service request not found"));

    if(serviceRequestDto.getDescription()!=null){
        serviceRequest.setDescription(serviceRequestDto.getDescription());
    }
    if(serviceRequestDto.getPrice()!=null){
        serviceRequest.setPrice(serviceRequestDto.getPrice());
    }
    if(serviceRequestDto.getStatus()!=null){
        serviceRequest.setStatus(serviceRequestDto.getStatus());
    }
    //
        if(serviceRequest.getStatus().equals(ServiceRequest.Status.FINISHED)){
            serviceRequest.setEndDate(LocalDate.now());
        }
      return   serviceRequestRepository.save(serviceRequest);
    }
    @Transactional
    public ServiceRequest updateServiceRequestWithUser(Long id, ServiceRequestWithUserNameDto request) {//todo zmapowac tego potwora ifowego

        ServiceRequest serviceRequest = serviceRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service request not found"));
        Optional<AppUser> user= appUserRepository.findByPhoneNumber(request.getPhoneNumber());
        if(user.isPresent()){
        if(!serviceRequest.getAppUser().getPhoneNumber().equals(user.get().getPhoneNumber())){
            throw new IllegalArgumentException("CHUJUW STO");
        }
        }

        AppUser appUser = serviceRequest.getAppUser();
        appUser.setUserName(request.getUserName());
        appUser.setPhoneNumber(request.getPhoneNumber());
        serviceRequest.setPrice(request.getPrice());
        serviceRequest.setDescription(request.getDescription());
        serviceRequest.setStatus(request.getStatus());
        if(serviceRequest.getStatus().equals(ServiceRequest.Status.FINISHED)){
            serviceRequest.setEndDate(LocalDate.now());
        }
        return serviceRequestRepository.save(serviceRequest);
    }
}
