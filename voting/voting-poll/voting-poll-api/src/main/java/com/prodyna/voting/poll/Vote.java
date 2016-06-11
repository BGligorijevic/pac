package com.prodyna.voting.poll;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
// we consider votes equal if they come from the same user and relate to the same optionId
@EqualsAndHashCode(of = {"user", "optionId"})
@AllArgsConstructor
public class Vote {

    private String _id;

    @NotNull
    private String user;

    @NotNull
    private String optionId;

    @NotNull
    private Date changeDate;

    /**
     * Please do not remove, Spring Data needs default constructor.
     */
    public Vote() {
    }

    public Vote(String optionId) {
        this.optionId = optionId;
    }
}
