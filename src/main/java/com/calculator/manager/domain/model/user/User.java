package com.calculator.manager.domain.model.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class User implements UserDetails {
    Long id;
    String username;
    String password;
    StatusType status;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "USER");
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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

    /**
     * Control if the token from user is active or not.
     * If the user to do log out the user will be the INACTIVE status
     * @return
     */
    public boolean isActive() {
        return this.status == StatusType.ACTIVE;
    }
}
