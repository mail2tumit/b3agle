package xyz.tumit.b3agle.b0ne.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.tumit.b3agle.b0ne.dto.B0neManger;
import xyz.tumit.b3agle.b0ne.properties.B0neProperties;

@RequiredArgsConstructor
@RestController
public class B0neController {

    private final B0neProperties b0neProperties;

    @GetMapping("/b0ne/manager")
    public B0neManger getBoneManager() {
        return B0neManger.builder()
                .name(b0neProperties.getManager().getName())
                .email(b0neProperties.getManager().getEmail())
                .build();
    }

}
