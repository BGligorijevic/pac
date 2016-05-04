package com.prodyna.voting.poll;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
public class PollOption implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    /** Please do not remove, Spring Data needs default constructor. */
    public PollOption() {
    }
}
