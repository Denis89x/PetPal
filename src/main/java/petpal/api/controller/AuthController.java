package petpal.api.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import petpal.api.dto.AccountDTO;
import petpal.api.dto.AuthenticationDTO;
import petpal.security.JWTUtil;
import petpal.api.service.RegistrationService;
import petpal.store.model.Account;
import petpal.util.AccountValidator;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class AuthController {

    JWTUtil jwtUtil;
    ModelMapper modelMapper;
    AccountValidator accountValidator;
    RegistrationService registrationService;
    AuthenticationManager authenticationManager;

    @PostMapping("/registration")
    public Map<String, String> performRegistration(
            @RequestBody @Valid AccountDTO accountDTO,
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
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
                        authenticationDTO.getPassword());
        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            return Map.of("message", "Incorrect credentials!");
        }
        String token = jwtUtil.generateToken(authenticationDTO.getUsername());
        return Map.of("jwt-token", token);
    }

    public Account convertToAccount(AccountDTO accountDTO) {
        return this.modelMapper.map(accountDTO, Account.class);
    }
}