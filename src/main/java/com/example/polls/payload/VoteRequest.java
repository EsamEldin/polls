package com.example.polls.payload;

/**
 * @author <egadEldin.ext@orange.com> Essam Eldin
 *
 */
import javax.validation.constraints.NotNull;

public class VoteRequest {
    @NotNull
    private Long choiceId;

    public Long getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(Long choiceId) {
        this.choiceId = choiceId;
    }
}
