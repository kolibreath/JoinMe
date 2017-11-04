package com.joinme.model;

import java.util.List;

/**
 * Created by kolibreath on 17-10-21.
 */

public class HotAppList {

    private List<String> 游戏;
    private List<String> 聊天社交;
    private List<String> 金融理财;

    public List<String> get游戏() {
        return 游戏;
    }

    public void set游戏(List<String> 游戏) {
        this.游戏 = 游戏;
    }

    public List<String> get聊天社交() {
        return 聊天社交;
    }

    public void set聊天社交(List<String> 聊天社交) {
        this.聊天社交 = 聊天社交;
    }

    public List<String> get金融理财() {
        return 金融理财;
    }

    public void set金融理财(List<String> 金融理财) {
        this.金融理财 = 金融理财;
    }
}
