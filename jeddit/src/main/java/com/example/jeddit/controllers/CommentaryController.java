package com.example.jeddit.controllers;

import com.example.jeddit.exceptions.DataNotFoundException;
import com.example.jeddit.exceptions.NotCorrectDataException;
import com.example.jeddit.exceptions.NotEnoughRightsException;
import com.example.jeddit.exceptions.NotValidToken;
import com.example.jeddit.models.entitys.Commentary;
import com.example.jeddit.models.models.CommentaryCreateRequest;
import com.example.jeddit.models.models.JWTTokenRequest;
import com.example.jeddit.models.models.ApiResponse;
import com.example.jeddit.servicies.impl.CommentaryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/commentaries")
public class CommentaryController {

    @Autowired
    private CommentaryServiceImpl commentaryService;

    @PostMapping("/{postId}/")
    private ResponseEntity<Object> createCommentary(@PathVariable long postId, @RequestBody CommentaryCreateRequest request) {
        try {
            commentaryService.create(postId, request);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Success comment"));
        } catch (DataNotFoundException | NotCorrectDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        } catch (NotValidToken e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    private ResponseEntity<Object> createCommentary(@PathVariable long id) {
        try {
            Commentary commentary = commentaryService.get(id);
            return ResponseEntity.status(HttpStatus.OK).body(commentary);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Object> deleteCommentary(@PathVariable long id, @RequestBody JWTTokenRequest request) {
        try {
            commentaryService.delete(id, request);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Success delete"));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        } catch (NotValidToken | NotEnoughRightsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        }
    }
}
