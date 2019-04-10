package com.oauth2lab.clientresttemplate.user;

import com.oauth2lab.clientresttemplate.oauth.AuthorizationCodeTokenService;
import com.oauth2lab.clientresttemplate.oauth.OAuth2Token;
import com.oauth2lab.clientresttemplate.security.ClientUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

@Controller
public class MainPage {
    @Autowired
    private AuthorizationCodeTokenService tokenService;

    @Autowired
    private UserRepository users;

    @GetMapping("/")
    public String home(){
        return "index";
    }

    // 通过授权码获取令牌，并且通过UserRepository.save()存储令牌
    @GetMapping("/callback")
    public ModelAndView callback(String code, String state){
        ClientUserDetails userDetails = (ClientUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        ClientUser clientUser = userDetails.getClientUser();

        OAuth2Token token = tokenService.getToken(code);
        clientUser.setAccessToken(token.getAccessToken());

        Calendar tokenValidity = Calendar.getInstance();
        long validIn = System.currentTimeMillis()+Long.parseLong(token.getExpiresIn());
        tokenValidity.setTime(new Date(validIn));
        clientUser.setAccessTokenValidity(tokenValidity);

        users.save(clientUser);
        return new ModelAndView("redirect:/mainpage");
    }

    // 先拿到用户的登录数据；
    //检查是否拥有令牌：没有令牌就请求授权；
    //有授权，则添加展示数据
    //展示数据包括：web应用赋予的展示数据，和访问资源服务器获得的userinfo数据
    @GetMapping("/mainpage")
    public ModelAndView mainpage(){
        ClientUserDetails userDetails = (ClientUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        ClientUser clientUser = userDetails.getClientUser();

        if(clientUser.getAccessToken()==null){
            String authEndpoint = tokenService.getAuthorizationEndpoint();
            return new ModelAndView("redirect:" + authEndpoint);
        }

        clientUser.setEntries(Arrays.asList(
                new Entry("entry 1"),
                new Entry("entry 2")
        ));

        ModelAndView mv = new ModelAndView("mainpage");
        mv.addObject("user", clientUser);

        tryToGetUserInfo(mv, clientUser.getAccessToken());
        return mv;
    }

    private void tryToGetUserInfo(ModelAndView mv, String token) {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer" + token);
        String endpoint = "http://localhost:8080/api/userinfo";

        try {
            RequestEntity<Object> request = new RequestEntity<>(
                    headers, HttpMethod.GET, URI.create(endpoint));
            ResponseEntity<UserInfo> userinfo = restTemplate.exchange(request, UserInfo.class);

            if(userinfo.getStatusCode().is2xxSuccessful()){
                mv.addObject("userinfo", userinfo.getBody());
            }else {
                throw new RuntimeException("it was not possible to retrieve user profile");
            }
        } catch (RestClientException e) {
            throw new RuntimeException("it was not possible to retrieve user profile");
        }
    }
}
