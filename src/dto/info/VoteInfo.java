package dto.info;

import java.util.Map;

public class VoteInfo {

    private final Map<Integer, Integer> numberOfVote;

    public VoteInfo(Map<Integer, Integer> numberOfVote){
        this.numberOfVote = numberOfVote;
    }
}
