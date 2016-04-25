package com.prodyna.voting.poll;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class PollRestApiTest {

    @InjectMocks
    private PollRestApi pollRestApi;

    @Mock
    private PollService pollService;

    /**
     * Test for {@link PollRestApi#getAllPolls()}.
     */
    @Test
    public void gets_all_polls() {
        List<Poll> polls = new ArrayList<>();
        Poll poll1 = new Poll();
        poll1.setTitle("poll1 title");
        poll1.setDescription("poll1 description");
        polls.add(poll1);

        when(pollService.getAllPolls()).thenReturn(polls);
        List<Poll> allPolls = pollRestApi.getAllPolls();
        assertTrue(allPolls != null);
        assertTrue(allPolls.size() == 1);
        assertTrue(allPolls.get(0).getTitle().equals("poll1 title"));
        assertTrue(allPolls.get(0).getDescription().equals("poll1 description"));
        assertTrue(allPolls.get(0).getChangeDate() == null);
    }
}
