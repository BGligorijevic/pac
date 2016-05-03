package com.prodyna.voting.poll;

import com.prodyna.voting.auth.user.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Poll implements Serializable {

    private static final long serialVersionUID = 1L;

    private String _id;

    private String title;

    private String description;

    private User author;

    private Date changeDate;

    private List<PollOptions> pollOptions;
}
