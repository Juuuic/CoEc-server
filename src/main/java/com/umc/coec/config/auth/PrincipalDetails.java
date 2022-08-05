package com.umc.coec.config.auth;

import com.umc.coec.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PrincipalDetails implements UserDetails {

      private User user;

      @Override
      public Collection<? extends GrantedAuthority> getAuthorities() {
            Collection<GrantedAuthority> collection = new ArrayList<>();
            collection.add(() ->"ROLE_"+user.getRoleType());
            return collection;
      }

      @Override
      public String getPassword() {
            return user.getPassword();
      }

      @Override
      public String getUsername() {
            return user.getEmail();
      }

      @Override
      public boolean isAccountNonExpired() {
            return user.getIsAccountNonExpired();
      }

      @Override
      public boolean isAccountNonLocked() {
            return user.getIsAccountNonLocked();
      }

      @Override
      public boolean isCredentialsNonExpired() {
            return user.getIsCredentialNonExpired();
      }

      @Override
      public boolean isEnabled() {
            return user.getIsEnabled();
      }


      public Long getId() {
            return user.getId();
      }
}
