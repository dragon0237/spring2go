package com.oauth2lab.clientresttemplate.security;

import com.oauth2lab.clientresttemplate.user.ClientUser;
import com.oauth2lab.clientresttemplate.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository users;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Optional<ClientUser> optionalClientUser = users.findByUsername(username);
        if(!optionalClientUser.isPresent()){
            throw new UsernameNotFoundException("invalid username or password");
        }
        return new ClientUserDetails(optionalClientUser.get());
    }
}
