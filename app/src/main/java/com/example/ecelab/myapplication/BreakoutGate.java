// original source:
// http://droidcake.com/2010/12/30/how-to-create-classic-game-on-android/
// no author listed

package com.example.ecelab.myapplication;

import android.graphics.Color;
import android.graphics.Typeface;

import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.RepeatingSpriteBackground;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.opengl.texture.source.AssetTextureSource;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;

import java.util.ArrayList;
import java.util.List;

public class BreakoutGate extends BaseGameActivity {

    // the width of the game screen
    private static final int CAMERA_WIDTH = 1024;
    // the height of the game screen
    private static final int CAMERA_HEIGHT = 512;
    // the speed of the game
    private static final float DEMO_VELOCITY = 200.0f;

//	// ========== Lab Q7 - Uncomment ========
//	// time between gate advances
//	private static final float ADVANCE_DELAY = 15.0f;
//	// ========== Lab Q7 - Uncomment ========

    // variable used to manipulate the camera
    private Camera mCamera;
    // texture for the entire screen
    private Texture mTexture;
    // texture region for the pong
    private TextureRegion mPongTextureRegion;
    // texture region for the gate
    private TextureRegion mGateTextureRegion;
    private TextureRegion mGate2TextureRegion;
    // texture region for the balls
    private TiledTextureRegion mBallTextureRegion;
    // list that holds all the gates
    private List<Sprite> gateSpritesList = new ArrayList<>();
    // holds the font for the scoreboard
    private Font mFont;
    // holds the texture for the scoreboard
    private Texture mFontTexture;
    // the actual text of the nameBox
    private Font mFont2;
    // holds the texture for the nameBox
    private Texture mFont2Texture;
    // the actual text of the scoreBox
    ChangeableText scoreBox;
    // the actual text of the nameBox
    Text nameBox;
    // initialize score to 0 when the game loads
    private int score = 0;

//	// ========== Lab Q7 - Uncomment ========
//	// time since last shift
//	private float shiftTime = 0;
//	// ========== Lab Q7 - Uncomment ========

    // sounds used in the program
    private Sound mTileHit;
    private Sound mPaddleHit;
    private Sound mDeath;

//  // ========== Lab Q4 - Uncomment ==========
//  // current delta X for drawing gates
//  int delX = 0;
//  // ========== Lab Q4 - Uncomment ==========

    // ========== Lab Q6 - Comment ==========
    // the width of the blocks to be drawn
    final int blockWidth = 48;
    final int blockHeight = 60;
    // number of rows to draw on the scene
    final int numRows = 3;
    // ========== Lab Q6 - Comment ==========

//  // ========== Lab Q6 - Uncomment ==========
//  // the width of the blocks to be drawn
//  final int blockWidth = 36;
//  final int blockHeight = 48;
//  // number of rows to draw on the scene
//  final int numRows = 4;
//  // ========== Lab Q6 - Uncomment ==========

