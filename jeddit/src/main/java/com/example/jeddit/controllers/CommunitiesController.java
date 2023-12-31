package com.example.jeddit.controllers;

import com.example.jeddit.exceptions.*;
import com.example.jeddit.models.models.ErrorModel;
import com.example.jeddit.models.models.JWTTokenRequest;
import com.example.jeddit.models.models.StandardResponse;
import com.example.jeddit.models.models.communities.CommunitiesCreateRequest;
import com.example.jeddit.models.models.communities.CommunityChangeDescriptionRequest;
import com.example.jeddit.models.models.communities.CommunityInfoResponse;
import com.example.jeddit.servicies.CommunitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/communities")
public class CommunitiesController {

    @Autowired
    private CommunitiesService communitiesService;

    @PostMapping("/")
    @ResponseBody
    private ResponseEntity<Object> createCommunity(@RequestBody CommunitiesCreateRequest request) {
        try {
            communitiesService.createCommunity(request);
            return ResponseEntity.status(HttpStatus.OK).body(new StandardResponse(true, "Success create"));
        } catch (NotUniqueDataException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new StandardResponse(false, new ErrorModel(409, "CONFLICT", e.getMessage()), "error"));
        } catch (NotValidToken e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new StandardResponse(false, new ErrorModel(401, "UNAUTHORIZED", e.getMessage()), "error"));
        } catch (NotCorrectDataException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StandardResponse(false, new ErrorModel(400, "BAD_REQUEST", e.getMessage()), "error"));
        }
    }

    @GetMapping("/{title}")
    @ResponseBody
    private ResponseEntity<Object> getCommunity(@PathVariable String title) {
        try {
            CommunityInfoResponse response = communitiesService.getCommunity(title);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (DataNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StandardResponse(false, new ErrorModel(400, "BAD_REQUEST", e.getMessage()), "error"));
        }
    }

    @PutMapping("/{title}/change_description")
    @ResponseBody
    private ResponseEntity<Object> changePassword(@PathVariable String title, @RequestBody CommunityChangeDescriptionRequest request){
        try{
            communitiesService.changeDescription(title, request);
            return ResponseEntity.status(HttpStatus.OK).body(new StandardResponse(true, "Success description change"));
        } catch (DataNotFoundException | NotCorrectDataException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StandardResponse(false, new ErrorModel(400, "BAD_REQUEST", e.getMessage()), "error"));
        } catch (NotEnoughRightsException | NotValidToken e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new StandardResponse(false, new ErrorModel(401, "UNAUTHORIZED", e.getMessage()), "error"));
        }
    }

    @DeleteMapping("/{title}")
    @ResponseBody
    private ResponseEntity<Object> deleteCommunity(@PathVariable String title, @RequestBody JWTTokenRequest request) {
        try {
            communitiesService.deleteCommunity(title, request);
            return ResponseEntity.status(HttpStatus.OK).body(new StandardResponse(true, "Success delete"));
        } catch (DataNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StandardResponse(false, new ErrorModel(400, "BAD_REQUEST", e.getMessage()), "error"));
        } catch (NotEnoughRightsException | NotValidToken e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new StandardResponse(false, new ErrorModel(401, "UNAUTHORIZED", e.getMessage()), "error"));
        }
    }
}