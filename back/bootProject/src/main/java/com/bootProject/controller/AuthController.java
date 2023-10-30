package com.bootProject.controller;

import com.bootProject.common.exception.BusinessException;
import com.bootProject.config.SecurityUtil;
import com.bootProject.dto.MemberDTO;
import com.bootProject.dto.TokenDTO;
import com.bootProject.jwt.TokenProvider;
import com.bootProject.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final TokenProvider tokenProvider;

    /*
    * 회원가입
    */
    @PostMapping("/signup")
    public ResponseEntity<MemberDTO> signup(@RequestBody MemberDTO memberDto)  throws BusinessException {
        return ResponseEntity.ok(authService.signup(memberDto));
    }

    /*
    * 로그인
    */
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody(required = false) MemberDTO memberDto) {
        return ResponseEntity.ok(authService.login(memberDto));
    }

    /*
    * 로그아웃
    */
    @PostMapping("/logout")
    public void logOut(HttpServletRequest request, HttpServletResponse response) {
        authService.logOut(request, response);
    }

    /** Auth 2.0 Login (네이버) **/
    @GetMapping("/naver-login")
    public void naverLogin(HttpServletRequest request, HttpServletResponse response) {

    }

    /** Auth 2.0 Login (카카오) **/
    @GetMapping("/kakao-login")
    public void kakaoLogin(HttpServletRequest request, HttpServletResponse response) {

    }

}
