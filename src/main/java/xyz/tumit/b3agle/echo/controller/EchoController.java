package xyz.tumit.b3agle.echo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.tumit.b3agle.b0ne.properties.B0neProperties;
import xyz.tumit.b3agle.b0ne.properties.Str0ngProperties;
import xyz.tumit.b3agle.echo.dto.Echo;

@RequiredArgsConstructor
@RestController
public class EchoController {

    private final B0neProperties b0neProperties;
    private final Str0ngProperties strongB0neProperties;

    @GetMapping("/echo")
    public Echo echo() {
        return Echo.builder()
                .dbPwd(strongB0neProperties.getDbPwd())
                .dbUser(strongB0neProperties.getDbUser())
                .managerName(b0neProperties.getManager().getName())
                .managerEmail(b0neProperties.getManager().getEmail())
                .build();
    }
}
