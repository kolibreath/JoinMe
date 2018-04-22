package com.joinme.model;

/**
 * Created by kolibreath on 17-10-21.
 */

public class Password {
    String raiserId;
    String raiseTime;
    public static String fliters[] =
            {"学习怎么能够说停就停？","现在是联网模式,想要抛弃你的队友吗"};
    public static String part1 = "[从前浪费过很多时间，现在不想再浪费时间了]一起开始";
    public static String part2 = "的学习吧，复制这条信息，学习--JoinMe，我等你,id";
    public Password(String raiserId, String raiseTime) {
        this.raiserId = raiserId;
        this.raiseTime = raiseTime;
    }

    @Override
    public String toString() {
        String message = part1 +  raiseTime + part2  + raiserId;
        return message;
    }

    public String getRaiserId() {
        return raiserId;
    }

    public String getRaiseTime() {
        return raiseTime;
    }
}
