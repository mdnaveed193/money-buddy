package com.app.financemanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.financemanager.responseStructure.ResponseStructure;
import com.app.financemanager.service.DashboardService;

import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<ResponseStructure<Map<String, Object>>> getDashboard() {
        return dashboardService.getDashboard();
    }

}
