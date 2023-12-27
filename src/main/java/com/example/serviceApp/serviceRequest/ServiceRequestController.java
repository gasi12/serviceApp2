package com.example.serviceApp.serviceRequest;

import com.example.serviceApp.customer.Customer;
import com.example.serviceApp.serviceRequest.Dto.ServiceRequestWithCustomerEditorDto;
import com.example.serviceApp.serviceRequest.Dto.*;
import com.example.serviceApp.serviceRequest.status.StatusHistoryDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.itextpdf.text.*;
//import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/services")
public class ServiceRequestController {

    private final ServiceRequestService serviceRequestService;
    private final ConcurrentHashMap<Long, List<String>> userOrdersCache = new ConcurrentHashMap<>();

    @GetMapping("/cache")
    public String getUserOrders() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(userOrdersCache);
    }
    @GetMapping("/report")
    public void generateReport(HttpServletResponse response) throws DocumentException, IOException {
        // Retrieve data from database
        List<ServiceRequestDto> s = serviceRequestService.getServiceRequests();

        // Create PDF document
        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Add report content to the PDF
        Font font = FontFactory.getFont("Arial", 12);
        font.setStyle(1);
        Paragraph header = new Paragraph("Report Title", font);
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);

        for (ServiceRequestDto d : s){
            Paragraph row = new Paragraph();
            row.add("Data: "+ d.toString());
            row.setSpacingAfter(10); // Adjust spacing between rows
            document.add(row);
        }
        BarcodeQRCode qrCode = new BarcodeQRCode("12/2023",100,100,null);
        document.add(qrCode.getImage());


        document.close();
        response.setStatus(200);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=report.pdf");
    }

    @GetMapping("/service/{id}")//ok
    public ServiceRequestWithDetailsDto findById(@PathVariable Long id) {
        return serviceRequestService.findById(id);
    }

    @GetMapping("/services")//ok
    List<ServiceRequestDto> getAllRequests() {
        return serviceRequestService.getServiceRequests();
    }

    @PutMapping("/service/addtouser/{id}")//ok
    public ServiceRequestDto addServiceToUser(@PathVariable Long id, @RequestBody ServiceRequestWithUserNameDto requestDto) {
        return ServiceRequestDtoMapper.mapToServiceRequestDto(serviceRequestService.addServiceRequestToUser(id, requestDto));
    }

    @DeleteMapping("/service/{id}")//ok
    public void deleteServiceRequest(@PathVariable Long id) {
        serviceRequestService.deleteById(id);
    }

    @GetMapping("/service-requests-with-user-name")//ok
    public List<ServiceRequestWithUserNameDto> getAllServiceRequestsWithUserName(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        return serviceRequestService.findAllServiceRequestsWithUserName2(pageNo, pageSize);
    }

    @GetMapping("/service/status/{status}")//ok
    public List<ServiceRequestWithUserNameDto> findAllByStatus(@PathVariable String status) {
        return serviceRequestService.findAllByStatus(status);
    }

    @PutMapping("/service/{id}")//ok
    public ServiceRequestWithDetailsDto updateServiceRequest(@PathVariable Long id, @RequestBody ServiceRequestUpdateDto serviceRequestDto) {
        return serviceRequestService.updateServiceRequest(id, serviceRequestDto);
    }

    @PutMapping("/service/{id}/user")//ok
    public ServiceRequestWithUserNameDto updateServiceRequestWithUser(@PathVariable Long id, @RequestBody ServiceRequestWithUserNameDto service) {
        return serviceRequestService.updateServiceRequestWithUser(id, service);
    }
    @PostMapping("/service/{id}/status")
    public StatusHistoryDto addStatus(@PathVariable Long id, @RequestBody StatusHistoryDto status){
        return serviceRequestService.addStatus(id,status);
    }
    @GetMapping("/stats/avgtime")
    public ResponseEntity<Double> findAverageTime(){
        return ResponseEntity.ok(serviceRequestService.findAverageServiceDuration());
    }
    @GetMapping("/stats/revenue")
    public List<RevenuePerPeriod> getRevenue(){
        return serviceRequestService.getRevenueByPeriod();
    }
    @GetMapping("/stats/status")
    public Map<ServiceRequest.Status, Long>getStatusNumbers(){
        return serviceRequestService.countStatus();
    }
}
