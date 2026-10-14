package com.example.backend.security;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/public/auth")
public class AuthController {

    private final UserAccountRepository userAccountRepository;
    private final SecurityContextRepository securityContextRepository;

    public AuthController(
    		UserAccountRepository userAccountRepository,
            SecurityContextRepository securityContextRepository) {
        this.userAccountRepository = userAccountRepository;
        this.securityContextRepository = securityContextRepository;
    }

    public record LoginRequest(String username, String password) {}
    public record SignupUserDetail(String username, String password, String name, String phone) {}
    public record UserProfileDetail(String username, String name, String phone) {}

    /*
     * Response
	     
	     {
		    "message": "Login successful",
		    "userAccount": {
		        "id": 1,
		        "username": "amit12",
		        "password": "demo123",
		        "email": null,
		        "fullname": "Amit Sharma",
		        "phone": "9874563210",
		        "address": "Lal Bagh, Mayur Vihar, Delhi, 100001"
		    }
		 }
		 
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody LoginRequest request,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) {
    	System.out.println(request.username()+" "+request.password());
    	
    	//first check whether same user has session active 
    	HttpSession httpSession = servletRequest.getSession(false);
    	 UserAccountRecord userAccount=null;
    	if(httpSession==null) {
    		 var user = userAccountRepository.findUserAccountRecordByUsername(request.username());
    		
    		 if (user.isPresent()) {
    				userAccount = user.get();
    				if (userAccount.password().equals(request.password())) {
    					httpSession=servletRequest.getSession(true);
    					httpSession.setAttribute("userAccount", userAccount);
    				}else {
    					return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
    							.body(Map.of("error", "Username password mismatch"));
    				}
    		 }else{
    				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not found"));
    		 }
    	}

        return ResponseEntity.ok(
                Map.of(
                        "message", "Login successful",
                        "userAccount", httpSession.getAttribute("userAccount")
                )
        );
    }
    
    @PostMapping("/signup")
    public ResponseEntity<Map<String,String>> signup(
    		@RequestBody SignupUserDetail signupUserDetail,
    		HttpServletRequest httpServletRequest,
    		HttpServletResponse httpSerResponse){
    	
    	
    	
    	return ResponseEntity.ok(Map.of(
    			"message","Signup Successful"
    			)
    		);
    }
    
    @GetMapping("/me")
	public ResponseEntity<?> currentUser(HttpServletRequest httpServletRequest) {

    	HttpSession httpSession = httpServletRequest.getSession(false);
		if (httpSession == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		return ResponseEntity.ok(
			java.util.Map.of(
				"message", "Profile Details",
				"userAccount", httpSession.getAttribute("userAccount"),
				"authorities", List.of()
			)
		);
	}
    
    @PostMapping("/logout")
    public ResponseEntity<Map<String,String>> logout(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
    	SecurityContextLogoutHandler logoutHandler =new SecurityContextLogoutHandler();
    	logoutHandler.setSecurityContextRepository(securityContextRepository);
    	logoutHandler.logout(httpRequest, httpResponse, SecurityContextHolder.getContext().getAuthentication());
    	return ResponseEntity.ok(Map.of("message","Logout Successful."));
    }
}