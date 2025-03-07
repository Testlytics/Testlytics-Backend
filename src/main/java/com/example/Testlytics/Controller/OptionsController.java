//package com.example.Testlytics.Controller;
//
//import com.example.Testlytics.DTO.OptionsDTO;
//import com.example.Testlytics.Service.OptionsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/options")
//public class OptionsController {
//
//    @Autowired
//    private OptionsService optionsService;
//
//    @GetMapping("/question/{questionId}")
//    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
//    public List<OptionsDTO> getOptionsByQuestion(@PathVariable UUID questionId) {
//        return optionsService.getOptionsByQuestionId(questionId);
//    }
//
//    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
//    public OptionsDTO getOptionById(@PathVariable UUID id) {
//        return optionsService.getOptionById(id);
//    }
//
//    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
//    public OptionsDTO createOption(@RequestBody OptionsDTO optionDTO) {
//        return optionsService.createOption(optionDTO);
//    }
//
//    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public OptionsDTO updateOption(@PathVariable UUID id, @RequestBody OptionsDTO optionDTO) {
//        return optionsService.updateOption(id, optionDTO);
//    }
//
//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public void deleteOption(@PathVariable UUID id) {
//        optionsService.deleteOption(id);
//    }
//}
