package com.example.epicor.assignment.controller;


import com.example.epicor.assignment.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Praveen Paidisetty
 *
 * this is the controller layer.
 */
@RestController
@RequestMapping("/api/v1")
public class AssignmentController {

    @Autowired
    private AssignmentService service;


    /**
     *
     * @return this method provides you the total count of words excluding the exclusions from the data source.
     */
    @GetMapping("/totalCount")
    public ResponseEntity<?> getTotalCountOfWords() {
        try {
            Integer count = service.getTotalWordsCount();
            return ResponseEntity.status(200).body(count);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500)
                    .body(e.getMessage());
        }
    }

    /**
     *
     * @return this method returns the top five words which has the most occurences.
     */
    @GetMapping("/topFive")
    public ResponseEntity<?> getTopFiveWords() {
        try {
            return ResponseEntity.status(200).body(service.topFiveFrequentWords());
        }catch(Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(500)
                    .body(e.getMessage());
        }
    }


    /**
     *
     * @return this method provides the fifty distinct words sorted in natural alphabetic order.
     */
    @GetMapping("/topFiftyUniqueWords")
    public ResponseEntity<?> getTopFiftyUniqueWords() {
        try {
            return ResponseEntity.status(200).body(service.getTopFiftyUniqueWords());
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(500)
                    .body(e.getMessage());
        }
    }

}
