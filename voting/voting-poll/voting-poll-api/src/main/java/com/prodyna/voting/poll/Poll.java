package com.prodyna.voting.poll;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Poll {

    private String _id;

    @NotNull
    private String title;

    private String description;

    @NotNull
    private String authorId;

    @NotNull
    private Date changeDate;

    @NotNull
    private List<PollOption> pollOptions;
}
