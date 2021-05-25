import javax.swing.*;
import java.awt.*;

public class Mario extends GameObject implements Update{
    static Mario mario;
    MainFrame mf;
    Image imgWalkRight,imgStandRight,imgJumpRight,imgWalkLeft,imgStandLeft;
    Image imgJumpLeft,imgWalkRight1,imgWalkLeft1,imgDead,imgShadowTime1,imgShadowTime2;
    int speed, timer, jumpHeight;
    boolean left,right,up= false;
    Shadow shadow;
    int shadowTimes = 0;
    boolean isOnGround,isJump,isCreateShadow,isOnTransformGate = false;
    int jumpTime = 0;
    boolean isGetKey,isDead = false;
    String direction = "Right";
    String imagePath;
    //构造方法
    private Mario(MainFrame mf){
        this.mf = mf;
        shadow = new Shadow(this, mf);
        loadImage();
        speed = 2;
        jumpHeight = 0;
        x = 50;
        y = 600;
    }//构造方法
    //实例化方法
    public static Mario getInstance(MainFrame mf){
        if(mario == null){
            mario = new Mario(mf);
        }
        return mario;
    }
    //加载图片
    public void loadImage(){
        //加载图片
        imagePath = mf.setting.marioImgPath;
        img = new ImageIcon(imagePath + "standRight.png").getImage();
        imgWalkRight = new ImageIcon(imagePath + "runRight.png").getImage();
        imgWalkLeft = new ImageIcon(imagePath + "runLeft.png").getImage();
        imgWalkLeft1 = new ImageIcon(imagePath + "runLeft1.png").getImage();
        imgWalkRight1 = new ImageIcon(imagePath + "runRight1.png").getImage();
        imgStandRight = new ImageIcon(imagePath + "standRight.png").getImage();
        imgStandLeft = new ImageIcon(imagePath + "standLeft.png").getImage();
        imgJumpRight = new ImageIcon(imagePath + "jumpRight.png").getImage();
        imgJumpLeft = new ImageIcon(imagePath + "jumpLeft.png").getImage();
        imgDead = new ImageIcon("image/character/gg.png").getImage();
        imgShadowTime1 = new ImageIcon("image/character/shadowTime1.png").getImage();
        imgShadowTime2 = new ImageIcon("image/character/shadowTime2.png").getImage();
        w = img.getWidth(null);
        h = img.getHeight(null);
    }//加载图片
    //内部类阴影
    static class Shadow extends GameObject{
        Image imgShadowLeft,imgShadowRight;
        Mario mario;
        String imagePath;
        public Shadow(Mario mario, MainFrame mf){
            this.mario = mario;
            imagePath = mf.setting.marioImgPath;
            imgShadowLeft = new ImageIcon(imagePath + "shadowLeft.png").getImage();
            imgShadowRight = new ImageIcon(imagePath + "shadowRight.png").getImage();
        }
        public void create(){
            if (!mario.isCreateShadow&&!mario.isOnTransformGate&&mario.shadowTimes>0){
                mario.isCreateShadow = true;
                if (mario.direction.equals("left")){
                    img = imgShadowLeft;
                }
                else{
                    img = imgShadowRight;
                }
                this.x = mario.x;
                this.y = mario.y;
            }
            else if (mario.isCreateShadow){
                mario.isCreateShadow = false;
                mario.shadowTimes --;
                mario.jumpTime = 0;
                mario.x = x;
                mario.y = y;
            }
        }
        public void draw(Graphics g2){
            if (mario.isCreateShadow){
                g2.drawImage(img, x, y, null);
            }
        }
    }
    //碰撞检测(return true为可移动)
    public boolean hit(String str){
        Rectangle myRect = new Rectangle(x, y, w ,h);
        Rectangle rect;
        for(int i = mf.map.bricks.size() - 1; i>= 0;i--){
            Brick brick = mf.map.bricks.get(i);
            switch (str) {
                case "left": {
                    rect = new Rectangle(brick.x, brick.y, brick.w + 2, brick.h);
                    break;
                }
                case "right": {
                    rect = new Rectangle(brick.x - 2, brick.y, brick.w, brick.h);
                    break;
                }
                case "up": {
                    rect = new Rectangle(brick.x, brick.y, brick.w, brick.h + 2);
                    break;
                }
                case "down": {
                    rect = new Rectangle(brick.x, brick.y - 2, brick.w, brick.h);
                    break;
                }
                default:
                    throw new IllegalStateException("Unexpected value: " + str);
            }
            if (myRect.intersects(rect)){
                if (str.equals("down")){
                    isOnGround = true;
                    jumpTime = 2;
                }
                else if (str.equals("up")){
                    jumpHeight = 100;
                    jumpTime = 0;
                }
                return false;
            }
            else if (str.equals("down")){
                isOnGround = false;
            }
        }
        for(int i = mf.map.oneWayBricks.size() - 1;i>=0;i--){
            Map.OneWayBrick oneWayBrick = mf.map.oneWayBricks.get(i);
            switch (str){
                    case "left": {
                        rect = new Rectangle(oneWayBrick.x, oneWayBrick.y, oneWayBrick.w + 2, oneWayBrick.h);
                        break;
                    }
                    case "right": {
                        rect = new Rectangle(oneWayBrick.x - 2, oneWayBrick.y, oneWayBrick.w, oneWayBrick.h);
                        break;
                    }
                    case "up": {
                        rect = new Rectangle(oneWayBrick.x, oneWayBrick.y, oneWayBrick.w, oneWayBrick.h + 2);
                        break;
                    }
                    case "down": {
                        rect = new Rectangle(oneWayBrick.x, oneWayBrick.y - 2, oneWayBrick.w, oneWayBrick.h);
                        break;
                    }
                    default:
                        throw new IllegalStateException("Unexpected value: " + str);
                }
                if (myRect.intersects(rect)){
                    if (!str.equals(oneWayBrick.direction)){
                        if (str.equals("down")){
                            isOnGround = true;
                            jumpTime = 2;
                        }
                        else if (str.equals("up")){
                            jumpHeight = 100;
                            jumpTime = 0;
                        }
                        return false;
                    }
                    else{
                        if (oneWayBrick.direction.equals("left")){
                            x --;
                        }else if (oneWayBrick.direction.equals("right")){
                            x ++;
                        }
                    }
                }
                else if (str.equals("down")){
                    isOnGround = false;
                }
            }
        return true;
    }
    //死亡动画
    public void dead(){
        isDead = true;
        img = imgDead;
    }
    //Mario动画
    private void behave(){
        if (direction.equals("right")){
            if (isOnGround){
                if (right) {
                    timer++;
                    if (timer % 40 <= 20) {
                        img = imgWalkRight;
                    }
                    else {
                        img = imgWalkRight1;
                    }
                } else {
                    img = imgStandRight;
                    timer = 0;
                }
            }
            else{
                img = imgJumpRight;
            }
        }
        if (direction.equals("left")){
            if (isOnGround){
                if (left) {
                    timer ++;
                    if (timer % 40 <= 20) {
                        img = imgWalkLeft;
                    }
                    else {
                        img = imgWalkLeft1;
                    }
                } else {
                    img = imgStandLeft;
                    timer = 0;
                }
            }
            else{
                img = imgJumpLeft;
            }
        }
    }
    //Mario的更新
    @Override
    public void update() {
        if (!isDead){
            run();
            if (up){
                jumpHeight ++;
            }
            if (jumpHeight > 100){
                up = false;
                jumpHeight = 0;
            }
            behave();
            isOnTransformGate();
        }
    }
    //检查是否位于传送门
    public void isOnTransformGate(){
        Map.TransformGate a = mf.map.transformGateA;
        Map.TransformGate b = mf.map.transformGateB;
        boolean A = GameFunction.isCollide(this, a)||a.checkCollide(this);
        boolean B = GameFunction.isCollide(this, b)||b.checkCollide(this);
        isOnTransformGate = A || B;
    }
    //跳跃
    public void jump(){
        if (isJump){
            return;
        }
        isJump = true;
        if (isOnGround||jumpTime>0){
            up = true;
            jumpHeight = 0;
            isOnGround = false;
        }
        jumpTime--;
    }
    //移动
    public void run(){
        if (left&&x > 5&&hit("left")){
            x -= speed;
        }
        if (right&& x < 995 - w&&hit("right")){
            x += speed;
        }
        if (up&&hit("up")&&jumpHeight<=100){
            y -= 3 * speed * (100 - jumpHeight)/120;
        }
        if (y+h<700&& hit("down")){
            y += 2;
        }
    }
    //绘制图形
    public void draw(Graphics g2){
        g2.drawImage(img, x, y, null);
        shadow.draw(g2);
        for (int i = 0; i < 8; i++) {
            if(i<shadowTimes)g2.drawImage(imgShadowTime1,25+i*25, 650, null);
            else g2.drawImage(imgShadowTime2, 25+i*25, 650, null);
        }
    }
}
