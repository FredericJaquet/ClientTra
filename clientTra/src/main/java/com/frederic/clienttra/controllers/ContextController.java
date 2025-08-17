package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.read.ContextDTO;
import com.frederic.clienttra.services.ContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/context")
@RequiredArgsConstructor
public class ContextController {

    private final ContextService contextService;

    @GetMapping
    public ResponseEntity<ContextDTO> getContext(){
        return ResponseEntity.ok(contextService.getContext());
    }
}
