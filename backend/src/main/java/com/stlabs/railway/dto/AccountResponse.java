package com.stlabs.railway.dto;

import com.stlabs.railway.model.PortalUser;

public class AccountResponse {
    private final Long id;
    private final String name;
    private final String email;

    public AccountResponse(PortalUser user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
}
