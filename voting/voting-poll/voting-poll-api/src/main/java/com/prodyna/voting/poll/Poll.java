package com.prodyna.voting.poll;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
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
    private String creator;

    @NotNull
    private Date changeDate;

    @NotNull
    private List<PollOption> pollOptions = new ArrayList<>();
}
