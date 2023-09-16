package petpal.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import petpal.dto.AccountDTO;
import petpal.dto.AuthenticationDTO;
import petpal.model.Account;
import petpal.security.JWTUtil;
import petpal.service.RegistrationService;
import petpal.util.AccountValidator;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final AccountValidator accountValidator;
    private final RegistrationService registrationService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(JWTUtil jwtUtil, ModelMapper modelMapper, AccountValidator accountValidator, RegistrationService registrationService, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.accountValidator = accountValidator;
        this.registrationService = registrationService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/registration")
    public Map<String, String> performRegistration(@RequestBody @Valid AccountDTO accountDTO,
                                      BindingResult result) {
        Account account = convertToAccount(accountDTO);

        accountValidator.validate(account, result);

        if (result.hasErrors())
            return Map.of("message", "Ошибка");

        registrationService.register(account);

        String token = jwtUtil.generateToken(account.getUsername());
        return Map.of("jwt-token", token);
    }

    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody AuthenticationDTO authenticationDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
                        authenticationDTO.getPassword());

        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException exception) {
            return Map.of("message","Incorrect credentials!");
        }

        String token = jwtUtil.generateToken(authenticationDTO.getUsername());
        return Map.of("jwt-token", token);
    }

    public Account convertToAccount(AccountDTO accountDTO) {
        return this.modelMapper.map(accountDTO, Account.class);
    }
}
