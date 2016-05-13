package com.prodyna.voting.vote;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VotingOptionResult {

    private String optionId;

    private int countVotes;

    private double percentage;
}
