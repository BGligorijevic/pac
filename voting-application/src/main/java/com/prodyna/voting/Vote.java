package com.prodyna.voting;

import java.util.Date;
import java.util.List;

import com.prodyna.auth.user.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Vote {

    private String title;

    private String description;

    private User creator;

    private Date created;

    private List<VoteOptions> voteOptions;
}
