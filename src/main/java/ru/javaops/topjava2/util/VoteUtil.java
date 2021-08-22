package ru.javaops.topjava2.util;




import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.to.BaseTo;
import ru.javaops.topjava2.to.VoteTo;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class VoteUtil {
    public static List<VoteTo> getTos(Collection<Vote> votes) {
        return votes.stream()
                .map(VoteUtil::createTo)
                .sorted(Comparator.comparing(BaseTo::getId))
                .toList();
    }
    public static VoteTo createTo(Vote vote) {
        return new VoteTo(vote.getId(),vote.getRegDate(),vote.getUser().getName(),vote.getRestaurant().getName());
    }
}

