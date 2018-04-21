package pl.szotaa.util;

import lombok.RequiredArgsConstructor;

import java.security.Principal;

@RequiredArgsConstructor
public class TestPrincipal implements Principal {

    private final String principalName;

    @Override
    public String getName() {
        return principalName;
    }
}