package com.backend.ecommercebackend.controller;

import com.backend.ecommercebackend.dto.request.*;
import com.backend.ecommercebackend.model.admin.credit.CreditCard;
import com.backend.ecommercebackend.model.admin.credit.Header1;
import com.backend.ecommercebackend.model.admin.credit.Header2;
import com.backend.ecommercebackend.model.admin.doortodoor.DoorHeader;
import com.backend.ecommercebackend.model.admin.doortodoor.DoorToDoor;
import com.backend.ecommercebackend.model.admin.doortodoor.DoorToDoorStep;
import com.backend.ecommercebackend.model.admin.support.Support;
import com.backend.ecommercebackend.model.admin.support.SupportHeader;
import com.backend.ecommercebackend.model.admin.support.SupportStep;
import com.backend.ecommercebackend.model.admin.term.UserTerm;
import com.backend.ecommercebackend.repository.admin.credit.CreditCardRepository;
import com.backend.ecommercebackend.repository.admin.credit.Header1Repository;
import com.backend.ecommercebackend.repository.admin.doortodoor.DoorHeaderRepository;
import com.backend.ecommercebackend.repository.admin.doortodoor.DoorToDoorRepository;
import com.backend.ecommercebackend.repository.admin.doortodoor.DoorToDoorStepRepository;
import com.backend.ecommercebackend.repository.admin.support.SupportHeaderRepository;
import com.backend.ecommercebackend.repository.admin.support.SupportRepository;
import com.backend.ecommercebackend.repository.admin.support.SupportStepRepository;
import com.backend.ecommercebackend.repository.admin.term.UserTermRepository;
import com.backend.ecommercebackend.service.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor

public class AdminController {

