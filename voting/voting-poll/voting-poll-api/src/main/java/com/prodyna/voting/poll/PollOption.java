package com.prodyna.voting.poll;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PollOption {

    @NotNull
    private String _id;

    @NotNull
    private String name;

    /**
     * Please do not remove, Spring Data needs default constructor.
     */
    public PollOption() {
    }

    public PollOption(String name) {
        this.name = name;
    }
}
