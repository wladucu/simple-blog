package com.wludio.blog.web.controller;

import com.wludio.blog.facade.FacadeUserService;
import com.wludio.blog.facade.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final FacadeUserService facadeUserService;

    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDto>> getUserDtos(@RequestParam(defaultValue = "0") final Integer pageNo,
                                                     @RequestParam(defaultValue = "10") final Integer pageSize,
                                                     @RequestParam(defaultValue = "id") final String sortBy) {

        log.info("Request to getUsers");
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        List<UserDto> users = facadeUserService.findAll(pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(users);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getUserDtoById(@PathVariable @Min(1) final Long id) {

        log.info("Request to getUserById : {}", id);

        Optional<UserDto> user = facadeUserService.findById(id);

        if (user.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(user.get());
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> create(@Valid @RequestBody final UserDto user) {
        log.info("Request to create user: {}", user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(facadeUserService.create(user));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable @Min(1) final Long id) {
        log.info("Request to delete user with the id : {}", id);

        facadeUserService.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> update(@PathVariable @Min(1) final Long id,
                                          @Valid @RequestBody final UserDto user) {
        log.info("Request to update user with the id: {} and info: {} ", id, user);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(facadeUserService.update(id, user));
    }
}
