package com.wallet.cronnetwork.Config;


import com.wallet.cronnetwork.Data.MyAuthDto;
import com.wallet.cronnetwork.Data.NcoinCustomerDto;
import com.wallet.cronnetwork.Service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthProvider implements AuthenticationProvider {
    @Autowired
    UserServiceImpl userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String id = authentication.getName();
        String password = authentication.getCredentials().toString();
        return authenticate(id, password);

    }



    public Authentication authenticate(String id, String password)  throws AuthenticationException {
        String role = "";
        NcoinCustomerDto ncoinCustomerDto = userService.findByUsername(id, password);
        if(ncoinCustomerDto == null){
            return null;
        }

        System.out.println(ncoinCustomerDto.getId());

        switch (ncoinCustomerDto.getEnabled()){
            case 1:
                role = "ROLE_ADMIN";
                break;
            case 2:
                role = "ROLE_USER";
                break;

        }

        List<GrantedAuthority> grantedAuthorityList = new ArrayList<GrantedAuthority>();
        grantedAuthorityList.add(new SimpleGrantedAuthority(role));

        return new MyAuthDto(id, password, grantedAuthorityList, ncoinCustomerDto);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

