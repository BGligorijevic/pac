package com.prodyna.voting.poll;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PollOption {

    @NotNull
    private String _id;

    @NotNull
    private String name;

    @NotNull
    private String pollId;

    @Transient
    private boolean voted;

    @NotNull
    private List<Vote> votes = new ArrayList<>();

    /**
     * Please do not remove, Spring Data needs default constructor.
     */
    public PollOption() {
    }

    public PollOption(String name, String pollId) {
        _id = UUID.randomUUID().toString();
        this.pollId = pollId;
        this.name = name;
    }
}
