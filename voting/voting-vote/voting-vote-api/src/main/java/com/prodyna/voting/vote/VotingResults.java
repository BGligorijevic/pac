package com.prodyna.voting.vote;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class VotingResults {

    private String pollId;

    List<VotingOptionResult> votingOptionResults;

    /**
     * Please do not remove, Spring Data needs default constructor.
     */
    public VotingResults() {
    }
}
