package SMU.BAMBOO.Hompage.global.jwt.userDetails;

import SMU.BAMBOO.Hompage.domain.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final String studentId;
    private final String password;
    private final Role roles;

    public CustomUserDetails(String studentId, String password, Role roles) {
        this.studentId = studentId;
        this.password = password;
        this.roles = roles;
    }

    // 해당 Member 의 권한을 return
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(roles.name()));
        return authorities;
    }

    @Override
    public String getUsername() {
        return studentId;
    }

    @Override
    public String getPassword() {
        return password;
    }

    // 계정의 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠겨있는지 여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Credential이 만료됐는지 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 활성화 여부
    @Override
    public boolean isEnabled() {
        return true;
    }
}
