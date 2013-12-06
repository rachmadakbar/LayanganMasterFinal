package game;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.ConfigChooserOptions;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.augmentedreality.BaseAugmentedRealityGameActivity;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.adt.color.Color;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Display;
import android.view.Menu;

public class AugmentedActivity extends BaseAugmentedRealityGameActivity implements SensorEventListener {
	
	// ===========================================================
		// Constants
		// ===========================================================

		int cameraWidth;
		int cameraHeight;
		
		// ===========================================================
		// Fields
		// ===========================================================
		
		private ITextureRegion mKiteTextureRegion, mBoxTextureRegion, mCoinScoreTextureRegion, 
		mButtonUpTextureRegion, mButtonDownTextureRegion, mBgOverlayTextureRegion, mHomeTextureRegion;
		private Sprite kite, background, coinScore, powerBox1, powerBox2, powerBox3, buttonUp, buttonDown, fire, fire2, fire3, 
		water, water2, water3, shield, shield2, shield3, bgOverlay, home;
		private int accellerometerSpeedX;
		private int accellerometerSpeedY;
		private SensorManager sensorManager;
		private float centerX;
		private float centerY;
		private float bottomLimit, topLimit;
		private Camera camera;
		private Scene scene = new Scene();
		private BitmapTextureAtlas mBitmapTextureAtlas, mItemBitmapTextureAtlas;
		
		/*buat sprite burung*/
		private BuildableBitmapTextureAtlas mBirdBitmapTextureAtlas;
		private TiledTextureRegion mBirdTextureRegionRight;
		private TiledTextureRegion mBirdTextureRegionLeft;
		
		/*untuk sprite coin*/
		private ITextureRegion mCoinTextureRegion;
		private LinkedList CoinLL;
		private LinkedList CoinsToBeAdded;
		int hitCount = 0;
		
		/*untuk sprite power item*/
		private ITextureRegion mFireTextureRegion;
		private ITextureRegion mWaterTextureRegion;
		private ITextureRegion mShieldTextureRegion;
		private LinkedList FireLL;
		private LinkedList FireToBeAdded;
		private LinkedList WaterLL;
		private LinkedList WaterToBeAdded;
		private LinkedList ShieldLL;
		private LinkedList ShieldToBeAdded;
		
		private String box1 = "";
		private String box2 = "";
		private String box3 = "";
		private int pointer = 1;
		
		private float scale, paddingY;
		
		private LinkedList BirdLLRight;
		private LinkedList BirdLLLeft;
		private LinkedList BirdToBeAddedRight;
		private LinkedList BirdToBeAddedLeft;
		
		/*untuk text perolehan coin dan HP*/
		private Font mFont, mGameOverFont, mFinalFont;
		private Text scoreText, healthPoinText;
		private int healthPoin = 500;
		
		/*HUD game*/
		private HUD gameHUD;
		
		private boolean upIsTouchedFlag = false;
		private boolean downIsTouchedFlag = false;
		private boolean gameOverDisplayed = false;
		
		private Text gameOverText,timesUpText, finalText;
		private int kecepatan;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		// TODO Auto-generated method stub
		final Display display = getWindowManager().getDefaultDisplay();
		cameraWidth = display.getWidth();
		cameraHeight = display.getHeight();

