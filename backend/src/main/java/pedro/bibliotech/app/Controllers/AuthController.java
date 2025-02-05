package pedro.bibliotech.app.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pedro.bibliotech.app.Domain.User;
import pedro.bibliotech.app.Repositories.UserRepository;
import pedro.bibliotech.app.Services.DTOs.AuthenticationDTO;
import pedro.bibliotech.app.Services.DTOs.LoginResponseDTO;
import pedro.bibliotech.app.Services.DTOs.RegisterUserDTO;
import pedro.bibliotech.app.Services.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;

    private JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationDTO authenticationDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDTO.email(), authenticationDTO.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = jwtService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterUserDTO registerUserDTO) {
        if (this.userRepository.findByEmail(registerUserDTO.email()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(registerUserDTO.password());

        User newUser = new User(registerUserDTO.email(),
                encryptedPassword,
                registerUserDTO.role(),
                registerUserDTO.firstName(),
                registerUserDTO.lastName(),
                registerUserDTO.phoneNumber(),
                registerUserDTO.cpf(),
                registerUserDTO.address());

        this.userRepository.save(newUser);
        return ResponseEntity.ok().build();
    }
}