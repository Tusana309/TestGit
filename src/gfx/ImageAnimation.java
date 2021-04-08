package gfx;

import javafx.scene.image.Image;

/*
Nếu Character Assets được lưu dưới dạng nhiều file ảnh, gộp lại thành Array Image[]
thì dùng class này.
Ví dụ: ufo instanceof AnimatedImage, có array imageArray, thì set
ufo.frames = imageArray; ufo.durations = 0.1;

Để vẽ ra Canvas thì
double t = (l - System.nanoTime()) / 1000000000.0; (l trong AnimationTimer() class Game)
render() g.drawImage(ufo.getFrame(t), x, y);
 */

public class ImageAnimation {

    private int speed, index;
    private long lastTime, timer;
    private Image[] frames;

    public ImageAnimation(int speed, Image[] frames) {
        this.speed = speed;
        this.frames = frames;
        index = 0;
        timer = 0;
        lastTime = System.currentTimeMillis();
    }

    public void tick() {
        timer += System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();

        if (timer > speed) {
            index++;
            timer = 0;
            if (index >= frames.length) {
                index = 0;
            }
        }
    }

    public Image getCurrentFrame() {
        return frames[index];
    }
}
