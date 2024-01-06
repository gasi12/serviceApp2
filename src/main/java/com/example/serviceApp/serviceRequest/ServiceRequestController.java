package com.example.serviceApp.serviceRequest;

//import com.itextpdf.text.*;
//import com.itextpdf.text.pdf.PdfWriter;

import com.example.serviceApp.serviceRequest.newDtos.ServiceRequestDtoAll;
import com.example.serviceApp.serviceRequest.newDtos.ServiceRequestSummaryDto;
import com.example.serviceApp.serviceRequest.status.dto.StatusHistoryDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

        import java.util.List;
        import java.util.concurrent.ConcurrentHashMap;

@CrossOrigin(origins = "*")
@RestController
@Slf4j
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/services")
public class ServiceRequestController {

    private final ServiceRequestService serviceRequestService;
    private final ConcurrentHashMap<Long, List<String>> userOrdersCache = new ConcurrentHashMap<>();

//    @GetMapping("/cache")
//    public String getUserOrders() throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        return objectMapper.writeValueAsString(userOrdersCache);
//    }
//    @GetMapping("/report")
//    public void generateReport(HttpServletResponse response) throws DocumentException, IOException {
//        // Retrieve data from database
//        List<ServiceRequestDto> s = serviceRequestService.getServiceRequests();
//
//        // Create PDF document
//        Document document = new Document();
//        PdfWriter.getInstance(document, response.getOutputStream());
//        document.open();
//
//        // Add report content to the PDF
//        Font font = FontFactory.getFont("Arial", 12);
//        font.setStyle(1);
//        Paragraph header = new Paragraph("Report Title", font);
//        header.setAlignment(Element.ALIGN_CENTER);
//        document.add(header);
//
//        for (ServiceRequestDto d : s){
//            Paragraph row = new Paragraph();
//            row.add("Data: "+ d.toString());
//            row.setSpacingAfter(10); // Adjust spacing between rows
//            document.add(row);
//        }
//        BarcodeQRCode qrCode = new BarcodeQRCode("12/2023",100,100,null);
//        document.add(qrCode.getImage());
//
//
//        document.close();
//        response.setStatus(200);
//        response.setContentType("application/pdf");
//        response.setHeader("Content-Disposition", "attachment; filename=report.pdf");
//    }
//

    @PostMapping("/service/device/{deviceId}")

    public ServiceRequestAddDto addServiceToDevice(@PathVariable Long deviceId,@RequestBody ServiceRequestAddDto requestAddDto){
        return serviceRequestService.addServiceToDevice(deviceId,requestAddDto);
    }

    @GetMapping("/service/{id}")//ok

    public ServiceRequestDtoAll findById(@PathVariable Long id) {

        return serviceRequestService.findByIdWithAllDetails(id);
    }

    @GetMapping("/services")

    public List<ServiceRequestSummaryDto> getAllRequests(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer pageSize) {
        return serviceRequestService.getServiceRequests(page, pageSize);
    }

    @DeleteMapping("/service/{id}")//ok

    public void deleteServiceRequest(@PathVariable Long id) {
        serviceRequestService.deleteById(id);
    }

    @GetMapping("/service/status/{status}")//ok

    public List<ServiceRequestSummaryDto> findAllByStatus(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer pageSize,@PathVariable ServiceRequest.Status status) {
        return serviceRequestService.findByStatus(page, pageSize, status);
    }

    @GetMapping("/service/status/notfinished")//ok

    public List<ServiceRequestSummaryDto> findAllNotFinished(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer pageSize) {
        return serviceRequestService.getServiceRequestsByStatusNotFinished(page,pageSize);
    }

    @PostMapping("/service/{id}/status")

    public StatusHistoryDto addStatus(@PathVariable Long id, @RequestBody StatusHistoryDto status){
        return serviceRequestService.addStatus(id,status);
    }

    @PutMapping("/service/{id}")

    public ServiceRequestAddDto editServiceRequest(@PathVariable Long id,@Valid @RequestBody ServiceRequestAddDto requestAddDto){
        return serviceRequestService.editServiceRequest(id,requestAddDto);
    }

//
//    @PutMapping("/service/addtouser/{id}")//ok
//    public ServiceRequestDto addServiceToUser(@PathVariable Long id, @RequestBody ServiceRequestWithUserNameDto requestDto) {
//        return ServiceRequestDtoMapper.mapToServiceRequestDto(serviceRequestService.addServiceRequestToUser(id, requestDto));
//    }
//

//
//    @GetMapping("/service-requests-with-user-name")//ok
//    public List<ServiceRequestWithUserNameDto> getAllServiceRequestsWithUserName(
//            @RequestParam(defaultValue = "0") int pageNo,
//            @RequestParam(defaultValue = "10") int pageSize) {
//        return serviceRequestService.findAllServiceRequestsWithUserName2(pageNo, pageSize);
//    }
//

//
//    @PutMapping("/service/{id}")//ok
//    public ServiceRequestWithDetailsDto updateServiceRequest(@PathVariable Long id, @RequestBody ServiceRequestUpdateDto serviceRequestDto) {
//        return serviceRequestService.updateServiceRequest(id, serviceRequestDto);
//    }
//
//    @PutMapping("/service/{id}/user")//ok
//    public ServiceRequestWithUserNameDto updateServiceRequestWithUser(@PathVariable Long id, @RequestBody ServiceRequestWithUserNameDto service) {
//        return serviceRequestService.updateServiceRequestWithUser(id, service);
//    }

//    @GetMapping("/stats/avgtime")
//    public ResponseEntity<Double> findAverageTime(){
//        return ResponseEntity.ok(serviceRequestService.findAverageServiceDuration());
//    }
//    @GetMapping("/stats/revenue")
//    public List<RevenuePerPeriod> getRevenue(){
//        return serviceRequestService.getRevenueByPeriod();
//    }
//    @GetMapping("/stats/status")
//    public Map<ServiceRequest.Status, Long>getStatusNumbers(){
//        return serviceRequestService.countStatus();
//    }
}
