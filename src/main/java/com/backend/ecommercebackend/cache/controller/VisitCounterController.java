package com.backend.ecommercebackend.cache.controller;

import com.backend.ecommercebackend.cache.service.VisitCounterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/admin")
public class VisitCounterController {

  private final VisitCounterService visitCounterService;

  public VisitCounterController(VisitCounterService visitCounterService) {
    this.visitCounterService = visitCounterService;
  }

  @GetMapping("/total-count")
  public Long getVisitCount() {
    return visitCounterService.getVisitCount();
  }

  @PostMapping("/count-visits")
  public void incrementVisitCount() {
    visitCounterService.incrementVisitCount();
  }
}
