package com.prodyna.voting.poll;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
public class PollOptions implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    public PollOptions() {
    }
}
