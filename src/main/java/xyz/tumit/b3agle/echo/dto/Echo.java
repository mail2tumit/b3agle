package xyz.tumit.b3agle.echo.dto;

import lombok.Builder;

@Builder
public record Echo(String dbUser, String dbPwd, String managerName, String managerEmail) {
}
