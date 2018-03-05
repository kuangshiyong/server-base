package cn.bjzfgcjs.idefense.api.bean;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Audio {

    @Min(1)
    private Integer position;

    private String file;

    private Boolean play;

    @Min(1)
    private Integer loop;

    @NotNull
    private Integer volume;

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Boolean getPlay() {
        return play;
    }

    public void setPlay(Boolean play) {
        this.play = play;
    }

    public Integer getLoop() {
        return loop;
    }

    public void setLoop(Integer loop) {
        this.loop = loop;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }
}
