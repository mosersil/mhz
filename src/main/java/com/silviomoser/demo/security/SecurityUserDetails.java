package com.silviomoser.demo.security;

import com.silviomoser.demo.data.Role;
import com.silviomoser.demo.data.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


public class SecurityUserDetails extends User implements UserDetails {

    private static final String SPRING_ROLE_PREFIX = "ROLE_";

    private static final long serialVersionUID = 1L;

    public SecurityUserDetails(User user) {
        if (user == null) return;
        this.setId(user.getId());
        this.setUsername(user.getUsername());
        this.setPassword(user.getPassword());
        this.setCreatedDate(user.getCreatedDate());
        this.setLastModifiedDate(user.getLastModifiedDate());
        this.setRoles(user.getRoles());
        this.setPerson(user.getPerson());
        this.setActive(user.isActive());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : super.getRoles()) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(SPRING_ROLE_PREFIX + role.getType().name());
            authorities.add(authority);
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public String getPassword() {
        return super.getPassword();
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
        return isActive();
    }
}