    private final SupportService supportService;
    private final SupportRepository supportRepository;
    private final SupportStepRepository supportStepRepository;
private final UserTermService userTermService;
    private final UserTermRepository userTermRepository;
    private final DoorToDoorStepService doorToDoorStepService;
    private final DoorToDoorStepRepository doorToDoorStepRepository;
    private final DoorToDoorService doorToDoorService;
    private final DoorToDoorRepository doorToDoorRepository;
    private final CreditCardService creditCardService;
    private final CreditCardRepository creditCardRepository;
    private final DoorHeaderRepository doorHeaderRepository;
    private final SupportHeaderRepository supportHeaderRepository;
    private final Header1Repository header1Repository;
private  final  OrderService orderService;
    @DeleteMapping("/support/{id}")
    @Operation(summary = "Xidmetleri idye gore silmek ucun endpoint")
    public ResponseEntity<Support> deleteSupport(@PathVariable int id) {
        supportRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/supportStep/{id}")
    @Operation(summary = "Xidmetler merhelelerini idye gore silmek ucun endpoint")
    public ResponseEntity<SupportStep> deleteSupportStep(@PathVariable int id) {
        supportStepRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/credit/{id}")
    @Operation(summary = "Xidmetler merhelelerini idye gore silmek ucun endpoint")
    public ResponseEntity<SupportStep> deleteCreditCard(@PathVariable int id) {
        creditCardRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/doorStep/{id}")
    @Operation(summary = "Xidmetler merhelelerini idye gore silmek ucun endpoint (Qapidan qapiya)")
    public ResponseEntity<DoorToDoorStep> deleteDoorToDoorStep(@PathVariable int id) {
        doorToDoorStepRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/door/{id}")
    @Operation(summary = "Faydalari idye gore silmek ucun endpoint")
    public ResponseEntity<Support> deleteDoorToDoor(@PathVariable int id) {
        doorToDoorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/support")
    @Operation(summary = "Xidmetleri elave etmek ucun endpoint")
    public ResponseEntity<Support> addSupport(@RequestBody SupportRequest supportRequest) {
        Support createdSupport = supportService.addSupport(supportRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSupport);
    }
    @PostMapping("/supportStep")
    @Operation(summary = "Xidmet merhelelerini elave etmek ucun endpoint")
    public ResponseEntity<?> addSupportSteps(@RequestBody SupportStepRequest supportStepRequest) {
        if (supportStepRepository.existsByStepOrder(supportStepRequest.getStepOrder())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bu sıra doludur");
        }

        SupportStep createdSupportStep = supportService.addSupportStep(supportStepRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdSupportStep);
    }
    @PostMapping("/term")
    @Operation(summary = "Terms/Policy elave edib deyismek ucun")

    public ResponseEntity<UserTerm> addTerm(@RequestBody UserTermRequest userTermRequest){
        UserTerm userTerm = userTermService.addTerm(userTermRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userTerm);
    }
    @PostMapping("/doorHeader")
    @Operation(summary = "Catdirilma Header elave edib deyismek ucun(Qapidan qapiya)")
    public ResponseEntity<DoorHeader> addDoorHeader(@RequestBody DoorHeaderRequest doorHeaderRequest){
        doorHeaderRepository.deleteAll();

        DoorHeader doorHeader = doorToDoorService.addHeader(doorHeaderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(doorHeader);
    }
    @PostMapping("/supportHeader")
    @Operation(summary = "Xidmet sehifesine Header elave edib deyismek ucun")
    public ResponseEntity<SupportHeader> addDoorHeader(@RequestBody SupportHeaderRequest supportHeaderRequest){
        supportHeaderRepository.deleteAll();

        SupportHeader supportHeader = supportService.addHeader(supportHeaderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(supportHeader);
    }
    @PostMapping("/cardHeader1")
    @Operation(summary = "Xidmet sehifesine Header elave edib deyismek ucun")
    public ResponseEntity<Header1> addHeader1(@RequestBody Header1Request header1Request){
        header1Repository.deleteAll();

        Header1 header1 = creditCardService.addHeader1(header1Request);
        return ResponseEntity.status(HttpStatus.CREATED).body(header1);
    }
    @PostMapping("/cardHeader2")
    @Operation(summary = "Xidmet sehifesine Header 2 elave edib deyismek ucun")
    public ResponseEntity<Header2> addHeader2(@RequestBody Header2Request header2Request){
        header1Repository.deleteAll();

        Header2 header2 = creditCardService.addHeader2(header2Request);
        return ResponseEntity.status(HttpStatus.CREATED).body(header2);
    }
    @PostMapping("/doorStep")
    @Operation(summary = "Xidmetleri merhelelerini etmek ucun endpoint(Qapidan qapiya)")
    public ResponseEntity<?> addDoorStep(@RequestBody DoorToDoorStepRequest doorToDoorStepRequest) {
        if (doorToDoorStepRepository.existsByStepOrder(doorToDoorStepRequest.getStepOrder())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bu sıra doludur");
        }

        DoorToDoorStep createdDoorToDoorStep = doorToDoorStepService.addDoorToDoorStep(doorToDoorStepRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDoorToDoorStep);
    }

    @PostMapping("/door")
    @Operation(summary = "Faydalari elave etmek ucun endpoint")

    public DoorToDoor addDoor( @RequestPart("doorToDoorRequest") DoorToDoorRequest doorToDoorRequest,
                               @RequestPart("file") MultipartFile multipartFile){
        return doorToDoorService.addDoorToDoor(doorToDoorRequest, multipartFile);
    }
    @PostMapping("/credit")
    @Operation(summary = "Kredit kartlarini elave etmek ucun endpoint")

    public CreditCard addCredit(@RequestPart("creditCardRequest") CreditCardRequest creditCardRequest,
                                @RequestPart("file") MultipartFile multipartFile){
        return creditCardService.addCreditCard(creditCardRequest, multipartFile);
    }
    @PutMapping("/support/{id}")
    @Operation(summary = "Xidmetleri idye gore update etmek ucun endpoint")
    public ResponseEntity<Support> updateSupport(@PathVariable int id, @RequestBody SupportRequest supportRequest) {
        Support updatedSupport = supportService.updateSupport(id, supportRequest);
        return ResponseEntity.ok(updatedSupport);
    }

    @PutMapping("/supportStep/{id}")
    @Operation(summary = "Xidmet merhelelerini idye gore update etmek ucun endpoint")
    public ResponseEntity<?> updateSupportStep(@PathVariable int id, @RequestBody SupportStepRequest supportStepRequest) {
        Optional<SupportStep> existingSupportStep = supportStepRepository.findById(id);
        if (existingSupportStep.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Xidmet merhelesi tapilmadi");
        }

        SupportStep currentSupportStep = existingSupportStep.get();
        if (currentSupportStep.getStepOrder() != supportStepRequest.getStepOrder() &&
                supportStepRepository.existsByStepOrder(supportStepRequest.getStepOrder())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bu sıra doludur");
        }

         SupportStep updatedSupportStep = supportService.updateSupportStep(id, supportStepRequest);

        return ResponseEntity.status(HttpStatus.OK).body(updatedSupportStep);
    }
    @PutMapping("/doorStep/{id}")
    @Operation(summary = "Xidmet merhelelerini idye gore update etmek ucun endpoint (Qapidan qapiya)")
    public ResponseEntity<?> updateDoorToDoorStep(
            @PathVariable int id,
            @RequestBody DoorToDoorStepRequest doorToDoorStepRequest) {
        try {
            DoorToDoorStep updatedStep = doorToDoorStepService.updateDoorToDoorStep(id, doorToDoorStepRequest);
            return ResponseEntity.ok(updatedStep); // Başarılı yanıt
        } catch (IllegalArgumentException e) {
            // stepOrder doluysa hata döndür
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            // Eğer kayıt bulunamazsa hata döndür
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PutMapping("/door/{id}")
    @Operation(summary = "Faydalari idye gore update etmek ucun endpoint")
    public DoorToDoor updateDoor(
            @PathVariable int id,
            @RequestPart("doorToDoorRequest") DoorToDoorRequest doorToDoorRequest,
            @RequestPart(value = "file", required = false) MultipartFile multipartFile) throws IOException, IOException {
        return doorToDoorService.updateDoorToDoor(id, doorToDoorRequest, multipartFile);
    }
    @PutMapping("/credit/{id}")
    @Operation(summary = "Kredit kartlarini idye gore update etmek ucun endpoint")
    public CreditCard updateCreditCard(
            @PathVariable int id,
            @RequestPart("creditCardRequest") CreditCardRequest creditCardRequest,
            @RequestPart(value = "file", required = false) MultipartFile multipartFile) throws IOException, IOException {
        return creditCardService.updateCreditCard(id, creditCardRequest, multipartFile);
    }
    @PutMapping("/order/{orderId}")
    @Operation(summary = "İstifadəçinin sifarişlərinin statusunu update etmək üçün endpoint")
    public ResponseEntity<String> updateOrderStatus(
            @PathVariable Long orderId, @RequestBody OrderStatusRequest orderStatusRequest) {
        try {
            orderService.updateOrderStatus(orderId, orderStatusRequest);
            return ResponseEntity.ok("Order status updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }
    @GetMapping("/grouped-by-status")
    @Operation(summary = "Sifarisleri statuslarina gore qruplasdirmaq ucun endpoint  ")
    public Map<String, Long> getOrdersGroupedByStatus() {
        return orderService.getOrdersGroupedByStatus();
    }




}

