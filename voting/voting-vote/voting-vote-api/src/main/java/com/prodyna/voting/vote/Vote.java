package com.prodyna.voting.vote;

import com.prodyna.voting.auth.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class Vote {

    private String _id;

    @NotNull
    private User user;

    @NotNull
    private String pollId;

    @NotNull
    private String optionId;
}