    // this is run when the game is first loaded.
    // i.e. when the application is launched
    @Override
    public Engine onLoadEngine() {
        // sets the camera to the dimensions declared above
        this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        // returns an engine with the properties set by the passed arguments
        // below
        // true: fullscreen
        // ScreenOrientation.LANDSCAPE: sets to landscape orientation
        // RatioResolutionPolicy({width},{height}): sets the software renderer's
        // resolution policy
        // this.mCamera: sets to this class' instantiation of the camera
        return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
                new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
                this.mCamera).setNeedsSound(true));
    }

    // runs when resources are loaded
    // that is, when the actual pictures and textures are being loaded into the
    // game
    @Override
    public void onLoadResources() {
        initTextures();

        initSounds();

        setupScoreBoard();
        setupDeveloperName();
    }

    @Override
    public Scene onLoadScene() {
        this.mEngine.registerUpdateHandler(new FPSLogger());

        // creates a Scene with 1 player
        final Scene scene = new Scene(1);
        // this is an example of how to place a background image rather than a
        // solid color
        scene.setBackground(new RepeatingSpriteBackground(CAMERA_WIDTH,
                CAMERA_HEIGHT, this.mEngine.getTextureManager(),
                new AssetTextureSource(this, "gfx/backgroundLab8.png")));
        //scene.setBackground(new ColorBackground(0.1f, 0.5f, 0.5f));

        // sets the score display with the given font
        scoreBox = new ChangeableText(0, CAMERA_HEIGHT - 50, this.mFont,
                "Score: ", "Score: xxxxx".length());
        // sets the name box with the given font
        nameBox = new Text(430, CAMERA_HEIGHT - 170, this.mFont2,
                "by Cody Herring");

        // adds the score display to the screen
        scene.getTopLayer().addEntity(scoreBox);
        scene.getTopLayer().addEntity(nameBox);

        // these are used to set the position for the paddle sprite
        final int centerX = (CAMERA_WIDTH - this.mPongTextureRegion.getWidth()) / 2;
        final int centerY = (CAMERA_HEIGHT - this.mPongTextureRegion.getHeight()) - 50;
        final Sprite paddle = setupPaddle(centerX, centerY);

        // creates the ball object that bounces around
        final Ball ball = new Ball((CAMERA_WIDTH - this.mPongTextureRegion.getWidth()) / 2f, CAMERA_HEIGHT - this.mPongTextureRegion.getHeight(),
                this.mBallTextureRegion);
        // sets the speed of the ball according to the demo velocity
        ball.setVelocity(DEMO_VELOCITY, DEMO_VELOCITY);

        // adds the paddle and the ball to the current scene
        scene.getTopLayer().addEntity(paddle);
        scene.getTopLayer().addEntity(ball);

        fillBoxArea(scene);

        // adds a listener for the paddle in order to receive input
        scene.registerTouchArea(paddle);
        scene.setTouchAreaBindingEnabled(true);

        // handles all updates (frames) for the game
        scene.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void reset() {
            }

            @Override
            public void onUpdate(final float pSecondsElapsed) {
//              // ========== Lab Q7 - Uncomment ========
//              shiftTime += pSecondsElapsed;
//              boolean shiftFail = false;
//              if (shiftTime > ADVANCE_DELAY) {
//                  shiftTime = 0;
//                  for (Sprite gateSprite : gateSpritesList) {
//                      gateSprite.setPosition(gateSprite.getX(), gateSprite.getY() + blockHeight);
//                      if (gateSprite.getY() + gateSprite.getBaseHeight() > paddle.getY() * 1.05) {
//                          shiftFail = true;
//                      }
//                  }
//                  addRow(0, scene);
//              }
//              if (shiftFail) {
//                  playerDeath(paddle, ball, -10);
//                  resetGame(scene);
//              }
//              // ========== Lab Q7 - Uncomment ========

                // detects if the ball falls below the paddle
                // i.e. lose condition
                // this subtracts 1 point from the score
                // this may be changed to have more drastic consequences such as
                // restarting the entire game
                if (ballBelowPaddle(paddle, ball)) {
                    playerDeath(paddle, ball, -1);
                }

                // ========== Lab Q8 - Comment ========
                // reflect the ball when it hits the paddle
                if (paddle.collidesWith(ball)) {
                    BreakoutGate.this.mPaddleHit.play();
                    ball.setVelocityY(-DEMO_VELOCITY);
                }
                // ========== Lab Q8 - Comment ========

//              // ========== Lab Q8 - Uncomment ========
//				// reflect the ball when it hits the paddle
//				if (paddle.collidesWith(ball)) {
//					BreakoutGate.this.mPaddleHit.play();
//					//ball.setVelocityY(-DEMO_VELOCITY);
//
//
//					//code to make the paddle "curved" so the ball will deflect at different angles when hit with different parts of paddle:
//					// get a positive number that is the quantity of the ratio of the difference from the middle of the ball to middle of the paddle,
//					//	over the half of the width of the paddle.
//					double diff =
//							Math.abs ( ((ball.getX()+ball.getWidth()/2) - (paddle.getX()+paddle.getWidth()/2)) / (paddle.getWidth()/2) );
//					// set the ball's Y velocity to negative the cosine of the above ratio
//					ball.setVelocityY(-(float)Math.cos(diff)*DEMO_VELOCITY);
//					// set the ball's X velocity to the sine of the above ratio, but keep its original sign
//					ball.setVelocityX(Math.signum(ball.getVelocityX())*(float)Math.sin(diff)*DEMO_VELOCITY );
//					// -- Gabe Petrie
//				}
//              // ========== Lab Q8 - Uncomment ========

                final List<Sprite> toRemoved = checkForCollisions(ball);

                removeSprite(scene, toRemoved);
            }

        });

        return scene;
    }

    private List<Sprite> checkForCollisions(final Ball ball) {
        final List<Sprite> toRemoved = new ArrayList<>();
        for (Sprite box : gateSpritesList) {
            // if the ball collides with the box
            // AND it hits the bottom specifically
            if (ball.collidesWith(box)) {
                updateScore(1);

                // add the box to the list of sprites to remove
                toRemoved.add(box);
                // play the "tile hit" sound
                BreakoutGate.this.mTileHit.play();
                checkCollisionDirection(ball, box);

                break;
            }
        }
        return toRemoved;
    }

    private void removeSprite(final Scene scene, final List<Sprite> toRemoved) {
        // this runs once per update/frame
        BreakoutGate.this.runOnUpdateThread(() -> {
            for (Sprite sprite : toRemoved) {
                // remove all of the sprites in the "toRemoved" list
                scene.getTopLayer().removeEntity(sprite);
                gateSpritesList.remove(sprite);
            }
        });
    }

    private void addRow(int screenY, final Scene scene) {
        int screenX = 0;

	    // ========== Lab Q4 - Comment ========
        TextureRegion texture = this.mGateTextureRegion;
        for (int i = 0; i < CAMERA_WIDTH / blockWidth; i++) {
            Sprite gateSprite;
            gateSprite = new Sprite(screenX, 10 + screenY, texture);
            gateSpritesList.add(gateSprite);
            scene.getTopLayer().addEntity(gateSprite);
            screenX += blockWidth;
        }
	    // ========== Lab Q4 - Comment ========

//	    // ========== Lab Q4 - Uncomment ========
//      TextureRegion texture;
//		if (delX == 0) {
//			delX = ???;
//			texture = this.mGateTextureRegion; // Draw shifted back gate
//		} else {
//			delX = ???;
//			texture = this.mGateTextureRegion; // Draw front gate
//		}
//
//		// this places blocks until there is no more room on the screen
//		for (int i = 0; i < (CAMERA_WIDTH - delX) / blockWidth; i++) {
//			Sprite gateSprite;
//			gateSprite = new Sprite(screenX + delX, screenY, texture);
//			gateSpritesList.add(gateSprite);
//			scene.getTopLayer().addEntity(gateSprite);
//			screenX += blockWidth;
//		}
//	    // ========== Lab Q4 - Uncomment ========
    }

    private void fillBoxArea(final Scene scene) {

        // this places rows until requested initial rows are all placed
        for (int screenY = 0; screenY < numRows * blockHeight; screenY += blockHeight) {

            addRow(screenY, scene);

        }
    }

    private Sprite setupPaddle(final int centerX, final int centerY) {
        return new Sprite(centerX, centerY, this.mPongTextureRegion) {
            @Override
            public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
                                         final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                // this gets the X coordinate of the finger touch,
                // then subtracts the width of the paddle,
                // setting the paddle location to the bottom even if
                // the touch is not directly on top of the paddle.
                float newX = pSceneTouchEvent.getX() - this.getWidth() / 2;
                newX = Math.max(newX, 0);
                newX = Math.min(newX, CAMERA_WIDTH - this.getWidth());
                this.setPosition(newX, centerY);
                return true;
            }
        };
    }

    @Override
    public void onLoadComplete() {

    }

    // used to hold game logic information for the ball
    private static class Ball extends AnimatedSprite {
        public Ball(final float pX, final float pY,
                    final TiledTextureRegion pTextureRegion) {
            super(pX, pY, pTextureRegion);
        }

        // occurs every update/frame
        // this handles the bouncing on the borders of the window
        @Override
        protected void onManagedUpdate(final float pSecondsElapsed) {
            // if the ball hits the left border
            if (this.mX < 0) {
                this.setVelocityX(DEMO_VELOCITY);
            } //
            // if the ball hits the right border
            else if (this.mX + this.getWidth() > CAMERA_WIDTH) {
                this.setVelocityX(-DEMO_VELOCITY);
            }

            // if the ball hits the bottom border
            if (this.mY < 0) {
                this.setVelocityY(DEMO_VELOCITY);
            } //
            // if the ball hits the top border
            else if (this.mY + this.getHeight() > CAMERA_HEIGHT) {
                this.setVelocityY(-DEMO_VELOCITY);
            }

            super.onManagedUpdate(pSecondsElapsed);
        }
    }

    private void initSounds() {
        // sets the root for the sound effects to the mfx folder
        SoundFactory.setAssetBasePath("mfx/");
        // initialize all the sounds
        try {
            this.mTileHit = SoundFactory.createSoundFromAsset(
                    this.mEngine.getSoundManager(), this, "crush.ogg");
            this.mPaddleHit = SoundFactory.createSoundFromAsset(
                    this.mEngine.getSoundManager(), this, "paddlehit.ogg");
            this.mDeath = SoundFactory.createSoundFromAsset(
                    this.mEngine.getSoundManager(), this, "death.ogg");
        } catch (Exception e) {
            Debug.e("Error: " + e);
        }
    }

    private void initTextures() {
        // initializes all the texture regions
        // do not change this area unless you know what you're doing
        this.mTexture = new Texture(512, 128,
                TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.mPongTextureRegion = TextureRegionFactory.createFromAsset(
                this.mTexture, this, "gfx/pong.png", 0, 0);
        this.mBallTextureRegion = TextureRegionFactory.createTiledFromAsset(
                this.mTexture, this, "gfx/ball.png", 97, 0, 1, 1);
        this.mGateTextureRegion = TextureRegionFactory.createFromAsset(
                this.mTexture, this, "gfx/NANDlargeBlue.png", 114, 0);
        this.mGate2TextureRegion = TextureRegionFactory.createFromAsset(
                this.mTexture, this, "gfx/OR2B2largeGreen.png", 228, 0);
        // end initialize textures

        // loads the texture for the entire screen
        this.mEngine.getTextureManager().loadTexture(this.mTexture);
    }

    private void setupScoreBoard() {
        // sets up font for scoreboard
        this.mFontTexture = new Texture(256, 256, TextureOptions.BILINEAR);
        this.mFont = new Font(this.mFontTexture, Typeface.create(
                Typeface.DEFAULT, Typeface.BOLD), 48, true, Color.WHITE);
        this.mEngine.getTextureManager().loadTexture(this.mFontTexture);
        this.mEngine.getFontManager().loadFont(this.mFont);
    }

    private void setupDeveloperName() {
        // sets up font for name of developer
        this.mFont2Texture = new Texture(256, 256, TextureOptions.BILINEAR);
        this.mFont2 = new Font(this.mFont2Texture, Typeface.create(
                Typeface.DEFAULT, Typeface.BOLD), 30, true, Color.GRAY);
        this.mEngine.getTextureManager().loadTexture(this.mFont2Texture);
        this.mEngine.getFontManager().loadFont(this.mFont2);
    }

    private void checkCollisionDirection(final Ball ball, Sprite box) {
        // the tolerance for collision detection
        float eps = 0.2f;
        // coordinates for top left of the ball
        float aX = ball.getX();
        float aY = ball.getY();
        // height and width of the ball
        float aH = ball.getBaseHeight();
        float aW = ball.getBaseWidth();
        // coordinates for top left of the collided box
        float bX = box.getX();
        float bY = box.getY();
        // height and width of the collided box
        float bH = box.getBaseHeight();
        float bW = box.getBaseWidth();

        // bottom of ball hits the top of a block
        if (Math.abs((aY + aH) - bY) <= bH * eps) {
            ball.setVelocityY(-ball.getVelocityY());
        } // top of ball hits the bottom of a block
        else if (Math.abs(aY - (bH + bY)) <= bH * eps) {
            ball.setVelocityY(-ball.getVelocityY());
        } // right side of ball hits the left side of a block
        else if (Math.abs((aX + aW) - bX) <= bW * eps) {
            ball.setVelocityX(-ball.getVelocityX());
        } // left side of ball hits the right side of a block
        else if (Math.abs(aX - (bW + bX)) <= bW * eps) {
            ball.setVelocityX(-ball.getVelocityX());
        }
    }

    private void resetBall(final Sprite paddle, final Ball ball) {
        // ========== Lab Q6 - Comment ========
        ball.setPosition(CAMERA_WIDTH / 2f + ball.getBaseWidth(), CAMERA_HEIGHT / 2f - ball.getBaseHeight());
        // ========== Lab Q6 - Comment ========

//	    // ========== Lab Q6 - Uncomment ========
//		ball.setPosition(paddle.getX() + ball.getBaseWidth(), paddle.getY() - ball.getBaseHeight());
//	    // ========== Lab Q6 - Uncomment ========
    }

    private void playerDeath(final Sprite paddle, final Ball ball, final int scoreDelta) {
        this.mDeath.play();
        this.updateScore(scoreDelta);
        resetBall(paddle, ball);
    }

    private void updateScore(final int delta) {
        // adjust score and change text
        score += delta;
        scoreBox.setText("Score: " + score);
    }

    private boolean ballBelowPaddle(final Sprite paddle, final Ball ball) {
        return ball.getY() + ball.getBaseHeight() > paddle.getY() * 1.05;
    }

    private void resetGame(Scene scene) {
        removeSprite(scene, gateSpritesList);
        // to avoid concurrency issues create a new list rather than reusing the old
        gateSpritesList = new ArrayList<>();
        fillBoxArea(scene);
    }
}
