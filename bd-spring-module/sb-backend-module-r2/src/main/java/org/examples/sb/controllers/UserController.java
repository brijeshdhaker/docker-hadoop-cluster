package org.examples.sb.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.examples.sb.exceptions.AppException;
import org.examples.sb.models.AppRestResponse;
import org.examples.sb.models.User;
import org.examples.sb.repositories.entities.UserEntity;
import org.examples.sb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;


@Slf4j
@RestController
@CrossOrigin(origins = {"*"}, maxAge = 3600)
@RequestMapping(path ="/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DiscoveryClient discoveryClient;
    
    @GetMapping("/fetchUsers")
    public String fetchFromOrderService(HttpServletRequest httpServletRequest) {
        log.info(httpServletRequest.getHeader("X-Custom-Header"));

        ServiceInstance userService = discoveryClient.getInstances("sb-backend-module-r2").get(0);  //here this service id came from application name only (go to order application properties u can see the application name
        RestClient restClient = RestClient.builder()
                .baseUrl(userService.getUri())
                .defaultHeader("X-Tenant-ID", "DC-R2")
                .defaultHeader("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IkNOdjBPSTNSd3FsSEZFVm5hb01Bc2hDSDJYRSIsImtpZCI6IkNOdjBPSTNSd3FsSEZFVm5hb01Bc2hDSDJYRSJ9.eyJhdWQiOiJhcGk6Ly83ZjFjZjRkNy1jYTI0LTQ3YzItYmYxNy02MWE4YTc5NjY3OWUiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC9kYTVhYzhmNy0xM2Q2LTQ2ZTctODE1ZC0wMTJiMDExMjMxNDgvIiwiaWF0IjoxNzQ3NzkxNjM2LCJuYmYiOjE3NDc3OTE2MzYsImV4cCI6MTc0Nzc5NzEyMiwiYWNyIjoiMSIsImFpbyI6IkFlUUFHLzhaQUFBQXRWdlQwbi8zbTVNOExlbE43dFg4ZlRMdTN5Z21BbmRTcW1HTXh6S3VNRkd6VE9uamhENjRjWFVJTWhac0VvWTVrOUZDVVgyeDg3bnJNR0hJS1lTbE5FWllVOWVicEd0VUtWSDh5VXd5QjYyQTNRTWJLV2RSa1NNdFRIcWowMGZWNExRNnFpQmI3YWk2NVBpOEJZV1hHem5HaTVGUWIxUG9nY0lsWHBzOVRRdGY3blN0bFlpeDR5Qm1SYzY1OWVrSHpyNFVkMGJlaEZHeFdoT2xNeEM3Z2JETGJQWmUvbnRYWjZtK21KVWZrVnF1dlpZYUt4UmwyeXNiV1E3NlZUMld3Rk4rZnBLbmUyL29UUnpIaVcwN0U1Qzd0VmdPeDFjVnNFRlprcjRwd3JNPSIsImFtciI6WyJwd2QiLCJtZmEiXSwiYXBwaWQiOiJjNWMwNjJkOC00ZmUyLTQzMTktOTg5Ny1hNTllNTdlZDdhZDIiLCJhcHBpZGFjciI6IjEiLCJlbWFpbCI6ImJyaWplc2hkaGFrZXJAZ21haWwuY29tIiwiZmFtaWx5X25hbWUiOiJEaGFrZXIiLCJnaXZlbl9uYW1lIjoiQnJpamVzaCBLIiwiZ3JvdXBzIjpbImJjZDk5OTM1LWM5MjItNDI4OS04YmVlLTE3NDM2OWMxNTdhNSIsImViZGRjZDM3LTIwYTUtNDI3Mi1hZThkLTdlOTBiZTRhN2U0NiIsIjI5MTMyYzQzLWMyNjMtNDAxNS05ZjQyLTljODQ4ZDliMmYzYiIsIjhjYTczMTQ1LTU5YjctNGE2My04ODY5LTRhMGNkYTE2ZDAyMyIsIjY0ODk0MDhlLTEyMWEtNGQ0OS1iYjQ1LWRiZTA3NDQzNTE3OSIsIjNjMTlmMWVhLWY5ZTMtNDdkMC05ZDRhLWFhOTdjMjlkMDg2NyJdLCJpZHAiOiJsaXZlLmNvbSIsImlwYWRkciI6IjEwNi4yMTMuODMuNzQiLCJuYW1lIjoiQnJpamVzaCBLIERoYWtlciIsIm9pZCI6ImI3ODI0ZDU0LWVmZjgtNGJkMS05Y2FmLWZmZTI1OWQwMzhkMCIsInJoIjoiMS5BVllBOThoYTJ0WVQ1MGFCWFFFckFSSXhTTmYwSEg4a3lzSkh2eGRocUtlV1o1NmZBRHBXQUEuIiwic2NwIjoiQXVkaXQuUmVhZCBBdWRpdC5Xcml0ZSBVc2VyLlJlYWQgVXNlci5Xcml0ZSIsInNpZCI6IjAwNGU5ZDE5LTQ2MzgtZjJjNC1hOGM1LTA3MDM0MmJhOTlkYiIsInN1YiI6Im15dFFEMk1QY1hycEFSUmtmRW9yMXg4YkIzckZhVDhwOEdScnE3b2k1cVEiLCJ0aWQiOiJkYTVhYzhmNy0xM2Q2LTQ2ZTctODE1ZC0wMTJiMDExMjMxNDgiLCJ1bmlxdWVfbmFtZSI6ImxpdmUuY29tI2JyaWplc2hkaGFrZXJAZ21haWwuY29tIiwidXRpIjoiLVNfYVc3TlJPVXFaWTFQZl9HNGVBQSIsInZlciI6IjEuMCIsInhtc19mdGQiOiJ4TjNCUDdmQ2dyeWRueVhUT0ZwZ1pyX3Nxdk5sV1lUQUtfZGkxR3podnlnQllYTnBZWE52ZFhSb1pXRnpkQzFrYzIxeiJ9.LzmE07twVlbP1ApDhf1u5X_Pz5GVftAJioMVzsvLjc18MkyGNNF7Vwhs-bQmCNDRr5Ts9tKuM3StzOgoGmV23yACUDPQDXA7AteFDpyKdWYjWL9SWk9mT5BYxEQEIQEfV8_AWkfjg6hSZWdWilxLq8eGVHJvp-TP8E0ktN2Ug-XEU2CzBEk-YUtr4aR5pezIJ0nUQaY1Ij2tm9402lVNS8_UfJLLz_h-pnb1mAHbnLckZtIzcNhP2w6apFiuvArBPEuN7AFLQl7kEfXE-0oj4fVsT16xX7WLriY619wYsDdTGM6eve00d2Qfgs3QjfQK7UCAhB_t8t0OzXTCN0H38g")
                .build();

        return restClient.get()
                .uri("/api/v1/r2/users")
                .retrieve()
                .body(String.class);
    }

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