		camera = new Camera(0, 0, cameraWidth, cameraHeight);
		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
						cameraWidth, cameraHeight), camera);
		final ConfigChooserOptions configChooserOptions = engineOptions
				.getRenderOptions().getConfigChooserOptions();
		configChooserOptions.setRequestedRedSize(8);
		configChooserOptions.setRequestedGreenSize(8);
		configChooserOptions.setRequestedBlueSize(8);
		configChooserOptions.setRequestedAlphaSize(8);
		configChooserOptions.setRequestedDepthSize(16);
		return engineOptions;
	}

	@Override
	protected void onCreateResources() throws IOException {
		// TODO Auto-generated method stub
		mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),
				512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("asset/");
		try {
			ITexture bg_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/score_overlaybg.png");
						}
					});

			ITexture kite_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/kite1.png");
						}
					});
			ITexture coin_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/coin.png");
						}
					});
			ITexture fire_asset = new BitmapTexture(
					this.getTextureManager(), new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/fire.png");
						}
					});
			ITexture water_asset = new BitmapTexture(
					this.getTextureManager(), new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/water.png");
						}
					});
			ITexture shield_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/shield.png");
						}
					});
			
			ITexture kotak_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/kotak.png");
						}
					});
			
			ITexture buttonUp_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/ulur.png");
						}
					});
			
			ITexture buttonDown_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/tarik.png");
						}
					});
			
			ITexture coinIndicator_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/coin2.png");
						}
					});
			
			ITexture homeButton_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/home.png");
						}
					});
			
			bg_asset.load();
			kite_asset.load();
			coin_asset.load();
			fire_asset.load();
			water_asset.load();
			shield_asset.load();
			kotak_asset.load();
			buttonUp_asset.load();
			buttonDown_asset.load();
			coinIndicator_asset.load();
			homeButton_asset.load();
			
			mBgOverlayTextureRegion = TextureRegionFactory.extractFromTexture(bg_asset);
			mKiteTextureRegion = TextureRegionFactory.extractFromTexture(kite_asset);
			mCoinTextureRegion = TextureRegionFactory.extractFromTexture(coin_asset);
			mFireTextureRegion = TextureRegionFactory.extractFromTexture(fire_asset);
			mWaterTextureRegion = TextureRegionFactory.extractFromTexture(water_asset);
			mShieldTextureRegion = TextureRegionFactory.extractFromTexture(shield_asset);
			mBoxTextureRegion = TextureRegionFactory.extractFromTexture(kotak_asset);
			mButtonUpTextureRegion = TextureRegionFactory.extractFromTexture(buttonUp_asset);
			mButtonDownTextureRegion = TextureRegionFactory.extractFromTexture(buttonDown_asset);
			mCoinScoreTextureRegion = TextureRegionFactory.extractFromTexture(coinIndicator_asset);
			mHomeTextureRegion = TextureRegionFactory.extractFromTexture(homeButton_asset);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.mBirdBitmapTextureAtlas = new BuildableBitmapTextureAtlas(this.getTextureManager(), 512, 256, TextureOptions.NEAREST);
		this.mBirdTextureRegionRight = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBirdBitmapTextureAtlas, this, "bird1.png", 4, 1);
		this.mBirdTextureRegionLeft = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBirdBitmapTextureAtlas, this, "bird2.png", 4, 1);

		try {
			this.mBirdBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
			this.mBitmapTextureAtlas.load();
			this.mBirdBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}
		
		FontFactory.setAssetBasePath("font/");

		mFont = FontFactory.createFromAsset(mEngine.getFontManager(),
		            mEngine.getTextureManager(), 256, 256, TextureOptions.BILINEAR,
		            this.getAssets(), "Averia-Regular.ttf", 14f, true,
		            Color.WHITE_ABGR_PACKED_INT);
		mFont.load();
		
		mGameOverFont = FontFactory.createFromAsset(mEngine.getFontManager(),
	            mEngine.getTextureManager(), 256, 256, TextureOptions.BILINEAR,
	            this.getAssets(), "Ding Dong Daddyo NF.ttf", 30f, true,
	            Color.WHITE_ABGR_PACKED_INT);
		mGameOverFont.load();
		
		mFinalFont = FontFactory.createFromAsset(mEngine.getFontManager(),
	            mEngine.getTextureManager(), 256, 256, TextureOptions.BILINEAR,
	            this.getAssets(), "Candara.ttf", 22f, true,
	            Color.WHITE_ABGR_PACKED_INT);
		mFinalFont.load();
	}

	@Override
	protected Scene onCreateScene() {
		// TODO Auto-generated method stub
		
		scale = cameraWidth / 400f;
		paddingY = (cameraHeight - 240 * scale) / 2;
		kecepatan = 0;
		
		sensorManager = (SensorManager) this
				.getSystemService(this.SENSOR_SERVICE);
		sensorManager.registerListener(this, sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				sensorManager.SENSOR_DELAY_GAME);
		
		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.mEngine.registerUpdateHandler(new IUpdateHandler() {
			public void onUpdate(float pSecondsElapsed) {
				updateKitePosition();
			}

			public void reset() {
				// TODO Auto-generated method stub
			}
		});

//		Scene scene = new Scene();
		//scene.setBackground(new Background(0,0,0,0));
		scene.getBackground().setColor(Color.TRANSPARENT);
		
		centerX = cameraWidth/2f;
		centerY = cameraHeight/2f;
		
		bgOverlay = new Sprite(0, 0, mBgOverlayTextureRegion, getVertexBufferObjectManager());
		bgOverlay.setScale(scale);
		bgOverlay.setPosition(cameraWidth / 2,
				cameraHeight - bgOverlay.getHeightScaled() / 2
						- paddingY);
		scene.attachChild(bgOverlay);
		
		kite = new Sprite(centerX, centerY, mKiteTextureRegion, this.getVertexBufferObjectManager());
		kite.setScaleCenter(0,0);
		kite.setScale(scale);
		scene.attachChild(kite);
		
		gameHUD = new HUD();
		
		this.CoinLL = new LinkedList<Sprite>();
		this.CoinsToBeAdded = new LinkedList<Sprite>();
		this.FireLL = new LinkedList<Sprite>(); 
		this.FireToBeAdded = new LinkedList<Sprite>();
		this.WaterLL = new LinkedList<Sprite>();
		this.WaterToBeAdded = new LinkedList<Sprite>();
		this.ShieldLL = new LinkedList<Sprite>();
		this.ShieldToBeAdded = new LinkedList<Sprite>();
		this.BirdLLRight = new LinkedList<Sprite>();
		this.BirdLLLeft = new LinkedList<Sprite>();
		this.BirdToBeAddedLeft = new LinkedList<Sprite>();
		this.BirdToBeAddedRight = new LinkedList<Sprite>();
		
		
//		timeLimitTimeHandler();
		createCoinSpriteTimeHandler();
		scene.registerUpdateHandler(coinHandler);
		createPowerSpriteTimeHandler();
		scene.registerUpdateHandler(powerHandler);
		createBirdSpriteTimeHandler();
		scene.registerUpdateHandler(birdHandler);
		createControllers();
		
		scoreText = new Text(0, 0, mFont, "      ", getVertexBufferObjectManager());
		scoreText.setScale(scale);
		scoreText.setPosition(cameraWidth - scoreText.getWidthScaled()/2, cameraHeight - (bgOverlay.getHeightScaled()/2) -paddingY);
		scene.attachChild(scoreText);
		
		healthPoinText = new Text(0, 0, mFont, "Health Poins: 500 ", getVertexBufferObjectManager());
		healthPoinText.setScale(scale);
		healthPoinText.setPosition(healthPoinText.getWidthScaled()/2, cameraHeight - (bgOverlay.getHeightScaled()/2) - paddingY);
		scene.attachChild(healthPoinText);
		
		powerBox1 = new Sprite(0, 0, mBoxTextureRegion, this.getVertexBufferObjectManager());
		powerBox1.setScale(scale);
		powerBox1.setPosition((cameraWidth/3), (powerBox1.getHeightScaled()/2) + paddingY);
		scene.attachChild(powerBox1);
		
		powerBox2 = new Sprite(0, 0, mBoxTextureRegion, this.getVertexBufferObjectManager());
		powerBox2.setScale(scale);
		powerBox2.setPosition((cameraWidth/2), (powerBox2.getHeightScaled()/2) + paddingY);
		scene.attachChild(powerBox2);
		
		powerBox3 = new Sprite(0, 0, mBoxTextureRegion, this.getVertexBufferObjectManager());
		powerBox3.setScale(scale);
		powerBox3.setPosition((cameraWidth * 2/3), (powerBox3.getHeightScaled()/2) + paddingY);
		scene.attachChild(powerBox3);
		
		coinScore = new Sprite(0, 0, mCoinScoreTextureRegion, getVertexBufferObjectManager());
		coinScore.setScale(scale);
		coinScore.setPosition(cameraWidth - scoreText.getWidthScaled() - coinScore.getWidthScaled(), cameraHeight - (coinScore.getHeightScaled()/2) - paddingY);
		scene.attachChild(coinScore);
		
		topLimit = cameraHeight ;
		bottomLimit = paddingY;
		
		fire = new Sprite(0, 0, mFireTextureRegion, this.getVertexBufferObjectManager());
		fire2 = new Sprite(0, 0, mFireTextureRegion, this.getVertexBufferObjectManager());
		fire3 = new Sprite(0, 0, mFireTextureRegion, this.getVertexBufferObjectManager());
		water = new Sprite(0, 0, mWaterTextureRegion, this.getVertexBufferObjectManager());
		water2 = new Sprite(0, 0, mWaterTextureRegion, this.getVertexBufferObjectManager());
		water3 = new Sprite(0, 0, mWaterTextureRegion, this.getVertexBufferObjectManager());
		shield = new Sprite(0, 0, mShieldTextureRegion, this.getVertexBufferObjectManager());
		shield2 = new Sprite(0, 0, mShieldTextureRegion, this.getVertexBufferObjectManager());
		shield3 = new Sprite(0, 0, mShieldTextureRegion, this.getVertexBufferObjectManager());
		
		fire.setScale(scale);
		fire2.setScale(scale);
		fire3.setScale(scale);
		
		water.setScale(scale);
		water2.setScale(scale);
		water3.setScale(scale);
		
		shield.setScale(scale);
		shield2.setScale(scale);
		shield3.setScale(scale);
		
		return scene;
	}
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public void onSensorChanged(SensorEvent event) {
		synchronized (this) {
			switch (event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				accellerometerSpeedX = (int) event.values[1];
				accellerometerSpeedY = (int) event.values[0];
				break;
			}
		}
	}
	
	private void updateKitePosition() {
		if ((accellerometerSpeedX != 0) || (accellerometerSpeedY != 0)) {
			// Set the Boundary limits
			int tL = 0;
			int lL = 0;
			int rL = cameraWidth - (int) kite.getWidthScaled();
			int bL = cameraHeight - (int) kite.getHeightScaled();

			// Calculate New X,Y Coordinates within Limits
			if (centerX >= lL)
				centerX += accellerometerSpeedX*scale;
			else
				centerX = lL;
			if (centerX <= rL)
				centerX += accellerometerSpeedX*scale;
			else
				centerX = rL;
			/*
			if (centerY >= tL)
				centerY += accellerometerSpeedY;
			else
				centerY = tL;
			if (centerY <= bL)
				centerY += accellerometerSpeedY;
			else
				centerY = bL;
			 */
			// Double Check That New X,Y Coordinates are within Limits
			if (centerX < lL)
				centerX = lL;
			else if (centerX > rL)
				centerX = rL;
			/*
			if (centerY < tL)
				centerY = tL;
			else if (centerY > bL)
				centerY = bL;
			*/
			kite.setPosition(centerX, centerY);
		}
	}
	
	private void timeLimitTimeHandler(){
		TimerHandler timeLimit;
		float limit = 30f;
		timeLimit = new TimerHandler(limit, true,
				new ITimerCallback() {

			public void onTimePassed(TimerHandler pTimerHandler) {
				onTimesUp();
			}
		});
		getEngine().registerUpdateHandler(timeLimit);
	}
	
	public void onTimesUp(){
		if (!gameOverDisplayed){
			displayTimesUpText();
		}
	}
	
	public void displayTimesUpText(){
	    Scene timesUp = new Scene();
	   
		timesUpText = new Text(0, 0, mGameOverFont, "Time's Up!", getVertexBufferObjectManager());
		timesUpText.setScale(scale);
		timesUpText.setPosition(cameraWidth/2, cameraHeight/2 + timesUpText.getHeightScaled()/2);
	    timesUp.attachChild(timesUpText);
	    
	    finalText = new Text(0, 0, mFinalFont, "Coins attained: "+String.valueOf(hitCount), getVertexBufferObjectManager());
	    finalText.setScale(scale);
	    finalText.setPosition(cameraWidth/2, cameraHeight/2 - finalText.getHeightScaled());
	    timesUp.attachChild(finalText);
	    
	    home = new ButtonSprite(0, 0, mHomeTextureRegion, getVertexBufferObjectManager());
		home.setScale(scale);
		home.setPosition(home.getWidthScaled()/2, home.getHeightScaled()/2 + paddingY);
		timesUp.attachChild(home);
	    
	    gameHUD.detachChildren();
	    
	    mEngine.setScene(timesUp);
	    
	}
	
	private void createCoinSpriteTimeHandler(){
		TimerHandler coinTimerHandler;
		
//		if (kecepatan > 0 && kecepatan <= 10){
//			float mEffectSpawnDelay = 3f;
//		}else if(kecepatan > 10 && kecepatan <= 19){
//			float mEffectSpawnDelay = 1f;
//		}else if(kecepatan > 19){
//			float mEffectSpawnDelay = 0.5f;
//		}
		
	    float mEffectSpawnDelay = 3f;
	 
	    coinTimerHandler = new TimerHandler(mEffectSpawnDelay, true,
				new ITimerCallback() {

					public void onTimePassed(TimerHandler pTimerHandler) {
						addCoin();
					}
				});

		getEngine().registerUpdateHandler(coinTimerHandler);
	}
	
	private void addCoin(){
		Random rand = new Random();

		float y = cameraHeight + mCoinTextureRegion.getHeight();
		float minX = mCoinTextureRegion.getWidth();
		float maxX = cameraWidth - mCoinTextureRegion.getWidth();
		float rangeX = maxX - minX;
		float x = rand.nextFloat() * rangeX + minX;

		Sprite coin = new Sprite(x, y, this.mCoinTextureRegion.deepCopy(),
				this.getVertexBufferObjectManager());
		coin.setScale(scale);
		this.scene.attachChild(coin);
		

		// Coin duration
		int minDuration = 2;
		int maxDuration = 3;
		int rangeDuration = maxDuration - minDuration;
		int actualDuration = rand.nextInt(rangeDuration) + minDuration;

		MoveYModifier mod = new MoveYModifier(actualDuration,
				cameraHeight - bgOverlay.getHeightScaled() - paddingY, - 100);

		coin.registerEntityModifier(mod.deepCopy());
		this.CoinsToBeAdded.add(coin);
	}
	
	IUpdateHandler coinHandler = new IUpdateHandler (){
		public void reset() {
		}
		public void onUpdate(final float pSecondsElapsed) {
			Iterator<Sprite> coin = CoinLL.iterator();
			Sprite _coin;
			boolean hit = false;

			// iterating over the targets
			while (coin.hasNext()) {
				_coin = coin.next();

				if (_coin.collidesWith(kite)) {
					hit = true;
				}

				if (hit) {
					removeSprite(_coin, coin);
					hitCount += 50;
					scoreText.setText(String.valueOf(hitCount));
					hit = false;
					//Debug.e("jumlah hitCount", String.valueOf(hitCount));
					
				}
				if (_coin.getY() < 0 ){
					removeSprite(_coin, coin);
				}
			}
			CoinLL.addAll(CoinsToBeAdded);
			CoinsToBeAdded.clear();	
		}
	};
	
	private void createPowerSpriteTimeHandler(){
		TimerHandler powerTimerHandler;
	    float mEffectSpawnDelay = 3f;
	 
	    powerTimerHandler = new TimerHandler(mEffectSpawnDelay, true,
				new ITimerCallback() {

					public void onTimePassed(TimerHandler pTimerHandler) {
						addPower();
					}
				});

		getEngine().registerUpdateHandler(powerTimerHandler);
	}
	
	public void addPower(){
		Random rand = new Random();
		int tmp = rand.nextInt(2);
		if (tmp == 1 ){
			int type = rand.nextInt(3);
			if (type == 0){
				float y = cameraHeight + mFireTextureRegion.getHeight();
				float minX = mFireTextureRegion.getWidth();
				float maxX = cameraWidth - mFireTextureRegion.getWidth();
				float rangeX = maxX - minX;
				float x = rand.nextFloat() * rangeX + minX;

				Sprite Fire = new Sprite(x, y, this.mFireTextureRegion.deepCopy(),
						this.getVertexBufferObjectManager());

				int minDurationFire = 2;
				int maxDurationFire = 4;
				int rangeDurationFire = maxDurationFire - minDurationFire;
				int actualDurationFire = rand.nextInt(rangeDurationFire)
						+ minDurationFire;

				MoveYModifier modFire = new MoveYModifier(actualDurationFire,
						cameraHeight - bgOverlay.getHeightScaled() - paddingY, - 100);
				Fire.registerEntityModifier(modFire.deepCopy());
				Fire.setScale(scale);
				this.scene.attachChild(Fire);
				this.FireToBeAdded.add(Fire);
			}
			else if (type == 1){
				float y = cameraHeight + mWaterTextureRegion.getHeight();
				float minX = mWaterTextureRegion.getWidth();
				float maxX = cameraWidth - mWaterTextureRegion.getWidth();
				float rangeX = maxX - minX;
				float x = rand.nextFloat() * rangeX + minX;

				Sprite Water = new Sprite(x, y, this.mWaterTextureRegion.deepCopy(),
						this.getVertexBufferObjectManager());

				int minDurationWater = 2;
				int maxDurationWater = 4;
				int rangeDurationWater = maxDurationWater - minDurationWater;
				int actualDurationWater = rand.nextInt(rangeDurationWater)
						+ minDurationWater;

				MoveYModifier modWater = new MoveYModifier(actualDurationWater,
						cameraHeight - bgOverlay.getHeightScaled() - paddingY, - 100);
				Water.registerEntityModifier(modWater.deepCopy());

				Water.setScale(scale);
				this.scene.attachChild(Water);
				this.WaterToBeAdded.add(Water);
			}
			else if (type == 2){
				float y = cameraHeight + mShieldTextureRegion.getHeight();
				float minX = mShieldTextureRegion.getWidth();
				float maxX = cameraWidth - mShieldTextureRegion.getWidth();
				float rangeX = maxX - minX;
				float x = rand.nextFloat() * rangeX + minX;

				Sprite Shield = new Sprite(x, y, this.mShieldTextureRegion.deepCopy(),
						this.getVertexBufferObjectManager());

				int minDurationShield = 2;
				int maxDurationShield = 4;
				int rangeDurationShield = maxDurationShield - minDurationShield;
				int actualDurationShield = rand.nextInt(rangeDurationShield)
						+ minDurationShield;

				MoveYModifier modShield = new MoveYModifier(actualDurationShield,
						cameraHeight - bgOverlay.getHeightScaled() - paddingY, - 100);
				Shield.registerEntityModifier(modShield.deepCopy());

				Shield.setScale(scale);
				this.scene.attachChild(Shield);
				this.ShieldToBeAdded.add(Shield);
			}
		}
	}
	
	IUpdateHandler powerHandler = new IUpdateHandler() {
		
		@Override
		public void reset() {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onUpdate(float pSecondsElapsed) {
			// TODO Auto-generated method stub
			Iterator<Sprite> fire = FireLL.iterator();
			Iterator<Sprite> water = WaterLL.iterator();
			Iterator<Sprite> shield = ShieldLL.iterator();
			Sprite _fire;
			Sprite _water;
			Sprite _shield;
			boolean hitFire = false;
			boolean hitWater = false;
			boolean hitShield = false;
			
			while (fire.hasNext()){
				_fire = fire.next();

				if (_fire.collidesWith(kite)) {
					hitFire = true;
				}

				if (hitFire) {
					removeSprite(_fire, fire);
					fireAddToBox();
					hitFire = false;
				}
				
				if(_fire.getY() < 0){
					removeSprite(_fire, fire);
				}
				
			}
			
			while (water.hasNext()){
				_water = water.next();

				if (_water.collidesWith(kite)) {
					hitWater = true;
				}

				if (hitWater) {
					removeSprite(_water, water);
					waterAddToBox();
					hitWater = false;
				}
				
				if(_water.getY() < 0){
					removeSprite(_water, water);
				}
			}
			
			while (shield.hasNext()){
				_shield = shield.next();

				if (_shield.collidesWith(kite)) {
					hitShield = true;
				}

				if (hitShield) {
					removeSprite(_shield, shield);
					shieldAddToBox();
					hitShield = false;			
				}
				
				if(_shield.getY() < 0){
					removeSprite(_shield, shield);
				}
			}
			
			FireLL.addAll(FireToBeAdded);
			FireToBeAdded.clear();
			
			WaterLL.addAll(WaterToBeAdded);
			WaterToBeAdded.clear();
			
			ShieldLL.addAll(ShieldToBeAdded);
			ShieldToBeAdded.clear();
		}
	};
	
	private void createBirdSpriteTimeHandler(){
		TimerHandler birdTimerHandler;
	    float mEffectSpawnDelay = 3f;
	 
	    birdTimerHandler = new TimerHandler(mEffectSpawnDelay, true,
				new ITimerCallback() {
					public void onTimePassed(TimerHandler pTimerHandler) {
						addBird();
					}
				});

		getEngine().registerUpdateHandler(birdTimerHandler);
	}
	
	public void addBird(){
		Random rand = new Random();
		float x = cameraWidth + mBirdTextureRegionRight.getWidth();
		float minY = mBirdTextureRegionRight.getHeight();
		float maxY = cameraHeight - mBirdTextureRegionRight.getHeight();
		float rangeY = maxY - minY;
		float y = rand.nextFloat() * rangeY + minY;
		
		float a = cameraWidth + mBirdTextureRegionRight.getWidth();
		float minB = mBirdTextureRegionRight.getHeight();
		float maxB = cameraHeight - mBirdTextureRegionRight.getHeight();
		float rangeB = maxB - minB;
		float b = rand.nextFloat() * rangeB + minB;
		
		final AnimatedSprite birdRight = new AnimatedSprite(x, y, this.mBirdTextureRegionRight.deepCopy(), this.getVertexBufferObjectManager());
		birdRight.setScale(scale);
		birdRight.animate(100);
		
		final AnimatedSprite birdLeft = new AnimatedSprite(a, b, this.mBirdTextureRegionLeft.deepCopy(), this.getVertexBufferObjectManager());
		birdLeft.setScale(scale);
		birdLeft.animate(100);
		
		
		int minDuration = 3;
		int maxDuration = 6;
		int rangeDuration = maxDuration - minDuration;
		int actualDuration = rand.nextInt(rangeDuration) + minDuration;
		
		int minLeft = 3;
		int maxLeft = 5;
		int rangeLeft = maxLeft - minLeft;
		int actualLeft = rand.nextInt(rangeLeft) + minLeft;
		
		MoveXModifier modRight = new MoveXModifier(actualDuration,
				-100, cameraWidth +100);
		
		MoveXModifier modLeft = new MoveXModifier(actualLeft, 
				cameraWidth+100, -100);
		
		birdRight.registerEntityModifier(modRight.deepCopy());
		birdLeft.registerEntityModifier(modLeft.deepCopy());
		this.BirdToBeAddedRight.add(birdRight);
		this.BirdToBeAddedLeft.add(birdLeft);
		this.scene.attachChild(birdRight);
		this.scene.attachChild(birdLeft);
	}

	IUpdateHandler birdHandler = new IUpdateHandler (){
		public void reset() {
		}
		public void onUpdate(final float pSecondsElapsed) {
			Iterator<Sprite> birdRight = BirdLLRight.iterator();
			Iterator<Sprite> birdLeft = BirdLLLeft.iterator();
			Sprite _bird;
			boolean hit = false;

			// iterating over the targets
			while (birdRight.hasNext()) {
				_bird = birdRight.next();

				if (_bird.collidesWith(kite)) {
					hit = true;
				}

				if (hit) {
					removeSprite(_bird, birdRight);
					healthPoin = healthPoin - 100;
					healthPoinText.setText("Health Poin: "+ String.valueOf(healthPoin));
					hit = false;	
				}
				if (_bird.getX() > cameraWidth){
					removeSprite(_bird, birdRight);
				}
			}
			// iterating over the targets
			while (birdLeft.hasNext()) {
				_bird = birdLeft.next();
					if (_bird.collidesWith(kite)) {
						hit = true;
					}
					if (hit) {
						removeSprite(_bird, birdLeft);
						healthPoin = healthPoin - 100;
						healthPoinText.setText("Health Poin: "+ String.valueOf(healthPoin));
						hit = false;
													
					}
					
					if (healthPoin == 0){
						onDie();
					}
					if (_bird.getX() < 0){
						removeSprite(_bird, birdLeft);
					}
			}
			
			BirdLLRight.addAll(BirdToBeAddedRight);
			BirdLLLeft.addAll(BirdToBeAddedLeft);
			BirdToBeAddedRight.clear();	
			BirdToBeAddedLeft.clear();
		}
	};
	
	public void removeSprite(final Sprite _sprite, Iterator<Sprite> it) {
		runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				scene.detachChild(_sprite);
			}
		});
		it.remove();
	}
	
	public void fireAddToBox(){
		if (pointer > 3 ){
			pointer = 1;
		}
		if (pointer == 1){
			if (box1 ==""){
				fire.setPosition(cameraWidth/3 - (powerBox1.getScaleCenterX()/2), (powerBox1.getHeightScaled()/2) + paddingY);
				scene.attachChild(fire);
			}else if (box1 == "water"){
				scene.detachChild(water);
				fire.setPosition(cameraWidth/3 - (powerBox1.getScaleCenterX()/2), (powerBox1.getHeightScaled()/2) + paddingY);
				scene.attachChild(fire);
			}else if (box1 == "shield"){
				scene.detachChild(shield);
				fire.setPosition(cameraWidth/3 - (powerBox1.getScaleCenterX()/2), (powerBox1.getHeightScaled()/2) + paddingY);
				scene.attachChild(fire);
			}
			box1 = "fire";
			++pointer;
		}
		else if (pointer == 2){
			if (box2 ==""){
				fire2.setPosition(cameraWidth/2 - (powerBox2.getScaleCenterX()/2), (powerBox2.getHeightScaled()/2) + paddingY);
				scene.attachChild(fire2);
			}else if (box2 == "water"){
				scene.detachChild(water2);
				fire2.setPosition(cameraWidth/2 - (powerBox2.getScaleCenterX()/2), (powerBox2.getHeightScaled()/2) + paddingY);
				scene.attachChild(fire2);
			}else if (box2 == "shield"){
				scene.detachChild(shield2);
				fire2.setPosition(cameraWidth/2 - (powerBox2.getScaleCenterX()/2), (powerBox2.getHeightScaled()/2) + paddingY);
				scene.attachChild(fire2);
			}
			box2 = "fire";
			++pointer;
		}
		else if (pointer == 3){
			if (box3 ==""){
				fire3.setPosition(cameraWidth *2/3 - (powerBox3.getScaleCenterX()/2), (powerBox3.getHeightScaled()/2) + paddingY);
				scene.attachChild(fire3);
			}else if (box3 == "water"){
				scene.detachChild(water3);
				fire3.setPosition(cameraWidth *2/3 - (powerBox3.getScaleCenterX()/2), (powerBox3.getHeightScaled()/2) + paddingY);
				scene.attachChild(fire3);
			}else if (box3 == "shield"){
				scene.detachChild(shield3);
				fire3.setPosition(cameraWidth *2/3 - (powerBox3.getScaleCenterX()/2), (powerBox3.getHeightScaled()/2) + paddingY);
				scene.attachChild(fire3);
			}
			box3 = "fire";
			++pointer;
		}	
	}
	
	public void waterAddToBox(){
		if (pointer > 3 ){
			pointer = 1;
		}
		if (pointer == 1){
			if (box1 ==""){
				water.setPosition(cameraWidth/3 - (powerBox1.getScaleCenterX()/2), (powerBox1.getHeightScaled()/2) + paddingY);
				scene.attachChild(water);
			}else if (box1 == "fire"){
				scene.detachChild(fire);
				water.setPosition(cameraWidth/3 - (powerBox1.getScaleCenterX()/2), (powerBox1.getHeightScaled()/2) + paddingY);
				scene.attachChild(water);
			}else if (box1 == "shield"){
				scene.detachChild(shield);
				water.setPosition(cameraWidth/3 - (powerBox1.getScaleCenterX()/2), (powerBox1.getHeightScaled()/2) + paddingY);
				scene.attachChild(water);
			}
			box1 = "water";
			++pointer;
		}
		else if (pointer == 2){
			if (box2 ==""){
				water2.setPosition(cameraWidth/2 - (powerBox2.getScaleCenterX()/2), (powerBox2.getHeightScaled()/2) + paddingY);
				scene.attachChild(water2);
			}else if (box2 == "fire"){
				scene.detachChild(fire2);
				water2.setPosition(cameraWidth/2 - (powerBox2.getScaleCenterX()/2), (powerBox2.getHeightScaled()/2) + paddingY);
				scene.attachChild(water2);
			}else if (box2 == "shield"){
				scene.detachChild(shield2);
				water2.setPosition(cameraWidth/2 - (powerBox2.getScaleCenterX()/2), (powerBox2.getHeightScaled()/2) + paddingY);
				scene.attachChild(water2);
			}
			box2 = "water";
			++pointer;
		}
		else if (pointer == 3){
			if (box3 ==""){
				water3.setPosition(cameraWidth *2/3 - (powerBox3.getScaleCenterX()/2), (powerBox3.getHeightScaled()/2) + paddingY);
				scene.attachChild(water3);
			}else if (box3 == "fire"){
				scene.detachChild(fire3);
				water3.setPosition(cameraWidth *2/3 - (powerBox3.getScaleCenterX()/2), (powerBox3.getHeightScaled()/2) + paddingY);
				scene.attachChild(water3);
			}else if (box3 == "shield"){
				scene.detachChild(shield3);
				water3.setPosition(cameraWidth *2/3 - (powerBox3.getScaleCenterX()/2), (powerBox3.getHeightScaled()/2) + paddingY);
				scene.attachChild(water3);
			}
			box3 = "water";
			++pointer;
		}	
	}
	
	public void shieldAddToBox(){
		if (pointer > 3 ){
			pointer = 1;
		}
		if (pointer == 1){
			if (box1 ==""){
				shield.setPosition(cameraWidth/3 - (powerBox1.getScaleCenterX()/2), (powerBox1.getHeightScaled()/2) + paddingY);
				scene.attachChild(shield);
			}else if (box1 == "fire"){
				scene.detachChild(fire);
				shield.setPosition(cameraWidth/3 - (powerBox1.getScaleCenterX()/2), (powerBox1.getHeightScaled()/2) + paddingY);
				scene.attachChild(shield);
			}else if (box1 == "water"){
				scene.detachChild(water);
				shield.setPosition(cameraWidth/3 - (powerBox1.getScaleCenterX()/2), (powerBox1.getHeightScaled()/2) + paddingY);
				scene.attachChild(shield);
			}
			box1 = "shield";
			++pointer;
		}
		else if (pointer == 2){
			if (box2 ==""){
				shield2.setPosition(cameraWidth/2 - (powerBox2.getScaleCenterX()/2), (powerBox2.getHeightScaled()/2) + paddingY);
				scene.attachChild(shield2);
			}else if (box2 == "fire"){
				scene.detachChild(fire2);
				shield2.setPosition(cameraWidth/2 - (powerBox2.getScaleCenterX()/2), (powerBox2.getHeightScaled()/2) + paddingY);
				scene.attachChild(shield2);
			}else if (box2 == "water"){
				scene.detachChild(water2);
				shield2.setPosition(cameraWidth/2 - (powerBox2.getScaleCenterX()/2), (powerBox2.getHeightScaled()/2) + paddingY);
				scene.attachChild(shield2);
			}
			box2 = "shield";
			++pointer;
		}
		else if (pointer == 3){
			if (box3 ==""){
				shield3.setPosition(cameraWidth *2/3 - (powerBox3.getScaleCenterX()/2), (powerBox3.getHeightScaled()/2) + paddingY);
				scene.attachChild(shield3);
			}else if (box3 == "fire"){
				scene.detachChild(fire3);
				shield3.setPosition(cameraWidth *2/3 - (powerBox3.getScaleCenterX()/2), (powerBox3.getHeightScaled()/2) + paddingY);
				scene.attachChild(shield3);
			}else if (box3 == "water"){
				scene.detachChild(water3);
				shield3.setPosition(cameraWidth *2/3 - (powerBox3.getScaleCenterX()/2), (powerBox3.getHeightScaled()/2) + paddingY);
				scene.attachChild(shield3);
			}
			box3 = "shield";
			++pointer;
		}	
	}
	
	/*Button Controller*/
	TimerHandler tUp;
	TimerHandler tDown;
	
	/*Button Controller*/
	private void createControllers(){
			
		final int tL = cameraHeight - (int)kite.getHeightScaled() - (int)paddingY;
		final int bL = (int) paddingY;
		
		/*Handler untuk button*/
		tUp = new TimerHandler(0.007f, true,
				new ITimerCallback() {
			@Override 
			public void onTimePassed(TimerHandler pTimerHandler) {
				if (centerY >= tL){
					centerY = tL;
				}   
				else{
	    			centerY += 2;
	            	kite.setPosition(centerX, centerY);	
	            }
			}
		});
		
		// our button
		buttonUp = new ButtonSprite(0, 0, mButtonUpTextureRegion, getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY){
				if (pSceneTouchEvent.isActionDown()){
					mEngine.registerUpdateHandler(tUp);

				}
				else if (pSceneTouchEvent.isActionUp()){
					mEngine.unregisterUpdateHandler(tUp);
					
				}
				return true;
			}
		};
		buttonUp.setScale(scale);
		buttonUp.setPosition(cameraWidth - buttonUp.getWidthScaled()/2, buttonUp.getHeightScaled()/2 + paddingY);
		
		/*Handler untuk button down*/
		tDown = new TimerHandler(0.007f, true,
				new ITimerCallback() {
					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						if (centerY <= bL){
							centerY = bL;
						}
						else{
			    			centerY -= 2;
			            	kite.setPosition(centerX, centerY);
			            }
					}
				});
		
		// our button
		buttonDown = new ButtonSprite(0, 0, mButtonDownTextureRegion, getVertexBufferObjectManager()){
			
			@Override
	 		public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY){
				if (pSceneTouchEvent.isActionDown()){
					mEngine.registerUpdateHandler(tDown);
				}
				if (pSceneTouchEvent.isActionUp()){
					mEngine.unregisterUpdateHandler(tDown);
				}
				return true;
			}	
		};
		buttonDown.setScale(scale);
		buttonDown.setPosition(buttonDown.getWidthScaled()/2, buttonDown.getHeightScaled()/2 + paddingY);
		
	    gameHUD.registerTouchArea(buttonUp);
	    gameHUD.registerTouchArea(buttonDown);
	    gameHUD.attachChild(buttonUp);
	    gameHUD.attachChild(buttonDown);
	    camera.setHUD(gameHUD);
	}
	
	public void displayGameOverText(){
	    Scene gameOver = new Scene();
	    
		gameOverText = new Text(0, 0, mGameOverFont, "Game Over!", getVertexBufferObjectManager());
		gameOverText.setScale(scale);
		gameOverText.setPosition(cameraWidth/2, cameraHeight/2 + gameOverText.getHeightScaled()/2);
	    gameOver.attachChild(gameOverText);
	    
	    finalText = new Text(0, 0, mFinalFont, "Coins attained: "+String.valueOf(hitCount), getVertexBufferObjectManager());
	    finalText.setScale(scale);
	    finalText.setPosition(cameraWidth/2, cameraHeight/2 - finalText.getHeightScaled());
	    gameOver.attachChild(finalText);
	    
	    home = new ButtonSprite(0, 0, mHomeTextureRegion, getVertexBufferObjectManager());
		home.setScale(scale);
		home.setPosition(home.getWidthScaled()/2, home.getHeightScaled()/2 + paddingY);
		gameOver.attachChild(home);
	    
	    gameOverDisplayed = true;
	    gameHUD.detachChildren();
	    
	    mEngine.setScene(gameOver);
	    
	}
	
	public void onDie(){
		if (!gameOverDisplayed){
			displayGameOverText();
		}
	}

}
