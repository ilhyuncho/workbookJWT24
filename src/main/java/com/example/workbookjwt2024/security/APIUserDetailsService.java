package com.example.workbookjwt2024.security;

import com.example.workbookjwt2024.domain.APIUser;
import com.example.workbookjwt2024.dto.APIUserDTO;
import com.example.workbookjwt2024.repository.APIUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class APIUserDetailsService implements UserDetailsService {

    private final APIUserRepository apiUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<APIUser> byId = apiUserRepository.findById(username);

        APIUser apiUser = byId.orElseThrow(() -> new UsernameNotFoundException("cannot find mid"));

        APIUserDTO dto = new APIUserDTO(apiUser.getMid(), apiUser.getMpw(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        log.info(dto);

        return dto;
    }
}
