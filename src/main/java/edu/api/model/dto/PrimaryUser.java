package edu.api.model.dto;

import edu.api.model.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class PrimaryUser implements UserDetails {
    private String name;
    private String lastName;
    private String userName;
    private String email;
    private String direction;
    private String password;
    private String cvu;
    private String wallet;
    private Collection<? extends GrantedAuthority> authorities;

    public PrimaryUser(String name, String lastName,String userName, String email, String direction, String password, String cvu, String wallet, Collection<? extends GrantedAuthority> authorities) {
        this.name = name;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.direction = direction;
        this.password = password;
        this.cvu = cvu;
        this.wallet = wallet;
        this.authorities = authorities;
    }

    public static PrimaryUser build(User user){
        List<GrantedAuthority> authorities =
                user.getRols().stream().map(rol -> new SimpleGrantedAuthority(rol
                .getRolName().name())).collect(Collectors.toList());
        return new PrimaryUser(user.getName(), user.getLastName(),user.getUserName(), user.getEmail(), user.getDirection(), user.getPassword(),user.getCvu(),user.getWallet(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
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

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
