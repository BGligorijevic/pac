package com.prodyna.voting.vote;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class Vote {

    private String _id;

    @NotNull
    private String pollId;

    @NotNull
    private String userId;

    @NotNull
    private String optionId;

    /**
     * Please do not remove, Spring Data needs default constructor.
     */
    public Vote(){
    }
}
