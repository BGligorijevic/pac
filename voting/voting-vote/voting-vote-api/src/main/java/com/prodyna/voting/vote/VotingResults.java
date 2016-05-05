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
}
