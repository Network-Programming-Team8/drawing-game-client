package dto.info;

import java.io.Serial;
import java.util.Map;

public class VoteInfo implements Info{

    @Serial
    private static final long serialVersionUID = 3794944618536264364L;
    private final Map<Integer, Integer> numberOfVote;

    public VoteInfo(Map<Integer, Integer> numberOfVote){
        this.numberOfVote = numberOfVote;
    }
}
