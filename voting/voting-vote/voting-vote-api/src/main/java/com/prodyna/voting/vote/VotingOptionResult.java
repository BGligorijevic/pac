package com.prodyna.voting.vote;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VotingOptionResult {

    private String optionId;

    private int countVotes;

    private double percentage;
}
