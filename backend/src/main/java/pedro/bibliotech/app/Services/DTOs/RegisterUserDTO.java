package pedro.bibliotech.app.Services.DTOs;

import pedro.bibliotech.app.Enums.RolesEnum;

public record RegisterUserDTO(
        String email,
        String password,
        RolesEnum role,
        String firstName,
        String lastName,
        String phoneNumber,
        String cpf,

        String address
    ) {
}
