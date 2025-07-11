package com.team.dev.event;

import com.team.dev.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;


@Setter
@Getter
public class AccountEvent extends ApplicationEvent {

    private User user;
    private  String applicationUrl;

    public AccountEvent(User user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}
