package com.oauth2lab.clientresttemplate.security;

import com.oauth2lab.clientresttemplate.user.ClientUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

public class ClientUserDetails implements UserDetails {
    private static final long serialVersionUID = 6579746565880009072L;

    private ClientUser clientUser;

    public ClientUserDetails(ClientUser clientUser) {
        this.clientUser = clientUser;
    }

    public ClientUser getClientUser() {
        return clientUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<>();
    }

    @Override
    public String getPassword() {
        return clientUser.getPassword();
    }

    @Override
    public String getUsername() {
        return clientUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
