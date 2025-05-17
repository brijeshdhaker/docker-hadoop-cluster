package org.examples.sb.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.examples.sb.exceptions.AppException;
import org.examples.sb.models.AppRestResponse;
import org.examples.sb.models.User;
import org.examples.sb.repositories.entities.UserEntity;
import org.examples.sb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_User.Write')")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        ResponseEntity<AppRestResponse> httpResponse;
        AppRestResponse appResponse;
        try {
            userService.saveUser(user);
            appResponse  = new AppRestResponse("User successfully added in system.");
            httpResponse = new ResponseEntity<>(appResponse, HttpStatus.CREATED);
        } catch (Exception ex) {
            appResponse  = new AppRestResponse(ex.getMessage());
            httpResponse = new ResponseEntity<>(appResponse,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return httpResponse;
    }


    @Tag(name = "get", description = "Retrieve All Users")
    @Operation(
        summary = "Update an employee",
        description = "Update an existing employee. The response is updated Employee object with id, first name, and last name."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",schema = @Schema(implementation = UserEntity.class))}),
        @ApiResponse(responseCode = "404", description = "Employee not found",content = @Content)
    })
    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_User.Read')")
    public Iterable<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_User.Write')")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_User.Write')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        ResponseEntity<?> httpResponse;
        AppRestResponse appResponse;
        try {
            Boolean result = userService.deleteUserById(id);
            if(result){
                appResponse  = new AppRestResponse(String.format("User with id %d successfully deleted.",id));
                httpResponse =  new ResponseEntity<>(appResponse, HttpStatus.OK);
            }else {
                appResponse  = new AppRestResponse(String.format("User with id %d not found in system.",id));
                httpResponse =  new ResponseEntity<>(appResponse,HttpStatus.NOT_FOUND);
            }
        } catch(AppException ex) {
            // log.error(ex.getMessage(),ex);
            appResponse  = new AppRestResponse(ex.getMessage());
            httpResponse = new ResponseEntity<>(appResponse,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return  httpResponse;
    }
}
