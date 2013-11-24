package game;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.augmentedreality.BaseAugmentedRealityGameActivity;
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

import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;

import game.*;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class Test extends BaseAugmentedRealityGameActivity implements SensorEventListener{
	// ===========================================================
	// Constants
	// ===========================================================

	private static int CAMERA_WIDTH;
	private static int CAMERA_HEIGHT;

	// ===========================================================
	// Fields
	// ===========================================================

	private ITextureRegion mK1,mK2,mK3;
	private Sprite face;
	private int accellerometerSpeedX;
	private int accellerometerSpeedY;
	private SensorManager sensorManager;
	private float centerX;
	private float centerY;

	/*buat sprite burung*/
	private BuildableBitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mBirdTextureRegion;
	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {
		final Display display = getWindowManager().getDefaultDisplay();
		CAMERA_WIDTH = display.getWidth();
		CAMERA_HEIGHT = display.getHeight();
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}


	
	public Scene onCreateScene() {
		sensorManager = (SensorManager) this
				.getSystemService(this.SENSOR_SERVICE);
		sensorManager.registerListener(this, sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				sensorManager.SENSOR_DELAY_GAME);
		
		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.mEngine.registerUpdateHandler(new IUpdateHandler() {
			public void onUpdate(float pSecondsElapsed) {
				updateSpritePosition();
			}

			public void reset() {
				// TODO Auto-generated method stub
			}
		});

		final Scene scene = new Scene();
		scene.setBackground(new Background(0,0,0,0));
		
		/* Calculate the coordinates for the face, so its centered on the camera. */
		centerX = CAMERA_WIDTH/2f;
		centerY = CAMERA_HEIGHT/2f;

		/* Create the face and add it to the scene. */
		if(Menu.player.kite==1)
		this.face = new Sprite(centerX, centerY, this.mK1, this.getVertexBufferObjectManager());
		else if(Menu.player.kite==2) 
			this.face = new Sprite(centerX, centerY, this.mK2, this.getVertexBufferObjectManager());
		else if(Menu.player.kite==3) 
			this.face = new Sprite(centerX, centerY, this.mK3, this.getVertexBufferObjectManager());
		
		scene.attachChild(this.face);

		final AnimatedSprite bird1 = new AnimatedSprite(0, CAMERA_WIDTH/3, this.mBirdTextureRegion, this.getVertexBufferObjectManager());
		bird1.animate(100);
		MoveXModifier terbangKanan = new MoveXModifier(5, 0, CAMERA_WIDTH);
		bird1.registerEntityModifier(terbangKanan);
		scene.attachChild(bird1);
		
		return scene;
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

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		//
	}
	
	private void updateSpritePosition() {
		if ((accellerometerSpeedX != 0) || (accellerometerSpeedY != 0)) {
			// Set the Boundary limits
			int tL = 0;
			int lL = 0;
			int rL = CAMERA_WIDTH - (int) face.getWidth();
			int bL = CAMERA_HEIGHT - (int) face.getHeight();

			// Calculate New X,Y Coordinates within Limits
			if (centerX >= lL)
				centerX += accellerometerSpeedX;
			else
				centerX = lL;
			if (centerX <= rL)
				centerX += accellerometerSpeedX;
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
			face.setPosition(centerX, centerY);
		}
	}

	@Override
	protected void onCreateResources() {
		// TODO Auto-generated method stub
		try {
			ITexture asset_k1 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/kite1.png");
						}
			});
			
			ITexture asset_k2 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/kite2.png");
						}
			});
			
			ITexture asset_k3 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/kite3.png");
						}
			});

			asset_k1.load();
			asset_k2.load();
			asset_k3.load();
			
			mK1 = TextureRegionFactory.extractFromTexture(asset_k1);
			mK2 = TextureRegionFactory.extractFromTexture(asset_k2);
			mK3 = TextureRegionFactory.extractFromTexture(asset_k3);
			
		} catch (IOException e) {
			Debug.e(e);
		}
		/*buat burung*/
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("asset/");

		this.mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(this.getTextureManager(), 512, 256, TextureOptions.NEAREST);
		this.mBirdTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "bird1.png", 4, 1);

		try {
			this.mBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
			this.mBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	@Override
	protected void onDestroy()
	{
	    super.onDestroy();
	        
	    if (this.isGameLoaded())
	    {
	        System.exit(0);    
	    }
	}
}

