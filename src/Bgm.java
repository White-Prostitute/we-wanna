import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class Bgm {
    static Bgm bgmInstance = new Bgm();
    private Clip bgm,menuBgm,DIY,getKeySound,button,handle;

    private Bgm(){
    }
    public static Bgm getInstance(){
        return bgmInstance;
    }
    //加载BGM
    public void loadBGM() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        //加载BGM
        bgm = AudioSystem.getClip();
        InputStream bgFile = this.getClass().getClassLoader().getResourceAsStream("music.wav");
        assert bgFile != null;
        AudioInputStream bgAis = AudioSystem.getAudioInputStream(bgFile);
        bgm.open(bgAis);
        //加载主菜单bgm
        menuBgm = AudioSystem.getClip();
        InputStream menuBgFile = this.getClass().getClassLoader().getResourceAsStream("menuBGM.wav");
        assert menuBgFile != null;
        AudioInputStream menuBgAis = AudioSystem.getAudioInputStream(menuBgFile);
        menuBgm.open(menuBgAis);
        //加载DIY界面背景音乐
        DIY = AudioSystem.getClip();
        InputStream DIYFile = this.getClass().getClassLoader().getResourceAsStream("DIY.wav");
        assert DIYFile != null;
        AudioInputStream DIYAis = AudioSystem.getAudioInputStream(DIYFile);
        DIY.open(DIYAis);
        //拿到钥匙
        getKeySound = AudioSystem.getClip();
        InputStream getKeyFile = this.getClass().getClassLoader().getResourceAsStream("getKey.wav");
        assert getKeyFile != null;
        AudioInputStream getKeyAis = AudioSystem.getAudioInputStream(getKeyFile);
        getKeySound.open(getKeyAis);
        //按钮
        button = AudioSystem.getClip();
        InputStream buttonFile = this.getClass().getClassLoader().getResourceAsStream("button.wav");
        assert buttonFile != null;
        AudioInputStream buttonAis = AudioSystem.getAudioInputStream(buttonFile);
        button.open(buttonAis);
        //开关
        handle = AudioSystem.getClip();
        InputStream handleFile = this.getClass().getClassLoader().getResourceAsStream("handle.wav");
        assert handleFile != null;
        AudioInputStream handleAis = AudioSystem.getAudioInputStream(handleFile);
        handle.open(handleAis);
    }
    //游戏内部的BGM的播放
    public void playBGM(){
        bgm.loop(Clip.LOOP_CONTINUOUSLY);
        menuBgm.stop();
        DIY.stop();
    }
    //菜单界面的BGM的播放
    public void playMenuBGM(){
        menuBgm.loop(Clip.LOOP_CONTINUOUSLY);
        bgm.stop();
        DIY.stop();
    }
    //DIY界面音乐的播放
    public void playDIY(){
        DIY.loop(Clip.LOOP_CONTINUOUSLY);
        menuBgm.stop();
        bgm.stop();
    }
    //拿到钥匙
    public void getKey(){
        getKeySound.loop(1);
    }
    //按钮
    public void button(){
        button.loop(1);
    }
    //开关
    public void handle(){
        handle.loop(1);
    }
}
