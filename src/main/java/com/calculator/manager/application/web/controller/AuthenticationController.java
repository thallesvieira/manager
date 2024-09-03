package com.calculator.manager.application.web.controller;

import com.calculator.manager.domain.model.Login;
import com.calculator.manager.domain.model.Token;
import com.calculator.manager.domain.model.user.StatusType;
import com.calculator.manager.domain.security.IAuthenticationFilter;
import com.calculator.manager.domain.service.ITokenService;
import com.calculator.manager.domain.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Controller responsible to realize login and sign out
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired
	private IAuthenticationFilter auth;

	@Autowired
	private IUserService userService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private ITokenService tokenService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Login login) {
		try{
            logger.info("User: {} trying  to log in", login.getUsername());
			String jwt = auth.authenticateLogin(login);
			logger.info("User: {} trying successfully logged in", login.getUsername());
			return ResponseEntity.ok(new Token(jwt));
		} catch (Exception ex) {
			logger.error("Error to login for user: {}", login.getUsername());
			throw ex;
		}
	}

	@GetMapping("/check")
	public ResponseEntity<?> checkToken() {
		try {
			logger.info("Checking if token is valid");
			return ResponseEntity.ok("");
		} catch (Exception ex) {
			logger.error("Error invalid token");
			throw ex;
		}
	}

	@PostMapping("/sign-out")
	public ResponseEntity<?> signOut() {
		try {
			Long userId = tokenService.getUserId(tokenService.retrieve(request));
			logger.info("User trying logged out");

			userService.updateStatus(userId, StatusType.INACTIVE);
			logger.info("User successfully logged out");
			return ResponseEntity.ok("");
		} catch (Exception ex) {
			logger.error("Error to login for user");
			throw ex;
		}
	}
}
