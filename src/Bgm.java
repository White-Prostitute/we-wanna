import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class Bgm {
    static Bgm bgmInstance = new Bgm();

    Clip bgm;
    InputStream bgFile;
    AudioInputStream bgAis;

    Clip menuBgm;
    InputStream menuBgFile;
    AudioInputStream menuBgAis;

    Clip DIY;
    InputStream DIYFile;
    AudioInputStream DIYAis;

    Clip getKeySound;
    InputStream getKeyFile;
    AudioInputStream getKeyAis;

    Clip attack;
    InputStream attackFile;
    AudioInputStream attackAis;

    Clip button;
    InputStream buttonFile;
    AudioInputStream buttonAis;

    Clip handle;
    InputStream handleFile;
    AudioInputStream handleAis;
    private Bgm(){
    }
    public static Bgm getInstance(){
        return bgmInstance;
    }
    //加载BGM
    public void loadBGM() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        //加载BGM
        bgm = AudioSystem.getClip();
        bgFile = this.getClass().getClassLoader().getResourceAsStream("music.wav");
        assert bgFile != null;
        bgAis = AudioSystem.getAudioInputStream(bgFile);
        bgm.open(bgAis);
        //加载主菜单bgm
        menuBgm = AudioSystem.getClip();
        menuBgFile = this.getClass().getClassLoader().getResourceAsStream("menuBGM.wav");
        assert menuBgFile != null;
        menuBgAis = AudioSystem.getAudioInputStream(menuBgFile);
        menuBgm.open(menuBgAis);
        //加载DIY界面背景音乐
        DIY = AudioSystem.getClip();
        DIYFile = this.getClass().getClassLoader().getResourceAsStream("DIY.wav");
        assert DIYFile != null;
        DIYAis = AudioSystem.getAudioInputStream(DIYFile);
        DIY.open(DIYAis);
        //拿到钥匙
        getKeySound = AudioSystem.getClip();
        getKeyFile = this.getClass().getClassLoader().getResourceAsStream("getKey.wav");
        assert getKeyFile != null;
        getKeyAis = AudioSystem.getAudioInputStream(getKeyFile);
        getKeySound.open(getKeyAis);
        //攻击
        attack = AudioSystem.getClip();
        attackFile = this.getClass().getClassLoader().getResourceAsStream("attack.wav");
        assert attackFile != null;
        attackAis = AudioSystem.getAudioInputStream(attackFile);
        attack.open(attackAis);
        //按钮
        button = AudioSystem.getClip();
        buttonFile = this.getClass().getClassLoader().getResourceAsStream("button.wav");
        assert buttonFile != null;
        buttonAis = AudioSystem.getAudioInputStream(buttonFile);
        button.open(buttonAis);
        //开关
        handle = AudioSystem.getClip();
        handleFile = this.getClass().getClassLoader().getResourceAsStream("handle.wav");
        assert handleFile != null;
        handleAis = AudioSystem.getAudioInputStream(handleFile);
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
    //攻击
    public void attack(){
        attack.loop(1);
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
