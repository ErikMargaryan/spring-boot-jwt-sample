package com.javainuse.controller;

import java.util.Objects;

import com.javainuse.model.JwtRefreshTokenRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import com.javainuse.config.JwtTokenUtil;
import com.javainuse.model.JwtRequest;
import com.javainuse.model.JwtResponse;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

	private final AuthenticationManager authenticationManager;

	private final JwtTokenUtil jwtTokenUtil;

	private final UserDetailsService jwtInMemoryUserDetailsService;

	public JwtAuthenticationController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserDetailsService jwtInMemoryUserDetailsService) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenUtil = jwtTokenUtil;
		this.jwtInMemoryUserDetailsService = jwtInMemoryUserDetailsService;
	}

	@PostMapping( "/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
			throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = jwtInMemoryUserDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);
		final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token, refreshToken));
	}

	private void authenticate(String username, String password) throws Exception {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

	@GetMapping("/refresh")
	public ResponseEntity<?> refreshToken(@RequestBody JwtRefreshTokenRequest tokenRequest) {
		String refreshToken = tokenRequest.getRefreshToken();
		boolean canTokenBeRefreshed = jwtTokenUtil.canTokenBeRefreshed(refreshToken);
		if (canTokenBeRefreshed) {
			String usernameFromToken = jwtTokenUtil.getUsernameFromToken(refreshToken);
			UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername(usernameFromToken);
			final String token = jwtTokenUtil.generateToken(userDetails);
			refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);
			return ResponseEntity.ok(new JwtResponse(token, refreshToken));
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
}
