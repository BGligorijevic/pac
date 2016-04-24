package com.prodyna.voting.vote;

import com.prodyna.voting.auth.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Vote {

    private String voteId;

    private String title;

    private String description;

    private User creator;

    private Date created;

    private List<VoteOptions> voteOptions;
}
